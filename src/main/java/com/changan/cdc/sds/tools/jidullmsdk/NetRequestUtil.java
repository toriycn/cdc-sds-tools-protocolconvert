package com.changan.cdc.sds.tools.jidullmsdk;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class NetRequestUtil {

    public static OkHttpClient initOkHttpClient(){
        OkHttpClient okHttpClient;
        System.out.println("initOkHttpClient:"+JiduCommonRequestParameter.getInstance().getOkHttpClient() );
        if(JiduCommonRequestParameter.getInstance().getOkHttpClient() != null){
            okHttpClient = JiduCommonRequestParameter.getInstance().getOkHttpClient();
        }
        else{
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(NetRequestParameter.DEFAULT_READTIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(NetRequestParameter.DEFAULT_CONNECTTIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(NetRequestParameter.DEFAULT_WRITETIMEOUT, TimeUnit.SECONDS)
                    .pingInterval(NetRequestParameter.DEFAULT_PINGINTERVAL,TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(NetRequestParameter.DEFAULT_MAXIDLECONNECTIONS,NetRequestParameter.DEFAULT_KEEPALIVEDURATION,TimeUnit.SECONDS))
                    .build();
        }
        return okHttpClient;
    }


    public static WebSocket createWebSocket(String baseUrl, WebSocketListener communicate) throws Exception {
        if(baseUrl == null || baseUrl.equals("")){
            throw new Exception("parameter baseUrl is null");
        }
        Builder builder = new Builder().url(baseUrl);
        try {
            if(JiduCommonRequestParameter.getInstance().getHttpHeader() !=null){
                System.out.println("initOkHttpClient:"+JiduCommonRequestParameter.getInstance().getHttpHeader() );
                Map<String,String> map = JiduCommonRequestParameter.getInstance().getHttpHeader();

                for(Entry<String,String> entities : map.entrySet()){
                    System.out.println("initOkHttpClient:"+ entities.getKey()+";"+entities.getValue());
                    builder.addHeader(entities.getKey(),entities.getValue());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Request request = builder.build();
        WebSocket ws =  initOkHttpClient().newWebSocket(request, communicate);
        return ws;
    }

    static class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            // 请求发起前可以进行一些处理，例如打印请求信息
            long startTime = System.nanoTime();
            System.out.println("Sending request: " + request.url());

            // 执行请求
            Response response = chain.proceed(request);

            // 请求结束后可以进行一些处理，例如打印响应时间和状态
            long endTime = System.nanoTime();
            long tookTime = endTime - startTime;
            System.out.println("Received response for: " + request.url() + " - " + response.code() + " - " + tookTime / 1e6f + "ms");

            // 返回响应结果
            return response;
        }
    }
}
