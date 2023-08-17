# Тестовое задание FTP Client


### Запуск приложенния
----

_Для запуска приложения необходимо:_
1. Склонировать репозиторий локально
    ```sh
    git clone https://github.com/fredisooon/ftp-client.git
    ```
2. Перейти в корень проекта
    ```sh
    cd ../fpt-client
    ```
3. Запустить исполняемый файл программы
    ```sh
    java -jar target/fpt-client-2.0.0-jar-with-dependencies.jar
    ```


### Сборка приложения
----
_Также приложения можно собрать с помощью `maven` Если `maven` не установлен, то необходимо установить его:_
1. С помощью менеджера пакетов homebrew
    ```sh
    brew install maven
    ```
    Либо с сайта [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Далее необходимо перейти в корень проекта
    ```sh
    cd ../fpt-client
    ```
3. В корне проекта необходимо запустить maven плагины clean и package
    ```sh
    mvn clean & mvn package
    ```
4. После сборки проекта необходимо запустить исполняемый файл
    ```sh
    java -jar target/fpt-client-2.0.0-jar-with-dependencies.jar
    ```
----
## Работа с приложением
Послу запуска приложения, пользователю будет доступно начальное меню:
![Стартовое меню](/docs/img.png "Стартовое меню")
