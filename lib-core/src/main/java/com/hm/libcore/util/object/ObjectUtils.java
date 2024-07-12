package com.hm.libcore.util.object;

import java.util.Collection;
import java.util.Map;

/**
 * Title: ObjectUtils.java
 * Description:对象操作类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public class ObjectUtils {

	 /** 
	 * 判断集合是否长度是否为0
	 *
	 * @param object 集合对象
	 * @return
	 */
	public static boolean isZeroSize(Object object){
		boolean result = false;
		if(object instanceof Collection){
			int size = ((Collection)object).size();
			if(size == 0){
				result = true;
			}
		}else if(object instanceof Map){
			int size = ((Map)object).size();
			if(size == 0){
				result = true;
			}
		}else if(object.getClass().isArray()){
			int size = ((Object[])object).length;
			if(size == 0){
				result = true;
			}
		}
		return result;
	}
}
