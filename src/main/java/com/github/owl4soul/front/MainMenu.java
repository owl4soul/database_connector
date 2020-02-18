package com.github.owl4soul.front;

import com.github.owl4soul.interfaces.ConsoleDbInteractionTaskPerformerImpl;
import com.github.owl4soul.interfaces.TaskPerformer;
import com.github.owl4soul.interfaces.VisualDbInteractionTaskPerformerImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * Created by lera.feoktistova on 18.02.2020.
 */
public class MainMenu {
	private static final Logger LOGGER = Logger.getLogger(MainMenu.class);

	public static void initStageAndShow(Stage primaryStage) {
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
				TaskPerformer taskPerformer = new VisualDbInteractionTaskPerformerImpl();
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
