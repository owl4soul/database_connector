package com.github.owl4soul;

import com.github.owl4soul.util.ApplicationStartupPathSignpost;
import com.github.owl4soul.util.PropertiesLoader;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.sql.Connection;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Path currentApplicationCatalog = ApplicationStartupPathSignpost.getApplicationStartupPath();
        LOGGER.info("Current application catalog: " + currentApplicationCatalog);

        // Если путь к текущему рабочему каталогу не был найден, считать это фатальной ошибкой
        // и завершать приложение аварийно.
        if (currentApplicationCatalog == null) {
            LOGGER.error("Fatal error occurred! No path to current work catalog was found!");
            System.exit(-1);
        }


        String currentPathMsg = "Каталог запуска приложения :\n" +
                currentApplicationCatalog +
                "\nПо данному пути приложение ищет файл с настройками для подключения к базе данных...\n\n";
        System.out.println(currentPathMsg);

        Properties databaseProperties = PropertiesLoader.getDatabasePropertiesFromFile();
        Connection connection = new JdbcDatabaseConnector().getDatabaseConnection(databaseProperties);
    }
}
