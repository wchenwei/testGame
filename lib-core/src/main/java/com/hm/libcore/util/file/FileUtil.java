package com.hm.libcore.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;

/**
 * Title: FileUtil.java
 * Description: 文件操作类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public class FileUtil {

	/**
	 * 序列化
	 * @param obj
	 * @param path
	 * @throws Exception
	 */
	public static void serializable(Object obj,String path) throws Exception{
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path));
		stream.writeObject(obj);
		stream.flush();
		stream.close();
	}
	
	/**
	 * 反序列化
	 * @param path
	 * @throws Exception
	 * @return obj
	 */
	public static Object reverseSerializable(String path,Object defaultObj) throws Exception{
		if(!new File(path).exists()){
			serializable(defaultObj, path);
		}
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
		Object obj = inputStream.readObject();
		inputStream.close();
		return obj;
	}
	
	
	/**
	 * 写入文件
	 * @param path
	 * @param content
	 * @param append
	 * @throws Exception
	 */
	public static void writeToFile(String path,String content,boolean append)throws Exception{
		Writer w = new FileWriter(path,append);
		w.write(content);
		w.flush();
		w.close();
	}

}
