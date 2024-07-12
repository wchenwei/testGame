package com.hm.action.push.conf;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;


//推送的配置文件
@Slf4j
public class PushConfig {
	private static String path = "push.properties";
    private static String certificatePassword="certificatePassword";
    private static String certificateFile="certificateFile";
    private static String iosPushEv="iosPushEv";
    private static Properties proFile = new Properties();
    
    public static String getCertificatePassword() {
		return getValue(certificatePassword);
	}
	public static String getCertificateFile() {
		return getValue(certificateFile);
	}
	public static boolean isIosPushEv() {
		return getBooleanValue(iosPushEv);
	}
	private static String getValue(String key) {
		if(proFile.size()<=0) {
			initProperties();
		}
        return proFile.getProperty(key);
    }
    public static int getIntValue(String key) {
		return Integer.parseInt(getValue(key));
	}
    public static boolean getBooleanValue(String key) {
		return Boolean.parseBoolean(getValue(key));
	}
    
    public static void initProperties() {
    	try {
    		FileInputStream fis = new FileInputStream(PushConfig.class.getResource("/").toURI().getPath()+path);
    		proFile.load(fis);
    		fis.close();
		} catch (Exception e) {
			log.error("解析"+path+"出错", e);
		}
    }
}
