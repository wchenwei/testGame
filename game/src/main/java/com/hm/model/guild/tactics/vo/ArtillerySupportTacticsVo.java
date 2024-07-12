package com.hm.model.guild.tactics.vo;

import com.hm.model.guild.tactics.ArtillerySupportTactics;

public class ArtillerySupportTacticsVo extends BaseGuildTacticsVo{
	private int skillId;
	
	public ArtillerySupportTacticsVo(ArtillerySupportTactics tactics) {
		super(tactics);
		this.skillId = tactics.getSkillId();
	}
	
}
