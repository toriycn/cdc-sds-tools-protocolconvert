package com.changan.cdc.sds.tools.jidullmsdk.ttsclient;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TTSUtil {

    public static byte[] submit_end(TtsendRequest ttsRequest)  {
        // LogUtils.e(TAG,"CATTS submit_end start");
        String json = JSONObject.toJSONString(ttsRequest);
        // LogUtils.e(TAG,"submit_end request: {}", json);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        byte[] header = {0x11, 0x00};
        int eventType = 2;
        byte[] packedEventType = packInt32BigEndian(eventType);
        ByteBuffer requestByte = ByteBuffer.allocate(header.length + packedEventType.length + Integer.BYTES + jsonBytes.length);
        requestByte.put(header).put(packedEventType).putInt(jsonBytes.length).put(jsonBytes);
        return requestByte.array();
    }

    public static byte[] submit_send(TtssendRequest ttsRequest)  {
        String json = JSONObject.toJSONString(ttsRequest);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        byte[] header = {0x11, 0x00};
        int eventType = 200;
        byte[] packedEventType = TTSUtil.packInt32BigEndian(eventType);
        ByteBuffer requestByte = ByteBuffer.allocate(header.length + packedEventType.length + Integer.BYTES + jsonBytes.length);
        requestByte.put(header).put(packedEventType).putInt(jsonBytes.length).put(jsonBytes);
        return requestByte.array();
    }

    public static String readJsonFile(String filePath) {
        StringBuilder jsonContent = new StringBuilder();
        Charset fileCharset = Charset.forName("UTF-8");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), fileCharset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonContent.toString();
    }

    public static byte[] submit_start(TtsstartRequest ttsRequest) {
        String json = JSONObject.toJSONString(ttsRequest);
        // LogUtils.e(TAG,"submit_start request: {}", json);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        byte[] header = {0x11, 0x00};
        int eventType = 1;
        byte[] packedEventType = packInt32BigEndian(eventType);
        ByteBuffer requestByte = ByteBuffer.allocate(header.length + packedEventType.length + Integer.BYTES + jsonBytes.length);
        requestByte.put(header).put(packedEventType).putInt(jsonBytes.length).put(jsonBytes);
        return requestByte.array();
    }

    public static byte[] packInt32BigEndian(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(value);
        return buffer.array();
    }

}
