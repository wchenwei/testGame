/**
 * 
 */
package com.hm.util;

import java.util.UUID;

/**
 * Title: Uuid8.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年9月8日 下午12:05:13
 * @version 1.0
 */
public class Uuid8 {
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
		"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
		"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
		"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
		"W", "X", "Y", "Z" };

public static String create() {
	StringBuffer shortBuffer = new StringBuffer();
	String uuid = UUID.randomUUID().toString().replace("-", "");
	for (int i = 0; i < 8; i++) {
		String str = uuid.substring(i * 4, i * 4 + 4);
		int x = Integer.parseInt(str, 16);
		shortBuffer.append(chars[x % 0x3E]);
	}
	return shortBuffer.toString();

}
}
