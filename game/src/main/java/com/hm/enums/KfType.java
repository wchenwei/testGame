package com.hm.enums;

import com.hm.libcore.mongodb.ServerGroup;

public enum KfType {
	Sports(1,"跨服竞技赛") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			return serverGroup.getServerurl();
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			return serverGroup.getHttpServerurl();
		}
	},
	Manor(2,"跨服领地战") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[1];
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[2];
		}
	},
	Score(3,"跨服积分战") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[1];
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[2];
		}
	},
	PKLevel(4,"跨服段位赛") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[1];
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[2];
		}
	},
	KfMine(5,"跨服资源战") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[1];
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[2];
		}
	},
	KfExpedetion(6,"跨服远征") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[1];
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[2];
		}
	},
	KfKingCanyon(7,"王者峡谷") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[1];
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[2];
		}
	},
	//8 跨服建筑征讨
	
	KfScuffle(9,"极地乱斗") {
		@Override
		public String getTcpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[1];
		}
		@Override
		public String getHttpUrl(ServerGroup serverGroup) {
			String[] urls = serverGroup.getManorUrl().split("#");
			return urls[0]+":"+urls[2];
		}
	},

    KfWorldWar(10, "世界大战") {
        @Override
        public String getTcpUrl(ServerGroup serverGroup) {
            String[] urls = serverGroup.getManorUrl().split("#");
            return urls[0] + ":" + urls[1];
        }

        @Override
        public String getHttpUrl(ServerGroup serverGroup) {
            String[] urls = serverGroup.getManorUrl().split("#");
            return urls[0] + ":" + urls[2];
        }
    },
	;
	
	private KfType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public abstract String getTcpUrl(ServerGroup serverGroup);
	public abstract String getHttpUrl(ServerGroup serverGroup);
	
	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}

	public static KfType getType(int type) {
		for (KfType buildType : KfType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
}