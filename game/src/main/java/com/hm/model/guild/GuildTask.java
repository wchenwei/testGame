package com.hm.model.guild;

import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;

/**
 * 
 * @Description: 部落任务
 * @author siyunlong  
 * @date 2019年10月8日 下午5:21:12 
 * @version V1.0
 */
public class GuildTask extends GuildComponent{
	private long score;//当前积分
	
	public void addScore(long playerId,long add) {
		this.score += add;
		SetChanged();
	}
	
	public long getScore() {
		return score;
	}

	public void resetData() {
		if(DateUtil.getCsWeek() == 1) {
			this.score = 0;
			SetChanged();
		}
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("guildTask", this);
	}
}
