package com.github.owl4soul;

import com.github.owl4soul.interfaces.InitConsoleDbInteractionTaskPerformerImpl;
import com.github.owl4soul.interfaces.TaskPerformer;
import com.github.owl4soul.util.ApplicationStartupPathSignpost;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.nio.file.Path;

/**
 * Главный класс приложения, запускающий взаимодействие пользователя с базой данных.
 */
public class App  {

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

        TaskPerformer taskPerformer = new InitConsoleDbInteractionTaskPerformerImpl();
		try {
			taskPerformer.performTask();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
}
