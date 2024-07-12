package com.hm.enums;

/**
 * 
 * @Description: 回复状态
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum MailState {
	NewMail(0,"不带附件新邮件"),
	NewReward(1,"带附件新邮件"),
	Read(2,"已读"),
	ReadNoGet(3,"已读未领取"),
	Get(4,"已领取"), 
	Del(5,"已删"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private MailState(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static MailState getState(int type){
		for (MailState kind : MailState.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}

	public static boolean isCanDel(int state){
		if(state == NewReward.getType() || state == ReadNoGet.getType() || state == Del.getType()){
			return false; 
		}
		return true; 
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
