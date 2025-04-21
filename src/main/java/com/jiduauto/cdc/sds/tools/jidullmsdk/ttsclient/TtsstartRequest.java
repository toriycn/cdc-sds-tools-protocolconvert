package com.jiduauto.cdc.sds.tools.jidullmsdk.ttsclient;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TtsstartRequest {
    @JSONField(name = "req_params")
    private Req_params req_params;
    // @JSONField(name = "request")
    // private Request request;

    @Data
    @Builder
    public static class Req_params {
        @JSONField(name = "speaker")
        private String speaker;
        @JSONField(name = "ref_audio_path")
        private String ref_audio_path; // 目前未生效，使用默认值即可
        @JSONField(name = "prompt_text")
        private String prompt_text;
        @JSONField(name = "prompt_lang")
        private String prompt_lang;
        @JSONField(name = "batch_size")
        private String batch_size;
        @JSONField(name = "text_split_method")
        private String text_split_method;
        @JSONField(name = "media_type")
        private String media_type;
        @JSONField(name = "streaming_mode")
        private String streaming_mode;
        @JSONField(name = "tuid")
        private String tuid;
    }
}

