package com.jiduauto.cdc.sds.tools.jidullmsdk.basesdk;

import com.jiduauto.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.jiduauto.cdc.sds.tools.jidullmsdk.SDKLogUtil;

public abstract class CommunicateWebSocketEvent {
  public abstract void onReceiveMsg(BaseDirective payload);
  public abstract void onOpen(BaseDirective payload);
  public abstract  void onClosing(BaseDirective payload);
  public abstract  void onClosed(BaseDirective payload);
  public abstract  void onError(BaseDirective payload);
  public abstract  void onCheckTimeout();

  public void outputLog(String s) {
    SDKLogUtil.log(s);
  }

  public  void onEndSn(String connectionSn) {}

    public void onExit() {
    }
}
