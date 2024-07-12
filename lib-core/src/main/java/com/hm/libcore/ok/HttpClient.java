package com.hm.libcore.ok;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-04-27 15:21
 **/
@Slf4j
public class HttpClient {

    /** 连接超时时间 */
    public static final int CONNECT_TIMEOUT = 5000;
    public static final long READ_TIMEOUT = 5000;

    /** 最大的空闲socket连接数 */
    public static int MAX_IDLE_CONNECTIONS = Math.max(Runtime.getRuntime().availableProcessors(), 1); //
    private static volatile OkHttpClient okHttpClient = null;
    private static volatile HttpClient instance = null;
    private static final String DEFAULTMEDIATYPE =  "application/x-www-form-urlencoded;charset=utf-8";

    private HttpClient() {
        if (instance == null){
            synchronized (HttpClient.class){
            	if (instance == null){
	                okHttpClient = (new OkHttpClient()).newBuilder().connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, 5L, TimeUnit.MINUTES))
	                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS).readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS).build();
            	}
            }
        }
    }
    public static HttpClient getHttpClient() {
        if (instance == null) {
            instance = new HttpClient();
        }
        return instance;
    }


    /**
     * 异步post请求
     */
    public void sendPostAsyn(String url, String param, Callback callBack) throws Exception{
        RequestBody requestBody = RequestBody.create(MediaType.parse(DEFAULTMEDIATYPE),param);
        Request request = (new Request.Builder()).url(url).post(requestBody).build();
        doRequestAsyn(request,callBack);
    }

    /**
     * 异步get请求
     */
    public void sendGetAsyn(String url, String param, Callback callBack) throws Exception {
        Request request = (new Request.Builder()).url(url+"?"+param).get().build();
        doRequestAsyn(request,callBack);
    }

    /**
     * 异步请求
     */
    private void doRequestAsyn(Request request, Callback callBack) throws Exception {
        okHttpClient.newCall(request).enqueue(callBack);
    }

    /**
     * 异步post请求
     */
    public Response sendPost(String url, String param) throws Exception{
        RequestBody requestBody = RequestBody.create(MediaType.parse(DEFAULTMEDIATYPE),param);
        return sendPost(url, requestBody);
    }

    public Response sendPost(String url, RequestBody requestBody) throws Exception {
        Request request = (new Request.Builder()).url(url).post(requestBody).build();
        return doRequest(request);
    }


    public Response sendGet(String url, String param) throws Exception{
        Request request = (new Request.Builder()).url(url+"?"+param).get().build();
        return doRequest(request);
    }

    private Response doRequest(Request request) throws Exception {
        return okHttpClient.newCall(request).execute();
    }
    
	public Response sendPost(String url, String param, String mediaType, Request.Builder builder) throws Exception {
		RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType != null ? mediaType : DEFAULTMEDIATYPE),
				param);
		Request request = (builder == null ? new Request.Builder() : builder).url(url).post(requestBody).build();
		return doRequest(request);
	}


}
