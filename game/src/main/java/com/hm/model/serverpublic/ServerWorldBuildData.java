package com.hm.model.serverpublic;

import com.hm.config.GameConstants;
import lombok.Data;

/**
 * @Description: 世界建筑
 * @author siyunlong  
 * @date 2019年10月9日 上午11:27:12 
 * @version V1.0
 */
@Data
public class ServerWorldBuildData extends ServerPublicContext{
	private int lv;//当前等级
	private long exp;//当前经验
	private boolean isRelease;//今日是否已经发布过
	private long endTime;//结束时间
	
	private int releaseLv;//发布的时等级
	private int secondAdd;//每秒加
	private int maxScore;//
	
	private int[] mainCitys = new int[3];
	
	public void addExp(long addExp) {
		this.exp += addExp;
	}
	
	public boolean isOpen() {
		return this.lv > 0;
	}
	public boolean isOver() {
		return System.currentTimeMillis() > this.endTime;
	}
	
	
	public void doReleaseTask(int secondAdd,int maxScore,int releaseMinute) {
		this.releaseLv = this.lv;
		this.isRelease = true;
		this.maxScore = maxScore;
		this.endTime = System.currentTimeMillis()+releaseMinute*GameConstants.MINUTE;
		this.secondAdd = secondAdd;
		save();
	}
	
	public void resetData() {
		this.isRelease = false;
		this.endTime = 0;
		save();
	}
	
}
