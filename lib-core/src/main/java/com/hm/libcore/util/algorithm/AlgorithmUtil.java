package com.hm.libcore.util.algorithm;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * Title: AlgorithmUtil.java
 * Description:算法工具类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public class AlgorithmUtil {
	
	/**
	 * MD5算法加密
	 * @param inStr
	 * @return
	 * @throws Exception
	 */
	public static String md5(String inStr){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			char[] charArray = inStr.toCharArray();

			byte[] byteArray = new byte[charArray.length];

			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];

			byte[] md5Bytes = md5.digest(byteArray);

			StringBuffer hexValue = new StringBuffer();

			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}

			return hexValue.toString();
		} catch (Exception e) {
		}
		return inStr;
	}
	
	/**
	 * 获取UUID
	 * @return
	 * @throws Exception
	 */
	public static String getUUID() throws Exception{
		return UUID.randomUUID().toString();
	}
	
	
}
