package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.bear.RecoverMpBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 
 * @Description: 蓝量变化
 * @author siyunlong  
 * @date 2018年10月16日 下午2:42:09 
 * @version V1.0
 */
public class AddMpEffect extends BaseSkillEffect{
	private boolean isAdd;//增加还是减少
	
	public AddMpEffect(boolean isAdd) {
		super(SkillEffectType.MpChange);
		this.isAdd = isAdd;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		unitList.forEach(myUnit -> {
			long effectFrame = skillFunction.getEffectFrame();
			long addMp = getAddMp(unit, myUnit, skill, skillFunction);
			RecoverMpBear bear = new RecoverMpBear(unit.getId(), addMp, frame.getId()+effectFrame,skillFunction.getId());
			myUnit.addBear(frame,bear);
		});
	}
	
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		def.addBear(frame,new RecoverMpBear(atk.getId(), getAddMp(atk, def, skill, skillFunction,hurtBear.getHurt()), hurtBear.getEffectFrame(),skillFunction.getId()));
	}
	
	@Override
	public void doDefHurtBear(Frame frame, Unit unit,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction) {
		unitList.forEach(myUnit -> {
			long effectFrame = hurtBear.getEffectFrame();
			long addMp = getAddMp(unit, myUnit, skill, skillFunction);
			RecoverMpBear bear = new RecoverMpBear(unit.getId(), addMp, effectFrame,skillFunction.getId());
			myUnit.addBear(frame,bear);
		});
	}
	
	public long getAddMp(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		long value = (long)skillFunction.getFunctionValue(atk,def,skill,args);
		return isAdd?value:-value;
	}
	
}

