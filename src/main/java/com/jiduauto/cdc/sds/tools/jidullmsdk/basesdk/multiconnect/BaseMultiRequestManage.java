package com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk.multiconnect;

import com.jiduauto.cdc.sds.tools.jidullmsdk.*;
import com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;
import okhttp3.OkHttpClient;

import java.nio.channels.Selector;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


//用来进行状态控制
public class BaseMultiRequestManage {
    private Selector selector = null;
    private ConcurrentHashMap<String, BaseWebSocketMultiCommunicate> wsHashMap= new ConcurrentHashMap<>();
    private CommunicateWebSocketEvent communicateWebSocketEvent;
    private AtomicBoolean runState = new AtomicBoolean(false);
    private AtomicInteger checkTimeOutTimes = new AtomicInteger(0);
    private String baseUrl = "";

    public BaseMultiRequestManage(String baseUrl,CommunicateWebSocketEvent pEvent){
        this.baseUrl = baseUrl;
        this.communicateWebSocketEvent =pEvent;
        try {
            selector = Selector.open();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        runState.set(true);

        new Thread("BaseMultiRequestManage-Timeout-Check"){
            @Override
            public void run() {
                try{
                    while(runState.get()){
                        checkTimeout();
                        sleep(NetRequestParameter.DEFAULT_MANAGE_RUNSTATE_SLEEPTIME);
                        if(checkTimeOutTimes.getAndIncrement() >5){
                            communicateWebSocketEvent.onExit();
                            runState.set(false);
                            break;
                        }
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.start();
        new Thread("BaseMultiRequestManage-UnsendMsg"){
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


    private void checkUnsendMsg() {
        Set<Entry<String, BaseWebSocketMultiCommunicate>> setWsMapEntity = wsHashMap.entrySet();
        for(Entry<String, BaseWebSocketMultiCommunicate> item : setWsMapEntity){
            item.getValue().sendAllMsg();
        }
    }

    private void checkTimeout() {
        if(this.getpManageEvent()!=null){
            this.getpManageEvent().onCheckTimeout();
        }
        Set<Entry<String, BaseWebSocketMultiCommunicate>> setWsMapEntity = wsHashMap.entrySet();
        for(Entry<String, BaseWebSocketMultiCommunicate> item : setWsMapEntity){
            //已经关闭，移除
            if(item.getValue().checkTimeout()){
                endConnectionSn(item.getKey());
            }
        }
    }
    public CommunicateWebSocketEvent getpManageEvent() {
        return communicateWebSocketEvent;
    }

    public String getBaseUrl(){
        return this.baseUrl;
    }

    protected void reviveCommunicate(String sn, BaseWebSocketMultiCommunicate llmWebScocketCommuniate){
        SDKLogUtil.log("BaseMultiRequestManage reviveCommunicate:sn="+sn);
        if(!this.wsHashMap.containsKey(sn)){
            this.wsHashMap.put(sn,llmWebScocketCommuniate);
        }
    }

    private BaseWebSocketMultiCommunicateResult innerConnectServer(String asrSn) throws Exception {
        BaseWebSocketMultiCommunicateResult communicateResult = new BaseWebSocketMultiCommunicateResult();
        if(baseUrl == null || baseUrl.equals("")){
            communicateResult.state = -1;
            return communicateResult;
        }

        BaseWebSocketMultiCommunicate llmWebScocketCommuniate = null;
        //已经存在，不再新建
        if(wsHashMap.containsKey(asrSn)){
            llmWebScocketCommuniate =   wsHashMap.get(asrSn);
            communicateResult.state = 1;
            communicateResult.communicate = llmWebScocketCommuniate;
        }
        else{
            llmWebScocketCommuniate = new BaseWebSocketMultiCommunicate(asrSn,this);
            wsHashMap.put(asrSn,llmWebScocketCommuniate);
            BaseWebSocketMultiCommunicate finalListener = llmWebScocketCommuniate;
            String baseUrl = getBaseUrl();
            if(getBaseUrl().contains("?")){
                 baseUrl += "&sn="+asrSn;
            }
            else{
                baseUrl =String.format("%s?sn=%s",getBaseUrl(),asrSn);
            }
            SDKLogUtil.log("BaseMultiRequestManage innerConnectServer:"+asrSn+";baseUrl="+baseUrl);
            String finalBaseUrl = baseUrl;
            new Thread("BaseMultiRequestManage-Connect"){
                @Override
                public void run() {
                    try {
                        NetRequestUtil.createWebSocket(finalBaseUrl, finalListener);
                        selector.wakeup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            communicateResult.state = 2;
            communicateResult.communicate = llmWebScocketCommuniate;
        }
        return  communicateResult;
    }

    public int sendRequest(String asrSn,Object requestJson){
        int state = 0 ;
        try{
            SDKLogUtil.log("BaseMultiRequestManage client sendRequest:"+asrSn+";requestJson="+requestJson);
            BaseWebSocketMultiCommunicateResult communicateResult = innerConnectServer(asrSn);
            if(communicateResult.state == 1 ||communicateResult.state == 2 ){
                state = communicateResult.communicate.sendOneMsgToServer(requestJson);
                //如果服务还未ready
                if(state !=2){
                    SDKLogUtil.log("BaseMultiRequestManage client send pending, sendRequest:"+asrSn+";requestJson="+requestJson);
                }
            }
            else{
                SDKLogUtil.log("BaseMultiRequestManage client send state error,state"+ communicateResult.state+";sendRequest:"+asrSn+";requestJson="+requestJson);
            }
            selector.wakeup();
        }catch (Exception ex){
            SDKLogUtil.log("BaseMultiRequestManage client sendRequest:"+asrSn+";requestJson="+requestJson+";exception:"+ex.getMessage());
            ex.printStackTrace();
        }
        return state;
    }

    public void endConnectionSn(String connectionSn){
        try {
            if(wsHashMap.containsKey(connectionSn)){
                SDKLogUtil.log("BaseMultiRequestManage client sn:"+connectionSn+" is removed");
                this.getpManageEvent().outputLog("sn:"+connectionSn+" is removed");
                this.getpManageEvent().onEndSn(connectionSn);
                BaseWebSocketMultiCommunicate wsInfo =   wsHashMap.remove(connectionSn);
                wsInfo.close();
                wsInfo= null;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //轻易不要调用
    public void exit(){
        SDKLogUtil.log("BaseMultiRequestManage exit 方法被调用");
        this.communicateWebSocketEvent.onExit();
        runState.set(false);
    }
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        JiduCommonRequestParameter.getInstance().setOkHttpClient(okHttpClient);
    }

    public void addHeader(String key,String value){
        if(key!=null && value!=null){
            JiduCommonRequestParameter.getInstance().getHttpHeader().put(key,value);
        }
    }
}
