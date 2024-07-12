
package com.hm.enums;


/**
 * Title: BuildType.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年4月29日 上午10:51:55 
 * @version 1.0
 */
public enum BuildType {
	CommandCentre(1,"总部"),
	CashWarehouse(2,"钞票仓库"),
	OilWarehouse(3,"石油仓库"),
	CrystalWarehouse(4,"水晶仓库"),
	Vault(5,"保险库"),
	Arsenal(6,"兵工厂"),
	TrainCentre(7,"训练中心"),
	Market(8,"市场"),
	AdminCentre(9,"行政中心"),
	CrystalMine(10,"晶矿"),
	;
	private BuildType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	
	private String desc;
	
	public static BuildType getBuildType(int type) {
		for (BuildType buildType : BuildType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

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
	//是否是矿场
	public static boolean isMine(int type){
		return type==CrystalMine.getType();
	}
	
	//是否可以进行生产
	public static boolean isProduct(int type){
		return type==Arsenal.getType()||type==TrainCentre.getType()||type==Market.getType()||type==AdminCentre.getType();
	}
	//是否影响资源上限(包含资源存储上限和保护上限)
	public static boolean isEffectResLimit(int type){
		return isMine(type)||type==CommandCentre.getType()||type==CashWarehouse.getType()||type==OilWarehouse.getType()||type==CrystalWarehouse.getType()||type==Vault.getType()||type==CrystalMine.getType();
	}
	
}

