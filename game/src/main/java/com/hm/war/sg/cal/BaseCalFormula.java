package com.hm.war.sg.cal;

import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 计算公式
 * @author siyunlong  
 * @date 2018年10月10日 下午1:20:57 
 * @version V1.0
 */
public abstract class BaseCalFormula {
	public abstract double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object...args);
}
