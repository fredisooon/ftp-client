package com.fyodor.menu;

import com.fyodor.ftp.FtpClient;
import com.fyodor.util.InputUtil;
import com.fyodor.util.log.Logger;

public class DevModeMenu implements Menu {
    private final int menuHeight = 4;
    public static boolean backToPrevious;
    @Override
    public void displayMenu() {
        backToPrevious = false;
        while (!backToPrevious) {
            System.out.println("\n===== DEV MODE TOOLS =====");
            System.out.println("1. Выгрузить список студентов.");
            System.out.println("2. Вывести список файлов на сервере");
            System.out.println("3. Скачать файл с сервера");
            System.out.println("4. Назад");
            processUserChoice();
        }
    }

    @Override
    public void processUserChoice() {
        int choice = InputUtil.getUserChoice(menuHeight);
        switch (choice) {
            case 1:
                FtpClient.uploadFile("/Users/fyodor/Desktop/test" +
                        "/fpt-client/src/main/resources/static/students.json", "fyodor-test.json");
                break;
            case 2:
                FtpClient.displayRemoteFiles();
                break;
            case 3:
                FtpClient.downloadRemoteFile("fyodor-test.json", "/Users/fyodor/Desktop/test" +
                        "/fpt-client/src/main/resources/static/students2.json");
                break;
            case 4:
                backToPrevious = true;
                break;
        }
    }
}
