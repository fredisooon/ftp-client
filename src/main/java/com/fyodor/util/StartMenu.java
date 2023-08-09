package main.java.com.fyodor.util;

import main.java.com.fyodor.App;


public class StartMenu {
    public StartMenu() {
    }
    public void renderStartMenu() {
        System.out.println("===== FTP CLI =====");
        System.out.println("1. Подключиться к FTP серверу");
        System.out.println("2. Завершение работы");
        processUserChoice();
    }

    public void processUserChoice() {
        int choice = InputUtil.getUserChoice();
        switch (choice) {
            case 1:
                FtpConnectionMenu ftpConnectionMenu = new FtpConnectionMenu();
                ftpConnectionMenu.displayMenu();
                break;
            case 2:
                System.out.println("Выход...");
                App.isRunning = false;
                break;
        }
    }
}
