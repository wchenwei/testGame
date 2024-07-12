package com.hm.war.sg.skillchoice;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 能力比例选择
 * @author siyunlong  
 * @date 2020年12月24日 下午4:00:53 
 * @version V1.0
 */
public class MpSortChoice extends BaseSkillChoice{
	private boolean isAtk;
	private boolean isTop;
	private boolean isBfb;
	private int num;
	

	public MpSortChoice(boolean isAtk, boolean isTop, boolean isBfb) {
		this.isAtk = isAtk;
		this.isTop = isTop;
		this.isBfb = isBfb;
	}

	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		UnitGroup tempGroup = isAtk ? atkGroup:defGroup;
		if(isBfb) {
			return getMpRateUnit(tempGroup.getLifeUnit(),isTop,this.num);
		}else{
			return getMpUnit(tempGroup.getLifeUnit(),isTop,this.num);
		}
	}
	
	@Override
	public void loadParm(String parm) {
		this.num = Integer.parseInt(parm);
	}
	
	//血量比排序
	public static List<Unit> getMpRateUnit(List<Unit> unitList,boolean isTop,int num) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		if(isTop) {
			return unitList.stream().sorted(Comparator.comparingDouble(Unit::getMpRate).reversed())
					.limit(num)
			.collect(Collectors.toList());
		}else{
			return unitList.stream().sorted(Comparator.comparingDouble(Unit::getMpRate))
					.limit(num)
			.collect(Collectors.toList());
		}
	}
	//血量最低的战车
	public static List<Unit> getMpUnit(List<Unit> unitList,boolean isTop,int num) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		if(isTop) {
			return unitList.stream().sorted(Comparator.comparingDouble(Unit::getCurMp).reversed())
					.limit(num)
			.collect(Collectors.toList());
		}else{
			return unitList.stream().sorted(Comparator.comparingDouble(Unit::getCurMp))
					.limit(num)
			.collect(Collectors.toList());
		}
	}
}
