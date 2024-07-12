package com.hm.model.guild.tactics.vo;

import com.hm.model.guild.tactics.AirdropTatics;

public class AirdropTaticsVo extends BaseGuildTacticsVo{
	private int lastTimes;
	private int maxTimes;
	
	public AirdropTaticsVo(AirdropTatics tactics) {
		super(tactics);
		this.lastTimes = tactics.getTimes();
		this.maxTimes = tactics.getMaxTimes();
	}
	
}
