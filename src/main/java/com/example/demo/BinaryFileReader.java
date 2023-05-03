package com.example.demo;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BinaryFileReader {

    private String fileName;

    public BinaryFileReader(String fileName) {
        this.fileName = fileName;
    }

    public String readFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);

        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[8];
        int readBytes = 0;
        while ((readBytes = dataInputStream.read(buffer)) != -1) {
            String binaryString = new String(buffer, 0, readBytes);
            int intValue = Integer.parseInt(binaryString, 2);
            stringBuilder.append((char) intValue);
        }

        dataInputStream.close();
        fileInputStream.close();

        return stringBuilder.toString();
    }
}
