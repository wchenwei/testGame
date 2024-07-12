package com.hm.libcore.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import cn.hutool.core.date.DateUtil;

public class TimeUtils{
	
	// ************************************************************************
	// fields
	
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SIMPETIME_FORMAT = "yyyy-MM-dd";
	public static final String SIMPETIME_FORMAT2 = "yyyyMMdd";
	
	// ************************************************************************
	// public interface
	
	// ------------------------------------------------------------------------
	public static String GetCurrentTimeStamp()
	{
		// note : don't change date format - it is used for posting dates to other applications
		SimpleDateFormat sdfDate = new SimpleDateFormat( DATETIME_FORMAT );
		return sdfDate.format( new Date() );
	}
	
	// ------------------------------------------------------------------------
	// alternative to obsolete Date.toGMTString() method
	public static String ToGMTString( Date date )
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat();
		
		sdfDate.setTimeZone( new SimpleTimeZone( 0, "GMT" ) );
		sdfDate.applyPattern( "dd MMM yyyy HH:mm:ss z" );
		
		return sdfDate.format( date );
	}
	
	public static String ToString( Date date )
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( DATETIME_FORMAT );
		return sdfDate.format( date );
	}
	public static String ToString2( Date date )
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );
		return sdfDate.format( date );
	}
	public static String ToString3( Date date )
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( "MM月dd日 HH:mm" );
		return sdfDate.format( date );
	}
	public static String ToStringHour( Date date )
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( "HH:mm" );
		return sdfDate.format( date );
	}
	public static String ToDesString( Date date )
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );
		return sdfDate.format( date );
	}
	public static String ToSimpleString( Date date )
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( "MM.dd" );
		return sdfDate.format( date );
	}
	
	public static String getYesterday() {
		Date d = TimeUtils.addDay(new Date(), -1);
		return TimeUtils.formatSimpeTime2(d);
	}
	
	// ------------------------------------------------------------------------
	public static Date ParseTimeStamp( String timestamp ) throws ParseException
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( DATETIME_FORMAT );
		
		return sdfDate.parse( timestamp );
	}
	
	public static Date ParseTimeStamp( String timestamp ,String DATETIME_FORMAT) throws ParseException
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat( DATETIME_FORMAT );
		
		return sdfDate.parse( timestamp );
	}
	
	// ------------------------------------------------------------------------
	public static Date ParseTimeStampSafe( String timestamp )
	{
		try
		{
			return ParseTimeStamp( timestamp );
		}
		catch ( ParseException e )
		{
			Log.Error( "Cannot parse time from %s", timestamp );
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	/*public static long MidnightTimeMillis( int utcOffset )
	{
		return MidnightTimeMillis( 0, utcOffset );
	}*/
		
	// ------------------------------------------------------------------------
	/*public static long MidnightTimeMillis( int dayOffset, int utcOffset )
	{
		Calendar cal = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
		cal.add( Calendar.DAY_OF_YEAR, dayOffset );
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );
		cal.add( Calendar.HOUR_OF_DAY, utcOffset );
		
		return cal.getTimeInMillis();
	}*/
	
	// ------------------------------------------------------------------------
	/*public static Date MidnightTime( int utcOffset )
	{
		return new Date( MidnightTimeMillis( utcOffset ) );
	}
	
	// ------------------------------------------------------------------------
	public static Date MidnightTime( int dayOffset, int utcOffset )
	{
		return new Date( MidnightTimeMillis( dayOffset, utcOffset ) );
	}*/
	
	public static String  formatSimpeTime(Date d) {
		SimpleDateFormat sdfDate = new SimpleDateFormat( SIMPETIME_FORMAT );
		return sdfDate.format(d);
	}
	
	public static String  formatSimpeTime2(Date d) {
		SimpleDateFormat sdfDate = new SimpleDateFormat( SIMPETIME_FORMAT2 );
		return sdfDate.format(d);
	}
	
	public static Date parseSimpeTime(String d) {
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat( SIMPETIME_FORMAT );
			return sdfDate.parse(d);
		} catch (Exception e) {
		}
		return null;
	}
	
	public static boolean isSameDay(Date date1,Date date2) {
		if(date1 == null || date2 == null) {
			return false;
		}
		if(formatSimpeTime(date1).equals(formatSimpeTime(date2))) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			
			return Integer.parseInt(String.valueOf(between_days));
		} catch (Exception e) {
		}
		return 0;
	}
	/**
	 * 计算两个日期之间相差的秒数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int secondsBetween(Date smdate, Date bdate){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / 1000;
			
			return Integer.parseInt(String.valueOf(between_days));
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	} 
	
	public static Date addDay(Date date,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, day);
		return cal.getTime();
	}
	
	public static Date addMinute(Date date,int mins) {
		if(date == null) {
			date = new Date();
		}
		Date d = new Date();
		d.setTime(System.currentTimeMillis()+mins*60*1000);
		return d;
	}
	
	/**
	 * 周一 到 周日 为 1-7
	 * @return
	 */
	public static int getWeek() {
		return getWeek(new Date());
	}
	
	/**
	 * 周一 到 周日 为 1-7
	 * @return
	 */
	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	    if (w <= 0) w = 7;
	    return w;
	}
	
	public static boolean isSameWeek(Date d1, Date d2) {
		if(d1 == null || d2 == null) {
			return false;
		}
		long t1 = (d1.getTime() - Time20160104)/TimeWeek;
		long t2 = (d2.getTime() - Time20160104)/TimeWeek;
		return t1 == t2;
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
	
	public static final long TimeWeek = 604800000L;
	public static final long Time20160104 = 1451836800000L;
	public static boolean isSameWeek(String date1, String date2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		} catch (Exception e) {
			return false;
		}
		return isSameWeek(d1, d2);
	}
	
	/**
	 * 计算日期的相差小时数
	 * @param d1 
	 * @param d2
	 * @return
	 */
	public static int getDifferHour(Date d1,Date d2) {
		if(d1 == null || d2 == null) {
			return Integer.MAX_VALUE;
		}
		long d = (d2.getTime() - d1.getTime())/3600000;
		return (int)d;
	}
	
	/**
	 * hiChina:计算日期的相差分钟数
	 * @param d1 
	 * @param d2
	 * @return
	 */
	public static int getDifferMinutes(Date d1,Date d2) {
		if(d1 == null || d2 == null) {
			return Integer.MAX_VALUE;
		}
		long d = (d2.getTime() - d1.getTime())/60000;
		return (int)d;
	}
	
	/**
	 * hiChina:计算日期的相差秒数
	 * @param d1 
	 * @param d2
	 * @return
	 */
	public static long getDifferSecs(Date d1,Date d2) {
		if(d1 == null || d2 == null) {
			return Integer.MAX_VALUE;
		}
		long d = (d2.getTime() - d1.getTime())/1000;
		return d;
	}
	
	/**
     * 判断是否过期
     *
     * @param startDate 开始时间
     * @param expireDay 有效期（天数） 过期返回true，否则返回false
     */
    public static boolean isOutTime(Date startDate, int expireDay) {
        if (startDate == null) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.setTime(startDate);
        cal.add(Calendar.DAY_OF_YEAR, expireDay);
        return now.after(cal.getTime());
    }
    /**
     *判断当前时间是否在两个日期之间 
     */
    public static boolean isBetweenTime(Date startDate,Date endDate){
    	Date now = new Date();
    	return now.after(startDate)&&now.before(endDate);
    }
    /**
     *把事件戳转化为标准格式时间字符串
     */
    public static String getTimeString(long d){
    	 SimpleDateFormat format =  new SimpleDateFormat(DATETIME_FORMAT);  
         return format.format(d);  
    }
    /**
     *把事件戳转化为DATE
     */
    public static Date getTimeDate(long d){
    	SimpleDateFormat format =  new SimpleDateFormat(DATETIME_FORMAT);
        try {
			return format.parse(getTimeString(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return new Date();
    }
    //获取当天0点0分0秒的时间戳
    public static long getNowZero(){
        return getNowZero(new Date());
    }
    
  //获取当前时间戳
	public static int getNowSecond(){
		return (int)(System.currentTimeMillis()/1000);//当前时间毫秒数
	}
	
	//获取当天0点0分0秒的时间戳
    public static long getNowZero(Date date){
    	return DateUtil.beginOfDay(date).getTime();
    }
	//获取本周的周末凌晨的实际
	public static Date getWeekDate() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day+7);
        return new Date(getNowZero(cal.getTime()));
	}
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat format =  new SimpleDateFormat(DATETIME_FORMAT);
        System.out.println(ToString(new Date(parseSimpeTime("2018-07-22").getTime()))); 
	}
	
	
}
