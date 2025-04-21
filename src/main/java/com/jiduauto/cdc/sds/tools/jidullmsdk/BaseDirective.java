package com.jiduauto.cdc.sds.tools.jidullmsdk;

import java.util.HashMap;
import java.util.Map;

public class BaseDirective {
    private String payload;
    private String connnectionSn;
    private String appendParameter;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    private String requestUrl;
    public String getAppendParameter() {
        return appendParameter;
    }
    public String getConnnectionSn() {
        return connnectionSn;
    }
    public void setConnnectionSn(String connnectionSn) {
        this.connnectionSn = connnectionSn;
    }
    public void setAppendParameter(String appendParameter) {
        this.appendParameter = appendParameter;
    }

    private Map<String,String> appendMsg = new HashMap<>();
    public static final String APPENDMSGKEY_AUDIO_ZONE = "AUDIO_ZONE";
    public static final String APPENDMSGKEY_ASRSN = "ASRSN";

    private int errcode = 1000;
    private String errmsg;

    public Map<String, String> getAppendMsg() {
        return appendMsg;
    }

    public void setAppendMsg(Map<String, String> appendMsg) {
        this.appendMsg = appendMsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
    @Override
    public String toString() {
        return "BaseDirective{" +
                ", payload='" + payload + '\'' +
                '}';
    }
}
