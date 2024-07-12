package com.hm.enums;

/**
 * 邮件类型
 * @author yl
 * @version 2013-3-2
 *
 */
public enum AttachmentItemType {

	BAG(1,"背包道具"),
	PARTS(2,"配件仓库道具"),
	WARUNIT(3,"战斗单位"),
	PIERCE(4,"魂魄"),
	COMMANDER1(5,"1星将领"),
	COMMANDER2(6,"2星将领"),
	COMMANDER3(7,"3星将领"),
	COMMANDER4(8,"4星将领");
	
	/**
	 * @param type
	 * @param desc
	 */
	private AttachmentItemType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
