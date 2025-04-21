package com.jiduauto.cdc.sds.tools.jidullmsdk.ttsclient;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TtssendRequest {
    @JSONField(name = "req_params")
    private Req_params req_params;
    // @JSONField(name = "request")
    // private Request request;

    @Data
    @Builder
    public static class Req_params {
        @JSONField(name = "text")
        private String text;
        @JSONField(name = "text_lang")
        private String text_lang;
        @JSONField(name = "stop_tag")
        private String stop_tag;
        @JSONField(name = "first_sentence")
        private boolean first_sentence;
    }
}