package com.hm.enums;

/**
 * 
 * @Description: 荣誉来源类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum HonorType {
	DeathTank(0,"牺牲坦克"),
	KillTank(1,"杀敌坦克"),
	OccourCity(2,"攻占城池"),
	AssistCity(3,"协助攻占"),
	Pvp1v1(4,"单挑"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private HonorType(int type, String desc) {
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
