package com.hm.enums;

import com.hm.config.city.AbstractWorldConfig;
import com.hm.config.city.WorldConfig;
import com.hm.servercontainer.world.AbstractCityWorld;
import com.hm.servercontainer.world.EuropeWorld;

public enum WorldType {
	Normal(0,"正常世界") {
		@Override
		public AbstractCityWorld createCityWorld(int serverId) {
			return new EuropeWorld(serverId);
		}

		@Override
		public AbstractWorldConfig createWorldConfig() {
			return new WorldConfig();
		}
	},
	KF(0,"跨服") {
		@Override
		public AbstractCityWorld createCityWorld(int serverId) {
			return null;
		}

		@Override
		public AbstractWorldConfig createWorldConfig() {
			return null;
		}
	},
	;
	
	private WorldType(int type,String desc){
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
	
	public abstract AbstractCityWorld createCityWorld(int serverId);
	public abstract AbstractWorldConfig createWorldConfig();
	
//	public static WorldType getType(int type) {
//		for (WorldType temp : WorldType.WorldList) {
//			if(type == temp.getType()) return temp; 
//		}
//		return null;
//	}
	
	public static WorldType getTypeByCityId(int id) {
		return WorldType.Normal;
	}

	public static WorldType[] WorldList = new WorldType[]{WorldType.Normal};
}
