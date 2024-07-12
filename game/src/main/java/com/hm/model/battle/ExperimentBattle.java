package com.hm.model.battle;

import com.hm.enums.BattleType;
import com.hm.model.player.BasePlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 征战蛮荒
 */
@Getter
@Setter
@NoArgsConstructor
public class ExperimentBattle extends MissionBattle {

	private int dayRewardCount;// 每日奖励领取次数
	
	public ExperimentBattle(BattleType battleType){
		super(battleType);
	}

	@Override
	public void resetDay(BasePlayer player) {
		this.dayRewardCount = 0;
	}

	public void addDayRewardCount(){
		this.dayRewardCount ++;
	}
}
