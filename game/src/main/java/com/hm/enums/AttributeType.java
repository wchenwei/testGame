package com.hm.enums;

import com.google.common.collect.Lists;

import java.util.List;

public enum AttributeType {
	Cash_Limit(1,"钞票储存上限"),
	Oil_Limit(2,"石油储存上限"),
	Crystal_Limit(3,"晶石储存上限"),
	Cash_Protect_Limit(4,"钞票保护上限"),
	Oil_Protect_Limit(5,"石油保护上限"),
	Crystal_Protect_Limit(6,"晶石保护上限"),
	Crystal_Speed(7,"晶石矿生产速度(min)"),
	Crystal_Product_Limit(8,"晶石矿生产上限"),
	;
	private AttributeType(int type,String desc){
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
	//矿场速度
//	public static AttributeType getMineSpeed(int buildType) {
//		if(buildType==BuildType.GoldFactory.getType()){
//			return GOLD_SPEED;
//		}else if(buildType==BuildType.FerrumFactory.getType()){
//			return FE_SPEED;
//		}else if(buildType==BuildType.Oilwell.getType()){
//			return OIL_SPEED;
//		}else if(buildType==BuildType.ElecFactory.getType()){
//			return ELEC_SPEED;
//		}
//		return null;
//	}
//	
	public static List<BuildType> getBuildTypeByAttribute(AttributeType type){
		if(type==Cash_Limit){
			return Lists.newArrayList(BuildType.CashWarehouse,BuildType.CommandCentre);
		}else if(type==Oil_Limit){
			return Lists.newArrayList(BuildType.OilWarehouse,BuildType.CommandCentre);
		}else if(type==Crystal_Limit){
			return Lists.newArrayList(BuildType.CrystalWarehouse,BuildType.CommandCentre);
		}else if(type==Cash_Protect_Limit||type==Oil_Protect_Limit||type==Crystal_Protect_Limit){
			return Lists.newArrayList(BuildType.Vault);
		}else if(type==Crystal_Speed||type==Crystal_Product_Limit){
			return Lists.newArrayList(BuildType.CrystalMine);
		}
		return null;
	}
//	
//	//兵营造兵速度
//	public static AttributeType getFactorySpeed(int buildType) {
//		if(buildType==BuildType.TankFactory.getType()){
//			return PRODUCE_TANK_SPEED;
//		}else if(buildType==BuildType.RocketFactory.getType()){
//			return PRODUCE_ROCK_SPEED;
//		}else if(buildType==BuildType.ArmorFactory.getType()){
//			return PRODUCE_ARMOR_SPEED;
//		}
//		return null;
//	}
//	//兵营容量
//	public static AttributeType getFactoryLimit(int buildType) {
//		if(buildType==BuildType.TankFactory.getType()){
//			return TANK_LIMIT;
//		}else if(buildType==BuildType.RocketFactory.getType()){
//			return ROCK_LIMIT;
//		}else if(buildType==BuildType.ArmorFactory.getType()){
//			return ARMOR_LIMIT;
//		}
//		return null;
//	}
	public static AttributeType getAddType(int type) {
		for (AttributeType temp : AttributeType.values()) {
			if(type == temp.getType()) return temp; 
		}
		return null;
	}
	//该建筑会影响哪些资源上限
	public static List<AttributeType> getTypeByBuildType(BuildType buildType){
		if(BuildType.CommandCentre==buildType) return Lists.newArrayList(AttributeType.Cash_Limit,AttributeType.Oil_Limit,AttributeType.Crystal_Limit);
		if(BuildType.CashWarehouse==buildType) return Lists.newArrayList(AttributeType.Cash_Limit);
		if(BuildType.OilWarehouse==buildType) return Lists.newArrayList(AttributeType.Oil_Limit);
		if(BuildType.CrystalWarehouse==buildType) return Lists.newArrayList(AttributeType.Crystal_Limit);
		if(BuildType.Vault==buildType) return Lists.newArrayList(AttributeType.Cash_Protect_Limit,AttributeType.Oil_Protect_Limit,AttributeType.Crystal_Protect_Limit);
		if(BuildType.CrystalMine==buildType) return Lists.newArrayList(AttributeType.Crystal_Speed,AttributeType.Crystal_Product_Limit);
		return null;
	}
	
	
}
