package com.hm.libcore.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * Title: JSONUtil.java
 * Description:JSON字符串操作类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public class JSONUtil {
	private JSONUtil(){}  
	
	 /** 
    * 对象转换成json字符串 
    * @param obj  
    * @return  
    */  
   public static String toJson(Object obj) {  
       return JSON.toJSONString(obj);  
   }  
   
   
   /** 
    * json字符串转成对象 
    * @param str   
    * @param type  
    * @return  
    */  
   public static <T> T fromJson(String str, Class<T> cls) {  
       return JSON.parseObject(str, cls);  
   } 
   
   
   public static <T> T fromJson(String str,TypeReference<T> type){
	   return JSON.parseObject(str,type);
   }
}
