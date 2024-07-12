package com.hm.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends com.hm.libcore.util.string.StringUtil{
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	public static boolean equals(String str1,String str2) {
    	return StringUtils.equals(str1, str2);
    }
	
	public static Map<Integer,Integer> strToMap(String str) {
		Map<Integer,Integer> map = Maps.newConcurrentMap();
		for (String info : str.split(",")) {
			String[] ks = info.split("_");
			map.put(Integer.parseInt(ks[0]), Integer.parseInt(ks[1]));
		}
		return map;
	}
	
	public static Map<Integer,Integer> strToMap(String str, String split1, String split2) {
		Map<Integer,Integer> map = Maps.newConcurrentMap();
		if(str == null || str.isEmpty()) {
			return map;
		}
		for (String info : str.split(split1)) {
			String[] ks = info.split(split2);
			if (ks.length >= 2) {
				map.put(Integer.parseInt(ks[0]), Integer.parseInt(ks[1]));
			}
		}
		return map;
	}

	public static Map<Double,Integer> strToDoubleMap(String str, String split1, String split2) {
		Map<Double,Integer> map = Maps.newConcurrentMap();
		for (String info : str.split(split1)) {
			String[] ks = info.split(split2);
			map.put(Double.parseDouble(ks[0]), Integer.parseInt(ks[1]));
		}
		return map;
	}
	public static Map<Integer,Double> strToIntDoubleMap(String str, String split1, String split2) {
		Map<Integer,Double> map = Maps.newConcurrentMap();
		for (String info : str.split(split1)) {
			String[] ks = info.split(split2);
			map.put(Integer.parseInt(ks[0]), Double.parseDouble(ks[1]));
		}
		return map;
	}

	public static Map<Integer,Long> strToMapLong(String str) {
		Map<Integer,Long> map = Maps.newConcurrentMap();
		for (String info : str.split(",")) {
			String[] ks = info.split(":");
			map.put(Integer.parseInt(ks[0]), Long.parseLong(ks[1]));
		}
		return map;
	}
	
	public static int[] strToIntArray(String str, String split){
		try{
			if(isNullOrEmpty(str)) {
				return new int[0];
			}
			String[] strArray = str.split(split);
			int[] array = new int[strArray.length];
			for(int i=0;i<strArray.length;i++){
				array[i] = MathUtils.parseInt(strArray[i]);
			}
			return array;
		}catch(Exception e){
			System.out.println("字符串转整数数组时出错");
			int[] array = new int[1];
			return array;
		}
	}

	public static long[] strToLongArray(String str, String split){
		try{
			if(isNullOrEmpty(str)) {
				return new long[0];
			}
			String[] strArray = str.split(split);
			long[] array = new long[strArray.length];
			for(int i=0;i<strArray.length;i++){
				array[i] = Long.parseLong(strArray[i]);
			}
			return array;
		}catch(Exception e){
			System.out.println("字符串转整数数组时出错");
			long[] array = new long[1];
			return array;
		}
	}
	public static double[] strToDoubleArray(String str, String split){
		try{
			if(isNullOrEmpty(str)) {
				return new double[0];
			}
			String[] strArray = str.split(split);
			double[] array = new double[strArray.length];
			for(int i=0;i<strArray.length;i++){
				array[i] = MathUtils.parseDouble(strArray[i]);
			}
			return array;
		}catch(Exception e){
			System.out.println("字符串转double数组时出错");
			double[] array = new double[1];
			return array;
		}
	}
	
	/**
	 * 交换两个相等的字符串数组
	 *
	 * @author yanpeng 
	 * @param a
	 * @param b  
	 *
	 */
	public static void swapArray(String[] a,String[] b){
    	String temp[] = new String[a.length]; //配件数组
    	System.arraycopy(a, 0, temp, 0, a.length);
    	System.arraycopy(b, 0, a, 0, b.length);
    	System.arraycopy(temp, 0, b, 0, temp.length);
	}
	
	/**
	 * 两个字符串相加 
	 *
	 * @author yanpeng 
	 * @param attr1 1:100,2:100,3:100,21:10
	 * @param attr2 1:100
	 * @return  
	 *
	 */
	public  static String addTwoString(String attr1,String attr2){
		if(attr1.isEmpty()) return attr2; 
		String[] a1 = attr1.split(","); //1:100,2:100,3:100,21:10
		String[] a2 = attr2.split(":"); //1:100
		StringBuffer attr = new StringBuffer(); 
		for(int i=0;i<a1.length;i++){
			String[] str1 = a1[i].split(":"); 
			int type1 = Integer.valueOf(str1[0]); 
			int type2 = Integer.valueOf(a2[0]); 
			int value = Integer.valueOf(str1[1]);
			if(type1 == type2){
				value += Integer.valueOf(a2[1]); 
				a1[i] = type1+":"+value; 
				for(int j=0;j<a1.length;j++){
					if(i==j){
						attr.append(a1[i]); 
					}else{
						attr.append(a1[j]);
					}
					attr.append(",");
				}
				return attr.deleteCharAt(attr.length()-1).toString(); 
			}
			
		}
		return attr1+=","+attr2; 
	}
	/**
	 * 验证字符串是否为手机号码
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone){
		 String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		 if(phone.length() != 11){
	         return false;
	     }
         Pattern p = Pattern.compile(regex);
         Matcher m = p.matcher(phone);
         boolean isMatch = m.matches();
         if(!isMatch){
             return false;
         } 
	     return true;
	}
	
	public static boolean isSimplePhone(String phone){
		 if(StrUtil.isEmpty(phone) || phone.length() != 11){
	         return false;
	     }
		/*String regex = "^((13[0-9])|(14[0-9])|(15([0-9]))|(17[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if(!isMatch){
            return false;
        } */
		if(!phone.startsWith("1")) {
			return false;
		}
	    return true;
	}
	
	public static int strToIntId(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static void main(String[] args) {
		boolean b = isSimplePhone("15225170989");
		System.out.println(b);
	}
}
