package com.changan.cdc.sds.tools.jidullmsdk.basesdk;

import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.CodeEnumEntity;
import com.changan.cdc.sds.tools.jidullmsdk.SDKLogUtil;
import com.changan.cdc.sds.tools.jidullmsdk.WebSocketInfo;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class BaseWebSocketCommunicate extends WebSocketListener {

    private String connectionSn = null;
    private WebSocketInfo webSocketInfo = null;
    private ConcurrentLinkedDeque<Object> msgList = null;
    private BaseRequestManage manager = null;

    //Communicate状态：0表示初始化，1表示Open中，2表示open完成，可以收发消息，-1表示已经关闭；
    private AtomicInteger communicateState  = new AtomicInteger(0);

    public BaseWebSocketCommunicate(String connectionSn, BaseRequestManage manager) throws Exception {
        if (connectionSn == null || connectionSn.equals("")) {
            throw new Exception("parameter sn is null");
        }
        webSocketInfo = new WebSocketInfo(manager.getpManageEvent());
        webSocketInfo.setConnectionSn(connectionSn);
        //从连接到收到第一包数据不能超过10s
        this.msgList = new ConcurrentLinkedDeque<Object>();
        this.connectionSn = connectionSn;
        this.manager = manager;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        communicateState.set(1);
        output("BaseWebSocketCommunicate: onOpen: " + "webSocket connect success" + this.connectionSn+";time:"+System.currentTimeMillis()+"；duration:"+(System.currentTimeMillis()-webSocketInfo.getCreatTime()));
        webSocketInfo.setWebSocket(webSocket);
        webSocketInfo.setConnectedTime(System.currentTimeMillis());
        webSocketInfo.setLastReceiveMsgTime(System.currentTimeMillis());
        webSocketInfo.setLastSendMsgTime(System.currentTimeMillis());
        super.onOpen(webSocket, response);
        // 这里一定不要阻塞
        new Thread(() -> {
            try {
                BaseDirective payload = new BaseDirective();
                payload.setConnnectionSn(connectionSn);
                webSocketInfo.getCommunicateWebSocketEvent().onOpen(payload);
                //send msg after connect
                this.sendAllMsg();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"BaseWebSocketCommunicate").start();
        manager.reviveCommunicate(connectionSn,this);
        // 这里千万别阻塞，包括WebSocketListener其它回调
        communicateState.set(2);

    }


    public AtomicInteger getCommunicateState() {
        return communicateState;
    }

    @Override
    public void onMessage(WebSocket webSocket, final String text) {
        super.onMessage(webSocket, text);
        webSocketInfo.setLastReceiveMsgTime(System.currentTimeMillis());
        new Runnable() {
            @Override
            public void run() {
                try {
                    BaseDirective directive = new BaseDirective();
                    directive.setConnnectionSn(connectionSn);
                    directive.setPayload(text);
                    webSocketInfo.getCommunicateWebSocketEvent().onReceiveMsg(directive);
                    return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    BaseDirective directive = new BaseDirective();
                    directive.setConnnectionSn(connectionSn);
                    directive.setErrcode(CodeEnumEntity.MESSAGE_FORMAT_ERROR.getCode());
                    directive.setErrmsg("BaseWebSocketCommunicate: OnMessage Content Format Error");
                    webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
                }
            }
        }.run();
        output("BaseWebSocketCommunicate: onMessage: " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        webSocketInfo.setLastReceiveMsgTime(System.currentTimeMillis());
        webSocketInfo.setLastReceiveMsgTime(System.currentTimeMillis());
        new Runnable() {
            @Override
            public void run() {
                try {
                    BaseDirective directive = new BaseDirective();
                    directive.setConnnectionSn(connectionSn);
                    directive.setPayload(bytes.base64());
                    webSocketInfo.getCommunicateWebSocketEvent().onReceiveMsg(directive);
                    return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    BaseDirective directive = new BaseDirective();
                    directive.setConnnectionSn(connectionSn);
                    directive.setErrcode(CodeEnumEntity.MESSAGE_FORMAT_ERROR.getCode());
                    directive.setErrmsg("BaseWebSocketCommunicate: OnMessage Content Format Error");
                    webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
                }
            }
        }.run();
        output("BaseWebSocketCommunicate: onByteString: " + bytes);
    }

    //onClosing方法表示服务端不再发送数据给客户端时的回调，准备关闭连接，我们可以在这个方法中关闭WebSocket连接。
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        if(communicateState.get() == -2 ||communicateState.get() == -1 ){
            output("BaseWebSocketCommunicate: onClosing Already :  " + connectionSn + code + "/" + reason);
            //如果已经关闭
            return;
        }
        output("BaseWebSocketCommunicate: onClosing:  " + connectionSn + code + "/" + reason);
        super.onClosing(webSocket, code, reason);
        BaseDirective directive = new BaseDirective();
        directive.setConnnectionSn(connectionSn);
        directive.setErrcode(CodeEnumEntity.SUUCEESS.getCode());
        directive.setErrmsg("Connection closing by server");
        webSocketInfo.getCommunicateWebSocketEvent().onClosing(directive);
        if(webSocket !=null) {
            this.webSocketInfo.closeSocket(1000, "response onClosing");
        }
        manager.endConnectionSn(this.connectionSn);
    }

    //onClosed方法表示已经被完全关闭了，这时候回调这个方法。onFailure方法在连接失败的时候会回调这个方法。
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        if(communicateState.get() == -2 ||communicateState.get() == -1 ){
            output("BaseWebSocketCommunicate: onClosed Already :  " + connectionSn + code + "/" + reason);
            //如果已经关闭
            return;
        }
        super.onClosed(webSocket, code, reason);
        output("onClosed: " + connectionSn + code + "/" + reason);
        BaseDirective directive = new BaseDirective();
        directive.setConnnectionSn(connectionSn);
        directive.setErrcode(CodeEnumEntity.SUUCEESS.getCode());
        directive.setErrmsg("Connection closed by server");
        webSocketInfo.setCloseTime(System.currentTimeMillis());
        webSocketInfo.getCommunicateWebSocketEvent().onClosed(directive);
        manager.endConnectionSn(this.connectionSn);
    }

    @Override
//    Invoked when a web socket has been closed due to an error reading from or writing to the network.
//    Both outgoing and incoming messages may have been lost. No further calls to this listener will be made.
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        BaseDirective directive = new BaseDirective();
        t.printStackTrace();
        output("BaseWebSocketCommunicate: onFailure: "+connectionSn+"；error:" + t.fillInStackTrace());
        directive.setConnnectionSn(connectionSn);
        if(t!=null){
            if(t instanceof UnknownHostException){
                directive.setErrcode(CodeEnumEntity.WEBSOCKET_NO_NETWORK.getCode());
                directive.setErrmsg("无网络连接");
            }
//            java.net.ConnectException
            else if(t instanceof ConnectException){
                directive.setErrcode(CodeEnumEntity.WEBSOCKET_NO_NETWORK.getCode());
                directive.setErrmsg("无网络连接");
            }
            else if(t instanceof SocketException){
                directive.setErrcode(CodeEnumEntity.WEBSOCKET_NO_NETWORK.getCode());
                directive.setErrmsg("无网络连接");
            }
            else if(t instanceof SocketTimeoutException){
                directive.setErrcode(CodeEnumEntity.WEBSOCKET_NO_NETWORK.getCode());
                directive.setErrmsg("无网络连接");
            }
            else{
                directive.setErrcode(CodeEnumEntity.WEBSOCKET_COMM_FAILURE.getCode());
                directive.setErrmsg("网络其他错误");
            }
            this.webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
        }
        else{
            directive.setErrcode(CodeEnumEntity.WEBSOCKET_COMM_FAILURE.getCode());
            webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
        }
        manager.endConnectionSn(this.connectionSn);
    }

    private void output(String s) {
        webSocketInfo.getCommunicateWebSocketEvent().outputLog(s);
    }

    public Boolean checkTimeout() {
        //已经被关闭
        if (this.webSocketInfo.getCloseTime() != null &&
                (System.currentTimeMillis() - this.webSocketInfo.getCloseTime()) > 1000) {
            SDKLogUtil.log("LLMWebSocketCommunicate check websocket closed "+this.connectionSn);
            BaseDirective directive = new BaseDirective();
            directive.setConnnectionSn(connectionSn);
            directive.setErrcode(CodeEnumEntity.WEBSOCKET_CLIENT_CHECK_REMOVED.getCode());
            directive.setPayload("cloud check websocket closed");
            this.webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
          return true;
        }
        //如果连接时间大于5s,或者说
        if (System.currentTimeMillis() - this.webSocketInfo.getCreatTime() > 10000) {
            if(this.webSocketInfo.getConnectedTime() == null){
                SDKLogUtil.log("BaseWebSocketCommunicate:  check connect timeout "+this.connectionSn);
                BaseDirective directive = new BaseDirective();
                directive.setConnnectionSn(connectionSn);
                directive.setErrcode(CodeEnumEntity.WEBSOCKET_CLIENT_CHECK_CONNECT_TIMEOUT.getCode());
                directive.setPayload("cloud check connect timeout");
                this.webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
                return true;
            }
        }
        return false;
        }

        public int sendOneMsgToServer (Object requestJson){
           if(requestJson!=null){
//               if(requestJson.length() >= NetRequestParameter.MAX_MESSAGE_LENGTH){
//                   BaseDirective directive = new BaseDirective();
//                   directive.setConnnectionSn(connectionSn);
//                   directive.setErrcode(CodeEnumEntity.WEBSOCKET_MESSAGE_OVERSIZE.getCode());
//                   directive.setPayload("Maximum request body size "+ NetRequestParameter.MAX_MESSAGE_LENGTH+" exceeded, actual body size "+requestJson.length());
//                   this.webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
//               }
               this.msgList.addLast(requestJson);
           }
           else{
               BaseDirective directive = new BaseDirective();
               directive.setConnnectionSn(connectionSn);
               directive.setErrcode(CodeEnumEntity.WEBSOCKET_MESSAGE_ISNULL.getCode());
               directive.setPayload("request body is null");
               this.webSocketInfo.getCommunicateWebSocketEvent().onError(directive);
           }
            return this.getCommunicateState().get();
        }

        protected void sendHeartAllMsg (String s) {
            if (webSocketInfo.getWebSocket() != null) {
                webSocketInfo.getWebSocket().send(s);
            }
        }

        protected void sendAllMsg () {
            if (webSocketInfo.getWebSocket() != null) {
                while(true){
                    Object s = msgList.pollFirst();
                    if(s!=null){
                        if(s instanceof String){
                            webSocketInfo.getWebSocket().send((String)s);
                        }
                        else{
                            ByteString byteString = ByteString.of((byte[])s);
                            webSocketInfo.getWebSocket().send(byteString);
                        }
                        String logmsg = "BaseWebSocketCommunicate:  send msg to Server:%s,%s,%s";
                        webSocketInfo.getCommunicateWebSocketEvent()
                                .outputLog(String.format(logmsg, connectionSn, System.currentTimeMillis() + "", s));
                        webSocketInfo.setLastSendMsgTime(System.currentTimeMillis());
                    }
                    else{
                        break;
                    }
                }
            }
        }
    public void close() {
        try{
            //此时服务应该会被回收
            SDKLogUtil.log("BaseWebSocketCommunicate:  close a websocket:"+this.connectionSn+";"+communicateState.get());
            //2024-05-07 ，当链接关闭时，clean msgList
            this.msgList.clear();
            //正在关闭中
            communicateState.set(-2);
            //只会发生在客户端动关闭的情况
            this.webSocketInfo.closeSocket(1000, "close by client");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //关闭完成
        communicateState.set(-1);
    }
}
