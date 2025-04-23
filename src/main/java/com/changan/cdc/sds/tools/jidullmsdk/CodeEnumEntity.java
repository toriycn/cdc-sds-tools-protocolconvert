package com.changan.cdc.sds.tools.jidullmsdk;

public enum CodeEnumEntity implements CodeEnum {
    SUUCEESS(1000,"SUUCEESS"),
    MESSAGE_FORMAT_ERROR(-100,"websocket返回数据格式错误"),
    WEBSOCKET_NO_NETWORK(-101, "无网络连接"),
    WEBSOCKET_COMM_FAILURE(-102,"通讯其他异常"),
    WEBSOCKET_CLIENT_CHECK_TIMEOUT(-103,"客户端主动检测超时" ),
    WEBSOCKET_CLIENT_CHECK_REMOVED(-104,"连接已经被移除" ),
    WEBSOCKET_CLIENT_CHECK_CONNECT_TIMEOUT(-105,"建立连接超时" ),
    WEBSOCKET_MESSAGE_OVERSIZE(-106,"消息超长" ),
    WEBSOCKET_MESSAGE_ISNULL(-107,"消息为空" ),
    LLMREQUEST_TIMEOUT(-108, "大模型通讯等待结果超时");
    //错误码
    public int code;
    //提示信息
    public String message;

    //构造函数
    CodeEnumEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }

    //获取状态码
    @Override
    public int getCode() {
        return code;
    }

    //获取提示信息
    @Override
    public String getMessage() {
        return message;
    }

}
