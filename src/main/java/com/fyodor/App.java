package com.fyodor;

import com.fyodor.util.ResourceUtil;
import com.fyodor.util.StartMenu;

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
