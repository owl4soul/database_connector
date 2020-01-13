package com.github.owl4soul.util;

/**
 * Класс, хранящий константы приложения.
 */
public class Constants {

	/**
	 * Прямой или обратный слэш вм зависимости от OS.
	 */
	public static final String SLASH = System.getProperty("file.separator");

	/**
	 * Имя файла, хранящего настройки подключения к бд.
	 */
	public static final String PROPERTIES_FILENAME = "database.properties";

	/**
	 * Полный путь к дефолтному файлу с настройками подключения к бд.
	 */
	public static final String DATABASE_PROPERTIES_FULL_PATH =
			ApplicationStartupPathSignpost.getApplicationStartupPath() +
			SLASH +
			PROPERTIES_FILENAME;
}
