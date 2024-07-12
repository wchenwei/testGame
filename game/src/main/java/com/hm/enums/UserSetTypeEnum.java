package com.hm.enums;

/**
 * 
 * @Description: 用户设置类型
 * @author zxj  
 * @version V1.0
 */
public enum UserSetTypeEnum {
	//夜间推送选项
	GuildReq(18, "是否接受部落邀请"), //1,开启拒绝。0关闭拒绝
	NoShowVip(19, "是否不显示vip")
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private UserSetTypeEnum(int type, String desc) {
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
	
	public static UserSetTypeEnum getType(int type) {
		for (UserSetTypeEnum temp : UserSetTypeEnum.values()) {
			if(type == temp.getType()) return temp; 
		}
		return null;
	}
}
