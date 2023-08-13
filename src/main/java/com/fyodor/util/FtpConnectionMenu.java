package com.fyodor.util;

import com.fyodor.ftp.FtpClient;
import com.fyodor.menu.Menu;
import com.fyodor.menu.ServerMenu;

public class FtpConnectionMenu implements Menu {
    @Override
    public void displayMenu() {
        System.out.println("\n===== CONNECTING TO FTP-SERVER =====");
        System.out.println("Введите IP-адрес FTP-сервера: ");
        String serverIp = InputUtil.readIpAddress();
        System.out.println("Введите логин: ");
        String username = InputUtil.readLogin();
        System.out.println("Введите пароль: ");
        String password = InputUtil.readPassword();

        boolean connection = FtpClient.connect(serverIp, username, password);
        if (connection) {
            ServerMenu menuUtil = new ServerMenu();
            menuUtil.displayMenu();
        }
    }
    @Override
    public void processUserChoice() {

    }
}
