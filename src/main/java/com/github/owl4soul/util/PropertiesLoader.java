package com.github.owl4soul.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Загрузчик проперти-файла с настройками бд.
 */
public class PropertiesLoader {

    private static final Logger LOGGER = Logger.getLogger(PropertiesLoader.class);

    /**
     * Получение пропертис из файла по заданному пути.
     * @return Properties
     */
    public static Properties getDatabasePropertiesFromFile(String pathToProperties) throws IOException {
        Properties databaseProperties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(pathToProperties);
        databaseProperties.load(fileInputStream);

        return databaseProperties;
    }
}
