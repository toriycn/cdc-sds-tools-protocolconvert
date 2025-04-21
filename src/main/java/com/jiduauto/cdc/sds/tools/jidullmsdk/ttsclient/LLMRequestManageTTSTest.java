package com.jiduauto.cdc.sds.tools.jidullmsdk.ttsclient;

import com.alibaba.fastjson.JSONObject;
import com.jiduauto.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.jiduauto.cdc.sds.tools.jidullmsdk.NetRequestParameter;
import com.jiduauto.cdc.sds.tools.jidullmsdk.asrclient.LLMRequestManageASRClient;
import com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk.BaseRequestManage;
import com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;
import com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk.multiconnect.BaseMultiRequestManageFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class LLMRequestManageTTSTest {

    BaseMultiRequestManageFactory manageFactory = BaseMultiRequestManageFactory.getInstance();

    //发送队列
    private ConcurrentHashMap<String,TtsRequestObject> concurrentHashMap = new ConcurrentHashMap();

    private ConcurrentHashMap<String,TtsRequestObject> resultConcurrentHashMap = new ConcurrentHashMap();
    private Selector selector = null;
    private AtomicBoolean runState = new AtomicBoolean(false);
    private String url = "wss://z1-pre-tusys.sda.changan.com.cn:443/tu-apigw/tts/api/v1/tu/text-to-speech/bidirection";
    public LLMRequestManageTTSTest(){
        try {
            selector = Selector.open();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        CommunicateWebSocketEvent event = new CommunicateWebSocketEvent() {
            @Override
            public void onReceiveMsg(BaseDirective payload) {
                String base64 =  payload.getPayload();
                byte[] bytes = Base64.getDecoder().decode(base64);
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                System.out.println(JSONObject.toJSONString(payload));
                int protocolVersion = (byteBuffer.get(0) & 0xff) >> 4;
                int messageType = (byteBuffer.get(1) & 0xff) >> 4;
                int messageTypeSpecificFlags = byteBuffer.get(1) & 0x0f;
                byte[] fourByte = new byte[4];
                byte[] header = new byte[2];
                byteBuffer.get(header, 0, 2);

                int version = (int) ((header[0] & 0xFF) >> 4);
                int headerSize = (int) ((header[0] & 0x0F) * 2);
                byte[] events = new byte[4];
                // bytes.position(2);
                byteBuffer.get(events, 0, 4);
                int intValue = ByteBuffer.wrap(events).getInt();
                int payload_start = headerSize + 4;
                byteBuffer.position(payload_start);
                byte[] payload_len_b = new byte[4];
                byteBuffer.get(payload_len_b, 0, 4);
                int payload_len = ByteBuffer.wrap(payload_len_b).getInt();

                byteBuffer.position(payload_start+4);
                byte[] payload_b = new byte[payload_len];
                byteBuffer.get(payload_b, 0, payload_len);
                boolean sentence_end = false;
                if(payload_len !=2 ){
                    String payload_str = new String(payload_b);
                    JSONObject jsonObject = JSONObject.parseObject(payload_str);
                    String data = (String) jsonObject.getString("data");
                    System.out.println(data);
                    JSONObject data_j = JSONObject.parseObject(data);
                    String sentence_idx = (String) data_j.getString("sentence_idx");
                    String tts_data = (String) data_j.getString("tts_data");
                    sentence_end = (boolean) data_j.getBoolean("sentence_end");

                    TtsRequestObject requestObject = resultConcurrentHashMap.get(payload.getConnnectionSn());

                    String[] index =sentence_idx.split("_");
                    int total = Integer.parseInt(index[0]);
                    requestObject.setSetenceTotal(total);
                    requestObject.getTts_result().add(Integer.parseInt(index[1]),tts_data);
//                    pEvent.onTTSData(total,Integer.parseInt(index[1]),sentence_end,tts_data);
                    if(sentence_end){
                        writeBytesToPcmFile(requestObject);
                        stop(payload.getConnnectionSn());
                    }
                }
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
                Set<Map.Entry<String, TtsRequestObject>> setWsMapEntity = resultConcurrentHashMap.entrySet();
                for(Map.Entry<String, TtsRequestObject> item : setWsMapEntity) {
                    long receiveTime = item.getValue().getReceiveTime();
                   if(System.currentTimeMillis()-receiveTime >10*1000){
                       //10s没有合成完，删除掉这个任务
                       resultConcurrentHashMap.remove(item.getKey());
                   }
                }
            }
        };
        manageFactory.setUrlEvent(url,event);
    }

    public static void writeBytesToPcmFile(TtsRequestObject requestObject) {
        String filePath = requestObject.getSid()+".pcm";

        File pcmFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(pcmFile)) {
            for(String ttsResult :requestObject.getTts_result()){
                // 将 byte 数组写入文件
                fos.write(Base64.getDecoder().decode(ttsResult)); fos.flush();
            }
           fos.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void start(){
        runState.set(true);
        new Thread("BaseRequestManage-Task-Check"){
            @Override
            public void run() {
                try{
                    while(runState.get()){
                        selector.select(NetRequestParameter.DEFAULT_MANAGE_CHECKUNSENDMSG_SLEEPTIME);
                        checkUnsendMsg();
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    public void create(String sid,String text){
        if(concurrentHashMap.contains(sid)){
            return ;
        }
        TtsRequestObject requestObject = new TtsRequestObject();
        requestObject.setSid(sid);
        requestObject.setText(text);
        requestObject.setType(0);
        requestObject.setReceiveTime(System.currentTimeMillis());
        concurrentHashMap.put(sid,requestObject);
        TtsRequestObject requestObject2 = new TtsRequestObject();
        requestObject2.setSid(sid);
        requestObject2.setText(text);
        requestObject2.setType(1);
        requestObject2.setReceiveTime(System.currentTimeMillis());
        concurrentHashMap.put(sid+"_1",requestObject2);

        resultConcurrentHashMap.put(sid,requestObject);
        selector.wakeup();
    }

    public void stop(String sid){
        this.manageFactory.enSession(url,sid);
        //不论哪种情况，都不再发送了
        resultConcurrentHashMap.remove(sid);
        concurrentHashMap.remove(sid);
    }

    public void destory(){
        runState.set(false);
        try {
            selector.wakeup();
            selector.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void checkUnsendMsg() {
        Set<Map.Entry<String, TtsRequestObject>> setWsMapEntity = concurrentHashMap.entrySet();
        for(Map.Entry<String, TtsRequestObject> item : setWsMapEntity){
            item.getValue().setSendTime(System.currentTimeMillis());
            if(item.getValue().getType() ==0 ){
                String speaker = "tongtong_huankuai";
                String tuid =  "00000";
                TtsstartRequest start_ttsRequest = TtsstartRequest.builder()
                        .req_params(TtsstartRequest.Req_params.builder()
                                .speaker(speaker)
                                .ref_audio_path("wav/tongtong_normal.wav")
                                .prompt_text("当然适合，他的功能非常全面，即使是小公司也能受益匪浅。")
                                .prompt_lang("zh")
                                .batch_size("1")
                                .text_split_method("cut5")
                                .media_type("raw")
                                .streaming_mode("true")
                                .tuid(tuid)
                                .build())
                        .build();
                manageFactory.sendRequest(url,item.getKey(), TTSUtil.submit_start(start_ttsRequest));
            }
            else if(item.getValue().getType()== -1){
                TtsendRequest end_ttsRequest = TtsendRequest.builder()
                        .req_params(TtsendRequest.Req_params.builder()
                                .indentation(" ")
                                .build())
                        .build();
                manageFactory.sendRequest(url,item.getKey(), TTSUtil.submit_end(end_ttsRequest));
            }
            else{
                String[] itemkey = item.getKey().split("_");
                TtssendRequest send_ttsRequest = TtssendRequest.builder()
                        .req_params(TtssendRequest.Req_params.builder()
                                .text(item.getValue().getText())
                                .text_lang("zh")
                                .stop_tag("")
                                .first_sentence(true)
                                .build())
                        .build();
                manageFactory.sendRequest(url,itemkey[0], submit_send(send_ttsRequest));
            }
            concurrentHashMap.remove(item.getKey());
        }
    }
    public static byte[] submit_send(TtssendRequest ttsRequest)  {
        String json = JSONObject.toJSONString(ttsRequest);
        // LogUtils.e(TAG,"submit_send request: {}", json);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        byte[] header = {0x11, 0x00};
        int eventType = 200;
        byte[] packedEventType = TTSUtil.packInt32BigEndian(eventType);
        ByteBuffer requestByte = ByteBuffer.allocate(header.length + packedEventType.length + Integer.BYTES + jsonBytes.length);
        requestByte.put(header).put(packedEventType).putInt(jsonBytes.length).put(jsonBytes);
        return requestByte.array();
    }

    public static void main(String[] args) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 1; i++) {
//                    int CHUNCKED_SIZE = 1280;
//                    LLMRequestManageASRClient test = new LLMRequestManageASRClient();
//                    String END_FLAG = "--end--";
//                    String sid = i+"";
//                    String filepath = "./1.pcm";
//                    byte[] bytes = new byte[CHUNCKED_SIZE];
//                    try (RandomAccessFile raf = new RandomAccessFile(filepath, "r")) {
//                        int len = -1;
//                        while ((len = raf.read(bytes)) != -1) {
//                            if (len < CHUNCKED_SIZE) {
//                                bytes = Arrays.copyOfRange(bytes, 0, len);
//                            }
//                            test.send(sid,bytes);
//                            Thread.sleep(40);
//                        }
//                        test.send(sid, END_FLAG.getBytes());
////                test.send(sid, END_FLAG);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    test.stop();
//                }
//            }
//        }).start();
        LLMRequestManageTTSTest test = new LLMRequestManageTTSTest();

        test.start();

        test.create("1","2017年，入选StubHub平台“全球十大巡演票房畅销女歌手”，并获得Mnet亚洲音乐大奖“最佳亚洲艺人奖”。2021年，其歌曲《光年之外》的MV被吉尼斯世界纪录认证为“YouTube上观看次数最多的中文音乐视频”。2022年发行音乐专辑《启示录》。2023年，举行邓紫棋“I AM GLORIA”世界巡回演唱会。2025年4月2日，成为签约作家。");
        test.create("1","导航到四通桥");
        test.create("3","你是谁");
        test.create("4","你是谁");
        test.create("5","你是谁");

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        test.destory();
    }

}
