
package com.hm.model.buff;

import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.enums.BuffType;

public class Buff {
	private int id;//buffId
	private long totalSecond;
	private long endTime;//结束时间
	private double value;
	
	public Buff() {
	}
	public Buff(BuffType buffType) {
		this.id = buffType.getType();
	}
	
	public long getEndTime() {
		return endTime;
	}
	
	public void setEndSecond(int addSecond) {
		this.endTime = System.currentTimeMillis()+addSecond*GameConstants.SECOND;
	}
	
	public boolean isOver() {
		return this.endTime >= 0 && System.currentTimeMillis()>this.endTime;
	}

	public void addBufferSecond(long addSecond) {
		if(this.endTime == 0) {
			this.endTime = System.currentTimeMillis()+addSecond*GameConstants.SECOND;
		}else{
			this.endTime += addSecond*GameConstants.SECOND;
		}
		this.totalSecond = DateUtil.getRemainTime(this.endTime);
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	public BuffType getType() {
		return BuffType.getType(id);
	}
}

