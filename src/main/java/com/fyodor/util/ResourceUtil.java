package com.fyodor.util;


import com.fyodor.ftp.FtpClient;

import java.io.IOException;

public class ResourceUtil {
    public static void closingResources() {
        InputUtil.closeResources();
        try {
            if (FtpClient.socket != null) FtpClient.socket.close();
            if (FtpClient.writer != null) FtpClient.writer.close();
            if (FtpClient.reader != null) FtpClient.reader.close();
            if (FtpClient.dataSocket != null) FtpClient.dataSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
