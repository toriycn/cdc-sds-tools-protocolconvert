package com.changan.cdc.sds.tools.jidullmsdk.llmsdk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.CodeEnumEntity;
import com.changan.cdc.sds.tools.jidullmsdk.JiduCommonRequestParameter;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.BaseRequestManage;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class LLMRequestManage extends CommunicateWebSocketEvent {

    private ConcurrentHashMap<String,LLMAppendMsg> snAndAsrSn=  new ConcurrentHashMap<String,LLMAppendMsg>();
    private String connectionSn = null;
    private LLMRequestResultEvent pEvent ;
    private static LLMRequestManage mInstance;
    public void setpEvent(LLMRequestResultEvent pEvent ){
        this.pEvent = pEvent;
    }
    private LLMRequestManage(){
        connectionSn =  UUID.randomUUID().toString();
        BaseRequestManage.getInstance().setBaseUrl(JiduCommonRequestParameter.getInstance().getBaseUrl());
        BaseRequestManage.getInstance().setpManageEvent(this);
    }

    public synchronized static LLMRequestManage getInstance() {
        if (mInstance == null)
            mInstance = new LLMRequestManage();
        return mInstance;
    }

    //返回0表示发送成功，其他表示发送失败
    public int sendMsg(String asrSn,int audioZone,String requestJson){
        if(connectionSn == null){
           connectionSn = UUID.randomUUID().toString();
        }
        System.out.println("JiduCommonRequestParameter : the base url is "+JiduCommonRequestParameter.getInstance().getBaseUrl());
        if(BaseRequestManage.getInstance().getBaseUrl() != null){
            BaseRequestManage.getInstance().setBaseUrl(JiduCommonRequestParameter.getInstance().getBaseUrl());
            BaseRequestManage.getInstance().setpManageEvent(this);
        }
        if(snAndAsrSn.contains(asrSn)){
            return -1;
        }
        LLMAppendMsg msg = new LLMAppendMsg();
        msg.setRequestTime(System.currentTimeMillis());
        msg.setLastResponseTime(System.currentTimeMillis());
        msg.setAudioZone(audioZone);
        //顺序发送，都会在一个堆栈中存储
        int sendResult =BaseRequestManage.getInstance().sendRequest(connectionSn,requestJson);
        if(sendResult >= 0){
            snAndAsrSn.put(asrSn,msg);
        }
        return sendResult;
    }

    @Override
    public void onReceiveMsg(BaseDirective directive) {
        LLMBaseDirective llmBaseDirective = new LLMBaseDirective();
        //从消息里获取asrSn，如果取不到，消息丢弃
        JSONObject jsonObject = JSONObject.parseObject(directive.getPayload());
        if (jsonObject.containsKey("data")) {
            jsonObject = jsonObject.getJSONObject("data");
        }
        String type = jsonObject.getString("type");
        if (type.equals("LLM")) {
            llmBaseDirective.setName(jsonObject.getJSONObject("header").getString("name"));
            llmBaseDirective.setNamespace(jsonObject.getJSONObject("header").getString("namespace"));
            llmBaseDirective.setPayload(jsonObject.getString("payload"));
            JSONObject jsonObjectPayload = jsonObject.getJSONObject("payload");
            String asrSn = jsonObjectPayload.getString("asrSn");
            if(snAndAsrSn.containsKey(asrSn)) {
                LLMAppendMsg appendMsg = snAndAsrSn.get(asrSn);
                appendMsg.setLastResponseTime(System.currentTimeMillis());
                llmBaseDirective.setAudioZone(appendMsg.getAudioZone());
                llmBaseDirective.setAsrSn(asrSn);
                if(llmBaseDirective.getName().equals("jidu_llm_error")){
                    System.out.println("LLMRequestManage receive a jidu_llm_error result:" + asrSn);
                    this.snAndAsrSn.remove(asrSn);
                    LLMBaseDirective llmBaseDirectiveClosed = new LLMBaseDirective();
                    llmBaseDirectiveClosed.setAsrSn(asrSn);
                    llmBaseDirectiveClosed.setAudioZone(appendMsg.getAudioZone());
                    this.pEvent.onReceiveLLMClose(llmBaseDirectiveClosed);
                   return;
                }
                snAndAsrSn.put(asrSn,appendMsg);
                this.pEvent.onReceiveLLM(llmBaseDirective);
                //不在LLM中主动关闭。需要下游主动关闭。
                if (jsonObjectPayload.containsKey("event_type") && jsonObjectPayload.containsKey("data")) {
                    if (jsonObjectPayload.getString("event_type").equals("message")) {
                        try {
                            JSONObject jsonObjectPayloadData = jsonObjectPayload.getJSONObject("data");
                            boolean is_end = jsonObjectPayloadData.getBoolean("is_end");
                            if (is_end) {
                                System.out.println("LLMRequestManage receive a result:" + asrSn);
                                this.snAndAsrSn.remove(asrSn);
                                LLMBaseDirective llmBaseDirectiveClosed = new LLMBaseDirective();
                                llmBaseDirectiveClosed.setAsrSn(asrSn);
                                llmBaseDirectiveClosed.setAudioZone(appendMsg.getAudioZone());
                                this.pEvent.onReceiveLLMClose(llmBaseDirectiveClosed);
                            }
                        }catch (Exception ex){
                                try {
                                    JSONArray jsonNLUPayLoad = jsonObjectPayload.getJSONArray("data");
                                    System.out.println("LLMRequestManage receive a NLU result:" + asrSn);
                                    this.snAndAsrSn.remove(asrSn);
                                    LLMBaseDirective llmBaseDirectiveClosed = new LLMBaseDirective();
                                    llmBaseDirectiveClosed.setAsrSn(asrSn);
                                    llmBaseDirectiveClosed.setAudioZone(appendMsg.getAudioZone());
                                    this.pEvent.onReceiveLLMClose(llmBaseDirectiveClosed);
                                }catch (Exception exError){
                                    exError.printStackTrace();
                                }
                        }
                    }
                }
            }
            else{
                System.out.println("LLMRequestManage receive a null result:"+ directive.getPayload());
            }
            return ;
        }
        if (type.equals("ERROR")) {
            String asrSn =jsonObject.getString("asrSn");
            if(snAndAsrSn.containsKey(asrSn)){
                LLMAppendMsg appendMsg = snAndAsrSn.get(asrSn);
                llmBaseDirective.setAsrSn(asrSn);
                llmBaseDirective.setAudioZone(appendMsg.getAudioZone());
                llmBaseDirective.setErrcode(jsonObject.getIntValue("code"));
                llmBaseDirective.setErrmsg(jsonObject.getString("errorMsg"));
                this.pEvent.onReceiveLLMError(llmBaseDirective);
                //默认认为出现错误的话，链路上也要关闭的
                System.out.println("LLMRequestManage receive a result:"+ asrSn);
                this.snAndAsrSn.remove(asrSn);
                LLMBaseDirective llmBaseDirectiveClosed = new LLMBaseDirective();
                llmBaseDirectiveClosed.setAsrSn(asrSn);
                llmBaseDirectiveClosed.setAudioZone(appendMsg.getAudioZone());
                this.pEvent.onReceiveLLMClose(llmBaseDirectiveClosed);
            }
            else{
                System.out.println("LLMRequestManage receive a null ERROR result:"+ asrSn);
            }
            return ;
        }

        if(type.equals("PUSH")){
            LLMBaseDirective llmBaseDirectivePush = new LLMBaseDirective();
            String asrSn =jsonObject.getString("asrSn");
            int audioZone =jsonObject.getIntValue("audioZone");
            llmBaseDirectivePush.setName(jsonObject.getJSONObject("header").getString("name"));
            llmBaseDirectivePush.setNamespace(jsonObject.getJSONObject("header").getString("namespace"));
            llmBaseDirectivePush.setPayload(jsonObject.getString("payload"));
            llmBaseDirectivePush.setAsrSn(asrSn);
            llmBaseDirectivePush.setAudioZone(audioZone);
            System.out.println("LLMRequestManage receive a push message");
            this.pEvent.onReceivePUSH(llmBaseDirectivePush);
        }
    }

    @Override
    public void onOpen(BaseDirective payload) {
          //nothing to do
    }

    @Override
    public void onClosing(BaseDirective payload) {
        //nothing to do
    }

    @Override
    public void onClosed(BaseDirective payload) {
        String sn = payload.getConnnectionSn();//这个是连接ID
        if(sn.equals(connectionSn)){
            connectionSn = null;
        }
        snAndAsrSn.forEachEntry(1, new Consumer<Entry<String,LLMAppendMsg>>() {
            /**
             * Performs this operation on the given argument.
             *
             * @param stringLLMAppendMsgEntry the input argument
             */
            @Override
            public void accept(Entry<String, LLMAppendMsg> stringLLMAppendMsgEntry) {
                System.out.println("LLMRequestManage: Connection closed "+ stringLLMAppendMsgEntry.getKey()+"; removed");
                LLMBaseDirective llmBaseDirective = new LLMBaseDirective();
                llmBaseDirective.setErrcode(CodeEnumEntity.SUUCEESS.getCode());
                llmBaseDirective.setAsrSn(stringLLMAppendMsgEntry.getKey());
                llmBaseDirective.setAudioZone(stringLLMAppendMsgEntry.getValue().getAudioZone());
                pEvent.onReceiveLLMClose(llmBaseDirective);
            }
        });
        snAndAsrSn.clear();
    }

    @Override
    public void onError(BaseDirective payload) {
        String sn = payload.getConnnectionSn();//这个是连接ID ，在error时也要清空重连生成新的ID
        if(sn.equals(connectionSn)){
            connectionSn = null;
        }
        System.out.println("LLMRequestManage: on Message Error"+ JSONObject.toJSONString(payload)+"; removed");
        snAndAsrSn.forEachEntry(1, new Consumer<Entry<String, LLMAppendMsg>>() {
            @Override
            public void accept(Entry<String, LLMAppendMsg> stringLongEntry) {
                System.out.println("LLMRequestManage: check error "+ stringLongEntry.getKey()+"; removed");
                LLMBaseDirective llmBaseDirective = new LLMBaseDirective();
                llmBaseDirective.setAsrSn(stringLongEntry.getKey());
                llmBaseDirective.setAudioZone(stringLongEntry.getValue().getAudioZone());
                llmBaseDirective.setErrcode(payload.getErrcode());
                llmBaseDirective.setErrmsg(payload.getErrmsg());
                pEvent.onConnectionError(llmBaseDirective);
            }
        });
        snAndAsrSn.clear();
    }

    @Override
    public void onCheckTimeout() {
    }

    @Override
    public void onEndSn(String _connectionSn) {
        if(_connectionSn !=null){
            if(_connectionSn.equals(this.connectionSn)){
                connectionSn = null;
                System.out.println("LLMRequestManage: connectionSn"+ _connectionSn+"; is cleaned");
            }
        }
    }
}
