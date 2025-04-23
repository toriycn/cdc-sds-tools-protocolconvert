package com.changan.cdc.sds.tools.jidullmsdk.llmsdk;

import java.util.HashMap;
import java.util.Map;

public class LLMBaseDirective {
    private String name;
    private String namespace;
    private String payload;
    private String asrSn;
    private int audioZone;
    private int errcode = 1000;
    private String errmsg;

    public int getAudioZone() {
        return audioZone;
    }

    public void setAudioZone(int audioZone) {
        this.audioZone = audioZone;
    }

    public String getAsrSn() {
        return asrSn;
    }

    public void setAsrSn(String asrSn) {
        this.asrSn = asrSn;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
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
                "name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", payload='" + payload + '\'' +
                ", asrSn='" + asrSn + '\'' +
                ", audioZone='" + audioZone + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
