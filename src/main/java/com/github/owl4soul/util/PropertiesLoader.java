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

    private static final String SLASH = System.getProperty("file.separator");

    private static final String PROPERTIES_FILENAME = "database.properties";

    private static final String DATABASE_PROPERTIES_PATH =
            ApplicationStartupPathSignpost.getApplicationStartupPath() +
            SLASH +
            PROPERTIES_FILENAME;


    /**
     * Получение пропертис из файла по дефолтному пути.
     * @return
     */
    public static Properties getDatabasePropertiesFromFile() {
        Properties databaseProperties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(DATABASE_PROPERTIES_PATH)){
            databaseProperties.load(fileInputStream);

            LOGGER.debug("Properties was loaded: ");
            LOGGER.debug("db.url=" + databaseProperties.getProperty("db.url"));
            LOGGER.debug("db.user=" + databaseProperties.getProperty("db.user"));
            LOGGER.debug("db.password=" + databaseProperties.getProperty("db.password"));
            LOGGER.debug("db.driver=" + databaseProperties.getProperty("db.driver"));
        } catch (IOException e) {
            LOGGER.error("An error occured while trying to get properties from file by path: " + DATABASE_PROPERTIES_PATH);
        }

        return databaseProperties;
    }
}
