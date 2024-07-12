package com.hm.libcore.util.string;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;


/**
 * Title: StringUtil.java
 * Description:字符串操作类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public class StringUtil {
	
	/**
	 * 再以后“,”分割的字符串后添加字符串
	 * @param str
	 * @param addStr
	 * @return
	 */
	public static String append2StringList(String str, String addStr){
		return StringUtils.isNotBlank(str) ? str + "," + addStr : addStr;
	}
	
	/**
	 * 编码
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encode(String str) throws Exception{
		return encode(str, "utf-8");
	}
	
	/**
	 * 编码URLDecoder
	 * @param str
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String encode(String str,String charset) throws Exception{
		return URLEncoder.encode(str, charset);
	}
	
	/**
	 * 解码
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decode(String str) throws Exception{
		return decode(str, "utf-8");
	}
	
	/**
	 * 解码URLDecoder
	 * @param str
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String decode(String str,String charset) throws Exception{
		return URLDecoder.decode(str, charset);
	}
	
	/**
	 * 字符串结尾处理“,”
	 * @param str
	 * @return
	 */
	public static String subEndDouhao(String str){
		return StringUtils.stripEnd(str,",");
	}
	
	/**
	 * 字符串结尾处理“;”
	 * @param str
	 * @return
	 */
	public static String subEndFenhao(String str){
		return StringUtils.stripEnd(str,";");
	}
	
	/**
	 * 字符串结尾处理“_”
	 * @param str
	 * @return
	 */
	public static String subEndXiahua(String str){
		return StringUtils.stripEnd(str,"_");
	}
	
	/**
	 * 切割以指定符号分割的字符串为List<String>集合
	 * @param str
	 * @return
	 */
	public static List<String> splitStr2StrList(String str,String pattern){
		List<String> result = new ArrayList<String>();
		if(StringUtils.isBlank(str)){
			return result;
		}
		String [] strArray = str.split(pattern);
		for (String string : strArray) {
			result.add(string);
		}
		return result;
	}
	
	public static List<Float> splitStr2FloatList(String str,String pattern){
		List<Float> result = new ArrayList<Float>();
		if(StringUtils.isBlank(str)){
			return result;
		}
		String [] strArray = str.split(pattern);
		for (String string : strArray) {
			result.add(Float.parseFloat(string));
		}
		return result;
	}
	
	public static List<Double> splitStr2DoubleList(String str,String pattern){
		List<Double> result = new ArrayList<Double>();
		if(StringUtils.isBlank(str)){
			return result;
		}
		String [] strArray = str.split(pattern);
		for (String string : strArray) {
			result.add(Double.parseDouble(string));
		}
		return result;
	}
	
	public static double[] splitStr2DoubleArray(String str,String pattern){
		String [] strArray = str.split(pattern);
		double[] result = new double[strArray.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Double.parseDouble(strArray[i]);
		}
		return result;
	}
	
	public static int[] splitStr2IntArray(String str,String pattern){
		String [] strArray = str.split(pattern);
		int[] result = new int[strArray.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(strArray[i]);
		}
		return result;
	}
	public static long[] splitStr2LongArray(String str,String pattern){
		String [] strArray = str.split(pattern);
		long[] result = new long[strArray.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Long.parseLong(strArray[i]);
		}
		return result;
	}
	
	/**
	 * 切割以指定符号分割的字符串为List<Long>集合
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static List<Long> splitStr2LongList(String str,String pattern){
		List<Long> result = new ArrayList<Long>();
		if(StringUtils.isBlank(str)){
			return result;
		}
		String [] strArray = str.split(pattern);
		for (String string : strArray) {
			result.add(Long.parseLong(string));
		}
		return result;
	}
	
	/**
	 * 切割以指定符号分割的字符串为List<Integer>集合
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static List<Integer> splitStr2IntegerList(String str,String pattern){
		List<Integer> result = new ArrayList<Integer>();
		if(StringUtils.isBlank(str)){
			return result;
		}
		String [] strArray = str.split(pattern);
		for (String string : strArray) {
			result.add(Integer.parseInt(string));
		}
		return result;
	}
	
	public static Map<Integer,Integer> splitStr2IntMap(String str,String sp1,String sp2){
		Map<Integer, Integer> tempMap = Maps.newHashMap();
		if(StringUtils.isBlank(str)){
			return tempMap;
		}
		for (String info : str.split(sp1)) {
			tempMap.put(Integer.parseInt(info.split(":")[0]), Integer.parseInt(info.split(":")[1]));
		}
		return tempMap;
	}

	public static List<String> objList2StringList(List<Object> objectList) {
		List<String> stringList = Lists.newArrayList();
		if (CollUtil.isEmpty(objectList)) {
			return stringList;
		}
		objectList.forEach(e -> stringList.add(String.valueOf(e)));
		return stringList;
	}

	public static String[] objList2StringArray(List<Object> objectList) {
		String[] strArray = new String[objectList.size()];
		for (int i = 0; i < objectList.size(); i++) {
			strArray[i] = String.valueOf(objectList.get(i));
		}
		return strArray;
	}
	
	/**
	 * 切割字符串
	 * @param str
	 * @param mark
	 * @return
	 */
	public static String getSubStringStr(String str,String mark){
		int startIndex = str.indexOf(mark);
		String returnStr = str.substring(0, startIndex);
		return returnStr;
	}
	
	/**
	 * 将url参数转换成map
	 * 
	 * @param param
	 *            aa=11&bb=22&cc=33
	 * @return
	 */
	public static Map<String, String> Url2Map(String param) {
		Map<String, String> map = new HashMap<String, String>(0);
		if (param != null && param.length() > 0) {
			String[] params = param.split("&");
			for (int i = 0; i < params.length; i++) {
				String[] p = params[i].split("=");
				if (p.length == 2) {
					map.put(p[0], p[1]);
				}
			}
		}
		return map;
	}

	/**
	 * 
	 * @param url
	 * @param sb
	 * @return
	 */
	public static Map<String, String> Url2Map(String url, StringBuilder sb) {
		Map<String, String> m = new HashMap<String, String>();
		if (url != null && url.length() > 0) {
			int pos = url.indexOf("?");
			if (pos >= 0) {
				String domain = url.substring(0, url.indexOf("?"));
				String params = url.substring(url.indexOf("?") + 1);
				String[] p2v = params.split("&");
				//m.put(path_key, domain);
				sb.append(domain);
				for (String p : p2v) {
					if (!p.equals("")) {
						String pName = "";
						String pValue = "";
						pName = p.split("=")[0];
						if (p.split("=").length == 2)
							pValue = p.split("=")[1];
						m.put(pName, pValue);
					}
				}
			} else {
				//m.put(path_key, url);
				sb.append(url);
			}
		}
		return m;
	}

	/**
	 * 将map转换成url
	 * 
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = s.substring(0, s.length() - 2);
		}
		return s;
	}
	
	public static String list2Str(List<Integer> list,String split){
		if(CollUtil.isEmpty(list)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int num:list){
			sb.append(num).append(split);
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}
	/**
	 * 去除空格、转行符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		if (isNullOrEmpty(str))
			return "";

		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}
}
