package com.changan.cdc.sds.tools.jidullmsdk.basesdk.multiconnect;

import com.changan.cdc.sds.tools.jidullmsdk.JiduCommonRequestParameter;
import com.changan.cdc.sds.tools.jidullmsdk.SDKLogUtil;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;
import okhttp3.OkHttpClient;

import java.util.concurrent.ConcurrentHashMap;


//用来进行状态控制
public class BaseMultiRequestManageFactory{
    private ConcurrentHashMap<String, BaseMultiRequestManage> wsHashMap= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, CommunicateWebSocketFactoryEvent> wsHashEventMap= new ConcurrentHashMap<>();
    private static BaseMultiRequestManageFactory mInstance;
    private BaseMultiRequestManageFactory() {

    }

    public synchronized static BaseMultiRequestManageFactory getInstance() {
        if (mInstance == null)
            mInstance = new BaseMultiRequestManageFactory();
        return mInstance;
    }

    public void setUrlEvent(String url ,CommunicateWebSocketEvent event){
        SDKLogUtil.log("BaseMultiRequestManageFactory client addEvent:"+url);
        CommunicateWebSocketFactoryEvent pEvent = new CommunicateWebSocketFactoryEvent();
        pEvent.setUrl(url);
        pEvent.setCommunicateWebSocketEvent(event);
        wsHashEventMap.put(url,pEvent);
    }

    public int sendRequest(String url ,String connectionSn,Object requestJson){
        int state = 0 ;
        try{
            SDKLogUtil.log("BaseMultiRequestManageFactory client sendRequest:"+connectionSn+";requestJson="+requestJson);
            BaseMultiRequestManage baseMultiRequestManage = wsHashMap.get(url);
            if(baseMultiRequestManage == null){
                CommunicateWebSocketEvent event = wsHashEventMap.get(url);
                if(event != null){
                    this.setUrlEvent(url,event);
                }
                baseMultiRequestManage = new BaseMultiRequestManage(url,wsHashEventMap.get(url));
                wsHashMap.put(url,baseMultiRequestManage);
            }
            state = baseMultiRequestManage.sendRequest(connectionSn,requestJson);
            SDKLogUtil.log("BaseMultiRequestManageFactory client sendRequest Result,state"+ state+";url="+url+";asrSn:"+connectionSn+";requestJson="+requestJson);
        }catch (Exception ex){
            SDKLogUtil.log("BaseMultiRequestManageFactory client sendRequest:"+connectionSn+";requestJson="+requestJson+";exception:"+ex.getMessage());
            ex.printStackTrace();
        }
        return state;
    }
    //轻易不要调用
    public void exit(String url){
        if(wsHashMap.containsKey(url))
        {
            SDKLogUtil.log("BaseMultiRequestManageFactory exit 方法被调用");
            this.wsHashMap.get(url).exit();
            this.wsHashMap.remove(url);
            wsHashEventMap.remove(url);
        }
    }

    public void endSession(String url,String sessionId){
        if(wsHashMap.containsKey(url))
        {
            this.wsHashMap.get(url).endConnectionSn(sessionId);
        }
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
