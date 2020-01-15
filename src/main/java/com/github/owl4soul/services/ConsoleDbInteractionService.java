package com.github.owl4soul.services;

import com.github.owl4soul.UserInput;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Сервис взаимодействия пользователя с базой данных через консоль.
 */
public class ConsoleDbInteractionService {

	private static final Logger LOGGER = Logger.getLogger(ConsoleDbInteractionService.class);

	/**
	 * Константа команды прерывания от пользователя.
	 */
	private static final String STOP_COMMAND = "stop";

	/**
	 * Флаг прерванности процесса.
	 */
	private boolean isInterrupted = false;

	/**
	 * Инстанс данного класса для реализации синглтона.
	 */
	private static ConsoleDbInteractionService instance;

	// Приватный конструктор для реализации синглтона.
	private ConsoleDbInteractionService() {
	}

	/**
	 * Простая реализация получения синглтона.
	 * @return инстанс данного класса, являющегося синглтоном.
	 */
	public static ConsoleDbInteractionService getSingleInstance() {
		if (instance == null) {
			instance = new ConsoleDbInteractionService();
		}
		return instance;
	}

	/**
	 * Интерактивное взаимодействие с пользователем.
	 * Работа метода заключается в получении запроса от пользователя и
	 * передаче этого запроса на выполнение в подключенную базу данных.
	 * Результат выполнения запроса выводится пользователю построчно.
	 */
	public void interactWithDdByUserInput(Connection connection) {
		System.out.println("Введите запрос: ");
		String userInput = new UserInput().getUserInput();

		// Прерывание процесса взаимодействия с бд через консоль по команде пользователя.
		if (userInput.equals(STOP_COMMAND)) {
			isInterrupted = true;
			System.out.println("Процесс прерван пользователем по команде: " + userInput);
			return;
		}

		try (// Получение стейтмента
			 Statement statement = connection.createStatement()) {

			// Получение результата выполнения запроса пользователя
			ResultSet resultSet = statement.executeQuery(userInput);

			while (resultSet.next()) {
				int columnsCount = resultSet.getMetaData().getColumnCount();

				System.out.println("\n");
				for (int i = 1; i <= columnsCount; i++) {
					System.out.print(resultSet.getString(i) + "    |    ");
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e);
			System.out.println("Запрос не может быть обработан!");
		}

		System.out.println("\n_______________________________________________________________________________\n");
	}

	/**
	 * Получение флага прерывания текущего процесса пользователем.
	 * @return true - если процесс взаимодействия помечен как прерванный.
	 */
	public boolean isInterrupted() {
		return isInterrupted;
	}
}
