package com.hm.enums;

/**
 * 
 * @Description: 玩家可开设性功能
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum PlayerFunctionType {
    TankStar(2,"战兽升星"),
    Driver(3,"战兽兽魂"),
    GodWar(4,"战兽布阵"),
    Master(5,"战兽图鉴"),
    TankFactory(9,"十连抽"),
    Guild(11,"部落"),
	GuildWar(12,"部落战"),
    FlashBattle(13,"闪电击"),
    FieldBoss(14,"兽王试炼"),
    Arena(16,"竞技场"),
    MilitaryRank(17,"军衔"),
    Mounts(18,"坐骑"),
	Trade(33,"海运贸易 "),
	RobSupply(34,"补给掠夺"),
	Shotting(37,"坦克打靶"),
	TankTech(38,"坦克科技（芯片，突破）"),
	Equip(39,"指挥官装备"),
	TrumpArena(40,"王牌竞技场"),
	TankEvolveStar(42,"坦克进化"),
	RandTask(44,"民情事件"),
	ResBack(47,"资源找回"),
	Mastery(49,"专精"),
	TankSpecial(52,"专精"),
	Agent(53,"特工"),
	MilitaryProject(54,"指挥官--军工"),
	WorldBoss(55,"世界boss"), 
	Passenger(57,"乘员"), 
	PlayerMedal(59,"玩家勋章系统"),
	MagicReform(63,"魔改"),
	MemorialHall(68,"纪念馆"),
	
	WarCraft(71,"兵法"),
	
	TankDriverAd(73,"坦克军职"),
	BuildUnlock(75,"基地解锁"),
	TankCaptive(76,"坦克俘虏"),
	TankSoldier(77,"奇兵"),
	AircraftCarrier(79,"航母"),
	Control(80,"中控"),

	XianDingMagic(84,"坦克车魔改"),

	TradeStock(85,"贸易股东"),
	Fish(86,"钓鱼玩法"),
	PlayerStrength(87,"力量系统"),
	;
	
	private PlayerFunctionType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static PlayerFunctionType getPlayerFunctionType(int type) {
		for (PlayerFunctionType buildType : PlayerFunctionType.values()) {
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
