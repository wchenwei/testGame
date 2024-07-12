package com.hm.enums;


/**
 * 
 * ClassName: MailType. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年3月14日 下午4:59:37 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public enum MailSendType {
	All(1,"单服"),
	Group(2,"群发"),
	One(3,"单发"),
	CreateServer(6,"創建阵营"),
	PlayerIds(7,"根据玩家id"),
	PlayerGuild(8,"玩家部落"),
	RegisterDate(9, "注册时间"),

	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private MailSendType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static MailSendType getMailType(int type){
		for (MailSendType kind : MailSendType.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}

	/**
	 * 是否是全服邮件
	 * @param type
	 * @return
	 */
	public static boolean isAllMail(int type){
		return type == All.getType();
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
