package com.hm.war.sg.statistics;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class UnitDamage {
	private int tankId;
	private long normal;
	private Multiset<Long> skillMap = HashMultiset.create();
	private long horseHurt;
	
	public UnitDamage(int tankId) {
		super();
		this.tankId = tankId;
	}

	public void addDamage(long skill,long damage) {
		if(skill != 0) {
			this.skillMap.add(skill, (int)damage);
		}else{
			this.normal += damage;
		}
	}
	
	public String toDamageInfo() {
		return tankId+"="+"普工:"+normal+"  技能:"+skillMap.toString();
	}
}
