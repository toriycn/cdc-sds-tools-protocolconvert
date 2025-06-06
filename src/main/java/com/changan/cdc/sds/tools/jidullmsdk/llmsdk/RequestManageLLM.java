package com.changan.cdc.sds.tools.jidullmsdk.llmsdk;

import com.alibaba.fastjson.JSONObject;
import com.changan.cdc.sds.tools.jidullmsdk.BaseDirective;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.CommunicateWebSocketEvent;
import com.changan.cdc.sds.tools.jidullmsdk.basesdk.multiconnect.BaseMultiRequestManageFactory;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RequestManageLLM extends CommunicateWebSocketEvent{
    private boolean enableAlwaysConn = false;
    private boolean isConnectWithServer = false;
    BaseMultiRequestManageFactory manageFactory = BaseMultiRequestManageFactory.getInstance();
    private static String connectionSn = UUID.randomUUID().toString();
    private  CommunicateWebSocketEvent pevent;
    private String tuid = null;
    private String url = null;
    private static RequestManageLLM mInstance;

    private AtomicBoolean runState = new AtomicBoolean(false);

    private RequestManageLLM() {
        runState.set(true);
        new Thread("RequestManageLLM-CheckMsg"){
            @Override
            public void run() {
                try{
                    int i = 0;
                    while(runState.get()){
                        if(tuid ==null){
                            return;
                        }
                        if(url ==null){
                            return;
                        }
                        if(enableAlwaysConn){
                            if(isConnectWithServer == false){
                                RequestObjectOnLLM requestObjectOnLLM = new RequestObjectOnLLM();
                                //普通消息上送
                                RequestManageLLM.getInstance().send("PING_MSG",requestObjectOnLLM);
                            }
                            Thread.sleep(1000*30);
                            if(i++>5){
                                RequestObjectOnLLM requestObjectOnLLM = new RequestObjectOnLLM();
                                RequestManageLLM.getInstance().send("PING_MSG",requestObjectOnLLM);
                                i=0;
                            }
                        }
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    @Override
    public void onReceiveMsg(BaseDirective payload) {
        try {
            System.out.println(JSONObject.toJSONString(payload));
            JSONObject object = JSONObject.parseObject(payload.getPayload());
            String data = object.getString("data");
            if(data.contains("camera")){
                RequestManageLLM.getInstance().sendFile(object.getString("requestId"),"D:\\workspace\\github\\cdc-sds-tools-protocolconvert\\","dog.jpeg","{}");
            }
            else if(data.contains("设置")){
                RequestObjectOnLLM requestObjectOnLLM = new RequestObjectOnLLM();
                //消息类型，需要协商
                requestObjectOnLLM.setType("CLIENT_STATUS_COLLECT");
                JSONObject returnMsg = new JSONObject();
                returnMsg.put("requestId",object.getString("requestId"));
                //消息payload可以自定义json
                returnMsg.put("airConditionTemperature",extractNumbers(data));
                requestObjectOnLLM.setPayload(returnMsg.toJSONString());
                //普通消息上送
                RequestManageLLM.getInstance().send("CLIENT_STATUS_COLLECT",requestObjectOnLLM);
            }
            else{
                List<String> collectString= new ArrayList<String>();
                collectString.addAll(Arrays.asList(data.split(",")));
                collectClientInfo(collectString,object.getString("requestId"));
            }
            if (pevent != null) {
                pevent.onReceiveMsg(payload);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static String extractNumbers(String str) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("").trim();
    }

    private  void collectClientInfo( List<String> keyList,String requestId) throws Exception {
        RequestObjectOnLLM requestObjectOnLLM = new RequestObjectOnLLM();
        //消息类型，需要协商
        requestObjectOnLLM.setType("CLIENT_STATUS_COLLECT");
        JSONObject returnMsg = new JSONObject();
        returnMsg.put("requestId",requestId);
        for(String key:keyList){
            if(key.contains("温度")){
                returnMsg.put(key, 86);
            }else{
                returnMsg.put(key, new Random().nextInt(100));
            }
        }
        //消息payload可以自定义json
        requestObjectOnLLM.setPayload(returnMsg.toJSONString());
        //普通消息上送
        RequestManageLLM.getInstance().send("CLIENT_STATUS_COLLECT",requestObjectOnLLM);
    }

    @Override
    public void onOpen(BaseDirective payload) {
//        connectionSn = UUID.randomUUID().toString();
        try {
            System.out.println(JSONObject.toJSONString(payload));
            if (pevent != null) {
                pevent.onOpen(payload);
            }
            isConnectWithServer = true;
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
            this.manageFactory.exit(url);
            isConnectWithServer = false;
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
        RequestManageLLM.getInstance().setUrl("wss://dev-edc.sda.changan.com.cn/dubhe/dubhe-gateway/dispatch/llm","tuid1");

//        RequestManageLLM.getInstance().setUrl("ws://127.0.0.1:8080/dispatch/llm","tuid1");
        RequestManageLLM.getInstance().enableAlwaysConnect();
        //同步事件：
        //           filePath :文件路径，不带分隔符
        //           fileName: 文件名
        //           appendJson:文件其他附加信息
        //           -1 文件不存在
        //           -2 文件不可读
        //           -3表示其他错误
        //           0 发送完成
//        RequestManageLLM.getInstance().sendFile(UUID.randomUUID().toString(),"D:\\workspace\\github\\cdc-sds-tools-protocolconvert\\","pic.png","{}");

//        //普通消息上送,
//        RequestObjectOnLLM requestObjectOnLLM = new RequestObjectOnLLM();
//        //消息类型，需要协商
//        requestObjectOnLLM.setType("EVENT_TRACKING");
//        JSONObject object = new JSONObject();
//        object.put("id","test msg");
//        //消息payload可以自定义json
//        requestObjectOnLLM.setPayload(object.toJSONString());
//        //普通消息上送
//        RequestManageLLM.getInstance().send("EVENT_TRACKING",requestObjectOnLLM);

        Thread.sleep(100000);
    }

    private void enableAlwaysConnect() {
        this.enableAlwaysConn = true;
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
        if(requestObjectOnLLM.getPayload()!=null){
            if(requestObjectOnLLM.getPayload().length() > 8192){
                throw new Exception("max request message length is 8192");
            }
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
    public int sendFile(String requestId,String filePath,String fileName,String appendJson) throws Exception {
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
            appendJsonStr.put("requestId", requestId);

            byte[] uuid = asBytes(UUID.fromString(requestId));
            int CHUNCKED_SIZE = 8192;
            byte[] bytes = new byte[CHUNCKED_SIZE];
            ByteBuffer fileBuffer = ByteBuffer.allocate((int) file.length());
            //UUID+Int+data
            ByteBuffer buffer = ByteBuffer.allocate(CHUNCKED_SIZE+16+4);
            try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
                int len = -1;
                int index = 0;
                while ((len = raf.read(bytes)) != -1) {
                    if (len < CHUNCKED_SIZE) {
                        buffer = ByteBuffer.allocate(len+16+4);
                        bytes = new byte[len];
                        bytes = Arrays.copyOfRange(bytes, 0, len);
                    }
                    fileBuffer.put(bytes);
                    buffer.put(uuid,0,16);
                    buffer.putInt(index++);
                    buffer.put(bytes);
                    manageFactory.sendRequest(url,connectionSn,buffer.array());
                    buffer = ByteBuffer.allocate(CHUNCKED_SIZE+16+4);
                    Thread.sleep(40);
                }
                fileBuffer.flip();
                md.update(fileBuffer.array());
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

    public static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
