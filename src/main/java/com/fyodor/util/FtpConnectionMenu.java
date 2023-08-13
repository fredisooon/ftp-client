package com.fyodor.util;


import com.fyodor.ftp.FtpClient;

public class FtpConnectionMenu {
    public FtpConnectionMenu() {
    }

    public void displayMenu() {
        System.out.println("\n=== Подключение к FTP-серверу ===");
        System.out.println("Введите IP-адрес FTP-сервера: ");
        String serverIp = InputUtil.readIpAddress();
        System.out.println("Введите логин: ");
        String username = InputUtil.readLogin();
        System.out.println("Введите пароль: ");
        String password = InputUtil.readPassword();

        boolean connection = FtpClient.connect(serverIp, username, password);
        if (connection) {
            MenuUtil menuUtil = new MenuUtil();
            while (FtpClient.isConnected) {
                menuUtil.displayMenu();
            }
        }
    }
}
