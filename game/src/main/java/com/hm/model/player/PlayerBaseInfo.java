/**  
 * Project Name:SLG_RebornGame.  
 * File Name:PlayerBaseInfo.java  
 * Package Name:com.hm.model.player  
 * Date:2017年12月11日上午9:11:15  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/
package com.hm.model.player;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import lombok.Getter;

import java.util.Date;

@Getter
public class PlayerBaseInfo extends PlayerDataContext {
	private transient String imei;
	// 注册时间
	private Date createDate;

	// 上一次登录时间
	private transient Date lastLoginDate;
	// 上一次离线时间
	private transient Date lastOffLineDate;
	//上次登录使用手机型号
	private transient String lastPohone;
	//服务器每日标识,用于每日玩家数据重置
	private transient String serverDayMark;
	//绑定的手机号码
	private String bindPhone;
	private boolean bindRewardFlag;//绑定领取标识
	private String bindRebendflag = "";//下次重置的时间，格式为年月日 20211221

	private int version;//当前版本号

	@Override
	public void initData() {
		super.initData();
		if(this.serverDayMark == null && this.lastLoginDate != null) {
			this.serverDayMark = TimeUtils.formatSimpeTime2(this.lastLoginDate);
			SetChanged();
		}
	}
	public void setImei(String imei) {
		this.imei=imei;
		SetChanged();
	}
	public String getImei() {
		return imei;
	}
	public Date getCreateDate() {
		if(createDate == null) {
			setCreateDate(new Date());
		}
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate=createDate;
		SetChanged();
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate=lastLoginDate;
		SetChanged();
	}
	public void setLastOffLineDate(Date lastOffLineDate) {
		this.lastOffLineDate=lastOffLineDate;
		SetChanged();
	}
	
	public void setServerDayMark(String serverDayMark) {
		this.serverDayMark = serverDayMark;
		SetChanged();
	}
	public void receiveBindReward(){
		this.bindRewardFlag = true;
		SetChanged();
	}
	public void checkAndBindPhone(){
		if(StrUtil.isEmpty(bindRebendflag)
				|| DateUtil.compare(new Date(),
				DateUtil.parse(bindRebendflag.length()==6?bindRebendflag+"01":bindRebendflag, "yyyyMMdd"))>=0) {
			this.bindRewardFlag = false;
			this.bindPhone="";
			this.bindRebendflag=DateUtil.toStr(DateUtil.getNextDays(new Date(),30), "yyyyMMdd");
			SetChanged();
		}
	}
	//创建账号的第几天
	public int getDay(){
		return (int) DateUtil.betweenDay(createDate, new Date(), true)+1;
	}


	public void bindPhone(String phone) {
		this.bindPhone = phone;
		SetChanged();
	}

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerBaseInfo",this);
	}
}
