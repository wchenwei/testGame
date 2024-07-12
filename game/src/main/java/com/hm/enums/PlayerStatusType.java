package com.hm.enums;

/**
 * 
 * @Description: 回复状态
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum PlayerStatusType {
	NotChat(1,"聊天禁言"),
	NotLogin(2,"封号"),
	BlackHome(3,"小黑屋"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private PlayerStatusType(int type, String desc) {
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
	public static PlayerStatusType getPlayerStatusType(int type) {
		for (PlayerStatusType playerStatusType : PlayerStatusType.values()) {
			if(playerStatusType.getType() == type) {
				return playerStatusType;
			}
		}
		return null;
	}
}
