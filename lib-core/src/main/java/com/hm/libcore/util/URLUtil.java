package com.hm.libcore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hm.libcore.ok.HttpRequestMap;
import org.json.JSONObject;

import com.google.gson.Gson;

import cn.hutool.http.HttpRequest;



/**
 * URL请求管理类
 * @author xogame
 *
 */
public class URLUtil {
	//最大重试次数
	public static int MaxRetryNum = 2;
	
	public static String urlGet(String sdd) {
    	StringBuffer sb = new StringBuffer();
        try {
            URL url= new URL(sdd);
            InputStreamReader in = new InputStreamReader(url.openStream(), "UTF-8");
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                if (line != null && !line.isEmpty()) {
                	sb.append(line);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return sb.toString();
    }
	
	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Properties parameters) {
    	StringBuilder sb = new StringBuilder();
		if(parameters != null) {
			for (Object key : parameters.keySet()) {
				sb.append(key.toString()+"="+parameters.getProperty(key.toString())+"&");
			}
		}
		if(sb.length()>0) {
			sb.substring(0, sb.length()-1);
		}
		final String param = sb.toString();
		
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Authorization", "Basic YWRtaW4=");  
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);  
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
    /**
     * 向指定 URL 发送POST方法的请求,可以使用 https访问
     * @param url
     * 				发送请求的 URL,可以是https和http两种
     * @param parameters
     * 				请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     * 			所代表远程资源的响应结果
     */
     public static String sendPost(final String url, final String parameters) {
    	 PrintWriter out = null;
    	 BufferedReader in = null;
    	 String    result = "";
          try {
              URL realUrl = new URL(url);
              if("https".equalsIgnoreCase(realUrl.getProtocol())){  
                  SslUtils.ignoreSsl();  
              }  
              
              URLConnection conn = realUrl.openConnection();
              conn.setRequestProperty("accept", "*/*");
              conn.setRequestProperty("connection", "Keep-Alive");
              conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
              conn.setDoOutput(true);
              conn.setDoInput(true);
              conn.setConnectTimeout(6*1000);//设置6秒的连接超时
              conn.setReadTimeout(6*1000); //设置6秒的读取超时
              out = new PrintWriter(conn.getOutputStream());
              out.print(parameters);
              out.flush();
              in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
              String line = null;
              while ((line = in.readLine()) != null) {
                  result += line;
              }
              } catch (Exception e) {
                  System.out.println("发送 POST 请求出现异常！"+e);
                  e.printStackTrace();
              }
              
              finally{
                  try{
                      if(out!=null){
                          out.close();
                      }
                      if(in!=null){
                          in.close();
                      }
                  }
                  catch(IOException ex){
                      ex.printStackTrace();
                  }
              }
          return result;
      }
    
    
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param parameters
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String postURL(String url, String parameters) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Authorization", "Basic YWRtaW4=");  
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);  
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(parameters);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * hiChina:向指定 URL 异步发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param parameters
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String AsyncSendPost(final String url, final String parameters) {
  		try {
  		    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
  			cachedThreadPool.execute(new Runnable() {
  				@Override
  				public void run() {
  					URLUtil.urlGet(url+"?"+parameters);
//  					URLUtil.postURL(url, parameters);
  				}
  			});
  		} catch (Exception e) {
//  			Log.Info(Log.GetExecutedMethodName() + ": " + e);
  		}
  		return null;
  	}
  	
    /** 
     * 发送HttpPost请求 
     *  
     * @param strURL 
     *            服务地址 
     * @param params 
     *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/> 
     * @return 成功:返回json字符串<br/> 
     */  
    public static String sendJsonPost(String strURL, String params) {  
    	HttpURLConnection connection = null;
        try {  
            URL url = new URL(strURL);// 创建连接  
            connection = (HttpURLConnection) url  
                    .openConnection();  
            connection.setDoOutput(true);  
            connection.setDoInput(true);  
            connection.setUseCaches(false);  
            connection.setInstanceFollowRedirects(true);  
            connection.setRequestMethod("POST"); // 设置请求方式  
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式  
            connection.setRequestProperty("Authorization", "Basic dXNlcjpwYXNzd29yZA==");  
            connection.connect();  
            OutputStreamWriter out = new OutputStreamWriter(  
                    connection.getOutputStream(), "UTF-8"); // utf-8编码  
            out.append(params);  
            out.flush();  
            out.close();  
            // 读取响应  
            int length = (int) connection.getContentLength();// 获取长度  
            InputStream is = connection.getInputStream();  
            if (length != -1) {  
                byte[] data = new byte[length];  
                byte[] temp = new byte[512];  
                int readLen = 0;  
                int destPos = 0;  
                while ((readLen = is.read(temp)) > 0) {  
                    System.arraycopy(temp, 0, data, destPos, readLen);  
                    destPos += readLen;  
                }  
                String result = new String(data, "UTF-8"); // utf-8编码  
                try {
                	JSONObject response = new JSONObject(result);
                	return String.valueOf(response.get("response"));
				} catch (Exception e) {
				}
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  finally {
        	try {
        		if(connection != null) {
    				connection.disconnect();
    			}
			} catch (Exception e2) {
			}
		}
        return null; // 自定义错误信息  
    }  
    
    public static class LUAScript
   	{
   		public String			name;
   		public List< String >	args;
   	}
    
    public static void testPutMatch() {
    	LUAScript ss = new LUAScript();
		ss.name = "get_online_rank_by_userid";
		List<String> ll = new ArrayList<String>();
		ll.add("{\"Name\":\"gold\",\"Game\":\"c5\"}");
		List<String> us = new ArrayList<>();
		us.add("c5rj");
		ll.add(new Gson().toJson(us));
		ss.args = ll;
		String pp = new Gson().toJson(ss);
		System.out.println(pp);
		
		String s = sendJsonPost("http://218.206.94.202:7383/redis/scripts", pp);
		System.out.println(s);
    }
    
    private class Item{
    	int id;
    	int count;
    	public Item(int id, int num){
    		this.id = id;
    		this.count = num;
    	}
    }
    /**
     * 解析utf-8编码后的 字符串
     * @param resource
     * @return
     */
    public static String decodeUTF8(String resource){
    	String result = null;
    	try {
    		result = URLDecoder.decode(resource, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 编码utf-8
     * @param resource
     * @return
     */
    public static String encodeUTF8(String resource){
    	String result = null;
    	try {
    		result = URLEncoder.encode(resource, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    public static String sendNewJsonPost(String strURL, String params) { 
    	return sendNewJsonPost(strURL, params, 1);
//    	return sendOkPost(strURL, params, 1);
    }
    
    public static String sendNewJsonPost(String strURL, String params,int num) { 
    	try {
    		String result2 = HttpRequest.post(strURL)
    				.contentType("application/json")
    				.header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
    				.header("Content-Type", "application/json")
    				.header("Accept", "application/json")
    				.setMaxRedirectCount(num)
    				.timeout(3000)
    				.body(params)
    				.execute().body();
    		JSONObject response = new JSONObject(result2);
    		return String.valueOf(response.get("response"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static String sendOkPost(String strURL, String params) { 
    	try {
    		String result2 = HttpRequestMap.create(strURL)
	    		.header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.body(params).sendPost();
    		JSONObject response = new JSONObject(result2);
    		return String.valueOf(response.get("response"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
	public static void main(String[] args) {
//		String json = urlGet("http://115.159.116.81:7379/");
//		System.out.println(json);
//		sendJsonPost("http://115.159.116.81:7379/redis/commands",
//				"{\"args\":[\"metadata:leaderboards\"],\"name\":\"HGETALL\"}");
//		LUAScript ss = new LUAScript();
//		ss.name = "put_scores";
//		List<String> ll = new ArrayList<String>();
//		ll.add("{\"Name\":\"global\",\"UserID\":\"tas\",\"Dimensions\":{\"country\":\"zc\"},\"Scores\":{\"score\":12.0}}");
//		ss.args = ll;
//		String pp = new Gson().toJson(ss);
//		System.out.println(pp);
//
//		sendJsonPost("http://119.29.89.218:7379/redis/scripts", pp);
//		sendJsonPost("http://127.0.0.1:7379/redis/scripts", pp);
		
//		sendPost("http://127.0.0.1:8080/mod-unkilled/task/createVouchers", "username=admin&password=admin");
//		testPutMatch();
		
//		long now = System.currentTimeMillis();
//		String re = urlGet("http://218.206.94.202:89/center/serverc5.jsp");
//		System.out.println((System.currentTimeMillis()-now)+":"+re);
//		sendJsonPost("http://115.159.58.173:86/FishServer/json_test.jsp", "{'a':b}");
		String url = "https://user.anzhi.com/web/cp/checkToken";
		Properties pro = new Properties();
		pro.put("time", "20170627170144");
		pro.put("appkey", "1497335807rMChND8QrYcfTuB7G159");
		pro.put("cptoken", "8141498554106478UT9UH815098");
		pro.put("sign", "8b208d3543f89967f212e46d9b45f5c1");
		pro.put("deviceId", "868403026133063");
		sendPost(url, pro);
//		sendJsonPost("http://115.159.58.173:86/FishServer/json_test.jsp", "{'a':b}");
//		urlGet("https://user.anzhi.com/web/cp/checkToken?time=20170627170144&appkey=1497335807rMChND8QrYcfTuB7G159&cptoken=8141498554106478UT9UH815098&sign=8b208d3543f89967f212e46d9b45f5c1&deviceId=868403026133063");
		sendPost("https://user.anzhi.com/web/cp/checkToken", "time20170627170144&appkey=1497335807rMChND8QrYcfTuB7G159&cptoken=8141498554106478UT9UH815098&sign=8b208d3543f89967f212e46d9b45f5c1&deviceId=868403026133063");
	}
}
