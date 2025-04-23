package com.changan.cdc.sds.tools.jidullmsdk.test;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.BaseRequestManage;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

public class LLMRequestManageIFlyTecAudioTest {

    private static final String contexts_JSON = "[{\"header\":{\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"env\":\"Test\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SDU240409.0412.86E\",\"timestamp\":\"1712637741041\",\"vehicleType\":\"MARS\"}},{\"header\":{\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区\",\"song_artist\":\"TFBOYS\",\"song_name\":\"剩下的盛夏\",\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}]";
    private static final String botSession_JSON = "[{\"asr\":\"音量调到30%\",\"dstData\":[{\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"systemControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.99998796,\"domain\":\"systemControl\",\"intent\":\"SET_VOLUME\",\"slots\":[{\"name\":\"ratio\",\"normalizedValue\":\"0.30\",\"value\":\"30%\"}]}}],\"isHitVts\":false,\"sn\":\"75ce8f97-41b0-4eb9-a6eb-d72bb71e403f-0_2\",\"source\":\"ASR_OFFLINE\",\"timestamp\":1704793156240},{\"asr\":\"退下\",\"dstData\":[{\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"generalControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":1,\"domain\":\"generalControl\",\"intent\":\"QUIT_VOICE\"}}],\"isHitVts\":false,\"sn\":\"3ce97010-86ab-45c1-8bef-9ffcd9cdb32c-0_3\",\"source\":\"ASR_OFFLINE\",\"timestamp\":1704793190258},{\"asr\":\"打开地图\",\"dstData\":[{\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"windowControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":1,\"domain\":\"windowControl\",\"intent\":\"GOTO_PAGE\",\"slots\":[{\"name\":\"name\",\"normalizedValue\":\"BAIDU_MAP\",\"value\":\"地图\"},{\"name\":\"action\",\"normalizedValue\":\"OPEN\",\"value\":\"打开\"}]}}],\"isHitVts\":false,\"sn\":\"cf3830da-c036-4a0e-a483-ba887e7967bb-0\",\"source\":\"JIDU_ONLINE\",\"timestamp\":1704793193194}]";

    public static void main(String[] args) throws Exception {
        BaseRequestManage manage = BaseRequestManage.getInstance();
//        manage.setBaseUrl( "wss://hydra-gateway.jiduapp.cn/ap/hydra-gateway/websocket/cerence/123456");
//        manage.setBaseUrl("ws://43.242.203.169:8080/websocket/cerence/123456");
        manage.setBaseUrl("ws://127.0.0.1:8088/websocket/customize/123456");

        CommunicateWebSocketEvent event = new CommunicateWebSocketEvent() {
            @Override
            public void onReceiveMsg(BaseDirective payload) {
                System.out.println(JSONObject.toJSONString(payload));
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
        manage.setpManageEvent(event);
        String asrSn = UUID.randomUUID().toString();
        Thread r1 = new Thread() {
            @Override
            public void run() {
                try {
                    sendAudioFrames(1, asrSn);
                    manage.sendRequest(asrSn,sendFinishFrame(asrSn));
                    Thread.sleep(50000);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };

        r1.start();

//        r2.start();
//        r3.start();
        Thread.sleep(100000000);
        manage.sendRequest(asrSn, sendFinish());

//        manage.endAudioZone(2);
//        manage.endAudioZone(3);
        manage.exit();
    }
    /**
     * STEP 2.2 实时发送音频数据帧
     */
    public static void sendAudioFrames(int audioZone, String asrSn) throws Exception {
        InputStream inputStream = null;
//        String filename = "16k-0.pcm";
        String filename = "weather2.pcm";
        File file = new File(filename);
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }
        int bytesPerFrame = Util.BYTES_PER_FRAME; // 一个帧 160ms的音频数据
        byte[] buffer = new byte[bytesPerFrame];
        int readSize;
        int i = 0;
        long nextFrameSendTime = System.currentTimeMillis();
        do {
            // 数据帧之间需要有间隔时间， 间隔时间为上一帧的音频长度
            Util.sleep(nextFrameSendTime - System.currentTimeMillis());
            try {
                readSize = inputStream.read(buffer);
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                readSize = -2;
            }
            if (readSize > 0) { // readSize = -1 代表流结束
                JSONObject json = new JSONObject();

                json.put("version", "1.0");
                json.put("asr_sn", asrSn);
                JSONObject asrParameters = new JSONObject();
                asrParameters.put("audio_format", "pcm");
                asrParameters.put("sample_rate", 16000);
                asrParameters.put("audio_index", i++);
                asrParameters.put("audio_data", Base64.getEncoder().encodeToString(buffer));
                asrParameters.put("contexts", JSONObject.parseArray(contexts_JSON));
                asrParameters.put("botSession", JSONObject.parseArray(botSession_JSON));
                json.put("asr_params",asrParameters);
                nextFrameSendTime = System.currentTimeMillis() + Util.bytesToTime(readSize);
                BaseRequestManage.getInstance().sendRequest(asrSn, json.toString());
            }
        } while (readSize >= 0);
    }

    /**
     * STEP 2.4 发送结束帧
     *
     * @throws JSONException Json解析错误
     */
    public static String sendFinishFrame(String asrSn) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("access_token", "123456");
        json.put("version", "1.0");
        json.put("asr_sn", asrSn);
        JSONObject asrParameters = new JSONObject();
        asrParameters.put("audio_format", "pcm");
        asrParameters.put("sample_rate", 16000);
        asrParameters.put("audio_index", -1);
        asrParameters.put("contexts", JSONObject.parseArray(contexts_JSON));
        asrParameters.put("botSession", JSONObject.parseArray(botSession_JSON));
        json.put("asr_params",asrParameters);
        return json.toString();
    }
    public static String sendFinish() {
        JSONObject json = new JSONObject();
        json.put("type","FINISH");
        return json.toString();
    }
}
