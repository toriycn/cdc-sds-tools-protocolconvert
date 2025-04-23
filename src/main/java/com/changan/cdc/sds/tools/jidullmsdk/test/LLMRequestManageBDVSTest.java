package com.changan.cdc.sds.tools.jidullmsdk.test;

import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.BaseRequestManage;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;

public class LLMRequestManageBDVSTest {


    public static void main(String[] args) throws Exception {
        BaseRequestManage manage = BaseRequestManage.getInstance();
        manage.setBaseUrl( "wss://hydra-gateway.jiduapp.cn/ap/hydra-gateway/websocket/baidu/123456");
//        manage.setBaseUrl("ws://127.0.0.1:8088/websocket/baidu/123456");
//        manage.setBaseUrl("ws://piprod.jiduapp.cn/api/hydra-gateway/websocket/llm/123456");
//        manage.setBaseUrl("wss://cdcsds.jiduprod.com/api/sds-openapi-wenxin/websocket/wait_cloud_nlu");



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
                    String msg = sendAudioFrames(asrSn);
                    manage.sendRequest(asrSn, msg);
                    Thread.sleep(300);
                    manage.sendRequest(asrSn, sendFinish());
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


//        manage.endAudioZone(2);
//        manage.endAudioZone(3);
        manage.exit();
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
                + "                \"query\": \"明天的天气\",\n"
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
