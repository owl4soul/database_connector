package com.github.owl4soul;

import com.github.owl4soul.front.MainMenu;
import com.github.owl4soul.services.TestService;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by lera.feoktistova on 20.02.2020.
 */
@Component
public class Program extends Application{

	@Resource
	TestService testService;

	public void goOn(String[] args) {
		testService.test();
		launch(args);
	}

	/**
	 * Запуск оконного приложения.
	 * @param primaryStage главный стейдж.
	 * @throws Exception любое исключение.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		MainMenu.initStageAndShow(primaryStage);
	}
}
