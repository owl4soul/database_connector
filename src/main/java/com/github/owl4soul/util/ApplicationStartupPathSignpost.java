package com.github.owl4soul.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс, отвечающий за получение сведений о каталоге запуска данного приложения.
 */
public class ApplicationStartupPathSignpost {

    /**
     * Получение пути к каталогу запуска.
     * Используется для получения пути по которому находится запускаемый jar на машине пользователя,
     * чтоб относительно него искать файл с properties для коннекта к бд.
     *
     * @return путь к текущему каталогу запуска.
     * @throws URISyntaxException ошибка при попытке получить URI из URL.
     */
    public Path getApplicationStartupPath() throws URISyntaxException {
        URL startupPath = getClass().getProtectionDomain().getCodeSource().getLocation();
        Path path = Paths.get(startupPath.toURI());
        path = path.getParent();

        return path;
    }

}
