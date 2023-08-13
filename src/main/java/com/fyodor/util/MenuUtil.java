package com.fyodor.util;


import com.fyodor.ftp.FtpClient;
import com.fyodor.service.StudentService;

public class MenuUtil {
    private final StudentService studentService;

    public MenuUtil() {
        this.studentService = new StudentService();
    }
    public void displayMenu()  {
        String hostAddress = FtpClient.socket.getInetAddress().toString().split("/")[0];
        String activeMode = "";
        if (FtpClient.mode != null) {
            activeMode = " | " + FtpClient.mode.name();
        }
        if (FtpClient.mode != null) {
            System.out.println("\n===== Меню " + hostAddress + activeMode + " =====");
            System.out.println("1. Получение списка студентов по имени");
            System.out.println("2. Получение информации о студенте по id");
            System.out.println("3. Добавление студента");
            System.out.println("4. Удаление студента по id");
            System.out.println("5. Отключиться от сервера");
            System.out.println("6. Выгрузить список студентов. (ONLY FOR TESTING)");
            System.out.println("7. Вывести список файлов (ONLY FOR TESTING)");
            System.out.println("8. Скачать файл с сервера (ONLY FOR TESTING)");
            System.out.println("9. Изменить режим работы");
        }
        else {
            System.out.println("\n===== Меню " + hostAddress + activeMode + " =====");
            System.out.println("5. Отключиться от сервера");
            System.out.println("6. Выгрузить список студентов. (ONLY FOR TESTING)");
            System.out.println("7. Вывести список файлов (ONLY FOR TESTING)");
            System.out.println("8. Скачать файл с сервера (ONLY FOR TESTING)");
            System.out.println("9. Выбрать режим работы");
        }
        processUserChoice();

    }


    public void processUserChoice() {
        int choice = InputUtil.getUserChoice();
        switch (choice) {
            case 1:
                studentService.getStudentsByName();
                break;
            case 2:
                studentService.getStudentInfoById();
                break;
            case 3:
                studentService.addStudent();
                break;
            case 4:
                studentService.deleteStudentById();
                break;
            case 5:
                FtpClient.disconnect();
                break;
            case 6:
                FtpClient.uploadFile("/Users/fyodor/Desktop/test" +
                        "/fpt-client/src/main/resources/static/students.json", "fyodor-test.json");
                break;
            case 7:
                FtpClient.displayRemoteFiles();
                break;
            case 8:
                FtpClient.downloadRemoteFile("fyodor-test.json", "/Users/fyodor/Desktop/test" +
                        "/fpt-client/src/main/resources/static/students2.json");
                break;
            case 9:
                FtpClient.setMode();
//                FtpClient.downloadFile("/test/students.json", "/Users/fyodor/Desktop/mock.txt");
                break;
        }
    }
}
