package com.hm.libcore.mongodb;


/**
 * 获取主键
 * 
 * @Author: siyunlong
 * @Version: V1.00
 * @Create Date: 2016-2-25上午11:24:37
 */
public class PrimaryKeyWeb {
	public static final String PrimaryTable = "PrimaryKey";

	/**
	 * 获取自增主键
	 * 
	 * @param gameName
	 * @return
	 */
	private static int getIncrementKey(String baseName, int serverId) {
		return getIncrementKey("key", baseName, serverId);
	}
	
	private static int getIncrementKey(String gameName, String keyName, int serverId) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		if(mongo == null) {
			return -1;
		}
		return mongo.getIncrementKey(keyName,1);
	}
	
	/**
	 * int 转换 36进制之后的值
	 * 
	 * @param gameName
	 * @return
	 */
	public static String getUserPrimaryKey(String gamePrefix, String gameName, int serverId) {
		return gamePrefix + Integer.toString(getIncrementKey(gameName, serverId), 36);
	}

	/**
	 * 获取36进制的唯一id
	 * 
	 * @param baseName
	 *            模块名
	 * @return
	 */
	public static String getPrimaryKey(String baseName, int serverId) {
		return Integer.toString(getIncrementKey(baseName, serverId), 36);
	}
	
	public static String getLoginStrIncrementKey(String baseName) {
		return Integer.toString(getLoginIncrementKey(baseName), 36);
	}
	public static int getLoginIncrementKey(String baseName) {
		return MongoUtils.getLoginMongodDB().getIncrementKey(baseName,1);
	}
	
	public static String getPrimaryKeyByDataBasename(String databaseName,String keyName) {
		MongodDB mongo = MongoUtils.getMongodDB(databaseName);
		return Integer.toString(mongo.getIncrementKey(keyName,1), 36);
	}

	/**
	 * 获取10进制的唯一id
	 * 
	 * @param baseName
	 *            模块名
	 * @return
	 */
	public static int getIntPrimaryKey(String baseName, int serverId) {
		return getIncrementKey(baseName, serverId);
	}
	
	public static int getIntPrimaryKey(Class c, int serverId) {
		return getIncrementKey(c.getSimpleName(), serverId);
	}
	
	public static void checkPrimaryKey(Class c,long value, int serverId) {
		String keyName = c.getSimpleName();
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		int curId = mongo.getIncrementKey(keyName,0);
		if(curId < value) {
			mongo.getIncrementKey(keyName, value);
		}
	}
}
