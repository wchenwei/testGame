package com.hm.war.sg.effect;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.AttrBear;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 
 * @Description: 偷取属性
 * @author siyunlong  
 * @date 2018年10月16日 下午2:42:09 
 * @version V1.0
 */
public class StealAttrEffect extends BaseSkillEffect{
	private TankAttrType attrType;//属性
	
	public StealAttrEffect(TankAttrType attrType) {
		super(SkillEffectType.StealAttr);
		this.attrType = attrType;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		unitList.forEach(def -> 
		{
			doEffectForDef(frame, unit, def,skill, skillFunction); 
		}
	);
	}
	
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit unit,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		doEffectForDef(frame, unit, def, skill, skillFunction);
	}
	
	
	private void doEffectForDef(Frame frame, Unit unit,Unit def,Skill skill, SkillFunction skillFunction) {
		long effectFrame = frame.getId() + skillFunction.getEffectFrame(unit,def);
		double addAttr = getValue(unit, def, skill, skillFunction);
		
		unit.addBear(frame,new AttrBear(def.getId(), effectFrame, attrType, addAttr,skillFunction));
		def.addBear(frame,new AttrBear(unit.getId(), effectFrame, attrType, -addAttr,skillFunction));
	}
	
	/**
	 * 偷取属性值
	 * @param atk
	 * @param def
	 * @param skill
	 * @param skillFunction
	 * @param args
	 * @return
	 */
	public double getValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		double rate = skillFunction.getFunctionValue(atk,def,skill,args);
		rate = Math.min(1, rate);
		double defAttrValue = def.getUnitAttr().getDoubleValue(attrType);
		return defAttrValue*rate;
	}
	
}

