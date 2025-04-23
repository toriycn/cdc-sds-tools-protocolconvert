package com.changan.cdc.sds.tools.jidullmsdk.basesdk.multiconnect;

import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;

public  class CommunicateWebSocketFactoryEvent extends CommunicateWebSocketEvent {
     private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public CommunicateWebSocketEvent getCommunicateWebSocketEvent() {
    return communicateWebSocketEvent;
  }

  public void setCommunicateWebSocketEvent(CommunicateWebSocketEvent communicateWebSocketEvent) {
    this.communicateWebSocketEvent = communicateWebSocketEvent;
  }

  private CommunicateWebSocketEvent communicateWebSocketEvent;

  @Override
  public void onReceiveMsg(BaseDirective payload) {
    payload.setRequestUrl(url);
    if(communicateWebSocketEvent!=null){
      communicateWebSocketEvent.onReceiveMsg(payload);
    }
  }

  @Override
  public void onOpen(BaseDirective payload) {
    payload.setRequestUrl(url);
    if(communicateWebSocketEvent!=null){
      communicateWebSocketEvent.onOpen(payload);
    }
  }

  @Override
  public void onClosing(BaseDirective payload) {
    payload.setRequestUrl(url);
    if(communicateWebSocketEvent!=null){
      communicateWebSocketEvent.onClosing(payload);
    }
  }

  @Override
  public void onClosed(BaseDirective payload) {
    payload.setRequestUrl(url);
    if(communicateWebSocketEvent!=null){
      communicateWebSocketEvent.onClosed(payload);
    }
  }

  @Override
  public void onError(BaseDirective payload) {
    payload.setRequestUrl(url);
    if(communicateWebSocketEvent!=null){
      communicateWebSocketEvent.onError(payload);
    }
  }

  @Override
  public void onCheckTimeout() {
    if(communicateWebSocketEvent!=null){
      communicateWebSocketEvent.onCheckTimeout();
    }
  }
}
