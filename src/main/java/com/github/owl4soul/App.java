package com.github.owl4soul;

import com.github.owl4soul.services.TestService;
import com.github.owl4soul.util.ApplicationStartupPathSignpost;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.nio.file.Path;

/**
 * Главный класс приложения, запускающий взаимодействие пользователя с базой данных.
 */
public class App  {

    private static final Logger LOGGER = Logger.getLogger(App.class);


    public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		TestService testService = applicationContext.getBean(TestService.class);

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

		Program program = applicationContext.getBean(Program.class);
		program.goOn(args);
	}


}
