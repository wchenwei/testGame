package com.hm.enums;

/**
 * 下线类型
 * @author yl
 * @version 2013-3-2
 *
 */
public enum LeaveOnlineType {

	SERVER(1,"服务器强制下线"),
	RELOGIN(2,"账号别处登录"), 
	KICK(3,"账号强制下线"),
	CAMPCONVERT(4, "阵营转换后下线重登"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private LeaveOnlineType(int type, String desc) {
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
