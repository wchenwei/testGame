
package com.hm.action.build.vo;
/**
 * Title: CancelBuildLvupVO.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年5月22日 上午10:49:53
 * @version 1.0
 */
public class CancelBuildLvupVO {
	
	private int type;//建筑物类型
	
	private int templateId;//如果是多种的
	
	private int blockId;//郊外建筑的地块ID
	
	private int lv;//等级

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

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}
	
	
	
}

