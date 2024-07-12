package com.hm.libcore.ok;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * @description: 请求
 * @author: chenwei
 * @create: 2020-04-28 09:31
 **/
@Slf4j
public class HttpQueue {

    private HttpRequestMap httpRequestMap;
    private HttpClient httpClient;


    public HttpQueue(HttpRequestMap httpRequestMap) {
        this.httpRequestMap = httpRequestMap;
        this.httpClient = HttpClient.getHttpClient();
    }

    public String httpGet() throws Exception {
        String data = createParams();
        Response response = this.httpClient.sendGet(httpRequestMap.getURI(), data);
        String result = response.body().string();
        if (log.isDebugEnabled()){
            log.error("result:{}", result);
        }
        if (!response.isSuccessful()){
            return "error";
        }
        return result;
    }

    public String httpPost() throws Exception {
    	Response response = null;
    	if(null != httpRequestMap.getBody()) {
    		response = this.httpClient.sendPost(httpRequestMap.getURI(), httpRequestMap.getBody(), httpRequestMap.getMediaType(), httpRequestMap.getHeaders());
    	} else {
	        String data = createParams();
	        response = this.httpClient.sendPost(httpRequestMap.getURI(), data);
    	}
        String result = response.body().string();
        if (log.isDebugEnabled()){
            log.error(" result:{}", result);
        }
        if (response.isSuccessful()){
            return result;
        }
        return "error";
    }

    public void httpPostAsyn() throws Exception {
        String data = createParams();
        this.httpClient.sendPostAsyn(httpRequestMap.getURI(), data, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("exception:{}", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    log.error("serverId:{} msg:{}", response.body().string());
                }else {
                    log.info("serverId:{} OK!! msg:{}",response.body().string());
                }
            }
        });
    }

    public void httpGetAsyn() throws Exception {
        String data = createParams();
        this.httpClient.sendGetAsyn(httpRequestMap.getURI(), data, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error(" exception:{}", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    log.error(" msg:{}", response.body().string());
                }else {
                    log.info(" OK!! msg:{}",response.body().string());
                }
            }
        });
    }


    private String createParams() {
        boolean flag = false;
        Iterator iterator;
        String req = "";
        try {
            for(iterator = httpRequestMap.getParams().keySet().iterator(); iterator.hasNext(); flag = true) {
                String key = (String)iterator.next();
                if (flag) {
                    req = req + "&";
                }
                req = req + key + "=" + URLEncoder.encode(String.valueOf(httpRequestMap.getParams().get(key)),"UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return req;
    }
}
