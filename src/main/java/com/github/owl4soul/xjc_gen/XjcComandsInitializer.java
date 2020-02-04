package com.github.owl4soul.xjc_gen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XjcComandsInitializer {

	// Версия схем
	private String sparkSchemasVersion = "v2_66";

	public List<String> generateXjcCommands() {

		// Генерируемый список команд для xjc
		List<String> commands = new ArrayList<>();
		initXjcCommands(commands);

		return commands;
	}

	private void initXjcCommands(List<String> commandsUpdatableList) {
		// Указываем абсолютный путь к сорцам
		String sparkFilesDir = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\" + Constants.CURRENT_SPARK_DIR_NAME;

		// Получаем содержимое корневой директории сорцов
		File sourceDir = new File(sparkFilesDir);

		// создаем папку src, если ее нет
		File srcDir = new File(sparkFilesDir + "\\src");
		srcDir.mkdir();

		// Цикт по всем схемам .xsd
		for (File file : sourceDir.listFiles()) {
			if (!file.isDirectory()) {

				String fileName = file.getName();

				String command = "xjc -encoding UTF-8 -d " +
								 Constants.ROOT_PATH + Constants.CURRENT_SPARK_DIR_NAME + "\\src\\" + Constants.COMMON_FOLDER +// куда (в какую папку)
								 " "
								 + Constants.ROOT_PATH + Constants.CURRENT_SPARK_DIR_NAME + "\\" + fileName; // откуда (из какой схемы)
				System.out.println("[COMMAND]" + command);
				commandsUpdatableList.add(command);

				// xjc -encoding UTF-8 -d D:\work\generateSpark\jaxb-ri-2.2.6_old\bin\20200203\src\AAA D:\work\generateSpark\jaxb-ri-2.2.6_old\bin\20200203\CompanyStructure.xsd
			}
		}
	}
}
