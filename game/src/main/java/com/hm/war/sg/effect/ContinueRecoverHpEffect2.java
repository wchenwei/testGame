package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.ContinueBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 计算的伤害值实时计算
 * @date 2024/2/27 13:45
 */
public class ContinueRecoverHpEffect2 extends BaseSkillEffect{
	private boolean isAdd;

	public ContinueRecoverHpEffect2(boolean isAdd) {
		super(SkillEffectType.RecoverHp);
		this.isAdd = isAdd;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList,Skill skill,SkillFunction skillFunction) {
		long effectFrame = frame.getId()+skillFunction.getEffectFrame();
		
		unitList.forEach(myUnit -> {
			long addHp = getAddHp(unit, myUnit, skill, skillFunction);
			if(addHp == 0) {
				return;
			}
			ContinueBear bear = new ContinueBear(unit, addHp, 0, effectFrame, skillFunction);
			bear.setEveryCal(true);
			myUnit.addBear(frame, bear);
		});
	}
	
	public long getAddHp(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		long v = (long)skillFunction.getFunctionValue(atk,def,skill,args);
		return isAdd?v:-v;
	}
	
}
