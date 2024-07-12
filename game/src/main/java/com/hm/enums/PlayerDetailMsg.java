package com.hm.enums;

import com.google.common.collect.Maps;
import com.hm.model.player.Aircraft;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerAircraftCarrier;

import java.util.List;
import java.util.Map;

public enum PlayerDetailMsg {
	Currency(1, "currency数据") {
		@Override
		public Object getData(Player player) {
			return player.playerCurrency();
		}
	},
	Tanks(2, "坦克") {
		@Override
		public Object getData(Player player) {
			return player.playerTank();
		}
	},
	PlayerCommander(3, "指挥官") {
		@Override
		public Object getData(Player player) {
			return player.playerCommander();
		}
	},
	PlayerEquip(4, "指挥官装备") {
		@Override
		public Object getData(Player player) {
			return player.playerEquip();
		}
	},
	PlayerTankPaper(5, "坦克图纸") {
		@Override
		public Object getData(Player player) {
			return player.playerTankPaper();
		}
	},
	PlayerStone(6, "宝石") {
		@Override
		public Object getData(Player player) {
			return player.playerStone();
		}
	},
	playerMilitaryLineup(7, "军阵信息") {
		@Override
		public Object getData(Player player) {
			return player.playerMilitaryLineup();
		}
	},
	playerMastery(8, "专精，研修等级") {
		@Override
		public Object getData(Player player) {
			return player.playerMastery();
		}
	},
	playerMemorialHall(9, "玩家纪念馆") {
		@Override
		public Object getData(Player player) {
			return player.playerMemorialHall();
		}
	},
	PlayerWarcraft (11, "兵法信息") {
		@Override
		public Object getData(Player player) {
			return player.playerWarcraft();
		}
	},
	PlayerBag(12, "玩家背包") {
		@Override
		public Object getData(Player player) {
			return player.playerBag();
		}
	},
	AircraftCarrier(13, "舰岛内容") {
		@Override
		public Object getData(Player player) {
			Map<String, Object> result = Maps.newHashMap();
			PlayerAircraftCarrier tempAircraft = player.playerAircraftCarrier();

			Map<Integer, Aircraft> aircraftMap = Maps.newHashMap();
			List<String> list = player.playerAircraftCarrier().getAircraftList();
			for(int i=0; i<list.size(); i++) {
				Aircraft aircraft = player.playerAircraft().getAircraft(list.get(i));
				aircraftMap.put(i+1, aircraft);
			}

			result.put("aircraftCarrier", tempAircraft);
			result.put("aircraft", aircraftMap);
			return result;
		}
	}
	;
	
	private PlayerDetailMsg(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public abstract Object getData(Player player);


	private int type;
	private String desc;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
