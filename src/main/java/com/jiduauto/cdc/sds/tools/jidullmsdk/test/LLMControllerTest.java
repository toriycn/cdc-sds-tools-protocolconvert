package com.jiduauto.cdc.sds.tools.jidullmsdk.test;

import com.alibaba.fastjson.JSONObject;
import com.jiduauto.cdc.sds.tools.jidullmsdk.JiduCommonRequestParameter;
import com.jiduauto.cdc.sds.tools.jidullmsdk.llmsdk.LLMBaseDirective;
import com.jiduauto.cdc.sds.tools.jidullmsdk.llmsdk.LLMRequestManage;
import com.jiduauto.cdc.sds.tools.jidullmsdk.llmsdk.LLMRequestResultEvent;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class LLMControllerTest {

    public static void main(String[] args) throws Exception {
        try{
            int i = 1/0;
        }catch (Throwable ex){
            System.out.println(ex instanceof ArithmeticException);
            ex.printStackTrace();
        }

//
//        LLMRequestManage magage = LLMRequestManage.getInstance();
////        JiduCommonRequestParameter.getInstance().setBaseUrl("wss://hydra-gateway.jiduapp.cn/ap/hydra-gateway/websocket/llm/11d046087cfd264e4301edff43fc7768");
////        JiduCommonRequestParameter.getInstance().setBaseUrl( "wss://apiprod.jiduapp.cn/api/hydra-gateway/websocket/llm/d4f14cbe4a96dd0f7b663bf55beb901f?sn=132649c5-921f-44c5-8091-3c2c5dda3b9e-0_27");
//        JiduCommonRequestParameter.getInstance().setBaseUrl( "ws://127.0.0.1:8088/websocket/llm/d4f14cbe4a96dd0f7b663bf55beb901f?sn=132649c5-921f-44c5-8091-3c2c5dda3b9e-0_27");
////        manage.setBaseUrl("wss://cdcsds.jiduapp.cn/api/hydra-gateway/websocket/llm/11d046087cfd264e4301edff43fc7768");
////
//        LLMRequestResultEvent requestResultEvent = new LLMRequestResultEvent() {
//            @Override
//            public void onReceiveLLM(LLMBaseDirective payload) {
//                System.out.println("test: msg"+JSONObject.toJSONString(payload));
//            }
//
//            @Override
//            public void onReceiveLLMClose(LLMBaseDirective payload) {
//                System.out.println("test close:"+JSONObject.toJSONString(payload));
//            }
//
//            @Override
//            public void onConnectionError(LLMBaseDirective payload) {
//                System.out.println("test error:"+JSONObject.toJSONString(payload));
//            }
//
//            @Override
//            public void onReceiveLLMError(LLMBaseDirective payload) {
//                System.out.println("test llm error:"+JSONObject.toJSONString(payload));
//            }
//
//            @Override
//            public void onReceiveLLMConnectionTimeout(LLMBaseDirective payload) {
//                System.out.println((JSONObject.toJSONString(payload)));
//            }
//        };
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .pingInterval(3,TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(2,10,TimeUnit.SECONDS))
//                .build();
//        JiduCommonRequestParameter.getInstance().setOkHttpClient(okHttpClient);
//        LLMRequestManage.getInstance().setpEvent(requestResultEvent);
//        for(int i=0;i<12;i++){
//            String asrSn = UUID.randomUUID().toString();
//            int error=  magage.sendMsg(asrSn,2,sendAudioFrames("怎么打开车门",asrSn));
////                        int error=  magage.sendMsg("fake-012b7e0b-7775-4dab-bc00-e0f161dce10d-0_3",2,context_query);
////            System.out.println("dddddd:"+error);
////            Thread.sleep(60000);
//            Thread.sleep(20*1000);
////            error=  magage.sendMsg("fake-012b7e0b-7775-4dab-bc00-e0f161dce10d-0_3",2,context_query);
////            Thread.sleep(20*1000);
////            manage.endAudioZone(0);
////            Thread.sleep(5000);
//        }
//        Thread.sleep(30000000);
//        System.out.println("stop zone");

    }

    public static String sendAudioFrames(){
        String msg = "{\"asrRewrite\":\"周杰伦是谁[SEP]他老婆是谁\",\"asrSn\":\"6997e6b8-5eab-480a-bd65-45f162928209-2_3\",\"audioZoneIndex\":2,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[{\"asr\":\"周杰伦是谁\",\"asrOutputRewrite\":\"周杰伦是谁\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"周杰伦是中国台湾流行乐男歌手、音乐人、演员、导演、编剧。\"},\"dmStatus\":{\"entitySlots\":[{\"name\":\"person\",\"normalizedValue\":\"周杰伦\",\"value\":\"周杰伦\"}],\"isMultiForce\":false,\"skill\":\"lifeService\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":100.0,\"domain\":\"lifeService\",\"intent\":\"CHARACTER_ENCYCLOPEDIA\",\"slots\":[{\"name\":\"person\",\"normalizedValue\":\"周杰伦\",\"value\":\"周杰伦\"}]}}],\"isHitVts\":false,\"sn\":\"6997e6b8-5eab-480a-bd65-45f162928209-2_2\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1720422067699}]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"Context_Query\",\"audioZoneIndex\":0,\"currentNaviLevel\":0,\"dumiContinue\":0,\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SRU240708.0143.91E\",\"timestamp\":\"1720422072266\",\"vehicleType\":\"MARS\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区\",\"latitude\":31.35722,\"longitude\":121.22704,\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"他老婆是谁\",\"asrRewrite\":\"周杰伦是谁[SEP]他老婆是谁\",\"asrSn\":\"6997e6b8-5eab-480a-bd65-45f162928209-2_3\",\"confidence\":1.0,\"domain\":\"context\",\"intent\":\"CONTEXT\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"MODEL\",\"nluTime\":0,\"nluType\":\"Online\",\"origin\":\"\",\"platform\":\"Jidu\",\"slots\":[],\"version\":\"v2.0\",\"voiceZone\":2}],\"query\":\"他老婆是谁\",\"sessionId\":\"\",\"timestamp\":1720422072265,\"vid\":\"1d4dfbfb0ffd429fe10380eca9d9e7d3\"}";
        return msg;
    }

    private final static String context_query = "{\"asrRewrite\":\"打开小宇宙[SEP]播放第一个\",\"asrSn\":\"fake-012b7e0b-7775-4dab-bc00-e0f161dce10d-0_3\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[{\"asr\":\"打开小宇宙\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"还没有安装过这个应用。\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"windowControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":1.0,\"domain\":\"windowControl\",\"intent\":\"GOTO_PAGE\",\"slots\":[{\"name\":\"name\",\"normalizedValue\":\"MICROCOSM\",\"value\":\"小宇宙\"},{\"name\":\"action\",\"normalizedValue\":\"OPEN\",\"value\":\"打开\"}]}}],\"isHitVts\":false,\"sn\":\"cbffd08a-edb5-4cdc-8762-9aba0fc9f289-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718327935704}]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"Context_Query\",\"audioZoneIndex\":0,\"dumiContinue\":0,\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"cb1851660a68c0a30323cfd37f4cf061\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SRU240606.2343.76P\",\"timestamp\":\"1718327965882\",\"vehicleType\":\"MARS\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区\",\"latitude\":31.36162,\"longitude\":121.24046,\"song_artist\":\"ReoNa\",\"song_name\":\"Untitled world (无题世界)\",\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"播放第一个\",\"asrRewrite\":\"打开小宇宙[SEP]播放第一个\",\"asrSn\":\"012b7e0b-7775-4dab-bc00-e0f161dce10d-0_3\",\"confidence\":0.95,\"domain\":\"context\",\"intent\":\"CONTEXT\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"origin\":\"播放第一个\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240322-8d70a7e6\",\"voiceZone\":0}],\"query\":\"播放第一个\",\"sessionId\":\"\",\"timestamp\":1718327965881,\"vid\":\"0ba3589d30db678c777606abe9cd69e6\"}";
    /**
     * STEP 2.2 实时发送音频数据帧
     *
     */
    public static String sendAudioFrames(String query,String asrSn) {
        String text = "{\n"
                + "    \"asrRewrite\": \"{{QUERY}}\",\n"
                + "    \"asrSn\": \"{{asrSn}}\",\n"
                + "    \"audioZoneIndex\": 0,\n"
                + "    \"botSessionList\": [\n"
                + "        {\n"
                + "            \"audioZoneIndex\": 0,\n"
                + "            \"sessionList\": [\n"
                + "\n"
                + "            ]\n"
                + "        },\n"
                + "        {\n"
                + "            \"audioZoneIndex\": 1,\n"
                + "            \"sessionList\": [\n"
                + "\n"
                + "            ]\n"
                + "        },\n"
                + "        {\n"
                + "            \"audioZoneIndex\": 2,\n"
                + "            \"sessionList\": [\n"
                + "\n"
                + "            ]\n"
                + "        },\n"
                + "        {\n"
                + "            \"audioZoneIndex\": 3,\n"
                + "            \"sessionList\": [\n"
                + "\n"
                + "            ]\n"
                + "        }\n"
                + "    ],\n"
                + "    \"client_context\": [\n"
                + "        {\n"
                + "            \"header\": {\n"
                + "                \"dialogRequestId\": \"\",\n"
                + "                \"messageId\": \"\",\n"
                + "                \"name\": \"JiduNgdState\",\n"
                + "                \"namespace\": \"ai.dueros.device_interface.thirdparty.jidu\"\n"
                + "            },\n"
                + "            \"payload\": {\n"
                + "                \"arbitrateResult\": \"JIYUE_KNOWLEDGE\",\n"
                + "                \"audioZoneIndex\": 0,\n"
                + "                \"dumiContinue\": 0,\n"
                + "                \"env\": \"Test\",\n"
                + "                \"isGroupChatOpen\": false,\n"
                + "                \"isInGroupChatState\": false,\n"
                + "                \"isInWenXinYiYanModel\": false,\n"
                + "                \"isInside\": true,\n"
                + "                \"isIntegrate\": false,\n"
                + "                \"isWenXinNlu\": false,\n"
                + "                \"isdrive\": false,\n"
                + "                \"jdvsVersion\": \"2.0.0.1\",\n"
                + "                \"jidutoken\": \"dcaa10b40eb11861aa83360c17be7efd\",\n"
                + "                \"protocolversion\": \"2.0.0\",\n"
                + "                \"speechAppVersion\": \"61SDU240603.0253.98E\",\n"
                + "                \"timestamp\": \"1717396762643\",\n"
                + "                \"vehicleType\": \"MARS\"\n"
                + "            }\n"
                + "        },\n"
                + "        {\n"
                + "            \"header\": {\n"
                + "                \"dialogRequestId\": \"\",\n"
                + "                \"messageId\": \"\",\n"
                + "                \"name\": \"SpeechState\",\n"
                + "                \"namespace\": \"ai.dueros.device_interface.thirdparty.jidu\"\n"
                + "            },\n"
                + "            \"payload\": {\n"
                + "                \"current_poi\": \"北京市东城区\",\n"
                + "                \"latitude\": 39.90876,\n"
                + "                \"longitude\": 116.39759,\n"
                + "                \"video_playState\": \"paused\"\n"
                + "            }\n"
                + "        }\n"
                + "    ],\n"
                + "    \"finalNluResult\": [\n"
                + "        {\n"
                + "            \"asr\": \"{{QUERY}}\",\n"
                + "            \"asrRewrite\": \"{{QUERY}}\",\n"
                + "            \"asrSn\": \"{{asrSn}}\",\n"
                + "            \"confidence\": 100,\n"
                + "            \"domain\": \"knowledge\",\n"
                + "            \"intent\": \"JIYUE_KNOWLEDGE\",\n"
                + "            \"isHaveDirective\": false,\n"
                + "            \"isRejected\": true,\n"
                + "            \"nluSource\": \"MODEL\",\n"
                + "            \"nluTime\": 1717396762622,\n"
                + "            \"nluType\": \"Online\",\n"
                + "            \"origin\": \"\",\n"
                + "            \"platform\": \"Jidu\",\n"
                + "            \"slots\": [\n"
                + "\n"
                + "            ],\n"
                + "            \"version\": \"v2.0\",\n"
                + "            \"voiceZone\": 0\n"
                + "        }\n"
                + "    ],\n"
                + "    \"query\": \"{{QUERY}}\",\n"
                + "    \"sessionId\": \"\",\n"
                + "    \"timestamp\": 1717396762642,\n"
                + "    \"vid\": \"1d4dfbfb0ffd429fe10380eca9d9e7d3\"\n"
                + "}";
        text = text.replace("{{QUERY}}",query);
        text = text.replace("{{QUERY}}",query);
        text = text.replace("{{QUERY}}",query);
        text = text.replace("{{asrSn}}",asrSn);
        text = text.replace("{{asrSn}}",asrSn);
        JSONObject json = JSONObject.parseObject(text);
        return json.toString();
    }


}
