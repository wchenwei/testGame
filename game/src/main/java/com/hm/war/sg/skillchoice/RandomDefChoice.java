package com.hm.war.sg.skillchoice;

import com.hm.util.RandomUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 
 * @Description: 随机不可重复对象-不重复！
 * @author siyunlong  
 * @date 2018年11月10日 上午9:41:22 
 * @version V1.0
 */
public class RandomDefChoice extends BaseSkillChoice{
	private int num;//随机个数
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		return RandomUtils.randomEleList(defGroup.getLifeUnit(), num);
	}
	
	@Override
	public void loadParm(String parm) {
		this.num = Integer.parseInt(parm);
	}
	
}
