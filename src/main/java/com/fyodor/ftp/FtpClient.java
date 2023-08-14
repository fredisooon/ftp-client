package com.fyodor.ftp;


import com.fyodor.model.Mode;
import com.fyodor.model.User;
import com.fyodor.util.InputUtil;
import com.fyodor.util.log.Logger;

import java.io.*;
import java.net.Socket;

public class FtpClient {
    public static boolean isConnected = false;
    private boolean connection;
    public static String address = null;
    public static User loggedInUser = null;


    public static Socket commandSocket = null;
    public static Socket dataTransferSocket = null;

    public static BufferedReader serverResponseReader = null;
    public static BufferedWriter serverCommandWriter = null;
    public static Mode mode = null;
    public static final int FTP_COMMAND_PORT = 21;
    public static final int FTP_DATA_TRANSFER_PORT = 20;
    public static OutputStream dataOutputStream = null;
    public static FileInputStream fileInputStream = null;


    public void connect(String ip, String username, String password) {
//        String serverAddress = "test.rebex.net";
//        String serverAddress = "eu-central-1.sftpcloud.io";
        String serverAddress = "ftp.dlptest.com";
        User user = new User(username, password);
        try {
            commandSocket = new Socket(serverAddress, FTP_COMMAND_PORT);
            serverResponseReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            serverCommandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));

            String response = getResponse();
            if (response.startsWith("2")) {
                // Отправка команды для аутентификации
                sendCommand("USER " + username + "\r\n");
                response = getResponse();

                if (response.startsWith("3")) {
                    // Отправка команды для передачи пароля
                    sendCommand("PASS " + password + "\r\n");
                    response = getResponse();

                    if (response.startsWith("2")) {
                        // Устанавливаем бинарный режим передачи данных
                        sendCommand("TYPE I\r\n");
                        response = getResponse();
                    }
                }
            }
            else {
                Logger.logWarning(response);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean connect(String ip, String username, String password, int o) {
//        String serverAddress = "test.rebex.net";
//        String serverAddress = "eu-central-1.sftpcloud.io";
        String serverAddress = "ftp.dlptest.com";

        try {
            commandSocket = new Socket(serverAddress, FTP_COMMAND_PORT);
            serverResponseReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            serverCommandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
            String response;

            getResponse();
            // Отправка команды для аутентификации
            sendCommand("USER " + username + "\r\n");
            getResponse();


            // Отправка команды для передачи пароля
            sendCommand("PASS " + password + "\r\n");
            getResponse();

            // Устанавливаем бинарный режим передачи данных
            sendCommand("TYPE I\r\n");
            getResponse();

            // Установка флага успешного подключения
            isConnected = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isConnected;
    }
    private static void sendCommand(String command) {
        try {
            serverCommandWriter.write(command);
            serverCommandWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getResponse() {
        String response = "";
        try {
            response = serverResponseReader.readLine();
            Logger.logServerResponse(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void uploadFile(String localFilePath, String remoteFilePath) {
        if (!isConnected) {
            Logger.logWarning("Not connected to the server.");
            return;
        }

        try {
            sendCommand("STOR " + remoteFilePath + "\r\n");
            getResponse();

            dataOutputStream = dataTransferSocket.getOutputStream();
            fileInputStream = new FileInputStream(localFilePath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead); // Записываем данные в сокет
            }

            // Закрываем потоки и сокет для передачи данных
            fileInputStream.close();
            dataOutputStream.close();
//            dataSocket.close()
            getResponse();


            Logger.logInfo("File uploaded successfully to " + remoteFilePath);
        } catch (FileNotFoundException e) {
            Logger.logWarning("Local file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            Logger.logError("File upload failed.");
            e.printStackTrace();
        }
    }


    public static void downloadRemoteFile(String remoteFilePath, String localFilePath) {
        if (!isConnected) {
            Logger.logWarning("Not connected to the server.");
            return;
        }

        try {
            // Отправляем команду RETR для скачивания файла
            sendCommand("RETR " + remoteFilePath + "\r\n");
            String response = getResponse();

            // Дожидаемся ответа сервера 150, который указывает на начало передачи данных
            if (response.startsWith("150")) {
                // Создаем поток для записи данных в локальный файл
                FileOutputStream fileOutputStream = new FileOutputStream(localFilePath);

                // Получаем соединение данных (предполагая пассивный режим)

                // Читаем данные и записываем их в локальный файл
                InputStream dataInputStream = dataTransferSocket.getInputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = dataInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                // Закрываем потоки и соединение данных
                fileOutputStream.close();
                dataInputStream.close();
//                dataSocket.close();

                // Читаем ответ сервера 226, который указывает на завершение передачи данных
                getResponse();

                Logger.logInfo("File downloaded successfully to " + localFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void disconnect() {
        if (commandSocket != null) {
            try {
                commandSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isConnected = false;
        mode = null;
    }

    public static void setMode() {
        System.out.println("\n===== Выбор режима работы =====");
        System.out.println("1. Пассивный(PASV)");
        System.out.println("2. Активный(PORT)");
        System.out.println("3. Назад");
        int choice = InputUtil.getUserChoice(3);

        switch (choice) {
            case 1:
                enablePassiveMode();
                break;
            case 2:
                enableActiveMode();
                break;
            case 3:
                System.out.println();
                break;
            default:
                Logger.logWarning("Invalid input!");
                break;
        }
    }

    private static void enableActiveMode() {
        if (mode != Mode.ACTIVE) {
            try {
//                String localAddress = InetAddress.getLocalHost().getHostAddress();
                String localAddress = "134.122.66.69";
                int localPort = 9999;
//                ServerSocket serverSocket = new ServerSocket(localPort);
//                Socket dataSocket = serverSocket.accept();

                // Формирование порта для команды PORT (в формате h1,h2,h3,h4,p1,p2)
//                String portCommand = String.format("%s,%s,%s,%s,%s,%s",
//                        localAddress.replace('.', ','), localPort / 256, localPort % 256);
                String portCommand = "192,168,0,96,39,15";
                System.out.println("Port command: " + portCommand);
                serverCommandWriter.write("PORT " + portCommand);
                serverCommandWriter.flush();
                String response = serverResponseReader.readLine();
                Logger.logServerResponse(response);
//                BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
//                PrintWriter dataWriter = new PrintWriter(dataSocket.getOutputStream(), true);

                // Пример чтения ответа от сервера
//                String response = dataReader.readLine();
//                System.out.println("Server response: " + response);
//                String response = serverResponseReader.readLine();
//                System.out.println("Server: " + response);
                mode = Mode.ACTIVE;
//                serverSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void enablePassiveMode() {
        if (mode != Mode.PASSIVE) {
            try {
                sendCommand("PASV\r\n");
                String response = getResponse();

                int startIndex = response.indexOf("(");
                int endIndex = response.indexOf(")");
                String extractedValues = response.substring(startIndex + 1, endIndex);

                String[] parts = extractedValues.split(",");
                String serverAddress1 = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
                int serverPort1 = Integer.parseInt(parts[4]) * 256 + Integer.parseInt(parts[5]);

                // Вывод сообщения о включении пассивного режима
                Logger.logInfo("Пассивный режим включен. Адрес: " + serverAddress1 + ", Порт: " + serverPort1);
                dataTransferSocket = new Socket(serverAddress1, serverPort1);
                mode = Mode.PASSIVE;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Logger.logWarning("Passive mode already set!");
        }

    }

    public static void displayRemoteFiles() {
        if (!isConnected) {
            Logger.logWarning("Not connected to the server.");
            return;
        }
        try {
            // Отправляем команду LIST серверу
            sendCommand("LIST\r\n");
            String response = getResponse();

            // Дожидаемся ответа сервера 150, который указывает на начало передачи данных
            if (response.startsWith("150")) {

                // Читаем данные о списках файлов и директорий
                BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataTransferSocket.getInputStream()));
                String line;
                while ((line = dataReader.readLine()) != null) {
                    System.out.println(line); // Выводим каждую строку списка на консоль
                }

                // Закрываем соединение данных и читателя
                dataReader.close();

                // Читаем ответ сервера 226, который указывает на завершение передачи данных
                getResponse();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnection() {
        return connection;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }
}
