package main.java.com.fyodor;

import main.java.com.fyodor.util.ResourceUtil;
import main.java.com.fyodor.util.StartMenu;


public class App implements Runnable {
    public static boolean isRunning = true;
    private void started() {
        while (isRunning) {
            StartMenu startMenu = new StartMenu();
            startMenu.renderStartMenu();
        }
        ResourceUtil.closingResources();
    }
    @Override
    public void run() {
        started();
    }

}
