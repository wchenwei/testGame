package com.hm.libcore.ok;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-04-30 16:39
 **/
public class HttpUtil {

    public static String httpGet(HttpRequestMap requestMap) {
        try {
        	HttpQueue queue = new HttpQueue(requestMap);
        	return queue.httpGet();
		} catch (Exception e) {
			requestMap.printUrlInfo();
			e.printStackTrace();
		}
        return null;
    }

    public static String httpPost(HttpRequestMap requestMap)  {
    	try {
    		HttpQueue queue = new HttpQueue(requestMap);
            return queue.httpPost();
		} catch (Exception e) {
			requestMap.printUrlInfo();
			e.printStackTrace();
		}
        return null;
    }

    public static void httpGetAsyn(HttpRequestMap requestMap) {
    	try {
    		 HttpQueue queue = new HttpQueue(requestMap);
    	     queue.httpGetAsyn();
		} catch (Exception e) {
			requestMap.printUrlInfo();
			e.printStackTrace();
		}
    }

    public static void httpPostAsyn(HttpRequestMap requestMap) {
    	try {
    		HttpQueue queue = new HttpQueue(requestMap);
            queue.httpPostAsyn();
		} catch (Exception e) {
			requestMap.printUrlInfo();
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param body 请求参数体
     * @return mediaType，如果无法判断返回null
     */
    public static String getMediaTypeByRequestBody(String body) {
		String mediaType = null;
		if (body != null && body.trim() != "") {
			char firstChar = body.charAt(0);
			switch (firstChar) {
			case '{':
			case '[':
				// JSON请求体
				mediaType = "application/json;charset=utf-8";
				break;
			case '<':
				// XML请求体
				mediaType = "application/xml;charset=utf-8";
				break;

			default:
				break;
			}
		}
		return mediaType;
	}
    
}
