package com.changan.cdc.sds.tools.jidullmsdk.llmsdk;

import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.multiconnect.BaseMultiRequestManageFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;


public class RequestManageLLM extends CommunicateWebSocketEvent{
    BaseMultiRequestManageFactory manageFactory = BaseMultiRequestManageFactory.getInstance();
    private static String connectionSn = UUID.randomUUID().toString();

    private  CommunicateWebSocketEvent pevent;
    private String tuid = null;
    private String url = null;
    private static RequestManageLLM mInstance;

    private RequestManageLLM() {
    }
    @Override
    public void onReceiveMsg(BaseDirective payload) {
        try {
            System.out.println(JSONObject.toJSONString(payload));
            if (pevent != null) {
                pevent.onReceiveMsg(payload);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onOpen(BaseDirective payload) {
        connectionSn = UUID.randomUUID().toString();
        try {
            System.out.println(JSONObject.toJSONString(payload));
            if (pevent != null) {
                pevent.onOpen(payload);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClosing(BaseDirective payload) {
        try {
            System.out.println(JSONObject.toJSONString(payload));
            if (pevent != null) {
                pevent.onClosing(payload);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClosed(BaseDirective payload) {
        try {
            System.out.println(JSONObject.toJSONString(payload));
            if (pevent != null) {
                pevent.onClosed(payload);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onError(BaseDirective payload) {
        try {
            System.out.println(JSONObject.toJSONString(payload));
            if (pevent != null) {
                pevent.onError(payload);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onCheckTimeout() {
        try {
            if (pevent != null) {
                pevent.onCheckTimeout();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public synchronized static RequestManageLLM getInstance() {
        if (mInstance == null)
            mInstance = new RequestManageLLM();
        return mInstance;
    }

    public void setUrl(String baseUrl,String tuid){
        if(baseUrl.endsWith("/")){
            this.url = baseUrl+tuid+"/"+connectionSn;
        }
        else{
            this.url = baseUrl+"/"+tuid+"/"+connectionSn;
        }
        this.tuid=tuid;
        manageFactory.setUrlEvent(url,this);
    }
    public void setReceiveEvent(CommunicateWebSocketEvent event){
        if(event!=null){
            this.pevent = event;
        }
    }
    public static void main(String[] args) throws Exception {
        //客户端分为三种：
        //       一种是直接发起；有连接；一种是网络断开一会又恢复。
        // 使用前必须设定 服务端url和tuid
        //最终请求路径为：baseurl+"/"+tuid+"/"+connectionSN; 其中connectionSN外部不可见
        RequestManageLLM.getInstance().setUrl("ws://127.0.0.1:8080/dispatch/llm/","tuid1");
        //同步事件：
        //           filePath :文件路径，不带分隔符
        //           fileName: 文件名
        //           appendJson:文件其他附加信息
        //           -1 文件不存在
        //           -2 文件不可读
        //           -3表示其他错误
        //           0 发送完成
        RequestManageLLM.getInstance().sendFile("D:\\workspace\\github\\cdc-sds-tools-protocolconvert\\","output.wav","{}");

        //普通消息上送,
        RequestObjectOnLLM requestObjectOnLLM = new RequestObjectOnLLM();
        //消息类型，需要协商
        requestObjectOnLLM.setType("EVENT_TRACKING");
        JSONObject object = new JSONObject();
        object.put("id","test msg");
        //消息payload可以自定义json
        requestObjectOnLLM.setPayload(object.toJSONString());
        //普通消息上送
        RequestManageLLM.getInstance().send("EVENT_TRACKING",requestObjectOnLLM);
    }

    public void send(String type ,RequestObjectOnLLM requestObjectOnLLM) throws Exception {
        if(type!=null){
            requestObjectOnLLM.setType(type);
        }
        if(this.tuid ==null){
            throw new Exception("tuid not set");
        }
        if(this.url ==null){
            throw new Exception("url not set");
        }
        if(requestObjectOnLLM.getPayload().length() > 8192){
            throw new Exception("max request message length is 8192");
        }
        manageFactory.sendRequest(url,connectionSn,JSONObject.toJSONString(requestObjectOnLLM));
    }
    /*
           filePath :文件路径，不带分隔符
           fileName: 文件名
           appendJson:文件其他附加信息
           -1 文件不存在
           -2 文件不可读
           0 发送完成
           -3表示其他错误
     */
    public int sendFile(String filePath,String fileName,String appendJson) throws Exception {
        if(this.tuid ==null){
            throw new Exception("tuid not set");
        }
        if(this.url ==null){
            throw new Exception("url not set");
        }
        try {
            if(appendJson == null){
                appendJson="{}";
            }
            String uploadUUID = UUID.randomUUID().toString();
            uploadUUID=uploadUUID.replace("-","");
            uploadUUID = uploadUUID.substring(0,8);
            JSONObject appendJsonStr = JSONObject.parseObject(appendJson);
            appendJsonStr.put("fileName", fileName);
            appendJsonStr.put("originPath", filePath);

            File file = new File(filePath + fileName);
            if (file.exists() == false) {
                return -1;
            }
            if (file.canRead() == false) {
                return -2;
            }
            String fileExtension = null;
            int lastIndex = fileName.lastIndexOf('.');
            if (lastIndex != -1) {
                fileExtension = filePath.substring(lastIndex + 1);
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            appendJsonStr.put("fileLength", file.length());
            appendJsonStr.put("fileExtension", fileExtension);
            appendJsonStr.put("UUID", uploadUUID);
            RequestObjectOnLLM requestObjectOnLLM = new RequestObjectOnLLM();
            requestObjectOnLLM.setType("FILE_UPLOAD");

            int CHUNCKED_SIZE = 8192;
            byte[] bytes = new byte[CHUNCKED_SIZE];
            ByteBuffer buffer = ByteBuffer.allocate(CHUNCKED_SIZE+8);
            try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
                int len = -1;
                while ((len = raf.read(bytes)) != -1) {
                    if (len < CHUNCKED_SIZE) {
                        bytes = Arrays.copyOfRange(bytes, 0, len);
                    }
                    md.update(bytes, 0, len);
                    buffer.put(uploadUUID.getBytes("UTF-8"),0,8);
                    buffer.put(bytes);
                    manageFactory.sendRequest(url,connectionSn,buffer.array());
                    buffer = ByteBuffer.allocate(CHUNCKED_SIZE+8);
                    Thread.sleep(40);
                }
                byte[] digest = md.digest();
                StringBuilder md5sb = new StringBuilder();
                for (byte b : digest) {
                    md5sb.append(String.format("%02x", b));
                }
                System.out.println("RequestManageLLM sendfile "+filePath+fileName+";md5:"+md5sb);
                appendJsonStr.put("MD5",md5sb);
                RequestObjectOnLLM requestObjectOnLLMEnd = new RequestObjectOnLLM();
                requestObjectOnLLMEnd.setPayload(appendJsonStr.toJSONString());
                send("FILE_UPLOAD",requestObjectOnLLMEnd);
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -3;
    }
}
