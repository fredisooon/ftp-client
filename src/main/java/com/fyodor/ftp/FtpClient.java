package main.java.com.fyodor.ftp;

import main.java.com.fyodor.model.Mode;
import main.java.com.fyodor.model.User;
import main.java.com.fyodor.util.InputUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpClient {
    public static boolean isConnected = false;
    public static String address = null;
    public static User loggedInUser = null;
    public static Socket socket = null;
    public static Socket dataSocket = null;
    public static OutputStream dataOutputStream = null;
    public static FileInputStream fileInputStream = null;
    public static BufferedReader reader = null;
    public static BufferedWriter writer = null;
    public static Mode mode = null;

    public static boolean connect(String ip, String username, String password) {
        System.out.println("\nConnecting to " + ip + ":" + username + ":" + password);
//        String serverAddress = "test.rebex.net";
        String serverAddress = "ftp.dlptest.com";
//        String serverAddress = "eu-central-1.sftpcloud.io";
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

            writer.write("TYPE I\r\n"); // Устанавливаем бинарный режим передачи данных
            writer.flush();
            response = reader.readLine(); // Читаем ответ сервера
            System.out.println("Server: " + response);



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

            writer.write("STOR " + remoteFilePath + "\r\n");
            writer.flush();
            String response = reader.readLine(); // Читаем ответ сервера
            System.out.println("Server: " + response);

            dataOutputStream = dataSocket.getOutputStream();
            fileInputStream = new FileInputStream(localFilePath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead); // Записываем данные в сокет
            }

            // Закрываем потоки и сокет для передачи данных
            fileInputStream.close();
            dataOutputStream.close();
//            dataSocket.close();
            response = reader.readLine(); // Читаем ответ сервера
            System.out.println("Server: " + response);



            System.out.println("File uploaded successfully to " + remoteFilePath);
        } catch (FileNotFoundException e) {
            System.out.println("Local file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File upload failed.");
            e.printStackTrace();
        }
    }


    public static void downloadRemoteFile(String remoteFilePath, String localFilePath) {
        if (!isConnected) {
            System.out.println("Not connected to the server.");
            return;
        }

        try {
            // Отправляем команду RETR для скачивания файла
            writer.write("RETR " + remoteFilePath + "\r\n");
            writer.flush();

            // Читаем ответ сервера
            String response = reader.readLine();
            System.out.println("Server: " + response);

            // Дожидаемся ответа сервера 150, который указывает на начало передачи данных
            if (response.startsWith("150")) {
                // Создаем поток для записи данных в локальный файл
                FileOutputStream fileOutputStream = new FileOutputStream(localFilePath);

                // Получаем соединение данных (предполагая пассивный режим)

                // Читаем данные и записываем их в локальный файл
                InputStream dataInputStream = dataSocket.getInputStream();
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
                response = reader.readLine();
                System.out.println("Server: " + response);

                System.out.println("File downloaded successfully to " + localFilePath);
            }
        } catch (IOException e) {
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

    public static void setMode() {
        System.out.println("\n===== Выбор режима работы =====");
        System.out.println("1. Пассивный(PASV)");
        System.out.println("2. Активный(PORT)");
        System.out.println("3. Назад");
        int choice = InputUtil.getUserChoice();

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
                System.out.println("Invalid input!");
                break;
        }
    }

    private static void enableActiveMode() {
        if (mode != Mode.ACTIVE) {
            try {
                String localAddress = InetAddress.getLocalHost().getHostAddress();
                System.out.println(localAddress);
                int localPort = 9999;
                ServerSocket serverSocket = new ServerSocket(localPort);
                Socket dataSocket = serverSocket.accept();

                // Формирование порта для команды PORT (в формате h1,h2,h3,h4,p1,p2)
                String portCommand = String.format("%s,%s,%s,%s,%s,%s",
                        localAddress.replace('.', ','), localPort / 256, localPort % 256,
                        localAddress.replace('.', ','), localPort / 256, localPort % 256);

                writer.write("PORT " + portCommand);
                writer.flush();
                BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                PrintWriter dataWriter = new PrintWriter(dataSocket.getOutputStream(), true);

                // Пример чтения ответа от сервера
                String response = dataReader.readLine();
                System.out.println("Server response: " + response);
//                String response = reader.readLine();
//                System.out.println("Server: " + response);
                mode = Mode.ACTIVE;
                serverSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void enablePassiveMode() {
        if (mode != Mode.PASSIVE) {
            try {
                writer.write("PASV\r\n");
                writer.flush();
                String response = reader.readLine();
                System.out.println("Server: " + response);

                int startIndex = response.indexOf("(");
                int endIndex = response.indexOf(")");
                String extractedValues = response.substring(startIndex + 1, endIndex);

                String[] parts = extractedValues.split(",");
                String serverAddress1 = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
                int serverPort1 = Integer.parseInt(parts[4]) * 256 + Integer.parseInt(parts[5]);

                // Вывод сообщения о включении пассивного режима
                System.out.println("Пассивный режим включен. Адрес: " + serverAddress1 + ", Порт: " + serverPort1);
                dataSocket = new Socket(serverAddress1, serverPort1);
                System.out.println(dataSocket.getInetAddress());
                mode = Mode.PASSIVE;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Passive mode already set!");
        }

    }

    public static void displayRemoteFiles() {
        if (!isConnected) {
            System.out.println("Not connected to the server.");
            return;
        }
        try {
            // Отправляем команду LIST серверу
            writer.write("LIST\r\n");
            writer.flush();

            // Читаем ответ сервера
            String response = reader.readLine();
            System.out.println("Server: " + response);

            // Дожидаемся ответа сервера 150, который указывает на начало передачи данных
            if (response.startsWith("150")) {
                // Устанавливаем соединение данных (в данном случае, предполагаем пассивный режим)
//                dataOutputStream = dataSocket.getOutputStream();
//                Socket dataSocket = getDataSocket();

                // Читаем данные о списках файлов и директорий
                BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                String line;
                while ((line = dataReader.readLine()) != null) {
                    System.out.println(line); // Выводим каждую строку списка на консоль
                }

                // Закрываем соединение данных и читателя
                dataReader.close();
//                dataSocket.close();

                // Читаем ответ сервера 226, который указывает на завершение передачи данных
                response = reader.readLine();
                System.out.println("Server: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
