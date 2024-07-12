package com.hm.enums;


/**
 * 
 * @Description: vip特权
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum VipPowType {
	/**
	 * 1:X	十连抽五折X次	
		2:X	府库收缴钞票	
		3:X	府库征收石油	
		4:X	坦克研发每个城上限	
		5:X	军演购买次数	
		6:X	闪电战可购买次数	
		7:X	经典战役购买次数	
		8:X	远征可重置次数	
		9:X	王牌对决次数	
		10:X	使命之路重置次数	
		11:X	使命之路boss购买次数	
		100:X	包含所有Xvip特权	
	 */
	TenDiscount(1,"十连抽五折X次"),
	TreasuryCash(2,"府库收缴钞票"),
	TreasuryOil(3,"府库征收石油"),
	TankResearchTop(4,"坦克研发每个城上限"),
	MilitaryBuy(5,"军演购买次数"),
	SDBattleBuy(6,"闪电战可购买次数"),
	JDBattleBuy(7,"经典战役购买次数"),
	YZBattleBuy(8,"远征可重置次数"),
	WPBattleBuy(9,"王牌对决次数"),
	SMZLBattleReset(10,"使命之路重置次数"),
	SMZLBattleBossBuy(11,"使命之路boss购买次数"),
	WarHelperMinutes(12,"国战挂机分钟数"),
	AutoAdvance(13,"国战挂自动突进"),
	RaidersBattle(14,"夺宝奇兵次数购买"),
	AutoSneakAttack(15,"国战挂机自动偷袭"),
	TreasuryFreeCount(16,"免费征收次数"),
	FlashFreeCount(17,"闪电战额外免费次数"),
	TrumpFreeCount(18,"王牌对决额外免费次数"),
	RecoveryBattleBuy(19,"战场寻宝每日可购买次数"),
	AgentCount(20,"每日调教特工购买次数%s次"),
	ModernBattleBuy(22,"每日现代战争可购买次数"),
	RaidBattleBuy(23,"每日单兵奇袭可购买次数"),
	FrontBattleBuy(24,"每日战争前线可购买次数"),
	DreamlandBattleBuy(27,"每日梦回沙场可购买次数"),
	RareTreasureBuy(28,"每日秘境宝藏可购买次数"),
	EndlessBattleBuy(29,"每日无尽战区可购买次数"),
    ArmyPressBorderBuy(30, "大军压境可购买次数"),
	CostCollectionBuy(31, "新征收可购买次数"),
	ArmyFete(32, "每日石油补给量提升%s"),
	PvpOil(33, "偷袭时额外消耗石油降低%s"),
	Trade(34, "海运贸易多获得一条船"),
	AllVip(100,"包含所有Xvip特权"),
	;
	
	private VipPowType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static VipPowType getVipPowType(int type) {
		for (VipPowType buildType : VipPowType.values()) {
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
