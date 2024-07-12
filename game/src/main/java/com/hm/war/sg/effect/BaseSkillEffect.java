package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;

import java.util.List;

/***
 * @Description: 基础技能效果
 * @author siyunlong  
 * @date 2018年9月27日 下午4:56:39 
 * @version V1.0
 */
public abstract class BaseSkillEffect {
	private SkillEffectType type;
	public BaseSkillEffect(SkillEffectType type) {
		this.type = type;
	}
	
	public SkillEffectType getType() {
		return type;
	}
	
	/**
	 * 大招技能触发
	 * @param frame
	 * @param unit
	 * @param unitList
	 * @param skill
	 * @param skillFunction
	 */
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction){
	}
	
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		
	}
	/**
	 * 受伤时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doDefHurtBear(Frame frame, Unit def,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		
	}
	
	
	public void doCalAttr(Unit atk, UnitAttr unitAttr, Skill skill, SkillFunction skillFunction){
		
	}
}
