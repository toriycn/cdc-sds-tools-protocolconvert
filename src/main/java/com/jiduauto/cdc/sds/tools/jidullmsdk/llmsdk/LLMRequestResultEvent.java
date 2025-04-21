package com.jiduauto.cdc.sds.tools.jidullmsdk.llmsdk;

import com.jiduauto.cdc.sds.tools.jidullmsdk.BaseDirective;

public interface LLMRequestResultEvent {
//    public abstract void onReceiveASR(LLMBaseDirective payload) ;
//    public abstract void onReceiveNlu(LLMBaseDirective payload) ;
//    public abstract void onReceiveTTS(LLMBaseDirective payload) ;
     public abstract void onReceivePUSH(LLMBaseDirective payload);
    public abstract void onReceiveLLM(LLMBaseDirective payload);

    public abstract void onReceiveLLMClose(LLMBaseDirective payload);
    //链路上的错误
    public abstract void onConnectionError(LLMBaseDirective payload);
    //LLM的错误
    public abstract void onReceiveLLMError(LLMBaseDirective payload);
    //连接超时
    public abstract void onReceiveLLMConnectionTimeout(LLMBaseDirective payload);
}
