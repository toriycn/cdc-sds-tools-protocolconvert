package com.changan.cdc.sds.tools.jidullmsdk.test;

import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.BaseRequestManage;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;

public class LLMRequestManageLLMTest {


    public static void main(String[] args) throws Exception {
        BaseRequestManage manage = BaseRequestManage.getInstance();
//        manage.setBaseUrl( "wss://hydra-gateway.jiduapp.cn/ap/hydra-gateway/websocket/baidu/123456");
        manage.setBaseUrl("wss://cdcsds.jidustaging.com/api/hydra-gateway/websocket/llm/123456");
//        manage.setBaseUrl("ws://cdcsds.jiduapp.cn/api/hydra-gateway/websocket/baidu/123456");
//        manage.setBaseUrl("wss://apistaging.jiduapp.cn/api/hydra-gateway/websocket/llm/fake-vid");
//        manage.setBaseUrl("wss://apiprod.jiduapp.cn/api/hydra-gateway/websocket/llm/123456?sn=4d06bd3d351d46b394fa99041743f8e1");



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
        String asrSn = "c2dfac35-d85a-4dd6-9b02-f2fcf7cd0967-6";
        Thread r1 = new Thread() {
            @Override
            public void run() {
                try {
                    for(int k =0;k<10000;k++) {
//                    String msg = sendAudioFrames(asrSn);
//                        manage.sendRequest(asrSn, sendAudioFrames());
//                        Thread.sleep(300);
//                        manage.sendRequest(asrSn, sendFinish());
//                    manage.sendRequest(asrSn, jiyue_knowledge_404);

//                    manage.sendRequest(asrSn, wenxin_change);
//                    manage.sendRequest(asrSn, ask_current_media);
                    manage.sendRequest(asrSn, context_query);
//                    manage.sendRequest(asrSn, draw);
//                    manage.sendRequest(asrSn, navi);
//                    manage.sendRequest(asrSn, custom_skill);
//                    manage.sendRequest(asrSn, wenxin);
//                    manage.sendRequest(asrSn, wenxin_mode);
//                    manage.sendRequest(asrSn, diagnosis);

//                    manage.sendRequest(asrSn, wti);
                        Thread.sleep(180 * 1000);
                    }
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


//        manage.endAudioZone(2);
//        manage.endAudioZone(3);
        manage.exit();
    }
    public static final String jiyue_knowledge_404 = "{\"asrRewrite\":\"这个车零买多少\",\"asrSn\":\"b723d206-3a58-4f51-922a-024817be1a00-0_24\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"JIYUE_KNOWLEDGE\",\"audioZoneIndex\":0,\"battery\":\"16\",\"currentNaviLevel\":0,\"dumiContinue\":0,\"electric_drive_type\":\"6\",\"env\":\"Prod\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":true,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"cc3a519714bef0b66e0e4d8443a7b381\",\"protocolversion\":\"2.0.0\",\"car_wing\":\"2\",\"speechAppVersion\":\"61SRU240817.2143.33P\",\"car_sys_version\":\"V2.0.0\",\"timestamp\":\"1725875501496\",\"vehicleType\":\"MARS\",\"vehicle_type\":\"1\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区叶城路上\",\"latitude\":31.36243,\"longitude\":121.24505,\"navigate_poi\":\"普惠路\",\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"这个车零\n"
            + "买多少\",\"asrRewrite\":\"这个车零买多少\",\"asrSn\":\"b723d206-3a58-4f51-922a-024817be1a00-0_24\",\"confidence\":100.0,\"domain\":\"knowledge\",\"intent\":\"JIYUE_KNOWLEDGE\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"MODEL\",\"nluTime\":1725875501463,\"nluType\":\"Online\",\"origin\":\"\",\"platform\":\"Jidu\",\"slots\":[],\"version\":\"v2.0\",\"voiceZone\":0}],\"query\":\"这个车零买多少\",\"sessionId\":\"\",\"timestamp\":1725875501496,\"vid\":\"cc01e9bab5f564a5da27e67d5578ad6a\"}";
    public static final String wenxin_change = "{\"asrRewrite\":\"讲个笑话[SEP]换一个\",\"asrSn\":\"fake-change-joke\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[{\"asr\":\"反馈问题\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"请说\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"carInfo\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"carInfo\",\"intent\":\"USER_FEEDBACK\"}}],\"isHitVts\":false,\"sn\":\"52cb61c8-993c-400c-9f7d-b2a4557d4fbc-0_35\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1722841827163},{\"asr\":\"天气怎么样\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"上海今天发布高温橙色预警，全天多云转晴，28度～39度，和昨天差不多，当前空气质量指数50，空气挺好的。\"},\"dmStatus\":{\"entitySlots\":[{\"name\":\"time\",\"normalizedValue\":\"2024-08-05 00:00:00\",\"value\":\"2024-08-05 00:00:00\"}],\"isMultiForce\":false,\"skill\":\"weather\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":100.0,\"domain\":\"weather\",\"intent\":\"SYS_WEATHER\",\"slots\":[{\"name\":\"time\",\"normalizedValue\":\"2024-08-05 00:00:00\",\"value\":\"2024-08-05 00:00:00\"}]}}],\"isHitVts\":false,\"sn\":\"52cb61c8-993c-400c-9f7d-b2a4557d4fbc-0_37\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1722841841936},{\"asr\":\"讲个笑话\",\"asrOutputRewrite\":\"\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"有个数学家觉得自己很聪明，每天都跟镜子里的自己说：“你是我见过最聪明的人。”\\n结果有一天，镜子碎了，他从此得了低调病。\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"lifeService\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"lifeService\",\"intent\":\"JOKE\"}}],\"isHitVts\":false,\"sn\":\"52cb61c8-993c-400c-9f7d-b2a4557d4fbc-0_38\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1722841844345}]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"Context_Query\",\"audioZoneIndex\":0,\"battery\":\"16\",\"currentNaviLevel\":0,\"dumiContinue\":0,\"electric_drive_type\":\"6\",\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":true,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"ca4d8d3eafbd2807cc3b4dabbcbf5b81\",\"protocolversion\":\"2.0.0\",\"car_wing\":\"2\",\"speechAppVersion\":\"61SRU240803.2007.29P\",\"car_sys_version\":\"V2.0.0\",\"timestamp\":\"1722841846983\",\"vehicleType\":\"MARS\",\"vehicle_type\":\"1\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区内部路上\",\"latitude\":31.35286,\"longitude\":121.21454,\"navigate_poi\":\"基地\",\"song_artist\":\"183club\",\"song_name\":\"迷魂计\",\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"换一个\",\"asrRewrite\":\"讲个笑话[SEP]换一个\",\"asrSn\":\"52cb61c8-993c-400c-9f7d-b2a4557d4fbc-0_39\",\"confidence\":0.95,\"domain\":\"context\",\"intent\":\"CONTEXT\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"origin\":\"换一个\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240730-73a8804e\",\"voiceZone\":0}],\"query\":\"换一个\",\"sessionId\":\"\",\"timestamp\":1722841846982,\"vid\":\"0067264904b5cdcfe9ed45491e9cf7bd\"}";
    public static final String context_query = "{\"asrRewrite\":\"打开小宇宙[SEP]播放第一个\",\"asrSn\":\"fake-context\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[{\"asr\":\"打开小宇宙\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"还没有安装过这个应用。\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"windowControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":1.0,\"domain\":\"windowControl\",\"intent\":\"GOTO_PAGE\",\"slots\":[{\"name\":\"name\",\"normalizedValue\":\"MICROCOSM\",\"value\":\"小宇宙\"},{\"name\":\"action\",\"normalizedValue\":\"OPEN\",\"value\":\"打开\"}]}}],\"isHitVts\":false,\"sn\":\"cbffd08a-edb5-4cdc-8762-9aba0fc9f289-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718327935704}]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"Context_Query\",\"audioZoneIndex\":0,\"dumiContinue\":0,\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"cb1851660a68c0a30323cfd37f4cf061\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SRU240606.2343.76P\",\"timestamp\":\"1718327965882\",\"vehicleType\":\"MARS\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区\",\"latitude\":31.36162,\"longitude\":121.24046,\"song_artist\":\"ReoNa\",\"song_name\":\"Untitled world (无题世界)\",\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"播放第一个\",\"asrRewrite\":\"打开小宇宙[SEP]播放第一个\",\"asrSn\":\"012b7e0b-7775-4dab-bc00-e0f161dce10d-0_3\",\"confidence\":0.95,\"domain\":\"context\",\"intent\":\"CONTEXT\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"origin\":\"播放第一个\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240322-8d70a7e6\",\"voiceZone\":0}],\"query\":\"播放第一个\",\"sessionId\":\"\",\"timestamp\":1718327965881,\"vid\":\"0ba3589d30db678c777606abe9cd69e6\"}";
    public static final String ask_current_media = "{\"asrRewrite\":\"\",\"asrSn\":\"fake-ask-current-media\",\"audioZoneIndex\":0,\"botSession\":[{\"asr\":\"嘴往后点\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"好的。\"},\"dmStatus\":{\"entitySlots\":[],\"hotWords\":[],\"skill\":\"carControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"carControl\",\"intent\":\"SET_SEAT_ELECTRIC\",\"slots\":[{\"name\":\"feature\",\"normalizedValue\":\"SEAT\",\"value\":\"座椅\"},{\"name\":\"direction\",\"normalizedValue\":\"BACK\",\"value\":\"往后\"}]}}],\"sn\":\"05054e57-5f2e-4126-ba89-286c92cc4561-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718767828351},{\"asr\":\"播放一首快节奏的歌曲\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"正在播放。\"},\"dmStatus\":{\"entitySlots\":[],\"hotWords\":[],\"skill\":\"music\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"music\",\"intent\":\"CONTROL_MUSIC\",\"slots\":[{\"name\":\"action\",\"normalizedValue\":\"PLAY\",\"value\":\"播放\"},{\"name\":\"tag\",\"normalizedValue\":\"快节奏\",\"value\":\"快节奏\"}]}}],\"sn\":\"3dcbdd10-f28c-4035-b2e8-82538219e8b0-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718767835111},{\"asr\":\"换一个\",\"dstData\":[{\"customContext\":{\"isInside\":true},\"dmStatus\":{\"entitySlots\":[],\"hotWords\":[],\"skill\":\"generalControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":1.0,\"domain\":\"generalControl\",\"intent\":\"CHANGE\",\"slots\":[]}}],\"sn\":\"200c3df5-f628-403a-895a-d7f775fd6332-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718767841129}],\"client_context\":[{\"header\":{\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"Context_Query\",\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":\"1\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"1.4.0\",\"speechAppVersion\":\"61SRU240427.0009.99P\",\"timestamp\":\"1718767853051\",\"vehicleType\":\"MARS\"}},{\"header\":{\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区\",\"song_artist\":\"那吾克热-NW\",\"song_name\":\"儿子娃娃\",\"song_playState\":\"playing\",\"video_playState\":\"paused\"}}],\"offlineNluResult\":[{\"asr\":\"这首歌不错谁唱的\",\"asrRewrite\":\"\",\"asrSn\":\"7c228add-7de4-470e-87a9-ee56652296dd-0\",\"confidence\":0.95,\"domain\":\"mediaControl\",\"intent\":\"ASK_CURRENT_MEDIA\",\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"nluType\":\"Offline\",\"origin\":\"这首歌不错谁唱的\",\"platform\":\"Jidu\",\"slots\":[],\"version\":\"1.4/model-v1.4-20240310-212a734e\",\"voiceZone\":0}],\"query\":\"这首歌不错谁唱的\",\"sessionId\":\"\",\"timestamp\":1718767853051,\"vid\":\"744ba04a1471145ea011a66418152300\"}";
    public static final String jiyue_knowledge = "{\"asrRewrite\":\"\",\"asrSn\":\"fake-knowledge\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"JIYUE_KNOWLEDGE\",\"audioZoneIndex\":0,\"battery\":\"16\",\"currentNaviLevel\":0,\"dumiContinue\":0,\"electric_drive_type\":\"6\",\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"2.0.0\",\"car_wing\":\"2\",\"speechAppVersion\":\"61SDU240625.0216.51E\",\"car_sys_version\":\"-1\",\"timestamp\":\"1719477936964\",\"vehicleType\":\"VENUS\",\"vehicle_type\":\"1\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"北京市东城区\",\"latitude\":39.90876,\"longitude\":116.39759,\"navigate_poi\":\"天津市\",\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"车门怎么打开\",\"asrRewrite\":\"\",\"asrSn\":\"ea4065a3-d513-4a23-a6ea-34569979f02d-0\",\"confidence\":0.95,\"domain\":\"knowledge\",\"intent\":\"JIYUE_KNOWLEDGE\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"nluType\":\"Offline\",\"origin\":\"车门怎么打开\",\"platform\":\"Jidu\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240619-6c910bf5\",\"voiceZone\":0}],\"query\":\"车门怎么打开\",\"sessionId\":\"\",\"timestamp\":1719477936964,\"vid\":\"81d4436ee54f33eeece31e9bd5aac88e\"}";
    public static final String draw = "{\"asrRewrite\":\"\",\"asrSn\":\"fake-draw\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[{\"asr\":\"语音音量大一点\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"调好了。\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"systemControl\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"systemControl\",\"intent\":\"ADJ_VOLUME\",\"slots\":[{\"name\":\"adjustment\",\"normalizedValue\":\"ADD\",\"value\":\"大\"},{\"name\":\"object\",\"normalizedValue\":\"VOICE\",\"value\":\"语音\"}]}}],\"isHitVts\":false,\"sn\":\"57941d58-9aea-4ec2-915d-63b050d40c50-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718263249682},{\"asr\":\"画一幅中秋节合家美满赏月的画\t\",\"dstData\":[{\"customContext\":{\"isInside\":true},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"lifeService\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":100.0,\"domain\":\"lifeService\",\"intent\":\"DRAW\"}}],\"isHitVts\":false,\"sn\":\"57941d58-9aea-4ec2-915d-63b050d40c50-0_2\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718263253517},{\"asr\":\"画一幅中秋节合家美满赏月的画\t\",\"dstData\":[{\"customContext\":{\"isInside\":true},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"lifeService\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"lifeService\",\"intent\":\"DRAW\"}}],\"isHitVts\":false,\"sn\":\"27d7bf67-c71b-46b4-a038-8a13646c5e0f-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1718263287664}]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"DRAW\",\"audioZoneIndex\":0,\"dumiContinue\":0,\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SRU240613.0139.22E\",\"timestamp\":\"1718263287702\",\"vehicleType\":\"MARS\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"北京市东城区\",\"latitude\":39.90876,\"longitude\":116.39759,\"navigate_poi\":\"岳阳楼区南湖公园\",\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"画一幅中秋节合家美满赏月的画\t\",\"asrRewrite\":\"\",\"asrSn\":\"27d7bf67-c71b-46b4-a038-8a13646c5e0f-0\",\"confidence\":0.95,\"domain\":\"lifeService\",\"intent\":\"DRAW\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"nluType\":\"Offline\",\"origin\":\"画一幅中秋节合家美满赏月的画\t\",\"platform\":\"Jidu\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240610-782da07d\",\"voiceZone\":0}],\"query\":\"画一幅中秋节合家美满赏月的画\t\",\"sessionId\":\"\",\"timestamp\":1718263287700,\"vid\":\"d2860a1b1ae5308f64bc72ee00d4552d\"}";
    public static final String navi = "{\"asrRewrite\":\"前方是什么河呀\",\"asrSn\":\"fake-navi\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[{\"asr\":\"现在还有多少电池\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"电量剩余52%。\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"carInfo\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"carInfo\",\"intent\":\"CHECK_SOC\"}}],\"isHitVts\":false,\"sn\":\"20997515-d6a9-4304-a99f-dc297b8fbb60-0_12\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1717637934515},{\"asr\":\"还能开多少公里\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"现在实估剩余续航228公里。\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"carInfo\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":100.0,\"domain\":\"carInfo\",\"intent\":\"CHECK_SOG\"}}],\"isHitVts\":false,\"sn\":\"20997515-d6a9-4304-a99f-dc297b8fbb60-0_13\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1717637940288},{\"asr\":\"今天下雨吗\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"上海今天没有雨，全天19度~25度\"},\"dmStatus\":{\"entitySlots\":[{\"name\":\"time\",\"normalizedValue\":\"2024-06-06 00:00:00\",\"value\":\"2024-06-06 00:00:00\"}],\"isMultiForce\":false,\"skill\":\"weather\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":100.0,\"domain\":\"weather\",\"intent\":\"SYS_RAIN\",\"slots\":[{\"name\":\"time\",\"normalizedValue\":\"2024-06-06 00:00:00\",\"value\":\"2024-06-06 00:00:00\"}]}}],\"isHitVts\":false,\"sn\":\"20997515-d6a9-4304-a99f-dc297b8fbb60-0_14\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1717637948151}]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"NaviAgentState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"angle\":\"347.75626\",\"audioZoneIndex\":0,\"dumiContinue\":0,\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":false,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"location\":\"31.20798095703125,121.35415228949653\",\"mapVersionInfo\":{\"cuid\":\"30a288ad761ea2cbc02b4323c78e89ef|0\",\"zid\":\"u9-_UJvI75LKRvzSZMD1yuIAWEotysbs01rJ5oqdFBajyVdGAPjyG9k8-WSExM7giX9zevsJNKw_lH3KTd5I1tA\",\"prefer\":\"288\",\"curlinkIdIdx\":\"89\",\"gsNumber\":{\"ISBNNumber\":\"甲测资字11111342\",\"adasGSNumber\":\"GS （2023）3114号/沪 S〔2023〕093号/粤S（2022）145号/京S（2023）0955号/浙S（2023）13号\",\"mapGSNumber\":\"GS（2023）4069号\"},\"origin\":\"31.1645,121.38111\",\"sessionId\":\"{\\\"codr\\\":\\\"30a288ad761ea2cbc02b4323c78e89ef|0_31.164500,121.381110_31.35605,121.22865_87\\\",\\\"loc\\\":\\\"nj\\\"}@669\",\"versionName\":\"23.200.1341.1717163988\",\"dest\":\"31.356307,121.228683\",\"mrsl\":\"\\\"g\\\":\\\"18_1\\\",\\\"w\\\":\\\"AAAA\\\",\\\"p\\\":\\\"288\\\",\\\"s\\\":\\\"1\\\",\\\"seq\\\":\\\"0\\\"\",\"versionCode\":\"2003641\"}}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"CURIOUS_EXPLORE_ENTRY\",\"audioZoneIndex\":0,\"currentAgentName\":\"navi\",\"dumiContinue\":0,\"env\":\"Prod\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"cd457cd7512bfa09a5c78c41a5c4f5c1\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SDU240531.2129.71P\",\"timestamp\":\"1717637959254\",\"vehicleType\":\"MARS\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市长宁区\",\"latitude\":31.20795,\"longitude\":121.35415,\"navigate_poi\":\"集度(上海)有限公司\",\"song_artist\":\"小小君\",\"song_name\":\"在法国的十三天\",\"song_playState\":\"playing\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"前方是什么河呀\",\"asrRewrite\":\"前方是什么河呀\",\"asrSn\":\"20997515-d6a9-4304-a99f-dc297b8fbb60-0_15\",\"confidence\":100.0,\"domain\":\"naviControl\",\"intent\":\"CURIOUS_EXPLORE_ENTRY\",\"isHaveDirective\":false,\"isRejected\":true,\"nluSource\":\"MODEL\",\"nluTime\":1717637959235,\"nluType\":\"Online\",\"origin\":\"\",\"platform\":\"Jidu\",\"slots\":[{\"name\":\"poi\",\"normalizedValue\":\"WATER_AREA\",\"value\":\"河\"},{\"name\":\"position\",\"normalizedValue\":\"FRONT\",\"value\":\"前方\"}],\"version\":\"v2.0\",\"voiceZone\":0}],\"query\":\"前方是什么河呀\",\"sessionId\":\"\",\"timestamp\":1717637959253,\"vid\":\"df67a7504d940da691dc6dc3537c3b98\"}";
    public static final String custom_skill = "{\"asrRewrite\":\"当副驾车门打开时就播报公路请上车\",\"asrSn\":\"fake-custom-skill\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"CUSTOMSKILL\",\"audioZoneIndex\":0,\"dumiContinue\":0,\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"cf52759a1723b597d091c30c2fe68c61\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SRU240605.0143.64P\",\"timestamp\":\"1717576519976\",\"vehicleType\":\"MARS\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区\",\"latitude\":31.35654,\"longitude\":121.22891,\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"当副驾车门打开时就播报公路请上车\",\"asrRewrite\":\"当副驾车门打开时就播报公路请上车\",\"asrSn\":\"cd5378f0-7387-4ae4-b08a-2966cde9377a-0\",\"confidence\":100.0,\"domain\":\"systemControl\",\"intent\":\"CUSTOM_VOICE_SKILL_ENTRY\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"MODEL\",\"nluTime\":1717576519949,\"nluType\":\"Online\",\"origin\":\"\",\"platform\":\"Jidu\",\"slots\":[{\"name\":\"condition\",\"normalizedValue\":\"副驾车门打开\",\"value\":\"副驾车门打开\"},{\"name\":\"objective\",\"normalizedValue\":\"播报公路请上车\",\"value\":\"播报公路请上车\"}],\"version\":\"v2.0\",\"voiceZone\":0}],\"query\":\"当副驾车门打开时就播报公路请上车\",\"sessionId\":\"\",\"timestamp\":1717576519976,\"vid\":\"1e9aa9c35662543a466f6b382abc39d6\"}";
    public static final String wenxin = "{\"asrRewrite\":\"\",\"asrSn\":\"fake-wenxin\",\"audioZoneIndex\":0,\"botSession\":[],\"client_context\":[{\"header\":{\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"WENXIN\",\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":\"1\",\"jidutoken\":\"cc695636e24cb000268ebac4da35de51\",\"protocolversion\":\"2.0.0\",\"speechAppVersion\":\"61SRU240513.0128.31P\",\"timestamp\":\"1719148209204\",\"vehicleType\":\"VENUS\"}},{\"header\":{\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"盐城市大丰区\",\"song_artist\":\"飞卢玄幻_不败升级\",\"song_name\":\"飞卢玄幻_不败升级\",\"song_playState\":\"playing\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"39乘以3是多少\",\"asrRewrite\":\"\",\"asrSn\":\"93dcfbec-3d78-4464-83fe-973752fbd1d5-0\",\"confidence\":0.95,\"domain\":\"wenXinYiYan\",\"intent\":\"WENXIN_YIYAN\",\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":1719148209178,\"nluType\":\"Offline\",\"origin\":\"39乘以3是多少\",\"platform\":\"PostProcess\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240424-162ce99e\",\"voiceZone\":0}],\"query\":\"39乘以3是多少\",\"sessionId\":\"\",\"timestamp\":1719148209204,\"vid\":\"caa41c6532c36a3a55561ed514a01a18\"}";
    public static final String wenxin_mode = "{\"asrRewrite\":\"\",\"asrSn\":\"fake-wenxin-mode\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[{\"asr\":\"打开文心一言\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"你好，我是文心一言。想要退出，可以说：退出文心一言。\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"lifeService\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":1.0,\"domain\":\"lifeService\",\"intent\":\"ERNIE_BOT\",\"slots\":[{\"name\":\"action\",\"normalizedValue\":\"OPEN\",\"value\":\"打开\"}]}}],\"isHitVts\":false,\"sn\":\"8e01225b-cf13-4d0d-ae6f-48aebb625185-0\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1721290775803}]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"WENXIN\",\"audioZoneIndex\":0,\"currentAgentName\":\"wenXin\",\"currentNaviLevel\":0,\"dumiContinue\":0,\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":true,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"2.0.0\",\"sessionId\":\"jidu_236b60eb-1f1d-414e-8386-fdebe08b5482\",\"speechAppVersion\":\"61SRU240718.0146.20E\",\"timestamp\":\"1721290781884\",\"vehicleType\":\"MARS\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"current_poi\":\"上海市嘉定区\",\"latitude\":31.35722,\"longitude\":121.22725,\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"十个字介绍周杰伦\",\"asrRewrite\":\"\",\"asrSn\":\"8e01225b-cf13-4d0d-ae6f-48aebb625185-0_2\",\"confidence\":0.0,\"domain\":\"wenXinYiYan\",\"intent\":\"wenxin_unknown\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"\",\"nluTime\":0,\"nluType\":\"Offline\",\"origin\":\"\",\"platform\":\"Jidu\",\"version\":\"\",\"voiceZone\":0}],\"query\":\"十个字介绍周杰伦\",\"sessionId\":\"\",\"timestamp\":1721290781884,\"vid\":\"d2860a1b1ae5308f64bc72ee00d4552d\"}";
    public static final String wti = "{\"asrRewrite\":\"\",\"asrSn\":\"fake-wti\",\"audioZoneIndex\":0,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"WENXIN\",\"audioZoneIndex\":0,\"battery\":\"16\",\"currentAgentName\":\"wenXin\",\"currentNaviLevel\":0,\"dumiContinue\":0,\"electric_drive_type\":\"8\",\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":true,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"2.0.0\",\"car_wing\":\"2\",\"speechAppVersion\":\"61SDU240725.0232.41E\",\"car_sys_version\":\"-1\",\"timestamp\":\"1721899926832\",\"vehicleType\":\"MARS\",\"vehicle_type\":\"1\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"car_state_code\":[\"AI-PPA-01\",\"AI-PPA-05\",\"AI-PPA-06\"],\"current_poi\":\"北京市东城区\",\"latitude\":39.90876,\"longitude\":116.39759,\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"车辆警报灯长什么样\",\"asrRewrite\":\"\",\"asrSn\":\"4208a5a2-6992-4a98-a7eb-672dece9d5e9-0_2\",\"confidence\":0.95,\"domain\":\"knowledge\",\"intent\":\"WTI_LIGHT_F_DIAGNOSIS\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"nluType\":\"Offline\",\"origin\":\"车辆警报灯长什么样\",\"platform\":\"Jidu\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240720-964209b1\",\"voiceZone\":0}],\"query\":\"车辆警报灯长什么样\",\"sessionId\":\"\",\"timestamp\":1721899926832,\"vid\":\"d2860a1b1ae5308f64bc72ee00d4552d\"}";
    public static final String diagnosis = "{\"asrRewrite\":\"\",\"asrSn\":\"fake-diagnosis\",\"audioZoneIndex\":1,\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[]},{\"audioZoneIndex\":1,\"sessionList\":[{\"asr\":\"搜索其他\",\"asrOutputRewrite\":\"\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"第一个是王府井百货，去这里吗？\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"navi\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"navi\",\"intent\":\"PARTY_PLAN_ENTRY\"}}],\"isHitVts\":false,\"sn\":\"2609b6a2-b01b-4e79-aefe-d581bfe7bcef-1\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1722390857812},{\"asr\":\"搜索在上海城隍庙和东方明珠之间的其他\",\"asrOutputRewrite\":\"\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"第一个是上海国金中心商场，去这里吗？\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"navi\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"navi\",\"intent\":\"PARTY_PLAN_ENTRY\"}}],\"isHitVts\":false,\"sn\":\"b5e30e82-a2ad-4d0c-9da6-420fd3e39e92-1\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1722390878351},{\"asr\":\"有几种打开车门的方式\",\"asrOutputRewrite\":\"\",\"dstData\":[{\"customContext\":{\"isInside\":true,\"tts\":\"### 打开车门有8种方式：\\n\\n\\n\\n\uE901     车外硬按键。\\n\\n\\n\\n\uE902     车内硬按键。\\n\\n\\n\\n\uE903     车内冗余开关：用于紧急情况下的开门。\\n\\n\\n\\n\uE904     手动推拉：除完全关闭状态外，可以手动推拉开启车门。\\n\\n\\n\\n\uE905     车内屏幕：通过车内屏幕控制开门。\\n\\n\\n\\n\uE906     车内外语音：通过语音指令“打开车门”来控制开门。\\n\\n\\n\\n\uE907     极越手机APP：通过手机APP控制开门。\\n\\n\\n\\n\uE908     自动感应：携带手机蓝牙钥匙靠近车辆主驾侧附近，车辆会自动解锁，主驾门自动打开。\\n\\n\\n\"},\"dmStatus\":{\"entitySlots\":[],\"isMultiForce\":false,\"skill\":\"knowledge\",\"type\":\"FINISH_DIALOGUE\"},\"nlu\":{\"confidence\":0.95,\"domain\":\"knowledge\",\"intent\":\"JIYUE_KNOWLEDGE\"}}],\"isHitVts\":false,\"sn\":\"1d0d9274-e539-46dc-b25d-b65184177b2f-1\",\"source\":\"JIDU_ONLINE\",\"speechMode\":\"inside\",\"timestamp\":1722390881743}]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}],\"client_context\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"arbitrateResult\":\"WENXIN\",\"audioZoneIndex\":0,\"battery\":\"16\",\"currentAgentName\":\"wenXin\",\"currentNaviLevel\":0,\"dumiContinue\":0,\"electric_drive_type\":\"8\",\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":true,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"dcaa10b40eb11861aa83360c17be7efd\",\"protocolversion\":\"2.0.0\",\"car_wing\":\"2\",\"speechAppVersion\":\"61SRU240730.0146.54E\",\"car_sys_version\":\"-1\",\"timestamp\":\"1722390897106\",\"vehicleType\":\"MARS\",\"vehicle_type\":\"1\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"SpeechState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"car_state_code\":[\"AI-PPA-01\",\"AI-PPA-05\",\"AI-PPA-06\"],\"current_poi\":\"北京市东城区\",\"latitude\":39.90876,\"longitude\":116.39759,\"song_playState\":\"paused\",\"video_playState\":\"paused\"}}],\"finalNluResult\":[{\"asr\":\"PPA为什么用不了了\",\"asrRewrite\":\"\",\"asrSn\":\"12a41ea9-0e16-47fd-92ca-792fc206c49a-1\",\"confidence\":0.95,\"domain\":\"knowledge\",\"intent\":\"AP_F_DIAGNOSIS\",\"isHaveDirective\":false,\"isRejected\":false,\"nluSource\":\"SDS_NLU_MODEL\",\"nluTime\":0,\"nluType\":\"Offline\",\"origin\":\"PPA为什么用不了了\",\"platform\":\"Jidu\",\"slots\":[],\"version\":\"2.0/model-v2.0-20240727-87449d74\",\"voiceZone\":1}],\"query\":\"PPA为什么用不了了\",\"sessionId\":\"\",\"timestamp\":1722390897105,\"vid\":\"7341ef688f96812d8f3d95a3a3607afe\"}";

    private static String sendAudioFrames(){
        String jsonMsg = "{\"data\":{\"authorization\":{\"bdvsid\":\"3563e3fc4f7dbb312e75c0743ddb6ee6\"},\"bdvs-device-id\":\"c9fff6a2aff0b348f99614a8f4d9fdcd\",\"bdvs-version\":2,\"contexts\":[{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"GpsState\",\"namespace\":\"ai.dueros.device_interface.location\"},\"payload\":{\"enable\":true,\"geoCoordinateSystem\":\"BD09LL\",\"latitude\":31.356740,\"longitude\":121.227050}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"ViewState\",\"namespace\":\"ai.dueros.device_interface.screen\"},\"payload\":{\"token\":\"{\\\"playState\\\":\\\"paused\\\"}\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"NavigationState\",\"namespace\":\"ai.dueros.device_interface.extensions.iov_navigation\"}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"CarState\",\"namespace\":\"bdvs.capability.car_control\"},\"payload\":{\"speed\":0,\"window\":{\"values\":[{\"nameValuePairs\":{\"degree\":0}},{\"nameValuePairs\":{\"degree\":0}},{\"nameValuePairs\":{\"degree\":0}},{\"nameValuePairs\":{\"degree\":0}}]}}},{\"header\":{\"dialogRequestId\":\"\",\"messageId\":\"\",\"name\":\"JiduNgdState\",\"namespace\":\"ai.dueros.device_interface.thirdparty.jidu\"},\"payload\":{\"audioZoneIndex\":1,\"currentNaviLevel\":0,\"dumiContinue\":0,\"env\":\"Test\",\"isGroupChatOpen\":false,\"isInGroupChatState\":false,\"isInWenXinYiYanModel\":false,\"isInside\":true,\"isIntegrate\":false,\"isWenXinNlu\":false,\"isdrive\":false,\"jdvsVersion\":\"2.0.0.1\",\"jidutoken\":\"cb1de5e4492fbddf40319561d728a181\",\"protocolversion\":\"2.0.0\",\"timestamp\":\"1724411758684\",\"vehicleType\":\"VENUS\"}}],\"event\":{\"header\":{\"dialogRequestId\":\"59fe1fc9-b589-4f05-9a38-b7e4d19fb76e\",\"messageId\":\"59fe1fc9-b589-4f05-9a38-b7e4d19fb76e\",\"name\":\"TextInput\",\"namespace\":\"bdvs.capability.text_input\"},\"payload\":{\"asrRejectState\":1,\"asrState\":\"FINAL\",\"idx\":4,\"query\":\"上海明天天气怎么样\",\"queryIdx\":5,\"sn\":\"4791d240-21c7-4e85-a7e4-365135f76997-1_5\",\"speechId\":7406292112632182875}},\"extensions-param\":{\"botSessionList\":[{\"audioZoneIndex\":0,\"sessionList\":[]},{\"audioZoneIndex\":1,\"sessionList\":[]},{\"audioZoneIndex\":2,\"sessionList\":[]},{\"audioZoneIndex\":3,\"sessionList\":[]}]},\"internal-param\":{},\"timestamp\":1724411758682},\"type\":\"BDVS_EVENT\"}";
        return jsonMsg;
    }
    private static String sendAudioFrames(String asrSn) {
        String jsonMsg = "{\n"
                + "    \"data\": {\n"
                + "        \"authorization\": {\n"
                + "            \"bdvsid\": \"3563e3fc4f7dbb312e75c0743ddb6ee6\"\n"
                + "        },\n"
                + "        \"bdvs-device-id\": \"c9fc56098dc0a6868e5518cae9a17ccd\",\n"
                + "        \"bdvs-version\": 2,\n"
                + "        \"contexts\": [\n"
                + "            {\n"
                + "                \"header\": {\n"
                + "                    \"dialogRequestId\": \"\",\n"
                + "                    \"messageId\": \"\",\n"
                + "                    \"name\": \"GpsState\",\n"
                + "                    \"namespace\": \"ai.dueros.device_interface.location\"\n"
                + "                },\n"
                + "                \"payload\": {\n"
                + "                    \"enable\": true,\n"
                + "                    \"geoCoordinateSystem\": \"BD09LL\",\n"
                + "                    \"latitude\": 31.35723,\n"
                + "                    \"longitude\": 121.22713\n"
                + "                }\n"
                + "            },\n"
                + "            {\n"
                + "                \"header\": {\n"
                + "                    \"dialogRequestId\": \"\",\n"
                + "                    \"messageId\": \"\",\n"
                + "                    \"name\": \"ViewState\",\n"
                + "                    \"namespace\": \"ai.dueros.device_interface.screen\"\n"
                + "                },\n"
                + "                \"payload\": {\n"
                + "                    \"token\": \"{\\\"playState\\\":\\\"0\\\"}\"\n"
                + "                }\n"
                + "            },\n"
                + "            {\n"
                + "                \"header\": {\n"
                + "                    \"dialogRequestId\": \"\",\n"
                + "                    \"messageId\": \"\",\n"
                + "                    \"name\": \"NavigationState\",\n"
                + "                    \"namespace\": \"ai.dueros.device_interface.extensions.iov_navigation\"\n"
                + "                },\n"
                + "                \"payload\": {\n"
                + "                    \"mapApp\": {\n"
                + "                        \"naviTimestamp\": 0\n"
                + "                    }\n"
                + "                }\n"
                + "            },\n"
                + "            {\n"
                + "                \"header\": {\n"
                + "                    \"dialogRequestId\": \"\",\n"
                + "                    \"messageId\": \"\",\n"
                + "                    \"name\": \"CarState\",\n"
                + "                    \"namespace\": \"bdvs.capability.car_control\"\n"
                + "                },\n"
                + "                \"payload\": {\n"
                + "                    \"speed\": 0,\n"
                + "                    \"window\": {\n"
                + "                        \"values\": [\n"
                + "                            {\n"
                + "                                \"nameValuePairs\": {\n"
                + "                                    \"degree\": 4\n"
                + "                                }\n"
                + "                            },\n"
                + "                            {\n"
                + "                                \"nameValuePairs\": {\n"
                + "                                    \"degree\": 4\n"
                + "                                }\n"
                + "                            },\n"
                + "                            {\n"
                + "                                \"nameValuePairs\": {\n"
                + "                                    \"degree\": 4\n"
                + "                                }\n"
                + "                            },\n"
                + "                            {\n"
                + "                                \"nameValuePairs\": {\n"
                + "                                    \"degree\": 4\n"
                + "                                }\n"
                + "                            }\n"
                + "                        ]\n"
                + "                    }\n"
                + "                }\n"
                + "            },\n"
                + "            {\n"
                + "                \"header\": {\n"
                + "                    \"dialogRequestId\": \"\",\n"
                + "                    \"messageId\": \"\",\n"
                + "                    \"name\": \"JiduNgdState\",\n"
                + "                    \"namespace\": \"ai.dueros.device_interface.thirdparty.jidu\"\n"
                + "                },\n"
                + "                \"payload\": {\n"
                + "                    \"audioZoneIndex\": 0,\n"
                + "                    \"currentNaviLevel\": 0,\n"
                + "                    \"dumiContinue\": 0,\n"
                + "                    \"env\": \"Test\",\n"
                + "                    \"isGroupChatOpen\": false,\n"
                + "                    \"isInGroupChatState\": false,\n"
                + "                    \"isInWenXinYiYanModel\": false,\n"
                + "                    \"isInside\": true,\n"
                + "                    \"isIntegrate\": false,\n"
                + "                    \"isWenXinNlu\": false,\n"
                + "                    \"isdrive\": false,\n"
                + "                    \"jdvsVersion\": \"2.0.0.1\",\n"
                + "                    \"jidutoken\": \"dcaa10b40eb11861aa83360c17be7efd\",\n"
                + "                    \"protocolversion\": \"2.0.0\",\n"
                + "                    \"timestamp\": \"1723260742805\",\n"
                + "                    \"vehicleType\": \"MARS\"\n"
                + "                }\n"
                + "            }\n"
                + "        ],\n"
                + "        \"event\": {\n"
                + "            \"header\": {\n"
                + "                \"dialogRequestId\": \"546a7cdc-83dd-49e4-8673-61d9e47b105f\",\n"
                + "                \"messageId\": \"546a7cdc-83dd-49e4-8673-61d9e47b105f\",\n"
                + "                \"name\": \"TextInput\",\n"
                + "                \"namespace\": \"bdvs.capability.text_input\"\n"
                + "            },\n"
                + "            \"payload\": {\n"
                + "                \"asrRejectState\": 0,\n"
                + "                \"asrState\": \"FINAL\",\n"
                + "                \"idx\": 5,\n"
                + "                \"query\": \"打开车门\",\n"
                + "                \"queryIdx\": 1,\n"
                + "                \"sn\": \"c2dfac35-d85a-4dd6-9b02-f2fcf7cd0967-6\",\n"
                + "                \"speechId\": 7401348524037159171,\n"
                + "                \"voiceprintInfo\": null\n"
                + "            }\n"
                + "        },\n"
                + "        \"extensions-param\": {\n"
                + "            \"botSessionList\": [\n"
                + "                {\n"
                + "                    \"audioZoneIndex\": 0,\n"
                + "                    \"sessionList\": []\n"
                + "                },\n"
                + "                {\n"
                + "                    \"audioZoneIndex\": 1,\n"
                + "                    \"sessionList\": [\n"
                + "\n"
                + "                    ]\n"
                + "                },\n"
                + "                {\n"
                + "                    \"audioZoneIndex\": 2,\n"
                + "                    \"sessionList\": [\n"
                + "\n"
                + "                    ]\n"
                + "                },\n"
                + "                {\n"
                + "                    \"audioZoneIndex\": 3,\n"
                + "                    \"sessionList\": [\n"
                + "\n"
                + "                    ]\n"
                + "                }\n"
                + "            ]\n"
                + "        },\n"
                + "        \"internal-param\": {\n"
                + "\n"
                + "        },\n"
                + "        \"timestamp\": 1723260742803\n"
                + "    },\n"
                + "    \"type\": \"BDVS_EVENT\"\n"
                + "}";

        return jsonMsg;
    }

    public static String sendFinish() {
        JSONObject json = new JSONObject();
        json.put("type","FINISH");
        return json.toString();
    }
}
