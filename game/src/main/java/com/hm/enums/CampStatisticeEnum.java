/**  
 * Project Name:SLG_GameHot.
 * File Name:CampStatisticeTypeEnum.java  
 * Package Name:com.hm.enums  
 * Date:2018年4月16日下午4:47:05  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.enums;

/**  
 * ClassName: CampStatisticeEnum. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月16日 下午4:47:05 <br/>  
 *  
 * @author zxj  
 * @version   
 */
public enum CampStatisticeEnum {
	//用于标示阵营的可领取状态
	Level1Get(1, "初级阵营荣誉可领取"),
	Level2Get(2, "中级阵营荣誉可领取"),
	Level3Get(3, "高级阵营荣誉可领取"),

	;
	
	private CampStatisticeEnum(int type, String desc) {
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
	
	public static CampStatisticeEnum getCampStat(int type){
		for (CampStatisticeEnum state : CampStatisticeEnum.values()) {
			if(type == state.getType()) return state; 
		}
		return null;
	}

}
