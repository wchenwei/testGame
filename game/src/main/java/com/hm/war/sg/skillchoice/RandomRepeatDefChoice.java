package com.hm.war.sg.skillchoice;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 随机可重复对象
 * @author siyunlong  
 * @date 2018年11月10日 上午9:40:28 
 * @version V1.0
 */
public class RandomRepeatDefChoice extends BaseSkillChoice{
	private int num;//随机个数
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		List<Unit> defList = defGroup.getLifeUnit();
		if(defList.isEmpty()) {
			return Lists.newArrayList();
		}
		return RandomUtil.randomEles(defList, num);
	}
	
	@Override
	public void loadParm(String parm) {
		this.num = Integer.parseInt(parm);
	}
}
