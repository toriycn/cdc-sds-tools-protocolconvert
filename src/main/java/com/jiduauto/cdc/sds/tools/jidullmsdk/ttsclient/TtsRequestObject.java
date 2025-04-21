package com.jiduauto.cdc.sds.tools.jidullmsdk.ttsclient;

import java.util.ArrayList;
import java.util.List;

public class TtsRequestObject {
    private String sid;
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type = 0;
    private  int setenceTotal;

    public String getLocalTtsResult() {
        return localTtsResult;
    }

    public void setLocalTtsResult(String localTtsResult) {
        this.localTtsResult = localTtsResult;
    }

    private String localTtsResult ;
    private List<String> tts_result = new ArrayList<String>();
    private long receiveTime;
    private long sendTime;
    private long endTime;
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public int getSetenceTotal() {
        return setenceTotal;
    }

    public void setSetenceTotal(int setenceTotal) {
        this.setenceTotal = setenceTotal;
    }

    public List<String> getTts_result() {
        return tts_result;
    }

    public void setTts_result(List<String> tts_result) {
        this.tts_result = tts_result;
    }


}
