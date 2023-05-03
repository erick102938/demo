package com.example.demo;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryTextWriter {

    private String fileName;

    public BinaryTextWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(String data) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

        byte[] bytes = data.getBytes();
        for (byte b : bytes) {
            String binaryString = Integer.toBinaryString(b);
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }
            dataOutputStream.writeBytes(binaryString);
        }

        dataOutputStream.close();
        fileOutputStream.close();
    }
}

