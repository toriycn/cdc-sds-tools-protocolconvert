package com.changan.cdc.sds.tools.jidullmsdk.asrclient;

import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.multiconnect.BaseMultiRequestManageFactory;

import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class RequestManageASRClient {

    private static final String BASE_URL = "ws://121.199.45.98:19000/iflytek/v1/autocar";
//    private static final String BASE_URL = "ws://pre-sds.sda.changan.com.cn/dubhe/dubhe-gateway/iflytek/v1/autoCar";
//    private static final String BASE_URL = "ws://dev-edc.sda.changan.com.cn/dubhe/dubhe-gateway/iflytek/v1/autoCar";
//    iflytek/v1/autoCar
    private static final String param = "{\"aue\":\"raw\",\"auth_id\":\"c73b57ffdc513bab6ed829b023b97769\",\"sample_rate\":\"16000\",\"result_level\":\"complete\",\"data_type\":\"audio\",\"scene\":\"main\",\"pers_param\":\"{\\\"uid\\\":\\\"c73b57ffdc513bab6ed829b023b97769\\\"}\"}";
    // APIKEY，见AIUI开放平台
    private static final String APIKEY = "679e74322ecb0eabaa9fe63798986bf0";
    private static final String APPID = "ae13b36d";
    BaseMultiRequestManageFactory manageFactory = BaseMultiRequestManageFactory.getInstance();
    String url = BASE_URL + getHandShakeParams();

    public RequestManageASRClient(){
        CommunicateWebSocketEvent event = new CommunicateWebSocketEvent() {
            @Override
            public void onReceiveMsg(BaseDirective payload) {
                System.out.println(payload);
            }

            @Override
            public void onOpen(BaseDirective payload) {
                System.out.println(JSONObject.toJSONString(payload));
            }

            @Override
            public void onClosing(BaseDirective payload) {
                System.out.println(JSONObject.toJSONString(payload));
            }

            @Override
            public void onClosed(BaseDirective payload) {
                System.out.println(JSONObject.toJSONString(payload));
            }

            @Override
            public void onError(BaseDirective payload) {
                System.out.println(JSONObject.toJSONString(payload));
            }

            @Override
            public void onCheckTimeout() {

            }
        };

        manageFactory.setUrlEvent(url,event);
    }

    private static String getHandShakeParams() {
        String paramBase64 = null;
        try {
            paramBase64 = new String(Base64.getEncoder().encode(param.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String curtime = System.currentTimeMillis() / 1000L + "";
        String signtype = "sha256";
        String originStr = APIKEY + curtime + paramBase64;
        String checksum = getSHA256Str(originStr);
        String handshakeParam = "?appid=" + APPID + "&checksum=" + checksum + "&curtime=" + curtime + "&param=" + paramBase64 + "&signtype=" + signtype;
        System.out.println("appid=" + APPID + "\nchecksum=" + checksum + "\ncurtime=" + curtime + "\nparam=" + paramBase64 + "\nsigntype=" + signtype);
        return handshakeParam;
    }

    /**
     * 利用Apache的工具类实现SHA-256加密
     * @param str
     * @return
     */
    private static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = bytesToHex(hash);
            System.out.println(str);
            System.out.println(encdeStr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(byteToHex(b));
        }
        return result.toString();
    }
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String byteToHex(byte b) {
        char[] hexChars = new char[2];
        int v = b & 0xFF;
        hexChars[0] = HEX_ARRAY[v >>> 4];
        hexChars[1] = HEX_ARRAY[v & 0x0F];
        return new String(hexChars);
    }
    public void send(String sid,byte[] audioMsg){
        manageFactory.sendRequest(url,sid, audioMsg);
    }
    public void send(String sid,String audioMsg){
        manageFactory.sendRequest(url,sid, audioMsg);
    }
    public void stop(){
        manageFactory.exit(url);
    }


    public static void main(String[] args) {

        for (int i = 0; i < 1; i++) {
            int CHUNCKED_SIZE = 1280;
            RequestManageASRClient test = new RequestManageASRClient();
            String END_FLAG = "--end--";
            String sid = i+"";
            String filepath = "./0.pcm";
            byte[] bytes = new byte[CHUNCKED_SIZE];
            try (RandomAccessFile raf = new RandomAccessFile(filepath, "r")) {
                int len = -1;
                while ((len = raf.read(bytes)) != -1) {
                    if (len < CHUNCKED_SIZE) {
                        bytes = Arrays.copyOfRange(bytes, 0, len);
                    }
                    test.send(sid,bytes);
                    Thread.sleep(100);
                }
//                test.send(sid, END_FLAG.getBytes());
                test.send(sid, END_FLAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            test.stop();
        }

    }

}
