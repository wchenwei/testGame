package com.hm.enums;

import com.hm.model.guild.tactics.*;

public enum GuildTacticsType {
	ArtillerySupport(1, "炮火支援") {
		@Override
		public AbstractGuildTactics buildGuildTactics(int second) {
			return new ArtillerySupportTactics(second);
		}
	},
	FinanceTatics(2, "金融战") {
		@Override
		public AbstractGuildTactics buildGuildTactics(int second) {
			return new FinanceTatics(second);
		}
	},
	Airdrop(3, "空投") {
		@Override
		public AbstractGuildTactics buildGuildTactics(int second) {
			return new AirdropTatics(second);
		}
	},
	TheSucker(4, "突进") {
		@Override
		public AbstractGuildTactics buildGuildTactics(int second) {
			return new TheSuckerTatics(second);
		}
	},
	Roadblock(5, "死战") {
		@Override
		public AbstractGuildTactics buildGuildTactics(int second) {
			return new RoadblockTatics(second);
		}
	},
	NoAirdrop(6, "禁空") {
		@Override
		public AbstractGuildTactics buildGuildTactics(int second) {
			return new NoAirdropTatics(second);
		}
	},
	;

	private GuildTacticsType(int type, String desc) {
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
	
	public static GuildTacticsType getType(int type) {
		for (GuildTacticsType buildType : GuildTacticsType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	
	public abstract AbstractGuildTactics buildGuildTactics(int second);
}
