package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;

/**
 * @Description: 玩家阵营信息
 * @author siyunlong  
 * @date 2019年4月29日 下午6:25:02 
 * @version V1.0
 */
public class PlayerPresidentData extends PlayerDataContext {
	private long exp;//今日获取经验数
	private transient int mark;//今日标识

	public long getExp() {
		return exp;
	}

	public void addExp(long add) {
		this.exp += add;
		SetChanged();
	}
	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}
	public void resetDay(int mark){
		this.exp = 0;
		this.mark = mark;
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerPresidentData", this);
	}
}
