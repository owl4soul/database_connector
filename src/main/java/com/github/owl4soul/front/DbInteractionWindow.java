package com.github.owl4soul.front;

import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Created by lera.feoktistova on 18.02.2020.
 */
public class DbInteractionWindow {

	public static void initStageAndShow() {
		Stage stage = new Stage();
		// Сцена
		Scene mainScene = new Scene(new FlowPane(), 600, 400);
		stage.setScene(mainScene);
		stage.setTitle("ХОБА");
		stage.show();
	}
}
