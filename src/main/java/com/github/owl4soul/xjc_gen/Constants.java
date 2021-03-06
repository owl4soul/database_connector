package com.github.owl4soul.xjc_gen;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	static final String COMMON_FOLDER = "all_new";

	static final String SCHEMAS_VERSION = "v2_52";

	static final String PATH_IN_PROJECT =
			"ru.spi2.irule.javaee.web.services.clients.spark." + SCHEMAS_VERSION + "." + COMMON_FOLDER +".generated.";

	// Имя директории, в которой лежат все схемы и папка src
	static final String CURRENT_SPARK_DIR_NAME = "20200205";


	static final String ROOT_PATH = "D:\\work\\generateSpark\\jaxb-ri-2.2.6_old\\bin\\";

	static final String FULLPATH_TO_COMMON_FOLDER =
			ROOT_PATH + CURRENT_SPARK_DIR_NAME + "\\src\\" + COMMON_FOLDER + "\\generated\\";

	static final Map<String, String> KNOWN_UNIC_CONTENT_CLASSES;

	static {
		Map<String, String> classNamesPerPaths = new HashMap<>();
		classNamesPerPaths.put("generated\\ObjectFactory.java", "ObjectFactory");
		classNamesPerPaths.put("generated\\Response.java", "Response");
//		classNamesPerPaths.put("generated\\IncludeInList.java", "IncludeInList");

		KNOWN_UNIC_CONTENT_CLASSES = classNamesPerPaths;
	}
}
