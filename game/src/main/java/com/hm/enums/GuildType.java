package com.hm.enums;

/**
 * 
 * @Description: 阵营类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum GuildType {
	PJ(-1,"叛军", "fff82c",""),
	QX(0,"中立", "fff82c","camp_name_0"),
	;
	
	/**
	 * @param type
	 * @param desc
	 * color ,对应的颜色，用于广播中，展示颜色
	 */
	private GuildType(int type, String desc, String color, String confName) {
		this.type = type;
		this.desc = desc;
		this.color = color;
		this.confName = confName;
	}

	private int type;
	
	private String desc;
	private String color;
	private String confName;

	public int getType() {
		return type;
	}


}
