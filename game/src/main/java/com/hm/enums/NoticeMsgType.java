package com.hm.enums;



/**
 * 广播发送类型类型
 */
public enum NoticeMsgType {
	/*0	没走马灯
	1	普通走马灯
	2	公告走马灯*/
	None(0,"没走马灯"),
	Sys(1,"普通走马灯"),
	Notice(3,"公告走马灯"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private NoticeMsgType(int type, String desc) {
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
	
	public static NoticeMsgType getType(int type){
		for (NoticeMsgType kind : NoticeMsgType.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}
	
	
	
}
