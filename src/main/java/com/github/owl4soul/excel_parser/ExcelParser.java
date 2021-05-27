package com.github.owl4soul.excel_parser;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelParser {

	// Сводная служебная инфа (количество категорий, строк и тп)
	private DocSummaryInfo docSummaryInfo;

	private StringBuilder psfsBuilder;
	private StringBuilder mapBuilder;

	public static final String LS = "\r\n";


	public static final String CATEGORY_NAME_REPLACEMENT = "[имя категории]";
	public static final String CONSTANT_NAME_REPLACEMENT = "[ИМЯ_КОНСТАНТЫ]";
	public static final String CONSTANT_VALUE_REPLACEMENT = "[значение.константы]";
	public static final String RUS_NAME_REPLACEMENT = "[Русское имя]";
	public static final String DESCRIPTION_REPLACEMENT = "[Описание]";

	public static final String categoryCommentNameTemplate = "// [имя категории]";
	public static final String psfsTemplate = "public static final String [ИМЯ_КОНСТАНТЫ] = \"[значение.константы]\"; // [Русское имя]";
	public static final String mapTemplate = "PRIVILEGE_FULL_INFO_MAP.put([ИМЯ_КОНСТАНТЫ], new PrivilegeFullInfo(\"[ИМЯ_КОНСТАНТЫ]\", \"[Русское имя]\", \"[Описание]\", \"[значение.константы]\"));";


	public int privilegesCount;

	public void start() {
		System.out.println("Excel Parser [START]");
		docSummaryInfo = new DocSummaryInfo();

		psfsBuilder = new StringBuilder();
		mapBuilder = new StringBuilder();

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
			parseRow(row);
		}

		System.out.println(psfsBuilder.toString());
		System.out.println(mapBuilder.toString());
	}

	private void parseRow(Row row) {
		// Определим Тип обрабатываемой строки
		RowType rowType = RowType.getRowType(row);

		// Создаем код: для каждой строки создавать соответствующие категории в виде комментов + мапа
		String template;
		switch (rowType) {
		case CATEGORY:
			String categoryName = row.getCell(0).getStringCellValue();

			template = categoryCommentNameTemplate;
			template = template.replace(CATEGORY_NAME_REPLACEMENT, categoryName);

			// Для public static final String
			// // Модель
			psfsBuilder.append(LS);
			psfsBuilder.append(LS);
			psfsBuilder.append(template);

			// Для мапы
			// // Модель
			mapBuilder.append(LS);
			mapBuilder.append(LS);
			mapBuilder.append(template);
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

	private void writeCodeRepresentationForDataRow(Row row, boolean mustBeUnusedCode) {
		PrivilegeDataRepresentation privilegeDataRepresentation = getPrivilegeRepresentationByRow(row);

		// Для public static final String
		// public static final String READ_ANY_MODEL = "read.any.model" // Открыть любую модель;
		String template = psfsTemplate;
		template = template.replace(CONSTANT_NAME_REPLACEMENT, privilegeDataRepresentation.constantName);
		template = template.replace(CONSTANT_VALUE_REPLACEMENT, privilegeDataRepresentation.shortName);
		template = template.replace(RUS_NAME_REPLACEMENT, privilegeDataRepresentation.rusName);
		psfsBuilder.append(LS);
		if (mustBeUnusedCode) {
			psfsBuilder.append("// ");
		}
		psfsBuilder.append(template);


		// PRIVILEGE_FULL_INFO_MAP.put(READ_ANY_MODEL, new PrivilegeFullInfo("READ_ANY_MODEL", "Открыть любое описание модели", "Открыть любое описание модели", "read.any.model"));
		template = mapTemplate;
		template = template.replace(CONSTANT_NAME_REPLACEMENT, privilegeDataRepresentation.constantName);
		template = template.replace(CONSTANT_NAME_REPLACEMENT, privilegeDataRepresentation.constantName);
		template = template.replace(RUS_NAME_REPLACEMENT, privilegeDataRepresentation.rusName);
		template = template.replace(DESCRIPTION_REPLACEMENT, privilegeDataRepresentation.description);
		template = template.replace(CONSTANT_VALUE_REPLACEMENT, privilegeDataRepresentation.shortName);
		mapBuilder.append(LS);
		if (mustBeUnusedCode) {
			mapBuilder.append("// ");
		}
		mapBuilder.append(template);
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

			// Если в первой колонке строки есть надпись, а в остальных трех нет надписи, то это КАТЕГОРИЯ
			String cell_val_1 = row.getCell(1).getStringCellValue();
			String cell_val_2 = row.getCell(2).getStringCellValue();
			String cell_val_3 = row.getCell(3).getStringCellValue();
			if (!cell_val_0.isEmpty() && cell_val_1.isEmpty() && cell_val_2.isEmpty() && cell_val_3.isEmpty()) {
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
