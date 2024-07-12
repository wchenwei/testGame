package com.hm.model.guild.tactics.vo;

import com.hm.model.guild.tactics.AbstractCityTactics;

public class BaseGuildTacticsVo {
	private int type;//类型
	private long endTime;//截止时间
	private int cityId;//作用城池id
	
	public BaseGuildTacticsVo(AbstractCityTactics tactics) {
		this.type = tactics.getType().getType();
		this.endTime = tactics.getEndTime();
		this.cityId = tactics.getCityId();
	}
	
	
}
