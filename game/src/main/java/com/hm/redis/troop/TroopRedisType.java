package com.hm.redis.troop;

public enum TroopRedisType {
	World(0,"世界部队") {
		@Override
		public TroopRedis create() {
			return new WorldTroopRedis();
		}
	},
	ArenaPrimary(1,"初级竞技场") {
		@Override
		public TroopRedis create() {
			return new ArenaPrimaryTroopRedis();
		}
	},
	ArenaMedium(2,"中级竞技场") {
		@Override
		public TroopRedis create() {
			return new ArenaMediumTroopRedis();
		}
	},
	KfWz(3,"跨服王者") {
		@Override
		public TroopRedis create() {
			return new KfWzTroopRedis();
		}
	},
	KFJJ(4,"跨服竞技场") {
		@Override
		public TroopRedis create() {
			return new KfJJTroopRedis();
		}
	},
	KFManor(5,"跨服领地战") {
		@Override
		public TroopRedis create() {
			return new KfManorTroopRedis();
		}
	},
	KFScore(6,"跨服积分") {
		@Override
		public TroopRedis create() {
			return new KfScoreTroopRedis();
		}
	},
	KFYy(7,"跨服远征") {
		@Override
		public TroopRedis create() {
			return new KfYyTroopRedis();
		}
	},
	KFMine(8,"跨服资源战") {
		@Override
		public TroopRedis create() {
			return new KfMineTroopRedis();
		}
	},
//	KFRank(9,"跨服排位赛") {
//		@Override
//		public TroopRedis create() {
//			return new KfRankTroopRedis();
//		}
//	},
	;
	
	private TroopRedisType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public abstract TroopRedis create();
	
	private int type;
	private String desc;
	
	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
}