package com.hm.enums;

import com.google.common.collect.Lists;
import com.hm.model.cityworld.troop.CityDefNpcTroop;
import com.hm.model.cityworld.troop.NpcCityTroop;

/**
 * 
 * @Description: npc类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum NpcConfType {
	RebelArmyNpc(-1,"叛軍") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return null;
		}
	},
	CityDef0(0,"城池防守") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(npcId, serverId,campId);
		}
	},
	CityDef1(1,"城池防守") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(npcId, serverId,campId);
		}
	},
	CityDef2(2,"城池防守") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(npcId, serverId,campId);
		}
	},
	CityDef3(3,"城池防守") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(npcId, serverId,campId);
		}
	},
	SupplyNpc(6,"补给npc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(npcId, serverId,campId);
		}
	},
	KfWzArmyBoss(13,"空降师bossnpc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(npcId, serverId,campId);
		}
	},
	KfWzArmyNormal(14,"空降师普通npc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(npcId, serverId,campId);
		}
	},
	KfScuffleAirportBoss(19,"极地乱斗空降师bossnpc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int guildId) {
			return new CityDefNpcTroop(this,npcId, serverId,guildId);
		}
	},
	KfScuffleAirportNormal(20,"极地乱斗空降师普通npc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(this,npcId, serverId,campId);
		}
	},
	KfScuffleTran(21,"极地乱斗运输队npc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(this,npcId, serverId,campId);
		}
	},
	KfScuffleAirborneElite(22,"极地乱斗空降师战术精英npc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(this,npcId, serverId,campId);
		}
	},
	KfScuffleAirborneNormal(23,"极地乱斗空降师战术普通npc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId,int campId) {
			return new CityDefNpcTroop(this,npcId, serverId,campId);
		}
	},
	KfWorldWarNpc(24, "跨服世界大战npc") {
		@Override
		public NpcCityTroop createNpcTroop(int npcId, int serverId, int campId) {
			return new CityDefNpcTroop(this, npcId, serverId, campId);
		}
	},
	FieldBoss(30,"兽王试炼");
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private NpcConfType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public NpcCityTroop createNpcTroop(int npcId,int serverId,int campId){
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
	public static NpcConfType getNpcConfType(int type) {
		return Lists.newArrayList(NpcConfType.values()).stream().filter(t ->t.getType()==type).findFirst().orElse(CityDef0);
	}
}
