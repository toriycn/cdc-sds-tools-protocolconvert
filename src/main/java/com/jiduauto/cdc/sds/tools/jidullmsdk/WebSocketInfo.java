package com.jiduauto.cdc.sds.tools.jidullmsdk;

import com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;
import okhttp3.WebSocket;

public class WebSocketInfo {

    private Long creatTime = System.currentTimeMillis();
    private Long connectedTime ;
    private Long lastSendMsgTime = System.currentTimeMillis();
    private Long lastReceiveMsgTime =System.currentTimeMillis() ;
    private Long closeTime ;
    private String connectionSn ;
    private WebSocket webSocket ;
    private CommunicateWebSocketEvent communicateWebSocketEvent;
    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public Long getConnectedTime() {
        return connectedTime;
    }
    public void setConnectedTime(Long connectedTime) {
        this.connectedTime = connectedTime;
    }
    public WebSocketInfo(CommunicateWebSocketEvent communicateWebSocketEvent){
        this.communicateWebSocketEvent= communicateWebSocketEvent;
    }
    public CommunicateWebSocketEvent getCommunicateWebSocketEvent() {
        return communicateWebSocketEvent;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }
    public Long getCloseTime() {
        return closeTime;
    }
    public Long getCreatTime() {
        return creatTime;
    }
    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public String getConnectionSn() {
        return connectionSn;
    }

    public void setConnectionSn(String connectionSn) {
        this.connectionSn = connectionSn;
    }

    public void closeSocket(int code,String reason){
        if(this.webSocket !=null){
            try{
                this.webSocket.close(code,reason);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            this.webSocket = null;
        }
        this.setCloseTime(System.currentTimeMillis());
    }

    public Long getLastSendMsgTime() {
        return lastSendMsgTime;
    }

    public void setLastSendMsgTime(Long lastSendMsgTime) {
        this.lastSendMsgTime = lastSendMsgTime;
    }

    public Long getLastReceiveMsgTime() {
        return lastReceiveMsgTime;
    }

    public void setLastReceiveMsgTime(Long lastReceiveMsgTime) {
        this.lastReceiveMsgTime = lastReceiveMsgTime;
    }


    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("connectionSn:"+this.connectionSn);
        if(this.creatTime !=null) {
            builder.append(";createTime:" + this.getCreatTime());
        }
        if(this.connectedTime !=null) {
            builder.append(";connectedTime:" + this.getConnectedTime());
        }
        if(this.lastReceiveMsgTime !=null) {
            builder.append(";lastReceiveMsgTime:" + this.getLastReceiveMsgTime());
        }
        if(this.lastSendMsgTime !=null) {
            builder.append(";lastReceiveMsgTime:" + this.getLastSendMsgTime());
        }
        if(this.closeTime !=null){
            builder.append(";closeTime:"+this.getCloseTime());
        }
        return builder.toString();
    }
}
