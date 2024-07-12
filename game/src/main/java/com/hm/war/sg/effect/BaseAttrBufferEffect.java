package com.hm.war.sg.effect;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.AttrBear;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;

import java.util.List;

public class BaseAttrBufferEffect extends BaseSkillEffect{
	protected TankAttrType attrType; 
	
	public BaseAttrBufferEffect(TankAttrType attrType) {
		super(SkillEffectType.AttrBuff);
		this.attrType = attrType;
	}
	
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
//		long overTimes = skillFunction.getMaxAttBuffOvers();//叠加层数
//		int funcId = skillFunction.getId();
//		final long endFrame = skillFunction.getContinuedFrame() < 0 ?-1:frame.getId()+skillFunction.getContinuedFrame();
		unitList.forEach(def -> 
			{
				long effectFrame = frame.getId() + skillFunction.getEffectFrame(unit,def);
				double addAttr = getAttrValue(unit,def,skill, skillFunction);
//				计算最大叠加层数
//				def.getUnitBuffs().resetSkillBuffOverTimes(skillFunction.getId(), overTimes);
//				AttrBuff buff = new AttrBuff(endFrame, attrType, addAttr,unit.getId(),skillFunction.getId(),skillFunction.getBuffKind());
//				def.getUnitBuffs().addBuff(buff);
				def.addBear(frame,new AttrBear(unit.getId(), effectFrame, attrType, addAttr,skillFunction));
			}
		);
	}
	
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
//		//计算最大叠加层数
//		long overTimes = skillFunction.getMaxAttBuffOvers();//叠加层数
//		def.getUnitBuffs().resetSkillBuffOverTimes(skillFunction.getId(), overTimes);
//		//普攻到达身上的时间+持续时间
//		long endFrame = hurtBear.getEffectFrame()+skillFunction.getContinuedFrame();
//		def.getUnitBuffs().addBuff(new AttrBuff(endFrame, attrType, getAttrValue(atk,def,skill, skillFunction),atk.getId(),skillFunction.getId(),skillFunction.getBuffKind()));
		
		double addAttr = getAttrValue(atk,def,skill, skillFunction);
		long effectFrame = hurtBear.getEffectFrame();
		def.addBear(frame,new AttrBear(atk.getId(), effectFrame, attrType, addAttr, skillFunction));
	}
	
	@Override
	public void doDefHurtBear(Frame frame, Unit unit,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction) {
		unitList.forEach(def -> 
			{
				long effectFrame = hurtBear.getEffectFrame();
				double addAttr = getAttrValue(unit,def,skill, skillFunction);
//				计算最大叠加层数
				def.addBear(frame,new AttrBear(unit.getId(), effectFrame, attrType, addAttr,skillFunction));
			}
		);
	}
	
	@Override
	public void doCalAttr(Unit atk, UnitAttr unitAttr, Skill skill, SkillFunction skillFunction){
		double addAttr = getAttrValue(atk,atk,skill, skillFunction);
		unitAttr.addAttr(attrType, addAttr);
	}
	
	public double getAttrValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getFunctionValue(atk,def,skill);
	}
}
