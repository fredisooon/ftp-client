package main.java.com.fyodor.ftp;

import main.java.com.fyodor.model.User;

import java.io.*;
import java.net.Socket;

public class FtpClient {
    public static boolean isConnected = false;
    public static String address = null;
    public static User loggedInUser = null;
    public static Socket socket = null;
    public static BufferedReader reader = null;
    public static BufferedWriter writer = null;

    public static boolean connect(String ip, String username, String password) {
        System.out.println("\nConnecting to " + ip + ":" + username + ":" + password);
//        String serverAddress = "test.rebex.net";
        String serverAddress = "ftp.dlptest.com";
        String serverPort = "21";

        try {
            socket = new Socket(serverAddress, Integer.parseInt(serverPort));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String response = reader.readLine();
            System.out.println("Server: " + response);

            // Отправка команды для аутентификации
            writer.write("USER " + username + "\r\n");
            writer.flush();

            response = reader.readLine();
            System.out.println("Server: " + response);

            // Отправка команды для передачи пароля
            writer.write("PASS " + password + "\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("Server: " + response);
            System.out.println("СОСТОЯНИЕ СОКЕТА(try): " + socket.isConnected());

            // Установка флага успешного подключения
            isConnected = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    public static void uploadFile(String localFilePath, String remoteFilePath) {
        if (!isConnected) {
            System.out.println("Not connected to the server.");
            return;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(localFilePath);
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            outputStream.close();

            System.out.println("File uploaded successfully to " + remoteFilePath);
        } catch (FileNotFoundException e) {
            System.out.println("Local file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File upload failed.");
            e.printStackTrace();
        }
    }

    public static void downloadFile(String remoteFilePath, String localFilePath) {
        if (!isConnected) {
            System.out.println("Not connected to the server.");
            return;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(localFilePath);
            InputStream inputStream = socket.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();

            System.out.println("File downloaded successfully to " + localFilePath);
        } catch (FileNotFoundException e) {
            System.out.println("Local file path not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File download failed.");
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isConnected = false;
    }
}
