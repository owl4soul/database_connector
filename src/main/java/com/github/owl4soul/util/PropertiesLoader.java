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
    public static Properties getDatabasePropertiesFromFile(String pathToProperties) {
        Properties databaseProperties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(pathToProperties)){
            databaseProperties.load(fileInputStream);
        } catch (IOException e) {
            LOGGER.error("An error occured while trying to get properties from file by path: " + pathToProperties);
            throw new IllegalStateException("No properties!");
        }

        return databaseProperties;
    }
}
