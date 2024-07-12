package com.hm.action.build.vo;

/**
 * Title: BuildlvUpTimeOverVO.java Description: Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * 
 * @author 李飞
 * @date 2015年5月21日 上午10:41:32
 * @version 1.0
 */
public class BuildlvUpNowVO {

	private int type;

	private int templateId;

	private int lv;

	private int blockId;

	private long gold;
	
	private int time;// 升级时间

	private int allTime;// 总时间

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}


	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getAllTime() {
		return allTime;
	}

	public void setAllTime(int allTime) {
		this.allTime = allTime;
	}
	
	

}
