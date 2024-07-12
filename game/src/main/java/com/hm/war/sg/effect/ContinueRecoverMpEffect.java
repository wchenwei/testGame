package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.ContinueBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class ContinueRecoverMpEffect extends BaseSkillEffect{
	private boolean isAdd;
	
	public ContinueRecoverMpEffect(boolean isAdd) {
		super(SkillEffectType.MpChange);
		this.isAdd = isAdd;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList,Skill skill,SkillFunction skillFunction) {
		long effectFrame = frame.getId()+skillFunction.getEffectFrame();

		unitList.forEach(myUnit -> {
			long addMp = getAddHp(unit, myUnit, skill, skillFunction);
			if(addMp == 0) {
				return;
			}
			ContinueBear bear = new ContinueBear(unit, 0, addMp, effectFrame, skillFunction);
			myUnit.addBear(frame, bear);
		});
	}
	
	
	public long getAddHp(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		long v = (long)skillFunction.getFunctionValue(atk,def,skill,args);
		return isAdd?v:-v;
	}
	
}