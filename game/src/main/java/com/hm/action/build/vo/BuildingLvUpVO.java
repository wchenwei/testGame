
package com.hm.action.build.vo;
/**
 * Title: BuildingLvUpVO.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年5月20日 下午1:32:14
 * @version 1.0
 */
public class BuildingLvUpVO {

	private int type;//建筑物类型
	
	private int time;//升级时间
	
	private int allTime;//总时间
	
	private int templateId;//如果是多种的
	
	private int blockId;//郊外建筑的地块ID

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	
}

