package com.hm.enums;
/**
 * ClassName: GuildImpeachStateEnum. <br/>  
 * Function: 用于辨识弹劾部落长的时候，标示部落成员的状态. <br/>  
 * date: 2019年3月25日 下午7:18:38 <br/>  
 * @author zxj  
 * @version
 */
public enum GuildImpeachStateEnum {
	Common(0,"没有投票的人"),
	ActMember(1,"发起弹劾的人"),
	Allow(2,"同意弹劾部落长"),
	Refuse(3,"不同意弹劾部落长"),
	
	;
	
	private GuildImpeachStateEnum(int type,String desc){
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

	public static GuildImpeachStateEnum get(int type) {
		for (GuildImpeachStateEnum temp : GuildImpeachStateEnum.values()) {
			if(type == temp.getType()) return temp; 
		}
		return Allow;
	}

	public static boolean isVoteType(int type){
		return type == Allow.getType() || type == Refuse.getType();
	}
}
