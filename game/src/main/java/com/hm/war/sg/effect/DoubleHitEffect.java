package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.UnitAtkAction;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.bear.NormalAtkBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class DoubleHitEffect extends BaseSkillEffect{
	
	public DoubleHitEffect() {
		super(SkillEffectType.DoubleHit);
	}

	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk, Unit def, HurtBear hurtBear, Skill skill, SkillFunction skillFunction){
		int interval = 2;//间隔2帧发送第二次普攻
		HurtBear newBear = UnitAtkAction.calNormalAtkHurt(frame, atk, def);
		long flyFrame = newBear.getEffectFrame() - frame.getId();
		newBear.setEffectFrame(newBear.getEffectFrame()+interval);
		def.addBear(frame,newBear);//添加受伤Bear
		
		atk.addBear(frame, new NormalAtkBear(atk, newBear,def.getId(),frame.getId()+interval,flyFrame));
	}
}
