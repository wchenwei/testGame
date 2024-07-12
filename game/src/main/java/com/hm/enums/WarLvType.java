/**  
 * Project Name:SLG_GameFuture.  
 * File Name:WarLvType.java  
 * Package Name:com.hm.enums  
 * Date:2017年12月26日下午2:04:38  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.enums;  
/**  
 * ClassName:WarLvType <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月26日 下午2:04:38 <br/>  
 * 军衔类型
 * @author   zqh  
 * @version  1.1  
 * @since    
 */
public enum WarLvType {
	Private(1,"列兵"),
	Corporal(2,"下士"),
	Sergeant(3,"中士"),
	SeniorSergeant(4,"上士"),
	SergentMaster(5,"军士长"),
	SecondLieutenant(6,"少尉"),
	Lieutenant(7,"中尉"),
	Captain(8,"上尉"),
	Major(9,"少校"),
	LieutenantColonel(10,"中校"),
	Colonel(11,"上校"),
	MajorGeneral(12,"少将"),
	LieutenantGeneral(13,"中将"),
	General(14,"上将"),
	General4th(15,"四星上将"),
	General5th(16,"五星上将"),
	Marshal(17,"元帅"),
	ChiefOfTheGeneralStaff(18,"总参谋长"),
	DefenceSecretary(19,"国防部长"),
	President(20,"总统");
	
	private WarLvType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static WarLvType getWarLvType(int type) {
		for (WarLvType buildType : WarLvType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	public static WarLvType getMaxType() {
		return WarLvType.President;
	}
}
  
