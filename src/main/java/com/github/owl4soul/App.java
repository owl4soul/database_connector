package com.github.owl4soul;

import com.github.owl4soul.interfaces.InitConsoleDbInteractionTaskPerformerImpl;
import com.github.owl4soul.interfaces.TaskPerformer;
import com.github.owl4soul.util.ApplicationStartupPathSignpost;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
public class App extends Application {

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

		launch(args);
	}

	/**
	 * Запуск оконного приложения.
	 * @param primaryStage главный стейдж.
	 * @throws Exception любое исключение.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
    	// Тайтл окна приложения
		primaryStage.setTitle("Amazing Application");

		// Лейбл с текстом
		Label descriptionLabel = new Label("Нажмите на кнопку для запуска взаимодействия с бд.");
		descriptionLabel.setAlignment(Pos.BOTTOM_CENTER);

		// Кнопка запуска некоторой функции приложения
		Button startButton = new Button();
		startButton.setText("Старт");
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TaskPerformer taskPerformer = new InitConsoleDbInteractionTaskPerformerImpl();
				try {
					taskPerformer.performTask();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		});

		// Корневой контейнер
		FlowPane root = new FlowPane();
		root.getChildren().addAll(descriptionLabel, startButton);

		// Сцена
		Scene mainScene = new Scene(root, 600, 400);
		primaryStage.setScene(mainScene);

		// Показ окна приложения
		primaryStage.show();
	}
}
