package com.fyodor;

import com.fyodor.ftp.FtpClient;
import com.fyodor.util.ResourceUtil;
import com.fyodor.menu.StartMenu;

public class App implements Runnable {
    private static App instance;
    public static boolean isRunning = true;

    private App() {}
    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }
    @Override
    public void run() {
        started();
    }
    private void started() {
        while (isRunning) {
            StartMenu startMenu = new StartMenu();
            startMenu.displayMenu();
        }
        ResourceUtil.closingResources();
    }
    public static void exit() {
        if (instance != null) {
            App.isRunning = false;
            if (FtpClient.isConnected)
                FtpClient.disconnect();
        }
    }

}
