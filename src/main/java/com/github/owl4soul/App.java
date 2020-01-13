package com.github.owl4soul;

import com.github.owl4soul.util.ApplicationStartupPathSignpost;
import com.github.owl4soul.util.Constants;
import com.github.owl4soul.util.PropertiesLoader;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Главный класс приложения, запускающий взаимодействие пользователя с базой данных.
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class);

    private static Connection connection;

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

        // Получение properties по заданному пути.
        Properties databaseProperties = PropertiesLoader.getDatabasePropertiesFromFile(Constants.DATABASE_PROPERTIES_FULL_PATH);

		LOGGER.debug("Properties was loaded: ");
		LOGGER.debug("db.url=" + databaseProperties.getProperty("db.url"));
		LOGGER.debug("db.user=" + databaseProperties.getProperty("db.user"));
		LOGGER.debug("db.password=" + databaseProperties.getProperty("db.password"));
		LOGGER.debug("db.driver=" + databaseProperties.getProperty("db.driver"));

        connection = new JdbcDatabaseConnector().getDatabaseConnection(databaseProperties);

        // Вывод сообщения о результате установки соединения с бд.
        if (connection != null) {
            LOGGER.info("Database connection successfully established.");
        }

		while (connection != null) {
			interactWithDdByUserInput();
		}
    }

    /**
     * Интерактивное взаимодействие с пользователем.
     * Работа метода заключается в получении запроса от пользователя и
     * передаче этого запроса на выполнение в подключенную базу данных.
     * Результат выполнения запроса выводится пользователю построчно.
     */
    private static void interactWithDdByUserInput() {
        System.out.println("Введите запрос: ");
        String userInput = new UserInput().getUserInput();


        try {
            // Получение стейтмента
            Statement statement = connection.createStatement();

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
}
