package com.hm.model.battle;

import com.hm.enums.BattleType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseBattle {
	private int battleId;


	public BaseBattle(BattleType type) {
		this.battleId = type.getType();
	}

	//是否能战斗
	public boolean isCanFight(Player player,int id) {
		return true;
	}

	public boolean isCanSweep(int id) {
		return false;
	}

	//系统每日重置
	public void resetDay(BasePlayer player) {
	}

	public int getMaxHistory(){
		return 0;
	}

}
