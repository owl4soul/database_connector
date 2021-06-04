package com.github.owl4soul.excel_parser;

import java.util.Map;

public class Blanks {

	public static final String Список = "Список";
	public static final String Профильбезопасности = "Профильбезопасности";
	public static final String Журналаудита = "Журналаудита";
	public static final String Источникданных = "Источникданных";
	public static final String Подключениексерверу = "Подключениексерверу";
	public static final String Ярлык = "Ярлык";
	public static final String Системнаяроль = "Системнаяроль";
	public static final String Папка = "Папка";
	public static final String Должностнаяроль = "Должностнаяроль";
	public static final String Учётнаязапись = "Учётнаязапись";
	public static final String Привилегия = "Привилегия";
	public static final String Взаимодействиепользователей = "Взаимодействиепользователей";
	public static final String Параметрысервера = "Параметрысервера";
	public static final String Материал = "Материал";
	public static final String Спецификациясервиса = "Спецификациясервиса";
	public static final String Модель = "Модель";
	public static final String Роль = "Роль";

	public static void getPsfCats(Map<String, String> categories) {
		categories.forEach((s, s2) -> {
			s = s.replaceAll(" ", "");
			String template = "public static final String [имя категории] = \"[имя категории]\";";
			template = template.replace("[имя категории]", s);

			System.out.println(template);
		});



//		categoriesAll.forEach(s -> {
//			count.getAndIncrement();
//			System.out.println(count + ". " + s);
//
//			String template = ExcelParserWithGroup.catMapTemplate;
//			template = template.replace(RUS_NAME_REPLACEMENT, s).replace(CONSTANT_NAME_REPLACEMENT, "iiiiiiiiiiiiiiii");
//
//			System.out.println(template);
//		});
	}
}
