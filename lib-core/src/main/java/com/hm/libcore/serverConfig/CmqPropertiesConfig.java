package com.hm.libcore.serverConfig;

import com.hm.libcore.util.pro.ProUtil;

public class CmqPropertiesConfig {
	private static ProUtil pro = new ProUtil("/cmq.properties");
	private static String CmqSecretId ="cmqSecretId";
	private static String CmqSecretKey ="cmqSecretKey";
	private static String CmqEndpoint ="cmqEndpoint";
	private static String DefaultQueueName = "defaultQueueName";
	
	public static void reload() {
		pro = new ProUtil("/cmq.properties");
	}
	
	public static String getValue(String key) {
		return pro.getValue(key);
	}
	public static int getIntValue(String key) {
		return Integer.parseInt(getValue(key));
	}
	public static double getDoubleValue(String key) {
		return Double.parseDouble(getValue(key));
	}
	public static boolean getBooleanValue(String key) {
		return Boolean.parseBoolean(getValue(key));
	}
	
	public static String getCmqSecretId(){
		return getValue(CmqSecretId);
	}
	public static String getCmqSecretKey(){
		return getValue(CmqSecretKey);
	}
	public static String getCmqEndpoint(){
		return getValue(CmqEndpoint);
	}

	public static String getDefaultQueueName() {
		return getValue(DefaultQueueName);
	}
	
	
	
}
