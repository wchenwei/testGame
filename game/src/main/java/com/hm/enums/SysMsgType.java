package com.hm.enums;




/**
 * 邮件类型
 * @author yl
 * @version 2013-3-2
 *
 */
public enum SysMsgType {
	Player(1,"玩家聊天"),
	System(2,"系统通知"),
	Radio(3,"系统广播"),
	Horn(4,"用户喇叭"),
	Share(5,"分享战报"),
	PLAYERACTION(6,"玩家活动广播"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private SysMsgType(int type, String desc) {
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
	
	public static SysMsgType getType(int type){
		for (SysMsgType kind : SysMsgType.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}
	
	
}
