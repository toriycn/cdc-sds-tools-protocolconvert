package com.jiduauto.cdc.sds.tools.jidullmsdk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * dbvs定义的协议数据
 */
public class BDVSPojo {

    public String getBdvs_version() {
        return bdvs_version;
    }

    public void setBdvs_version(String bdvs_version) {
        this.bdvs_version = bdvs_version;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getBdvs_device_id() {
        return bdvs_device_id;
    }

    public void setBdvs_device_id(String bdvs_device_id) {
        this.bdvs_device_id = bdvs_device_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public JSONArray getContexts() {
        return contexts;
    }

    public void setContexts(JSONArray contexts) {
        this.contexts = contexts;
    }

    public JSONObject getEvent() {
        return event;
    }

    public void setEvent(JSONObject event) {
        this.event = event;
    }

    public JSONObject getInternal_param() {
        return internal_param;
    }

    public void setInternal_param(JSONObject internal_param) {
        this.internal_param = internal_param;
    }

    public JSONObject getExtensions_param() {
        return extensions_param;
    }

    public void setExtensions_param(JSONObject extensions_param) {
        this.extensions_param = extensions_param;
    }

    private String bdvs_version;        //BDVS协议版本号
    private String authorization;       //用户鉴权
    private String bdvs_device_id;      //设备号
    private Long timestamp;             //时间戳
    private JSONArray contexts;         //设备端状态
    private JSONObject event;           //设备端事件
    private JSONObject internal_param;  //数据透传 sdk内部&&云端数据透传
    private JSONObject extensions_param;    //数据透传 客户端&&云端数据透传

    public static BDVSPojo convertToPojo(String jsonStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        BDVSPojo pojo = new BDVSPojo();
        pojo.setBdvs_version(jsonObject.getString("bdvs-version"));
        pojo.setAuthorization(jsonObject.getString("authorization"));
        pojo.setBdvs_device_id(jsonObject.getString("bdvs-device-id"));
        pojo.setTimestamp(jsonObject.getLong("timestamp"));
        pojo.setContexts(jsonObject.getJSONArray("contexts"));
        pojo.setEvent(jsonObject.getJSONObject("event"));
        if(jsonStr.contains("internal-param")){
            pojo.setInternal_param(jsonObject.getJSONObject("internal-param"));
        }
        if(jsonStr.contains("extensions-param")){
            pojo.setExtensions_param(jsonObject.getJSONObject("extensions-param"));
        }
        return pojo;
    }

    public static BDVSPojo convertToPojo(JSONObject jsonObject){
        BDVSPojo pojo = new BDVSPojo();
        pojo.setBdvs_version(jsonObject.getString("bdvs-version"));
        pojo.setAuthorization(jsonObject.getString("authorization"));
        pojo.setBdvs_device_id(jsonObject.getString("bdvs-device-id"));
        pojo.setTimestamp(jsonObject.getLong("timestamp"));
        pojo.setContexts(jsonObject.getJSONArray("contexts"));
        pojo.setEvent(jsonObject.getJSONObject("event"));
        pojo.setInternal_param(jsonObject.getJSONObject("internal-param"));
        pojo.setExtensions_param(jsonObject.getJSONObject("extensions-param"));
        return pojo;
    }
}
