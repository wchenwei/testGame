package com.hm.libcore.util;


import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;  
import java.security.cert.X509Certificate;  
   
import javax.net.ssl.HostnameVerifier;  
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;  
import javax.net.ssl.SSLSession;  
import javax.net.ssl.TrustManager;  
import javax.net.ssl.X509TrustManager;  
/**
 * hc https 证书问题
 * @author Administrator
 *
 */
public class SslUtils {
	/**
	 * keytool -genkeypair -alias www.xiaoao.com  -keyalg RSA  –keysize 4096 -keypass xiaoaogame -sigalg SHA256withRSA -dname "cn=www.xiaoao.com,ou=xxx,o=xxx,l=Beijing,st=Beijing,c=CN"  -validity 3650 -keystore www.xiaoao.com_keystore.jks -storetype JKS -storepass xiaoaogame
	 * @param type
	 * @param path
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static SSLContext createSSLContext(String type ,String path ,String password) throws Exception {
	    KeyStore ks = KeyStore.getInstance(type); /// "JKS"
	    InputStream ksInputStream = new FileInputStream(path); /// 证书存放地址
	    ks.load(ksInputStream, password.toCharArray());
	 	//KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂。
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());//getDefaultAlgorithm:获取默认的 KeyManagerFactory 算法名称。
	    kmf.init(ks, password.toCharArray());
	    //SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine 的工厂。
	    SSLContext sslContext = SSLContext.getInstance("TLS");
	    sslContext.init(kmf.getKeyManagers(), null, null);
	    return sslContext;
	}
	
	private static void trustAllHttpsCertificates() throws Exception {  
	    TrustManager[] trustAllCerts = new TrustManager[1];  
	    TrustManager tm = new miTM();  
	    trustAllCerts[0] = tm;  
	    SSLContext sc = SSLContext.getInstance("SSL");  
	    sc.init(null, trustAllCerts, null);  
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
	}  
	  
	static class miTM implements TrustManager,X509TrustManager {  
	    public X509Certificate[] getAcceptedIssuers() {  
	        return null;  
	    }  
	  
	    public boolean isServerTrusted(X509Certificate[] certs) {  
	        return true;  
	    }  
	  
	    public boolean isClientTrusted(X509Certificate[] certs) {  
	        return true;  
	    }  
	  
	    public void checkServerTrusted(X509Certificate[] certs, String authType)  
	            throws CertificateException {  
	        return;  
	    }  
	  
	    public void checkClientTrusted(X509Certificate[] certs, String authType)  
	            throws CertificateException {  
	        return;  
	    }  
	}  
	   
	/** 
	 * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用 
	 * @throws Exception 
	 */  
	public static void ignoreSsl() throws Exception{  
	    HostnameVerifier hv = new HostnameVerifier() {  
	        public boolean verify(String urlHostName, SSLSession session) {  
	            System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());  
	            return true;  
	        }  
	    };  
	    trustAllHttpsCertificates();  
	    HttpsURLConnection.setDefaultHostnameVerifier(hv);  
	}  
}  