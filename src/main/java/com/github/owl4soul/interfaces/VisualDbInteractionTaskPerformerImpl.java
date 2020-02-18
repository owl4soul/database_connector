package com.github.owl4soul.interfaces;

import com.github.owl4soul.JdbcDatabaseConnector;
import com.github.owl4soul.front.DbInteractionWindow;

import java.sql.Connection;

/**
 * Created by lera.feoktistova on 18.02.2020.
 */
public class VisualDbInteractionTaskPerformerImpl implements TaskPerformer {

	@Override
	public void performTask() throws IllegalAccessException, InstantiationException {
		Connection connection = JdbcDatabaseConnector.class.newInstance().getDatabaseConnection();

		DbInteractionWindow.initStageAndShow();
	}
}
