package main.java.com.fyodor;

public class App implements Runnable {
    private void started() {
        System.out.println("Application started");
    }
    @Override
    public void run() {
        started();
    }
}
