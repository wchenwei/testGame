package com.hm.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.google.common.collect.Range;
import com.hm.libcore.util.date.DateUtil;

import java.util.List;

public class OpenTime {
	//星期几开启
	private List<Integer> weeks;
	//每月的几号开启
	private List<Integer> days;
	//每天的分钟数-开始分钟到结束分钟
	private Range<Integer> minuteRange; 
	
	public OpenTime(String config) {
		initWD(config.split("_")[0]);
		initHM(config.split("_")[1]);
	}
	
	private void initWD(String wd) {
		if(wd.startsWith("W")) {
			this.weeks = StringUtil.splitStr2IntegerList(wd.substring(1, wd.length()), ",");
		}else if(wd.startsWith("D")){
			this.days = StringUtil.splitStr2IntegerList(wd.substring(1, wd.length()), ",");
		}
	}
	private void initHM(String hm) {
		this.minuteRange = Range.closed(calDayMinute(hm.split(",")[0]), calDayMinute(hm.split(",")[1]));
	}
	private int calDayMinute(String hm) {
		int hour = Integer.parseInt(hm.split(":")[0]);
		int minute = Integer.parseInt(hm.split(":")[1]);
		return hour*60+minute;
	}
	
	public boolean isOpen() {
		if(isFitDay() || isFitWeek()) {
			//判断时分
			return minuteRange.contains(DateUtil.getDayMinute());
		}
		return false;
	}
	public boolean isOpenMinute() {
		if(isFitDay() || isFitWeek()) {
			//判断时分
			return minuteRange.lowerEndpoint() == DateUtil.getDayMinute();
		}
		return false;
	}
	
	public boolean isEndMinute() {
		if(isFitDay() || isFitWeek()) {
			//判断时分
			return minuteRange.upperEndpoint() == DateUtil.getDayMinute();
		}
		return false;
	}
	
	private boolean isFitWeek() {
		return CollUtil.isNotEmpty(weeks) && weeks.contains(DateUtil.getCsWeek());
	}
	private boolean isFitDay() {
		return CollUtil.isNotEmpty(days) && days.contains(DateUtil.thisDayOfMonth());
	}
	
	public static void main(String[] args) {
//		OpenTime openTime = new OpenTime("W2,3,5_9:0,20:0");
//		System.err.println(openTime.isOpen());
//		System.err.println(openTime.isOpenMinute());
//		System.err.println(openTime.isEndMinute());
		System.err.println(new DateTime(1540561500425L));
	}
}
