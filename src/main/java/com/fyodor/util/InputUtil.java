package main.java.com.fyodor.util;

import java.util.Scanner;

/**
 * @author Fyodor
 * @version 0.0.1
 *
 * Note: 1)Можно добавить очистку консоли через определение среды исполнения и нативного 'cls' или 'clear'
 *       2)Так же можно добавить скрытый ввод пароля через звездочки '***'
 *
 */
public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);
    public static String readIpAddress() {
        try {
            String rawIp = scanner.nextLine();
            return rawIp.trim();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
    public static String readLogin() {
        try {
            String inputLogin = scanner.nextLine();
            return inputLogin.trim();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String readPassword() {
        try {
            String inputPassword = scanner.nextLine();
            return inputPassword.trim();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
    public static int getUserChoice() {
        System.out.print("Выберите пункт: ");
        int choiceIndex = scanner.nextInt();
        scanner.nextLine();
        return choiceIndex;
    }

    public static int readStudentId() {
        System.out.println("Введите id студента: ");
        int studentId = scanner.nextInt();
        return studentId;
    }
    public static String readStudentName() {
        System.out.println("Введите имя студента: ");
        String studentName = scanner.nextLine();
        return studentName;
    }

    public static void closeResources() {
        scanner.close();
    }
}
