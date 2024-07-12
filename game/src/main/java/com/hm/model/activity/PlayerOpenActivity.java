package com.hm.model.activity;

import cn.hutool.core.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;
import com.hm.model.player.BasePlayer;

import java.util.Date;

/**
 * @Description: 根据玩家注册时间开发活动
 * @author siyunlong  
 * @date 2018年5月31日 下午2:32:23 
 * @version V1.0
 */
public abstract class PlayerOpenActivity extends AbstractActivity{
	private int openDay;
	private int openTimeType;//0根据注册日期的零点计算时间，非0根据注册时间计算

	public PlayerOpenActivity(ActivityType type, int openTimeType, int openDay) {
		super(type);
		this.openDay = openDay;
		this.openTimeType = openTimeType;
	}
	
	@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		if(isOpen()) {
			//根据注册时间计算获得是否开放
			return System.currentTimeMillis() > getEndTime(player);
		}
		return true;
	}
	
	private long getEndTime(BasePlayer player) {
		Date createDate = player.playerBaseInfo().getCreateDate();
		if(this.openTimeType == 0) {
			return DateUtil.parseDate(DateUtil.formatDate(createDate)).getTime()+openDay*GameConstants.DAY;
		}
		return createDate.getTime() + openDay*GameConstants.DAY;
	}
}
