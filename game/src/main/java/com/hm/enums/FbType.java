package com.hm.enums;

/**
 * @Description: 副本类型
 * @author siyunlong  
 * @date 2018年2月24日 上午10:26:11 
 * @version V1.0
 */
public enum FbType {
	Command(1,"将领副本"),
	Part(2,"配件副本"),
	;
	
	private FbType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public int getType() {
		return type;
	}
	
	public static FbType getFbType(int type) {
		for (FbType buildType : FbType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
}
