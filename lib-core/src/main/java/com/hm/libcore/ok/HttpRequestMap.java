package com.hm.libcore.ok;

import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import okhttp3.Request;

import java.util.Map;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-04-30 16:49
 **/

public class HttpRequestMap {

    private String URI;
    private Map<String,Object> params = Maps.newConcurrentMap();
    private Request.Builder headers = new Request.Builder();
    private String mediaType = null;
    private String body = null;//请求体

    public HttpRequestMap(String URI) {
        this.URI = URI;
    }

    public String getURI() {
        return URI;
    }
    public HttpRequestMap setURI(String URI) {
        this.URI = URI;
        return this;
    }
    
    public HttpRequestMap putKeyVal(String key,Object val) {
        this.params.put(key, val);
    	return this;
    }

    public HttpRequestMap putAllMap(Map<String, Object> keyMap) {
        this.params.putAll(keyMap);
    	return this;
    }
    
    public HttpRequestMap header(String name,String value) {
    	headers.addHeader(name, value);
    	return this;
    }
    
    public HttpRequestMap mediaType(String mediaType) {
    	this.mediaType = mediaType;
    	return this;
    }
    
    public HttpRequestMap body(String body) {
    	return this.body(body, null);
    }
    
    public HttpRequestMap body(String body, String mediaType) {
    	this.body = body;
    	if(null != mediaType) {
    		this.mediaType(mediaType);
    	} else {
			//在用户未自定义的情况下自动根据内容判断
    		mediaType = HttpUtil.getMediaTypeByRequestBody(body);
			if(null != mediaType) {
				this.mediaType(mediaType);
			}
		}
    	return this;
    }
    
    public Request.Builder getHeaders() {
		return headers;
	}

	public void setHeaders(Request.Builder headers) {
		this.headers = headers;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, Object> getParams() {
        return params;
    }

    public String sendPost() {
    	return HttpUtil.httpPost(this);
    }
    public String sendGet() {
    	return HttpUtil.httpGet(this);
    }
    public void sendAsynPost() {
    	HttpUtil.httpPostAsyn(this);
    }
    public void sendAsynGet() {
    	HttpUtil.httpGetAsyn(this);
    }
    
    public static HttpRequestMap create(String url) {
    	return new HttpRequestMap(url);
    }
    public void printUrlInfo() {
    	System.err.println("url:"+URI+"  parm:"+ GSONUtils.ToJSONString(params));
    }
}
