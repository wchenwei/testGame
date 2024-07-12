package com.hm.enums;

public class ActionType {
	public static final ActionType None = new ActionType("未找到", -1);
	public static final ActionType ArmsStrength = new ActionType("军工厂-武器强化",1);
	public static final ActionType ArmsPaperAllot = new ActionType("军工厂-图纸分配",2);
	public static final ActionType ArmsStrengthFinish = new ActionType("军工厂-武器强化完成(收取武器)",3);
	public static final ActionType GuildTrans = new ActionType("转让部落长",4);
	public static final ActionType TreasureScore = new ActionType("宝库积分获得",5);
	public static final ActionType AgentLvUp = new ActionType("特工升级",6);
	public static final ActionType ElementDown = new ActionType("元件拆除",7);
	public static final ActionType AircraftStarUp = new ActionType("航母飞机升星",8);
    public static final ActionType CarrierStrikeReset = new ActionType("航母出击重置海域", 10);
	public static final ActionType CityWar = new ActionType("城战", 11);
	public static final ActionType ContryWar = new ActionType("国战", 12);
	public static final ActionType KFJoin = new ActionType("跨服参与", 14);
	public static final ActionType KFSingUp = new ActionType("跨服报名", 15);
	public static final ActionType MainFbFail = new ActionType("主线关卡，失败", 16);
    public static final ActionType Act20 = new ActionType("活动20 特殊事件", 17);
    public static final ActionType SelectActivityStage = new ActionType(" 活动- 选择的卡包信息", 18);
    public static final ActionType ArmsPaperDestory = new ActionType("军工厂-图纸丢弃", 19);
	public static final ActionType AircraftCarrierStarUp = new ActionType("航母升级",20);

	public static final ActionType AircraftCarrier = new ActionType("航母信息",21);
	public static final ActionType TankControlMsg = new ActionType("坦克中控信息",22);
	public static final ActionType SmallGame = new ActionType("小游戏",23);
	public static final ActionType GiftPushOpen = new ActionType("推送激活",24);
	public static final ActionType GiftPushBuy = new ActionType("推送购买",25);
	public static final ActionType Gift9You = new ActionType("九游礼包",26);
	public static final ActionType RemovePlayer = new ActionType("注销角色", 27);
	public static final ActionType Act117Reward = new ActionType("补偿 14日活动奖励", 28);
	public static final ActionType Strength = new ActionType("力量系统", 30);
	public static final ActionType MissionBox = new ActionType("领取挂机宝箱", 31);
	public static final ActionType MilitaryLv = new ActionType("头衔升级", 32);
	public static final ActionType CarLv = new ActionType("座驾升级", 33);
	public static final ActionType Stone = new ActionType("宝石信息", 34);
	public static final ActionType StrengthLv = new ActionType("强化等级", 35);
	private int code;
	private String desc;
	private String extra;

	public ActionType(String desc, int code) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}
	
	public String getExtra() {
		return extra;
	}

	public ActionType value(String extra) {
		ActionType clone = new ActionType(this.desc, this.code);
		clone.extra = extra;
		return clone;
	}
	public ActionType value(long extra) {
		return value(extra+"");
	}
}
