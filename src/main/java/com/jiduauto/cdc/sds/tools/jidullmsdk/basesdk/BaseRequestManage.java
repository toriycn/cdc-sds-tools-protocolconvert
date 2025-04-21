package com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk;

import com.jiduauto.cdc.sds.tools.jidullmsdk.JiduCommonRequestParameter;
import com.jiduauto.cdc.sds.tools.jidullmsdk.NetRequestParameter;
import com.jiduauto.cdc.sds.tools.jidullmsdk.NetRequestUtil;
import com.jiduauto.cdc.sds.tools.jidullmsdk.SDKLogUtil;
import java.nio.channels.Selector;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.OkHttpClient;


//用来进行状态控制
public class BaseRequestManage {
    private Selector selector = null;
    private ConcurrentHashMap<String, BaseWebSocketCommunicate> wsHashMap= new ConcurrentHashMap<>();
    public static BaseRequestManage mInstance;
    private CommunicateWebSocketEvent communicateWebSocketEvent;
    private AtomicBoolean runState = new AtomicBoolean(false);
    private String baseUrl = "";

    private BaseRequestManage(){
        try {
            selector = Selector.open();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        runState.set(true);

        new Thread("BaseRequestManage-Timeout-Check"){
            @Override
            public void run() {
                try{
                    while(runState.get()){
                        checkTimeout();
                        sleep(NetRequestParameter.DEFAULT_MANAGE_RUNSTATE_SLEEPTIME);
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.start();
        new Thread("BaseRequestManage-UnsendMsg"){
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
        Set<Entry<String, BaseWebSocketCommunicate>> setWsMapEntity = wsHashMap.entrySet();
        for(Entry<String, BaseWebSocketCommunicate> item : setWsMapEntity){
            item.getValue().sendAllMsg();
        }
    }

    private void checkTimeout() {
        if(this.getpManageEvent()!=null){
            this.getpManageEvent().onCheckTimeout();
        }
        Set<Entry<String, BaseWebSocketCommunicate>> setWsMapEntity = wsHashMap.entrySet();
        for(Entry<String, BaseWebSocketCommunicate> item : setWsMapEntity){
            //已经关闭，移除
            if(item.getValue().checkTimeout()){
                endConnectionSn(item.getKey());
            }
        }
    }
    public CommunicateWebSocketEvent getpManageEvent() {
        return communicateWebSocketEvent;
    }

    public void setpManageEvent(CommunicateWebSocketEvent communicateWebSocketEvent) {
        this.communicateWebSocketEvent = communicateWebSocketEvent;
    }

    public synchronized static BaseRequestManage getInstance() {
        if (mInstance == null)
            mInstance = new BaseRequestManage();
        return mInstance;
    }
    public String getBaseUrl(){
        return this.baseUrl;
    }
    public void setBaseUrl(String baseUrl){
       this.baseUrl=baseUrl;
    }

    protected void reviveCommunicate(String sn, BaseWebSocketCommunicate llmWebScocketCommuniate){
        SDKLogUtil.log("BaseRequestManage reviveCommunicate:sn="+sn);
        if(!this.wsHashMap.containsKey(sn)){
            this.wsHashMap.put(sn,llmWebScocketCommuniate);
        }
    }

    private BaseWebSocketCommunicateResult innerConnectServer(String asrSn) throws Exception {
        BaseWebSocketCommunicateResult communicateResult = new BaseWebSocketCommunicateResult();
        if(baseUrl == null || baseUrl.equals("")){
            communicateResult.state = -1;
            return communicateResult;
        }

        BaseWebSocketCommunicate llmWebScocketCommuniate = null;
        //已经存在，不再新建
        if(wsHashMap.containsKey(asrSn)){
            llmWebScocketCommuniate =   wsHashMap.get(asrSn);
            communicateResult.state = 1;
            communicateResult.communicate = llmWebScocketCommuniate;
        }
        else{
            llmWebScocketCommuniate = new BaseWebSocketCommunicate(asrSn,this);
            wsHashMap.put(asrSn,llmWebScocketCommuniate);
            BaseWebSocketCommunicate finalListener = llmWebScocketCommuniate;
            String baseUrl = getBaseUrl();
            if(getBaseUrl().contains("?")){
                 baseUrl += "&sn="+asrSn;
            }
            else{
                baseUrl =String.format("%s?sn=%s",getBaseUrl(),asrSn);
            }
            SDKLogUtil.log("BaseRequestManage innerConnectServer:"+asrSn+";baseUrl="+baseUrl);
            String finalBaseUrl = baseUrl;
            new Thread("BaseRequestManage-Connect"){
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
            SDKLogUtil.log("BaseRequestManage client sendRequest:"+asrSn+";requestJson="+requestJson);
            BaseWebSocketCommunicateResult communicateResult = innerConnectServer(asrSn);
            if(communicateResult.state == 1 ||communicateResult.state == 2 ){
                state = communicateResult.communicate.sendOneMsgToServer(requestJson);
                //如果服务还未ready
                if(state !=2){
                    SDKLogUtil.log("BaseRequestManage client send pending, sendRequest:"+asrSn+";requestJson="+requestJson);
                }
            }
            else{
                SDKLogUtil.log("BaseRequestManage client send state error,state"+ communicateResult.state+";sendRequest:"+asrSn+";requestJson="+requestJson);
            }
            selector.wakeup();
        }catch (Exception ex){
            SDKLogUtil.log("BaseRequestManage client sendRequest:"+asrSn+";requestJson="+requestJson+";exception:"+ex.getMessage());
            ex.printStackTrace();
        }
        return state;
    }

    public void endConnectionSn(String connectionSn){
        try {
            if(wsHashMap.containsKey(connectionSn)){
                SDKLogUtil.log("BaseRequestManage client sn:"+connectionSn+" is removed");
                this.getpManageEvent().outputLog("sn:"+connectionSn+" is removed");
                this.getpManageEvent().onEndSn(connectionSn);
                BaseWebSocketCommunicate wsInfo =   wsHashMap.remove(connectionSn);
                wsInfo.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //轻易不要调用
    public void exit(){
        SDKLogUtil.log("BaseRequestManage exit 方法被调用");
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
