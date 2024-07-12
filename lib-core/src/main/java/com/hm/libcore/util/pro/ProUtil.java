package com.hm.libcore.util.pro;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
/**
 * Title: ProUtil.java
 * Description:properties文件操作类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
@Slf4j
public class ProUtil {

	private Properties pro;
	
	public ProUtil(String path){
		pro = new Properties();
		try {
			InputStream stream = this.getClass().getResourceAsStream(path);
			if(stream == null) {
				if(path.startsWith("/")) {
					path = path.substring(1, path.length());
					try {
						stream = new FileInputStream(path);
					} catch (Exception e) {
						log.error(path+"文件不存在");
					}
				}
			}
			pro.load(stream);
			//pro.load(new FileInputStream(path));
		} catch (IOException e) {
			log.error("解析"+path+"出错", e);
		}
	}
	
	public String getValue(String key){
		return pro.getProperty(key);
	}
	
	public int getIntValue(String key) {
		return Integer.parseInt(getValue(key));
	}
	
	@SuppressWarnings("unchecked")
	public Enumeration getPropertyNames(){
		return pro.propertyNames();
	}
}
