package com.hm.model.serverpublic;

import cn.hutool.core.date.DateUtil;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 服务器开服数据
 * @author siyunlong  
 * @date 2018年2月1日 下午5:57:58 
 * @version V1.0
 */
@Setter
public class ServerOpenData extends ServerPublicContext{
	private Date firstOpenDate;//第一次开服时间
	private Date lastOpenDate;//上次开服时间

	private long openNum;//启动次数
	
	public void initOpenDate() {
		this.lastOpenDate = new Date();
		if(firstOpenDate == null) {
			this.firstOpenDate = this.lastOpenDate;
		}
		this.openNum ++;
		SetChanged();
	}
	
	public Date getFirstOpenDate() {
		return firstOpenDate;
	}

	public long getOpenNum() {
		return openNum;
	}
	/**
	 * 获取开服天数
	 * @return
	 */
	public int getOpenDay() {
		return (int)DateUtil.betweenDay(firstOpenDate, new Date(), true) + 1;
	}
	public Date getLastOpenDate() {
		return lastOpenDate;
	}

	public static void main(String[] args) {
		Date firstOpenDate = DateUtil.parse("2018-12-03 04:12:11");
		System.err.println(DateUtil.betweenDay(firstOpenDate, new Date(), true));
	}
}
