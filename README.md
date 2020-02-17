# database_connector
Создание соединения с базой данных, указанной в properties и выполнение запросов из консоли.

Для запуска приложения собрать jar с зависимостями командой mvn package.
Рядом с запускаемым jar (в той же папке, что и jar) должен располагаться файл database.properties
с настройками подключения к db.

Файл database.properties должен иметь следующий вид:

db.url=jdbc:postgresql://localhost:5432/alians

db.user=lera2

db.password=lera2

db.driver=org.postgresql.Driver

Запускать jar из консоли: java -jar path/app-with-dependencies.jar
