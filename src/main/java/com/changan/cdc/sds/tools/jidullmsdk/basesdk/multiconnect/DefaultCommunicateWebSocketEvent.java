package com.changan.cdc.sds.tools.jidullmsdk.basesdk.multiconnect;

import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;

public class DefaultCommunicateWebSocketEvent extends CommunicateWebSocketEvent
{
    @Override
    public void onReceiveMsg(BaseDirective payload) {
        System.out.println("DefaultCommunicateWebSocketEvent onReceiveMsg:"+JSONObject.toJSONString(payload));
    }

    @Override
    public void onOpen(BaseDirective payload) {
        System.out.println("DefaultCommunicateWebSocketEvent onOpen:"+JSONObject.toJSONString(payload));
    }

    @Override
    public void onClosing(BaseDirective payload) {
        System.out.println("DefaultCommunicateWebSocketEvent onClosing:"+JSONObject.toJSONString(payload));
    }

    @Override
    public void onClosed(BaseDirective payload) {
        System.out.println("DefaultCommunicateWebSocketEvent onClosed:"+JSONObject.toJSONString(payload));
    }

    @Override
    public void onError(BaseDirective payload) {
        System.out.println("DefaultCommunicateWebSocketEvent onError:"+JSONObject.toJSONString(payload));
    }

    @Override
    public void onCheckTimeout() {

    }
}
