package com.changan.cdc.sds.tools.jidullmsdk.ttsclient;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TtsendRequest {
    @JSONField(name = "req_params")
    private Req_params req_params;
    // @JSONField(name = "request")
    // private Request request;

    @Data
    @Builder
    public static class Req_params {
        @JSONField(name = "indentation")
        private String indentation;
    }

}