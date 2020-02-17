package com.github.owl4soul.interfaces;

import com.github.owl4soul.JdbcDatabaseConnector;
import com.github.owl4soul.services.ConsoleDbInteractionService;
import com.github.owl4soul.util.Constants;
import com.github.owl4soul.util.PropertiesLoader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * Инициализатор выполнения процесса консольного взаимодействия пользователя с бд.
 */
public class InitConsoleDbInteractionTaskPerformerImpl implements TaskPerformer {

	private static final Logger LOGGER = Logger.getLogger(InitConsoleDbInteractionTaskPerformerImpl.class);

	/**
	 * В данном контексте выполнением задачи является установка подключения к бд и запуск сервиса реализующего
	 * возможность взаимодействия пользователя с бд через консоль.
	 */
	@Override
	public void performTask() throws Exception {
		Properties databaseProperties = null;
		try {
			databaseProperties =
					PropertiesLoader.getDatabasePropertiesFromFile(Constants.DATABASE_PROPERTIES_FULL_PATH);
		} catch (IOException e) {
			LOGGER.error("No properties!", e);
			System.exit(-1);
		}

		try (// Установка соединения с бд
			 Connection connection = new JdbcDatabaseConnector().getDatabaseConnection(databaseProperties)) {

			// Вывод сообщения о результате установки соединения с бд.
			if (connection != null) {
				LOGGER.info("Database connection successfully established.");
			}

			// Получение инстанса сервиса консольного взаимодействия пользователя с бд.
			ConsoleDbInteractionService consoleDbInteractionService = ConsoleDbInteractionService.getSingleInstance();

			while (!consoleDbInteractionService.isInterrupted()) {
				consoleDbInteractionService.interactWithDdByUserInput(connection);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception("Could not perform task!");
		}
	}
}
