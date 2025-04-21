package com.jiduauto.cdc.sds.tools.jidullmsdk.ttsclient;

import java.io.*;

public class PcmToWavConverter {

    public static void main(String[] args) {
        String pcmFilePath = "D:\\workspace\\cdc-sds-tools-protocolconvert\\2.pcm";
        String wavFilePath = "output.wav";
        int sampleRate = 22050; // 采样率
        int channels = 1; // 声道数
        int bitDepth = 16; // 采样位数

        try {
            convertPcmToWav(pcmFilePath, wavFilePath, sampleRate, channels, bitDepth);
            System.out.println("PCM 文件已成功转换为 WAV 文件。");
        } catch (IOException e) {
            System.out.println("转换过程中出现错误: " + e.getMessage());
        }
    }

    public static void convertPcmToWav(String pcmFilePath, String wavFilePath, int sampleRate, int channels, int bitDepth) throws IOException {
        try (FileInputStream pcmInputStream = new FileInputStream(pcmFilePath);
             FileOutputStream wavOutputStream = new FileOutputStream(wavFilePath)) {

            // 计算音频数据的字节数
            int dataSize = pcmInputStream.available();

            // 写入 WAV 文件头
            writeWavHeader(wavOutputStream, sampleRate, channels, bitDepth, dataSize);

            // 复制 PCM 数据到 WAV 文件
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = pcmInputStream.read(buffer)) != -1) {
                wavOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static void writeWavHeader(OutputStream outputStream, int sampleRate, int channels, int bitDepth, int dataSize) throws IOException {
        int blockAlign = channels * (bitDepth / 8);
        int byteRate = sampleRate * blockAlign;

        // 写入 RIFF 头
        outputStream.write("RIFF".getBytes());
        outputStream.write(intToByteArray(36 + dataSize), 0, 4);
        outputStream.write("WAVE".getBytes());

        // 写入 fmt 块
        outputStream.write("fmt ".getBytes());
        outputStream.write(intToByteArray(16), 0, 4);
        outputStream.write(shortToByteArray((short) 1), 0, 2);
        outputStream.write(shortToByteArray((short) channels), 0, 2);
        outputStream.write(intToByteArray(sampleRate), 0, 4);
        outputStream.write(intToByteArray(byteRate), 0, 4);
        outputStream.write(shortToByteArray((short) blockAlign), 0, 2);
        outputStream.write(shortToByteArray((short) bitDepth), 0, 2);

        // 写入 data 块
        outputStream.write("data".getBytes());
        outputStream.write(intToByteArray(dataSize), 0, 4);
    }

    private static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };
    }

    private static byte[] shortToByteArray(short value) {
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF)
        };
    }
}    