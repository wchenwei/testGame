package com.hm.libcore.util.httppool;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @Description: http连接池管理
 * @author siyunlong  
 * @date 2019年11月6日 下午8:13:20 
 * @version V1.0
 */
public class HttpPoolClient {
	private HttpClient httpClient;
    private PoolingHttpClientConnectionManager connectionManager;
    private IdleConnectionMonitorThread idleConnectionMonitor;
    private ClientConfig clientConfig = new ClientConfig();
    private RequestConfig requestConfig;
    
    public HttpPoolClient() {
    	initHttpClient();
	}

	public void initHttpClient() {
		this.connectionManager = new PoolingHttpClientConnectionManager();
        this.connectionManager.setMaxTotal(this.clientConfig.getMaxConnectionsCount());
        this.connectionManager.setDefaultMaxPerRoute(this.clientConfig.getMaxConnectionsCount());
        this.connectionManager.setValidateAfterInactivity(1);
        HttpClientBuilder httpClientBuilder =
                HttpClients.custom().setConnectionManager(connectionManager);
        this.httpClient = httpClientBuilder.build();
        this.requestConfig =
                RequestConfig.custom()
                        .setContentCompressionEnabled(false)
                        .setConnectionRequestTimeout(
                                this.clientConfig.getConnectionRequestTimeout())
                .setConnectTimeout(this.clientConfig.getConnectionTimeout())
                .setSocketTimeout(this.clientConfig.getSocketTimeout()).build();
        this.idleConnectionMonitor = new IdleConnectionMonitorThread(this.connectionManager);
        this.idleConnectionMonitor.setDaemon(true);
        this.idleConnectionMonitor.start();
    }
	
	public String sendJson(String url,String json) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(this.requestConfig);
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.addHeader("Accept", "application/json"); // 设置接收数据的格式  
        httpPost.addHeader("Authorization", "Basic dXNlcjpwYXNzd29yZA==");  
        // 解决中文乱码问题
        StringEntity stringEntity = new StringEntity(json, "UTF-8");
        stringEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(stringEntity);
		HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
	        e.printStackTrace();
        } finally {
        }
        return null;
	}
	
	//TODO 沒有测试
//	public String postJson(String url,Map<String, Object> params) {
//		HttpPost httppost = new HttpPost(url);
//		httppost.setConfig(this.requestConfig);
//		setPostParams(httppost, params);
//		HttpResponse response = null;
//        try {
//            response = httpClient.execute(httppost, HttpClientContext.create());
//            HttpEntity entity = response.getEntity();
//            String result = EntityUtils.toString(entity, "utf-8");
//            EntityUtils.consume(entity);
//            return result;
//        } catch (Exception e) {
//	        e.printStackTrace();
//        } finally {
//            
//        }
//        return null;
//	}
	
	public void shutdown() {
		if(this.idleConnectionMonitor != null) {
			this.idleConnectionMonitor.shutdown();
		}
    }
	
	private static void setPostParams(HttpPost httpost,
            Map<String, Object> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
	private boolean isRequestSuccessful(HttpResponse httpResponse) {
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = -1;
        if (statusLine != null) {
            statusCode = statusLine.getStatusCode();
        }
        return statusCode / 100 == HttpStatus.SC_OK / 100;
    }
	
	public static void main(String[] args) {
//		HttpPoolClient httpPoolClient = new HttpPoolClient();
//		httpPoolClient.sendJson(url, json)
	}
}
