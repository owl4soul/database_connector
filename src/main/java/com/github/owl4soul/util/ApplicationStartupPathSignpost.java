package com.github.owl4soul.util;

import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс, отвечающий за получение сведений о каталоге запуска данного приложения.
 */
public class ApplicationStartupPathSignpost {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartupPathSignpost.class);

    /**
     * Получение пути к каталогу запуска.
     * Используется для получения пути по которому находится запускаемый jar на машине пользователя,
     * чтоб относительно него искать файл с properties для коннекта к бд.
     *
     * @return путь к текущему каталогу запуска.
     * @throws URISyntaxException ошибка при попытке получить URI из URL.
     */
    public static Path getApplicationStartupPath() {
        URL startupPath = ApplicationStartupPathSignpost.class.getProtectionDomain().getCodeSource().getLocation();
        Path path;
        try {
            path = Paths.get(startupPath.toURI());
        } catch (URISyntaxException e) {
            LOGGER.error("An error occurred while trying to get to get current work catalog path " +
                    "on the step of getting URI from URL!", e);
            return null;
        }
        path = path.getParent();

        return path;
    }

}
