Training Diary 
-----------------------------
REST-Приложение для ведения дневника тренировок, 
которое позволит пользователям записывать свои тренировки,
просматривать их и анализировать свой прогресс в тренировках.

## Описание
Тренировку одного типа можно заносить раз в день.
Пользователь может видеть только свои тренировки, 
администратор может видеть тренировки всех пользователей.

Stack: Java 17, JDBC, Lombok, Docker, Luiqbase, Test-containers, Mockito,
AssertJ, Mapstruct, Maven, Servlet, JWT, AspectJ, Jackson, Spring Core,
Spring AOP, Spring JDBC, Spring Security,  Swagger, HicariCP

## Запуск приложения
Этот репозиторий содержит Java приложение, 
которое использует базу данных, развернутую с помощью Docker Compose.
Следуйте инструкциям ниже, чтобы развернуть приложение и запустить его.

1. Склонируйте репозиторий:
   git clone git@github.com:A1ekseiPanov/TrainingDiary.git
2. Откройте проект в среде разработки.
3. Запустите Docker Compose для развертывания приложения
   через docker-compose.yml или через терминал docker-compose up. 
   Это развернет приложение в контейнере Docker.
4. Подождите несколько секунд, чтобы приложение полностью инициализировалась.
-----------------------------
### [REST API documentation](http://localhost:8080/swagger-ui/index.html)