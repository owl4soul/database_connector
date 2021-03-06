package com.github.owl4soul;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Класс для создания подключения к бд.
 */
public class JdbcDatabaseConnector {

    private static final Logger LOGGER = Logger.getLogger(JdbcDatabaseConnector.class);

    /**
     * Получение подключения к бд по заданным properties.
     *
     * @param properties настройки подключения, содержащие url, user, password, драйвер.
     * @return Connection активное подключение к указанной бд.
     */
    public Connection getDatabaseConnection(Properties properties) {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver");

        // Подключение драйвера.
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Driver with name " + driver + " could not be found!");
            throw new IllegalStateException("DRIVER FATAL ERROR!");
        }

        // Получение активного соединения к указанной бд.
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LOGGER.error("Connection refused!", e);
            throw new IllegalStateException("CONNECTION FATAL ERROR!", e);
        }

        return connection;
    }

}
