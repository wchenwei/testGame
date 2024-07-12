
package com.hm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Title: SLGDateUntil.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年8月4日 下午2:09:33
 * @version 1.0
 */
public class SLGDateUntil {

	
	public static boolean isSATURDAYorSUNDAY(){
		boolean flag = false;
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
	    	flag = true;
	    }
	    return flag;
	}
	
	public static boolean isSATURDAY(){
		boolean flag = false;
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
	    	flag = true;
	    }
	    return flag;
	}
	
	public static boolean isSUNDAY(){
		boolean flag = false;
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
	    	flag = true;
	    }
	    return flag;
	}
	
	
	public static boolean isTUESDAYorFRIDAY(){
		boolean flag = false;
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
	    	flag = true;
	    }
	    return flag;
	}
	
	
	public static boolean isMONDAYorTHUSDAY(){
		boolean flag = false;
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){
	    	flag = true;
	    }
	    return flag;
	}
	
	
	private static final DateFormat YYMMDDHHMMSS = new SimpleDateFormat("MM/dd HH:mm");
	
	public static synchronized String toStr(long time){
		return YYMMDDHHMMSS.format(new Date(time));
	}
	
	private static DateFormat HH = new SimpleDateFormat("HH");
	
	public static String getHour(long time){
		return HH.format(new Date(time));
	}
	
	
	public static boolean inTime(long time,int star,int end){
		String hour = HH.format(new Date(time));
		int nowHour = Integer.parseInt(hour);
		boolean flag = false;
		if(nowHour>=star&&nowHour<end){
			flag = true;
		}
		return flag;
	}
	
	public static boolean inTime2(long time,int star,int end){
		String hour = HH.format(new Date(time));
		int nowHour = Integer.parseInt(hour);
		boolean flag = false;
		if(nowHour>star&&nowHour<end){
			flag = true;
		}
		return flag;
	}
	/**
	 * 
	 * getNowTime:(返回当前时间). <br/>  
	 * TODO.<br/>  
	 *  
	 * @author jiayp  
	 * @return  返回当前时间
	 *
	 */
	public static String getNowTime(){
		return YYMMDDHHMMSS.format(new Date());
	}
}

