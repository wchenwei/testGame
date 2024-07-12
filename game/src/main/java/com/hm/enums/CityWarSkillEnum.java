package com.hm.enums;

public enum CityWarSkillEnum {
	PvpOneByOneLaunch(1,"偷袭", StatisticsType.PvpOneByOneLaunch),
	TroopRetreat(2,"撤退", StatisticsType.TroopRetreat),
	TroopAdvance(3,"突进", StatisticsType.TroopAdvance),
	
	
	

	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private CityWarSkillEnum(int type, String desc, StatisticsType statisticsType) {
		this.type = type;
		this.desc = desc;
		this.statisticsType = statisticsType;
	}

	private int type;
	
	private String desc;
	
	private StatisticsType statisticsType;
	

	public StatisticsType getStatisticsType() {
		return statisticsType;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	public static CityWarSkillEnum getType(int type){
		for (CityWarSkillEnum kind : CityWarSkillEnum.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}
}
