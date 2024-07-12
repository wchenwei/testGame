package com.hm.enums;

public enum RechargeType {
	NormalRecharge(1,"普通充值"),
	YueKa(2,"月卡"),
	GiftRecharge(3,"计费礼包"),
	ZhouKa(4,"周卡"),
	SuperWeapon(5,"超武"),
	GrowupRecharge(6,"成长计费点"),
	NewPlayerGift(7,"新手礼包"),
	GiftPackPay(8,"限时特惠（后台活动配置开启）"),
	PayEveryDay(9,"每日必买"),
	MidAutumn(10,"中秋活动"),
	Double11Acivity(12,"11.11活动"),
	DailyTaskGift(13,"高阶任务证书"),
	JiKa(14,"季卡"),
	ChristGift(15,"圣诞礼包"),
	Double12Acivity(16,"12.12活动"),
	Double11NewAcivity(17,"11.11活动 限时秒杀、狂欢盛典"),
    PayEveryDayGift(18, "每日必买五日礼包"),
	GiftBag(19, "周月礼包"),
	SeasonVipCard(20, "至尊季卡"),
	SubscribeCard(21, "订阅特权卡"),
	NoAdCard(22, "免广告卡"),
	FreeGift(23, "免费礼包"),
	TimeGift(26, "限时礼包"),

	;
	
	private RechargeType(int type, String desc) {
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

	public static boolean isDayCard(int type) {
		return type == YueKa.getType() || type == ZhouKa.getType()||type==JiKa.getType()
				||type==SeasonVipCard.getType()
				|| type==SubscribeCard.getType() || type==NoAdCard.getType();
	}
}
