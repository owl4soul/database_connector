package com.github.owl4soul.excel_parser;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelParserWithGroup {


	// Сводная служебная инфа (количество категорий, строк и тп)
	private DocSummaryInfo docSummaryInfo;

	private StringBuilder psfsBuilderForPrivileges;
	private StringBuilder mapBuilderForPrivileges;
	private StringBuilder mapBuilderForGroupWithPrivileges;
	private StringBuilder psfsBuilderForGroups;

	public static final String LS = "\r\n";


	public static final String CONSTANT_NAME_REPLACEMENT = "[ИМЯ_КОНСТАНТЫ]";
	public static final String CONSTANT_VALUE_REPLACEMENT = "[значение.константы]";
	public static final String RUS_NAME_REPLACEMENT = "[Русское имя]";
	public static final String DESCRIPTION_REPLACEMENT = "[Описание]";
	public static final String BLOCK_LINES_ADDING_PRIVILEGE_TO_SET_REPLACEMENT = "[БЛОК СТРОК ДОБАВЛЕНИЯ ПРИВИЛЕГИЙ В СЕТ]"; // в составе которого используется шаблон addingPrivilegeToSetTemplate

	public static final String categoryCommentNameTemplate = "// [ИМЯ_КОНСТАНТЫ] - [Русское имя]";
	public static final String psfsTemplate = "public static final String [ИМЯ_КОНСТАНТЫ] = \"[значение.константы]\"; // [Русское имя]";
	public static final String mapTemplate = "PRIVILEGE_FULL_INFO_MAP.put([ИМЯ_КОНСТАНТЫ], new PrivilegeFullInfo(\"[ИМЯ_КОНСТАНТЫ]\", \"[Русское имя]\", \"[Описание]\", \"[значение.константы]\"));";

	// Для групп привилегий
	//public static final String catMapTemplate = "CAT_MAP.put(\"[Русское имя]]\", \"[ИМЯ_КОНСТАНТЫ]\");";
	public static final Map<GroupDataRepresentation, Set<String>> GROUP_MAP = new LinkedHashMap<>();
	public static final String addingPrivilegeToSetTemplate = "privileges.add(Privilege.[ИМЯ_КОНСТАНТЫ]);"; // составляют БЛОК СТРОК ДОБАВЛЕНИЯ ПРИВИЛЕГИЙ В СЕТ
	public static final String groupMapTemplate = "privileges = new HashSet<>();\n" +
												  "[БЛОК СТРОК ДОБАВЛЕНИЯ ПРИВИЛЕГИЙ В СЕТ]\n" +
												  "groupFullInfo = new PrivilegeGroupFullInfo([ИМЯ_КОНСТАНТЫ], \"[Русское имя]\", privileges);\n" +
												  "DEFAULT_GROUP_PRIVILEGES.put([ИМЯ_КОНСТАНТЫ], groupFullInfo);";



	public int privilegesCount;

	public void start() {
		System.out.println("Excel Parser [START]");
		docSummaryInfo = new DocSummaryInfo();

		psfsBuilderForPrivileges = new StringBuilder();
		mapBuilderForPrivileges = new StringBuilder();
		mapBuilderForGroupWithPrivileges = new StringBuilder();
		psfsBuilderForGroups = new StringBuilder();

		String excelFilePath = "D:\\ExcelParserData\\doc.xlsx";


		File excelFile = new File(excelFilePath);

		try (FileInputStream fileInputStream = new FileInputStream(excelFile)){
			parseSheet(fileInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		System.out.println(docSummaryInfo.toString());
		System.out.println("Excel Parser [FINISH]");
	}

	private void parseSheet(FileInputStream fileInputStream) throws IOException {
		Workbook workbook = new XSSFWorkbook(fileInputStream);

		Sheet sheet = workbook.getSheetAt(0);

		int countOfAllDataRowsInSheet = sheet.getPhysicalNumberOfRows();
		System.out.println("Количество строк с данными в документе = " + countOfAllDataRowsInSheet);

		// Начинаем со 2-ой строки, чтоб пропустить служебную строку с наименованием колонок
		// (Значение константы в коде | Константа в коде | Системная привилегия | Описание разрешённых действий)
		for (int i = 1; i < countOfAllDataRowsInSheet; i++) {
			System.out.println("#" + (i + 1));
			Row row = sheet.getRow(i);
			//parseRowAndPutAsGroupOrPrivilegeToMap(row);
			parseRow(row);
		}

		// Тут набралась полная мапа GROUP_MAP - теперь распишем по ней код для мапы класса PrivilegeGRoup
		writeCodeByGroupedPrivilegesMap();

		// Здесь имеем уже полностью сформированный код для наполнения мапы Групп в статическом блоке
		// psf-нужны

		AtomicInteger count = new AtomicInteger();
		categoriesAll.forEach(s -> {
			count.getAndIncrement();
			//System.out.println(count + ". " + s);
		});
		//System.out.println(psfsBuilderForPrivileges.toString());
		//System.out.println(mapBuilderForPrivileges.toString());
		System.out.println("ok");
	}

	private void writeCodeByGroupedPrivilegesMap() {
		for (Map.Entry<GroupDataRepresentation, Set<String>> entryGroupCategories : GROUP_MAP.entrySet()) {
			// Сперва добавим все Привилегии в сет, затем этот сет присеттим к groupPrivilegeFullInfo и сложим в МАПУ
			StringBuilder allPrivilegesOfGroupAddingToSetSB = new StringBuilder();

			// Сама Группа
			GroupDataRepresentation groupDataRepresentation = entryGroupCategories.getKey();

			// private static final String группы привилегий
			String psfs = psfsTemplate;
			psfs = psfs.replace(CONSTANT_NAME_REPLACEMENT, groupDataRepresentation.constantName);
			psfs = psfs.replace(CONSTANT_VALUE_REPLACEMENT, groupDataRepresentation.constantName);
			psfs = psfs.replace(RUS_NAME_REPLACEMENT, groupDataRepresentation.rusName);
			psfsBuilderForGroups.append(psfs);
			psfsBuilderForGroups.append(LS);

			// Сет всех Привилегий в данной группе
			Set<String> allPrivilegesOfGroupSet = entryGroupCategories.getValue();

			boolean isNeedLS = false;
			for (String eachPrivilegeOfGroup : allPrivilegesOfGroupSet) {
				if (isNeedLS) {
					allPrivilegesOfGroupAddingToSetSB.append(LS);
				}
				isNeedLS = true;
				String template = addingPrivilegeToSetTemplate;
				template = template.replace(CONSTANT_NAME_REPLACEMENT, eachPrivilegeOfGroup);

				allPrivilegesOfGroupAddingToSetSB.append(template);
			}

			// Здесь уже составлен полный блок добавлений Привилегий в сет привилегий Группы: privileges.add(Privilege.[ИМЯ_КОНСТАНТЫ]);

			String templateFullFillGroup = groupMapTemplate;
			templateFullFillGroup =
					templateFullFillGroup.replace(BLOCK_LINES_ADDING_PRIVILEGE_TO_SET_REPLACEMENT, allPrivilegesOfGroupAddingToSetSB.toString());
			templateFullFillGroup = templateFullFillGroup.replace(CONSTANT_NAME_REPLACEMENT, groupDataRepresentation.constantName);
			templateFullFillGroup = templateFullFillGroup.replace(RUS_NAME_REPLACEMENT, groupDataRepresentation.rusName);
			templateFullFillGroup = templateFullFillGroup.replace(CONSTANT_NAME_REPLACEMENT, groupDataRepresentation.constantName);

			String commentWithGroupName = categoryCommentNameTemplate;
			commentWithGroupName =
					commentWithGroupName.replace(CONSTANT_NAME_REPLACEMENT, groupDataRepresentation.constantName);
			commentWithGroupName =
					commentWithGroupName.replace(RUS_NAME_REPLACEMENT, groupDataRepresentation.rusName);
			mapBuilderForGroupWithPrivileges.append("\r\n\n" + commentWithGroupName + "\n");
			mapBuilderForGroupWithPrivileges.append(templateFullFillGroup);
		}
	}

	public static List<String> categoriesAll = new ArrayList<>();

	private void parseRow(Row row) {
		// Определим Тип обрабатываемой строки
		RowType rowType = RowType.getRowType(row);

		// Создаем код: для каждой строки создавать соответствующие категории в виде комментов + мапа
		String template;
		switch (rowType) {
		case CATEGORY:
			String categoryName = row.getCell(0).getStringCellValue();
			categoriesAll.add(categoryName);

			template = categoryCommentNameTemplate;
			template = template.replace(CONSTANT_NAME_REPLACEMENT, categoryName);
			template = template.replace(RUS_NAME_REPLACEMENT, row.getCell(2).getStringCellValue());

			// Для public static final String
			// // Модель
			psfsBuilderForPrivileges.append(LS);
			psfsBuilderForPrivileges.append(LS);
			psfsBuilderForPrivileges.append(template);

			// Для мапы
			// // Модель
			mapBuilderForPrivileges.append(LS);
			mapBuilderForPrivileges.append(LS);
			mapBuilderForPrivileges.append(template);
			break;
		case DATA_NOT_FULL:
			writeCodeRepresentationForDataRow(row, true);
			break;
		case DATA_FULL:
			writeCodeRepresentationForDataRow(row, false);
			break;
		default:
			break;
		}
	}

	private GroupDataRepresentation currentGroup; // Текущая (последняя) группа (которая парсится в настоящий момент) - меняем имя при каждом последовательном обнаружении группы категорий как типа строки

	private void parseRowAndPutAsGroupOrPrivilegeToMap(Row row) {
		// Определим тип строки экселя - что там содержится, привилегия или группа
		RowType rowType = RowType.getRowType(row);

		// Сложим все в мапу, чтоб ясно видеть все группы и привилегии, включенные в них (когда заполним мапу полностью - будем обрабатывать ее, составляя код):

		switch (rowType) {
		case CATEGORY:
			GroupDataRepresentation groupDataRepresentation = getGroupRepresentationByRow(row);
			currentGroup = groupDataRepresentation;

			GROUP_MAP.computeIfAbsent(groupDataRepresentation, k -> new HashSet<>());
			break;
		case DATA_FULL: // TODO - DATA NOT FULL
			PrivilegeDataRepresentation privilegeDataRepresentation = getPrivilegeRepresentationByRow(row);
			GROUP_MAP.get(currentGroup).add(privilegeDataRepresentation.constantName);
		}
	}

	private void writeCodeRepresentationForDataRow(Row row, boolean mustBeUnusedCode) {
		PrivilegeDataRepresentation privilegeDataRepresentation = getPrivilegeRepresentationByRow(row);

		// Для public static final String
		// public static final String READ_ANY_MODEL = "read.any.model" // Открыть любую модель;
		String template = psfsTemplate;
		template = template.replace(CONSTANT_NAME_REPLACEMENT, privilegeDataRepresentation.constantName);
		template = template.replace(CONSTANT_VALUE_REPLACEMENT, privilegeDataRepresentation.shortName);
		template = template.replace(RUS_NAME_REPLACEMENT, privilegeDataRepresentation.rusName);
		psfsBuilderForPrivileges.append(LS);
		if (mustBeUnusedCode) {
			psfsBuilderForPrivileges.append("// ");
		}
		psfsBuilderForPrivileges.append(template);


		// PRIVILEGE_FULL_INFO_MAP.put(READ_ANY_MODEL, new PrivilegeFullInfo("READ_ANY_MODEL", "Открыть любое описание модели", "Открыть любое описание модели", "read.any.model"));
		template = mapTemplate;
		template = template.replace(CONSTANT_NAME_REPLACEMENT, privilegeDataRepresentation.constantName);
		template = template.replace(CONSTANT_NAME_REPLACEMENT, privilegeDataRepresentation.constantName);
		template = template.replace(RUS_NAME_REPLACEMENT, privilegeDataRepresentation.rusName);
		template = template.replace(DESCRIPTION_REPLACEMENT, privilegeDataRepresentation.description);
		template = template.replace(CONSTANT_VALUE_REPLACEMENT, privilegeDataRepresentation.shortName);
		mapBuilderForPrivileges.append(LS);
		if (mustBeUnusedCode) {
			mapBuilderForPrivileges.append("// ");
		}
		mapBuilderForPrivileges.append(template);
	}

	private GroupDataRepresentation getGroupRepresentationByRow(Row row) {
		GroupDataRepresentation groupDataRepresentation = new GroupDataRepresentation();

		String cell_val_0 = row.getCell(0).getStringCellValue(); // АНГЛИЙСКОЕ_ИМЯ_КОНСТАНТЫ
		String cell_val_2 = row.getCell(2).getStringCellValue(); // Русское имя

		groupDataRepresentation.constantName = cell_val_0;
		groupDataRepresentation.rusName = cell_val_2;

		return groupDataRepresentation;
	}

	private PrivilegeDataRepresentation getPrivilegeRepresentationByRow(Row row) {
		PrivilegeDataRepresentation privilegeDataRepresentation = new PrivilegeDataRepresentation();

		String cell_val_0 = row.getCell(0).getStringCellValue();
		String cell_val_1 = row.getCell(1).getStringCellValue();
		String cell_val_2 = row.getCell(2).getStringCellValue();
		String cell_val_3 = row.getCell(3).getStringCellValue();

		privilegeDataRepresentation.shortName = cell_val_0;
		privilegeDataRepresentation.constantName = cell_val_1;
		privilegeDataRepresentation.rusName = cell_val_2;
		privilegeDataRepresentation.description = cell_val_3;

		return privilegeDataRepresentation;
	}

	private class DocSummaryInfo {
		private int nullCount;
		private int tableColumnsTitlesCount;
		private int categoryCount;
		private int dataCount;

		@Override
		public String toString() {
			return "DocSummaryInfo{" + "nullCount=" + nullCount + ", tableColumnsTitlesCount=" +
				   tableColumnsTitlesCount + ", categoryCount=" + categoryCount + ", dataCount=" + dataCount + '}';
		}
	}

	private enum RowType {
		NULL,
		TABLE_COLUMNS_TITLE,
		CATEGORY,
		DATA_NOT_FULL,
		DATA_FULL;

		private static RowType getRowType(Row row) {
			if (row == null) {
				return NULL;
			}

			// Если в первой колонке строки надпись = "Значение константы в коде", то это ЗАГОЛОВОК ТАБЛИЦЫ
			String cell_val_0 = row.getCell(0).getStringCellValue();
			if (cell_val_0.equalsIgnoreCase("Значение константы в коде")) {
				return TABLE_COLUMNS_TITLE;
			}

			// Если в первой и третьей колонках строки есть надпись, а 2 и 4 - нет надписи, то это КАТЕГОРИЯ
			String cell_val_1 = row.getCell(1).getStringCellValue();
			String cell_val_2 = row.getCell(2).getStringCellValue();
			String cell_val_3 = row.getCell(3).getStringCellValue();
			if (!cell_val_0.isEmpty() && cell_val_1.isEmpty() && !cell_val_2.isEmpty() && cell_val_3.isEmpty()) {
				return CATEGORY;
			}

			// Во всех иных случаях это данные по привилегиям

			// Если это данные привилегии, но не все 4 ячейки заполнены, то это ДАННЫЕ_НЕПОЛНЫЕ
			if (cell_val_0.isEmpty() || cell_val_1.isEmpty() || cell_val_2.isEmpty() || cell_val_3.isEmpty()) {
				return DATA_NOT_FULL;
			}

			// Во всех иных случаях это ДАННЫЕ_ПОЛНЫЕ
			return DATA_FULL;
		}
	}

	private class GroupDataRepresentation {
		private String constantName;
		private String rusName;

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			GroupDataRepresentation that = (GroupDataRepresentation) o;
			return Objects.equals(constantName, that.constantName) && Objects.equals(rusName, that.rusName);
		}

		@Override
		public int hashCode() {
			return Objects.hash(constantName, rusName);
		}
	}


	private class PrivilegeDataRepresentation {
		private String shortName;
		private String constantName;
		private String rusName;
		private String description;


		@Override
		public String toString() {
			return "PrivelegeExcelDataRepresentation{" + "shortName='" + shortName + '\'' + ", constantName='" +
				   constantName + '\'' + ", rusName='" + rusName + '\'' + ", description='" + description + '\'' + '}';
		}
	}
}
