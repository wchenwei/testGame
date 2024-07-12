package com.hm.util;

import java.util.UUID;

public class InviteCodeGenerator {
	 public static String[] chars36 = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "", "1", "2", "3", "4", "5", "6", "7", "8", "9" };  
	    public static String[] chars62 = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };  
	  
	    public static String generateInviteCode()  
	    {  
	        StringBuffer shortBuffer = new StringBuffer();  
	        String uuid = UUID.randomUUID().toString().replace("-", "");  
	        for (int i = 0; i < 8; i++)  
	        {  
	            String str = uuid.substring(i * 4, i * 4 + 4);  
	            int x = Integer.parseInt(str, 16);  
	            // 如果是 chars62,则是x%62  
	            shortBuffer.append(chars62[x % 62]);  
	        }
	        //随机在前半部分和后半部分分别插入1个分隔符
	        shortBuffer.insert(MathUtils.random(1, 4), "-");
	        shortBuffer.insert(MathUtils.random(5, 8), "-");
	        return shortBuffer.toString();  
	    }  
	    
}
