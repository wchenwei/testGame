package com.hm.war.sg;

import com.google.common.collect.Lists;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.Map;

/**
 * @Description: 目标选择枚举
 * @author siyunlong  
 * @date 2018年9月26日 下午4:22:55 
 * @version V1.0
 */
public enum AtkChoiceEnum {
	GeneralAttack1(1,"普攻1") {
		//优先打前排并且同一列，比如1号位就打对方1号位，如果没有对位1号位，则从2,3,号位中随机，如果对方第一排没车的情况下才去中排选目标，最后从后排选目标
		@Override
		public List<Unit> choiceTargets(Unit atk, UnitGroup unitGroup) {
			Map<Integer,Unit> targetMap = unitGroup.getLifeUnitMap();
			int firstIndex = atk.getIndex()%3;
			for (int i = 1; i <= 3; i++) {
				Unit target = targetMap.get(firstIndex);
				if(target != null) {//选择对位
					return Lists.newArrayList(target);
				}
				//随机选择同一排的另外两个
				List<Integer> lines = ChoiceUtils.getLinesByLineNum(i);
				lines.remove((Integer)firstIndex);
				target = ChoiceUtils.randomChoiceUnit(lines, targetMap);
				if(target != null) {
					return Lists.newArrayList(target);
				}
				firstIndex += 3;
			}
			return Lists.newArrayList();
		}
	},
	GeneralAttack2(2,"普攻2") {
		//从后排开始随机，然后到中排，前排
		@Override
		public List<Unit> choiceTargets(Unit atk,UnitGroup unitGroup) {
			Map<Integer,Unit> targetMap = unitGroup.getLifeUnitMap();
			for (int i = 3; i >=0 ; i--) {
				//随机选择同一排的另外两个
				List<Integer> lines = ChoiceUtils.getLinesByLineNum(i);
				Unit target = ChoiceUtils.randomChoiceUnit(lines, targetMap);
				if(target != null) {
					return Lists.newArrayList(target);
				}
			}
			return Lists.newArrayList();
		}
	},
	GeneralAttack3(3,"普攻3") {
		//从中后排开始随机，然后到前排
		@Override
		public List<Unit> choiceTargets(Unit atk,UnitGroup unitGroup) {
			Map<Integer,Unit> targetMap = unitGroup.getLifeUnitMap();
			Unit target = ChoiceUtils.randomChoiceUnit(Lists.newArrayList(3,4,5,6,7,8), targetMap);
			if(target != null) {
				return Lists.newArrayList(target);
			}
			target = ChoiceUtils.randomChoiceUnit(Lists.newArrayList(0,1,2), targetMap);
			if(target != null) {
				return Lists.newArrayList(target);
			}
			return Lists.newArrayList();
		}
	},
	
	
	;
	
	private AtkChoiceEnum(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	private String desc;
	//选择攻击目标
	public abstract List<Unit> choiceTargets(Unit atk,UnitGroup unitGroup);
	
	
	public static AtkChoiceEnum getTargetChoiceEnum(int type) {
		for (AtkChoiceEnum buildType : AtkChoiceEnum.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	
}
