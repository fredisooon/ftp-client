package main.java.com.fyodor.util;

import main.java.com.fyodor.ftp.FtpClient;
import main.java.com.fyodor.service.StudentService;


public class MenuUtil {
    private StudentService studentService;

    public MenuUtil() {
        this.studentService = new StudentService();
    }
    public void displayMenu()  {
        System.out.println("\n===== Меню " + FtpClient.socket.getInetAddress() + " =====");
        System.out.println("1. Получение списка студентов по имени");
        System.out.println("2. Получение информации о студенте по id");
        System.out.println("3. Добавление студента");
        System.out.println("4. Удаление студента по id");
        System.out.println("5. Отключиться от сервера");
        System.out.println("6. Загрузить список студентов. (ONLY FOR TESTING)");
        processUserChoice();
    }


    public void processUserChoice() {
        int choice = InputUtil.getUserChoice();
        switch (choice) {
            case 1:
                studentService.getStudentByName();
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
                        "/fpt-client/src/main/resources/static/students.json", "/test");
                break;
            case 7:
                FtpClient.downloadFile("/test/students.json", "/Users/fyodor/Desktop/mock.txt");
                break;
        }
    }
}
