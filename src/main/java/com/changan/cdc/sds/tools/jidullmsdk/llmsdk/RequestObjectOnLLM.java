package com.changan.cdc.sds.tools.jidullmsdk.llmsdk;

import com.alibaba.fastjson.JSONObject;

public class RequestObjectOnLLM {
    private String type ;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    private String payload;

}
