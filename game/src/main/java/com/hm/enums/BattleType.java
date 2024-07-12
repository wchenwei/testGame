package com.hm.enums;

import com.google.common.collect.Lists;
import com.hm.action.battle.Handler.AbstractBattleHandler;
import com.hm.action.battle.Handler.ExperimentBattleHandler;
import com.hm.action.battle.Handler.TowerBattleHandler;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.battle.BaseBattle;
import com.hm.model.battle.ExperimentBattle;
import com.hm.model.battle.TowerBattle;

public enum BattleType {
		MainBattle(1,"mission,世界地图推关"){
			@Override
			public BaseBattle getPlayerBattle() {
				return null;
			}
		},
		ExperimentBattle(3,"征战蛮荒") {
			@Override
			public BaseBattle getPlayerBattle() {
				return new ExperimentBattle(this);
			}

			@Override
			public AbstractBattleHandler getBattleHandler() {
				return SpringUtil.getBean(ExperimentBattleHandler.class);
			}
		},
		TowerBattle(8,"驭魂之地"){
			@Override
			public BaseBattle getPlayerBattle() {
				return new TowerBattle();
			}
			@Override
			public AbstractBattleHandler getBattleHandler() {
				return SpringUtil.getBean(TowerBattleHandler.class);
			}
		},
		;
		
		
		private BattleType(int type,String desc){
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
		
		public static BattleType getBattleType(int type) {
			return Lists.newArrayList(BattleType.values()).stream().filter(t -> t.getType()==type).findFirst().orElse(null);
		}
		public abstract BaseBattle getPlayerBattle();

		public AbstractBattleHandler getBattleHandler(){
			return null;
		}
}
