package com.hm.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GameCdConfig;
import com.hm.model.player.BasePlayer;

/**
 * 
 * @Description: CD类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum CDType {
	TankResearchJunior(1, "tank普通研发"),
    TankResearchSenior(2, "tank高级研发"),
    TankDraw(3, "tank绘制"),
    BattlefieldTreasure(4,"战场寻宝"),
	Agent(5, "调教特工体力"){
		@Override
		public int getMaxCount(BasePlayer player) {
			int maxCount = super.getMaxCount(player);
			if(player.getPlayerRecharge().haveSeasonVip()){
				CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
				maxCount = Math.max(maxCount, commValueConfig.getCommValue(CommonValueType.SeasonVipCard_Buf4));
			}
			return maxCount;
		}
	},
    TankTrain(6, "坦克训练"),
    GameEnergy(7, "小游戏体力"),
	Strength(8, "力量系统")
	;
	
	private CDType(int type,String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	private String desc;
	
	public int getMaxCount(BasePlayer player) {
		GameCdConfig gameCdConfig = SpringUtil.getBean(GameCdConfig.class);
		return gameCdConfig.getCdTemplate(type).getMaxCount();
	}
	public int getCdSecond(BasePlayer player) {
		GameCdConfig gameCdConfig = SpringUtil.getBean(GameCdConfig.class);
		return gameCdConfig.getCdTemplate(type).getCdSecond();
	}
	
	public int getDefaultCount() {
		GameCdConfig gameCdConfig = SpringUtil.getBean(GameCdConfig.class);
		return gameCdConfig.getCdTemplate(type).getBaseCount();
	}
	
	public boolean isDayReset() {
		return false;
	}
	
	public int getType() {
		return type;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static CDType getCDType(int type) {
		for (CDType temp : CDType.values()) {
			if(temp.getType() == type) {
				return temp;
			}
		}
		return null;
	}
	
}
