package com.github.owl4soul;

import com.github.owl4soul.util.ApplicationStartupPathSignpost;
import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Path currentApplicationCatalog = null;
        try {
             currentApplicationCatalog = ApplicationStartupPathSignpost.getApplicationStartupPath();
            LOGGER.info("Current application catalog: " + currentApplicationCatalog);
        } catch (URISyntaxException e) {
            LOGGER.error("An error occurred while trying to get application startup path!\n" + e.getMessage(), e);
        }

        String currentPathMsg = "Каталог запуска приложения :\n" +
                currentApplicationCatalog +
                "\nПо данному пути приложение ищет файл с настройками для подключения к базе данных...\n\n";
        System.out.println(currentPathMsg);
    }
}
