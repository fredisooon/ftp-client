package com.fyodor.menu;

import com.fyodor.ftp.FtpClient;
import com.fyodor.service.StudentService;
import com.fyodor.util.InputUtil;

public class CRUDMenu implements Menu{
    private final int menuHeight = 5;
    public static boolean backToPrevious;
    private final StudentService studentService;
    public CRUDMenu() {
        this.studentService = new StudentService();
    }

    @Override
    public void displayMenu() {
        backToPrevious = false;
        while (!backToPrevious) {
            System.out.println("\n===== FTP CRUD MENU =====");
            System.out.println("1. Получение списка студентов по имени");
            System.out.println("2. Получение информации о студенте по id");
            System.out.println("3. Добавление студента");
            System.out.println("4. Удаление студента");
            System.out.println("5. Назад");
            processUserChoice();
        }
    }

    @Override
    public void processUserChoice() {
        int choice = InputUtil.getUserChoice(5);
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
                backToPrevious = true;
                break;
        }
    }
}
