package com.hm.model.worldtroop;

import com.hm.config.GameConstants;
import com.hm.model.player.KFPServerUrl;
import lombok.Data;

@Data
public class TroopTemp {
	private int killNum;//本部队的杀敌数
	private long checkAutoPvpTime;//
	private long secondAdd;//每秒增加
	
	private long advanceSkillTime;//突进技能生效时间

	private boolean openAutoPvp;

	//跨服服务器id
	private KFPServerUrl kfpServerUrl;

	
	public void addKillNum() {
		this.killNum ++;
	}
	public int getKillNum() {
		return killNum;
	}
	
	public boolean checkAutoPvpTime() {
		if(openAutoPvp && System.currentTimeMillis() > checkAutoPvpTime+3*GameConstants.SECOND) {
			this.checkAutoPvpTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	public void doDeath() {
		this.killNum = 0;
		this.secondAdd = 0;
		this.checkAutoPvpTime = 0;
	}
}
