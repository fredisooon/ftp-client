package com.fyodor.menu;


import com.fyodor.App;
import com.fyodor.ftp.FtpClient;
import com.fyodor.util.InputUtil;

public class ServerMenu implements Menu {

    @Override
    public void displayMenu()  {
        while (FtpClient.isConnected) {
            System.out.println("\n===== FTP MENU " + currentHostAndMode() + " =====");
            System.out.println("1. CRUD операции");
            System.out.println("2. Выбрать режим работы" + hint());
            System.out.println("3. DEV TOOLS");
            System.out.println("4. Отключиться от сервера");
            System.out.println("5. Выход");
            processUserChoice();
        }
    }


    @Override
    public void processUserChoice() {
        int choice = InputUtil.getUserChoice(5);
        switch (choice) {
            case 1:
                CRUDMenu crudMenu = new CRUDMenu();
                crudMenu.displayMenu();
                break;
            case 2:
                FtpClient.setMode();
                break;
            case 3:
                DevModeMenu devModeMenu = new DevModeMenu();
                devModeMenu.displayMenu();
                break;
            case 4:
                FtpClient.disconnect();
                break;
            case 5:
                System.out.println("Выход...");
                App.exit();
                break;
        }
    }

    private String currentHostAndMode() {
        String hostAddress = FtpClient.socket.getInetAddress().toString().split("/")[0];
        String activeMode = "";
        if (FtpClient.mode != null) {
            activeMode = " | " + FtpClient.mode.name();
        }
        return hostAddress + activeMode;
    }

    private String hint() {
        if (FtpClient.mode == null)
            return " <-*";
        else
            return "";
    }
}
