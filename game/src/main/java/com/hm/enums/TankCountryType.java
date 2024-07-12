package com.hm.enums;


/**
 * 
 * @Description: 国家
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum TankCountryType {
	USA(1,"美"),
	RUS(2,"苏"),
	GER(3,"德"),
	GBR(4,"英"),
	CHN(5,"中"),
	;
	
	private TankCountryType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	public static TankCountryType getType(int type) {
		for (TankCountryType buildType : TankCountryType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
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
