package com.hm.enums;

/**
 * @Description: 玩家奖励模块
 * @author siyunlong  
 * @date 2019年3月28日 下午4:48:23 
 * @version V1.0
 */
public enum PlayerRewardMode {
	TowerBattle(8,null,true,false,"爬塔战役"),
	TrumpBattle(9,null,true,false,"王牌对决"),
	PVEMission(10,null,false,true,"PVE局结束结算"),
	PlayerLvEvent(11,null,false,true,"等级事件奖励"),
	ShopBuy(12,null,false,true,"商店购买经验"),
	ThreeDayActivity(13,null,false,true,"三日活动经验"),
	SevenRechargeActivity(14,null,false,true,"七日充值活动经验"),
	;
	
	private PlayerRewardMode(int type, ActivityType doubleActivityType,boolean berlinBuff,boolean isExpAdd,String desc) {
		this.type = type;
		this.activityType = doubleActivityType;
		this.berlinBuff = berlinBuff;
		this.isExpAdd = isExpAdd;
		this.desc = desc;
	}

	private int type;
	private ActivityType activityType;//双倍活动
	private boolean berlinBuff;
	private boolean isExpAdd;
	private String desc;
	
	public boolean isBerlinBuff() {
		return berlinBuff;
	}

	public ActivityType getDoubleActivityType() {
		return activityType;
	}
	
	public boolean isExpAdd() {
		return isExpAdd;
	}

	public static PlayerRewardMode getPlayerRewardMode(BattleType battleType) {
		switch (battleType) {
			case TowerBattle:
				return PlayerRewardMode.TowerBattle;
		}
		return null;
	}
}
