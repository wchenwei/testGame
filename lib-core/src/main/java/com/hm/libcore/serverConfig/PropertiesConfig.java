package com.hm.libcore.serverConfig;

import cn.hutool.core.convert.Convert;
import com.hm.libcore.util.pro.ProUtil;
import com.hm.libcore.util.string.StringUtil;

import cn.hutool.core.util.StrUtil;

public class PropertiesConfig {
	//private static ProUtil pro = new ProUtil("/config/common/game.properties");
	private static ProUtil pro = new ProUtil("/game.properties");
	private static String Redis_Url ="redisUrl";
	private static String Game_Name ="gameName";
	private static String Gm_Url ="gmUrl";
	private static String Download_Url ="downloadUrl";
	private static String Login_Url ="loginUrl";
	private static String UseGmOrder ="useGmOrder";
	private static String ServerType ="serverType";
	private static String HttpServer_Url="httpServer_url";
	
	public static void reload() {
		pro = new ProUtil("/game.properties");
	}
	
	public static String getValue(String key) {
		return pro.getValue(key);
	}
	public static int getIntValue(String key) {
		String value = getValue(key);
		if(StrUtil.isNotEmpty(value)) {
			return Integer.parseInt(getValue(key));
		}
		return 0;
	}
	public static double getDoubleValue(String key) {
		return Double.parseDouble(getValue(key));
	}
	public static boolean getBooleanValue(String key) {
		return Convert.toBool(getValue(key), false);
	}
	
	public static String getGmUrl(){
		return getValue(Gm_Url);
	}
	public static String getRedisUrl(){
		return getValue(Redis_Url);
	}
	public static String getGameName(){
		return getValue(Game_Name);
	}
	public static String getDownloadUrl(){
		return getValue(Download_Url);
	}
	public static String getLoginUrl(){
		return getValue(Login_Url);
	}
	public static int getServerType() {
		return getIntValue(ServerType);
	}
	public static String getHttpServer(){
		return getValue(HttpServer_Url);
	}
	
	/**
	 * 是否能够使用gm命令
	 */
	public static Boolean getUseGmOrder(int serverId){
        String info = getValue(UseGmOrder);
        if (StrUtil.equals(info, "-1")) {
            return true;
        }

        return StringUtil.splitStr2IntegerList(info, ",").contains(serverId);
	}
	
	
	
}
