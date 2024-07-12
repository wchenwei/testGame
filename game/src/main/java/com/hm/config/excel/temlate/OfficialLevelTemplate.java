package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("official_level")
public class OfficialLevelTemplate {
	private Integer level;
	private Integer achievement;
	private Integer achievement_totle;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getAchievement() {
		return achievement;
	}

	public void setAchievement(Integer achievement) {
		this.achievement = achievement;
	}
	public Integer getAchievement_totle() {
		return achievement_totle;
	}

	public void setAchievement_totle(Integer achievement_totle) {
		this.achievement_totle = achievement_totle;
	}
}
