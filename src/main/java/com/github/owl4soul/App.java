package com.github.owl4soul;

import com.github.owl4soul.util.ApplicationStartupPathSignpost;

import java.net.URISyntaxException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");


        try {
            System.out.println("Каталог запуска приложения " +
                    "(являющийся также путем, по которому должен располагаться файл с настройками " +
                    "для подключения к базе данных):\n" +
                    new ApplicationStartupPathSignpost().getApplicationStartupPath() +
                    "\n\n");

        } catch (URISyntaxException e) {
            // TODO: подключить log4j для логгирования.
            e.printStackTrace();
        }
    }
}
