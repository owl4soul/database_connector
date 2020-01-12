package com.github.owl4soul;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс для получения пользовательского ввода.
 */
public class UserInput {

    private static final Logger LOGGER = Logger.getLogger(UserInput.class);


    /**
     * Получение пользовательского ввода из консоли.
     *
     * @return строка, введенная пользователем.
     */
    public String getUserInput() {
        System.out.println("\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String userInput = null;
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            LOGGER.error("An error occurred while trying to read user input!", e);
        }

        return userInput;
    }
}
