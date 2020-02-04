package com.github.owl4soul.xjc_gen;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	// Имя директории, в которой лежат все схемы и папка src
	static final String CURRENT_SPARK_DIR_NAME = "20200203";


	static final String COMMON_FOLDER = "AAA";

	static final String ROOT_PATH = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\";

	static final String FULLPATH_TO_COMMON_FOLDER =
			ROOT_PATH + CURRENT_SPARK_DIR_NAME + "\\src\\" + COMMON_FOLDER + "\\generated\\";

	static final Map<String, String> KNOWN_UNIC_CONTENT_CLASSES;

	static {
		Map<String, String> classNamesPerPaths = new HashMap<>();
		classNamesPerPaths.put("generated\\ObjectFactory.java", "ObjectFactory");
		classNamesPerPaths.put("generated\\Response.java", "Response");
		classNamesPerPaths.put("generated\\IncludeInList.java", "IncludeInList");

		KNOWN_UNIC_CONTENT_CLASSES = classNamesPerPaths;
	}
}
