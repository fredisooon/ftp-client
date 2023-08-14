package com.fyodor.util;


import com.fyodor.ftp.FtpClient;

import java.io.IOException;

public class ResourceUtil {
    public static void closingResources() {
        InputUtil.closeResources();
        try {
            if (FtpClient.commandSocket != null) FtpClient.commandSocket.close();
            if (FtpClient.serverCommandWriter != null) FtpClient.serverCommandWriter.close();
            if (FtpClient.serverResponseReader != null) FtpClient.serverResponseReader.close();
            if (FtpClient.dataTransferSocket != null) FtpClient.dataTransferSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
