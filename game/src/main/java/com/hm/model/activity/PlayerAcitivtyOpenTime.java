package com.hm.model.activity;

import cn.hutool.core.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.model.player.BasePlayer;

import java.util.Date;

public class PlayerAcitivtyOpenTime {
	private long startTime;
	private long endTime;
	
	public PlayerAcitivtyOpenTime(){
		
	}
	
	//openTimeType 0根据startDate的零点计算时间，非0根据startDate计算
	public PlayerAcitivtyOpenTime(BasePlayer player,int openTimeType,Date startDate,int openDay) {
		this.startTime = startDate.getTime(); 
		if(openTimeType == 0) {//从0点开始计算
			this.endTime= DateUtil.beginOfDay(startDate).getTime()+openDay*GameConstants.DAY;
		}else{
			this.endTime = startTime + openDay*GameConstants.DAY;
		}
	}

	
	public boolean isOpen(){
		return System.currentTimeMillis() >=startTime && System.currentTimeMillis()<=endTime;  
	}
	
	public long getStartTime(){
		return this.startTime; 
	}

	public long getEndTime() {
		return endTime;
	}
}
