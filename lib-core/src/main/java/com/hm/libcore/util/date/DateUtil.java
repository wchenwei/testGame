package com.hm.libcore.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.hm.libcore.util.TimeUtils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.StrUtil;


/**
 * Title: DateUtil.java
 * Description:日期转换工具类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public class DateUtil extends cn.hutool.core.date.DateUtil{
	
	/**
	 * 默认的格式
	 */
	private static DateFormat YYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat YYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat HH = new SimpleDateFormat("HH");
	private static DateFormat MMDDHHMM = new SimpleDateFormat("MM-dd HH:mm");
	private static DateFormat MMDDHHMM2 = new SimpleDateFormat("yyMMddHHmmss");
	private static DateFormat YYMMDD2 = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 日期转默认格式字符串
	 * @param d
	 * @return
	 */
	public static String toStr(Date d){
		return YYMMDDHHMMSS.format(d);
	}
	public static String toStr2(Date d){
		return YYMMDD.format(d);
	}
	public static String toStr3(Date d){
		return HH.format(d);
	}

	/**
	 * 日期转指定格式字符串
	 * @param d
	 * @param pattern
	 * @return
	 */
	public static String toStr(Date d,String pattern){
		return new SimpleDateFormat(pattern).format(d);
	}
	
	/**
	 * 日期转默认格式字符串 
	 * @param time
	 * @return
	 */
	public static String toStr(long time){
		return YYMMDDHHMMSS.format(new Date(time));
	}
	
	/**
	 * 日期转默认格式字符串 
	 * @param time
	 * @return
	 */
	public static String toStr2(long time){
		return YYMMDD.format(new Date(time));
	}
	/**
	 * 日期转默认格式字符串(活动日期)
	 * @param time
	 * @return
	 */
	public static String toStr4activity(long time){
		return MMDDHHMM.format(new Date(time));
	}
	
	/**
	 * 日期转指定格式字符串
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String toStr(long time,String pattern){
		return new SimpleDateFormat(pattern).format(new Date(time));
	}
	/**
	 * 字符串转默认格式日期
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(String str) throws ParseException{
		return YYMMDDHHMMSS.parse(str); 
	}
	/**
	 * 字符串转默认格式日期
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate2(String str) throws ParseException{
		return YYMMDD.parse(str); 
	}
	/**
	 * 字符串转指定格式日期
	 * @param str
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(String str,String pattern) throws ParseException{
		return new SimpleDateFormat(pattern).parse(str); 
	}
	
	
	/**
	 * 获取下一天的时间
	 *
	 * @param nowDate 当前时间
	 * @return
	 */
	public static Date getNextDay(Date nowDate){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	
	/**
	 * 获取下一天的时间
	 *
	 * @param nowDate 当前时间
	 * @return
	 */
	public static long getNextDay(Date nowDate,int hour){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		c.set(Calendar.HOUR_OF_DAY,hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime().getTime();
	}
	
	
	
	/**
	 * 获取下一天的时间
	 *
	 * @param nowDate 当前时间
	 * @return
	 */
	public static long getTime(Date nowDate,int hour,int minute,int secound){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY,hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND,secound);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime().getTime();
	}
	
	/**
	 * 获取今天内指定时间
	 * @param hour
	 * @param minute
	 * @param secound
	 * @return
	 */
	public static long getAppointTime(int hour,int minute,int secound){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY,hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND,secound);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime().getTime();
	}
	
	/**
	 * 获取下一天的时间
	 *
	 * @param nowDate 当前时间
	 * @return
	 */
	public static Date getNextDayAndHMS(Date nowDate,int hour,int min,int sec){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1);
		c.set(Calendar.HOUR_OF_DAY,hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	
	/**
	 * 获取当天的时间
	 *
	 * @param nowDate 当前时间
	 * @return
	 */
	public static Date getNowDay(Date nowDate){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	/**
	 * 前一天的0点时间
	 * @param nowDate
	 * @return
	 */
	public static Date getProDay(Date nowDate){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 获得指定前几天时间
	 * @param nowDate
	 * @param day
	 * @return
	 */
	public static long getProDays(Date nowDate,int day){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-day);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime().getTime();
	}
	
	
	/**
	 * 获得指定前几天时间
	 * @param nowDate
	 * @param day
	 * @return
	 */
	public static long getNextDays(Date nowDate,int day){
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+day);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime().getTime();
	}
	
	/**
	 * 获取当天的时间
	 *
	 * @param nowDate 当前时间
	 * @return
	 */
	public static long getNowDay(long time){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime().getTime();
	}
	
	/**
	 * 当前时间是否超过比较时间
	 * @param checkDate
	 * @return
	 */
	public static boolean after(Date checkDate){
		return new Date().after(checkDate);
	}
	
	/**
	 * 当前时间是否小于比较时间
	 *
	 * @param checkDate
	 * @return
	 */
	public static boolean befor(Date checkDate){
		return new Date().before(checkDate);
	}
	
	/**
	 * 是否在当天
	 *
	 * @param nowDate
	 * @return
	 */
	public static boolean isInNowDay(Date checkDate){
		Date nowDate = new Date();
		long state = getNowDay(nowDate).getTime();
		long end = getNextDay(nowDate).getTime();
		long now = checkDate.getTime();
		return (now >= state && now < end);
	}
	
	/**
	 * 是否在当天
	 *
	 * @param nowDate
	 * @return
	 */
	public static boolean isInNowDay(long time){
		Date nowDate = new Date();
		long start = getNowDay(nowDate).getTime();
		long end = getNextDay(nowDate).getTime();
		return (time >= start && time < end);
	}
	
	 
	/**
	 * 将数据库内的timestamp字段对应的字符串去掉最后的.0
	 * @param timestamp
	 * @return
	 */
	public static String cutDbTimestamp(String timestamp){
		if(timestamp.indexOf(".0") != -1){
			return timestamp.substring(0, timestamp.length() - 2);
		}else{
			return timestamp;
		}
	}
	/**
	 * 当前时间到凌晨12点时间相差的毫秒数
	 * @param nowDate
	 * @return
	 */
	public static long getNowDayTime2NextDay(Date nowDate){
		long nowDateTime = nowDate.getTime();//现在的时间
		long nextDateTime = getNextDay(nowDate).getTime();
		long time = nextDateTime-nowDateTime;
		return time;
	}
	
	
	public static int getPhpTimestamp(){
		return (int)(System.currentTimeMillis()/1000);
	}
	
	public static int getPhpTimestamp(long time){
		return (int)(time/1000);
	}
	
	
	 public static int getWeekNumber(Date date){ 
	       Calendar calendar = Calendar.getInstance();
	        calendar.setFirstDayOfWeek(Calendar.MONDAY);

	        calendar.setTime(date);
	        return calendar.get(Calendar.WEEK_OF_YEAR);
	   
	 }
	 
	 /** 
	  * 取得指定日期所在周的第一天 

	  */ 
	  public static Date getFirstDayOfWeek(Date date) { 
	  Calendar c = new GregorianCalendar(); 
	  c.setFirstDayOfWeek(Calendar.MONDAY); 
	  c.setTime(date); 
	  c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday 
	  return c.getTime (); 
	  }

	  /** 
	  * 取得指定日期所在周的最后一天 
	   */ 
	  public static Date getLastDayOfWeek(Date date) { 
		  Calendar c = new GregorianCalendar(); 
		  c.setFirstDayOfWeek(Calendar.MONDAY); 
		  c.setTime(date); 
		  c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday 
		  return c.getTime(); 
	  }
	  
	  
	  public static boolean isInNowWeek(long time){
		  return getWeekNumber(new Date()) == getWeekNumber(new Date(time));
	  }
	
	
	/**
	 * 当前时间间隔状态
	 * @param time 
	 * @return 1连续间隔1天 2同一天 3间隔超过一天
	 */
	public static int isGoonDay(long time){

		int flag = 0;
		//下一天的日期  当前登录的日期  
		Date d1 =getNextDay(new Date(time));//下一天0
		
		long nowTime = System.currentTimeMillis();
		
		Date d2 = getNextDay(d1);//下一天的下一天0
		
		if(nowTime>=d1.getTime()&&nowTime<d2.getTime()){
			flag = 1;//连续间隔1天
		}
		if(nowTime<d1.getTime()){
			flag = 2;//同一天
		}
		if(nowTime>=d2.getTime()){
			flag = 3;//间隔超过一天
		}
		return flag;
	}
	/**
	 * 获取升级时间
	 * @param endTime
	 * @return
	 */
	public static int getRemainTime(long endTime){
		int remaining = (int)Math.ceil((endTime - System.currentTimeMillis())/1000d);
		return remaining <= 0 ? 0 : remaining;
	}
	
	public static int getWeek(Date date){  
//        Calendar cal = Calendar.getInstance();  
//        cal.setTime(date);  
//        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;  
//        if(week_index<0){  
//            week_index = 0;  
//        }   
//        return week_index;  
		int week = dayOfWeek(date);
		if(week == 1) {
			return 7;
		}
		return week-1;
    }  
	
	public static boolean isSameDay(Date date1,Date date2) {
		if(date1 == null || date2 == null) {
			return false;
		}
		if(toStr2(date1).equals(toStr2(date2))) {
			return true;
		}
		return false;
	}
	/**
	 * hiChina:计算日期的相差分钟数
	 * @param d1 
	 * @param d2
	 * @return
	 */
	public static long getDifferMinutes(Date d1,Date d2) {
		if(d1 == null || d2 == null) {
			return Integer.MAX_VALUE;
		}
		long d = (d2.getTime() - d1.getTime())/(60*1000);
		return d;
	}
	
	//获取剩余天数
	public static long betweenDay(long endDate) {
		return between(new Date(), new Date(endDate), DateUnit.DAY);
	}
	
	//获取今日n点的毫秒值
	public static long getTodayHour(int hour) {
		return offsetHour(beginOfDay(new Date()), hour).getTime();
	}
	
	//获取中国的星期几
	public static int getCsWeek() {
		int week = thisDayOfWeek();
		if(week == 1) {
			return 7;
		}
		return week-1;
	}
	public static int getCsWeek(Date date) {
		int week = dayOfWeek(date);
		if(week == 1) {
			return 7;
		}
		return week-1;
	}
	
	//获取1970年1月1日到现在的小时数
	public static int getCurHour(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (new Date(cal.getTimeInMillis()).getTime()/(3600*1000));
	}
	//获取当前时间的小时
	public static int getHour(){
		Calendar cal = Calendar.getInstance();
        /*int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
*/      int hour=cal.get(Calendar.HOUR_OF_DAY);//小时
        return hour;
	}
	public static int getDayMinute() {
		return DateUtil.thisHour(true)*60+DateUtil.thisMinute();
	}
	
	//转为时间参数
	public static double parseTimeDouble(long key) {
		long v = 990128115900L - Long.parseLong(MMDDHHMM2.format(new Date()));
		return Double.parseDouble(key+"."+v);
	}
	public static double parseTimeDouble(int key) {
		long v = 990128115900L - Long.parseLong(MMDDHHMM2.format(new Date()));
		return Double.parseDouble(key+"."+v);
	}
	//获取下一个整小时数（yyyy-MM-dd HH:00:00）
	public static Date getNextHour(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY)+1);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	//获取当前整小时数（yyyy-MM-dd HH:00:00）
	public static Date getCurrHour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static boolean isSameWeek(String serverDayMark,String playerDayMark) {
		try {
			if(StrUtil.isEmpty(serverDayMark) || StrUtil.isEmpty(playerDayMark)) {
				return false;
			}
			Date sdate = toDate(serverDayMark, "yyyyMMdd");
			Date pdate = toDate(playerDayMark, "yyyyMMdd");
			return TimeUtils.isSameWeek(sdate, pdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public static boolean isSameMonth(String serverDayMark,String playerDayMark) {
		try {
			if(StrUtil.isEmpty(serverDayMark) || StrUtil.isEmpty(playerDayMark)) {
				return false;
			}
			Date sdate = toDate(serverDayMark, "yyyyMMdd");
			Date pdate = toDate(playerDayMark, "yyyyMMdd");
			return isSameMonth(sdate, pdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public static boolean isSameMonth(Date d1, Date d2) {
		if(d1 == null || d2 == null) {
			return false;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		if(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
			return false;
		}
		return c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
	}

	/**
	 * 当前0秒开始后的 多少分钟的时间
	 * @param minute
	 * @return
	 */
	public static long nowAfterMinuteByZeroSecond(int minute) {
		return DateUtil.offsetSecond(new Date(), -DateUtil.thisSecond()).getTime() + minute*60000;
	}
	public static long beginWeekTime() {
		return beginOfWeek(new Date()).getTime();
	}
}










