package com.hm.war.sg.skillchoice;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.util.RandomUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 除了当前目标之外的随机n敌军
 * @author siyunlong  
 * @date 2020年8月18日 下午2:47:24 
 * @version V1.0
 */
public class NoTargetRandomChoice extends BaseSkillChoice{
	private int num;//随机个数
	
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		List<Unit> curDefs = atk.getAtkEngine().choiceTarget(defGroup);
		if(CollUtil.isEmpty(curDefs)) {
			return Lists.newArrayList();
		}
		List<Unit> lifeUnits = defGroup.getLifeUnit();
		lifeUnits.removeAll(curDefs);
		return RandomUtils.randomEleList(lifeUnits, num);
	}
	
	@Override
	public void loadParm(String parm) {
		this.num = Integer.parseInt(parm);
	}
}
