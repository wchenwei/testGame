package com.hm.enums;

/**
 * 
 * @Description: 商店道具购买后的道具数量类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum ShopItemNumType {
	Fixed(1,"固定量"),
	CollectionOne(2,"征收一次数量"),
	PeopleMax10(3,"10%人口上限"),
	PlayerLvCal(4,"玩家等级（玩家等级^*参数1+玩家等级*参数2+参数3）"),
	RecruitingOne(5,"max（玩家兵营单次招募数量，参数1）"),
	VipCal(6,"根据vip等级计算数量"),
	;
	
	private ShopItemNumType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static ShopItemNumType getShopItemNumType(int type) {
		for (ShopItemNumType buildType : ShopItemNumType.values()) {
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
