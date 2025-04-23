package com.changan.cdc.sds.tools.jidullmsdk;

import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;

public class JiduCommonRequestParameter {
    private OkHttpClient okHttpClient;
    private String baseUrl = "";
    private String vid;

    private Map<String,String> httpHeader = new HashMap<String,String>();

    private JiduCommonRequestParameter()
    {}
    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public static JiduCommonRequestParameter mInstance;

    public Map<String, String> getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(Map<String, String> httpHeader) {
        this.httpHeader = httpHeader;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public synchronized static JiduCommonRequestParameter getInstance() {
        if (mInstance == null)
            mInstance = new JiduCommonRequestParameter();
        return mInstance;
    }
}
