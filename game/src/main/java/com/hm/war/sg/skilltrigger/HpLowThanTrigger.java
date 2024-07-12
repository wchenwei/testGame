package com.hm.war.sg.skilltrigger;

import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 血量低于触发
 * @author siyunlong  
 * @date 2018年10月9日 下午7:48:59 
 * @version V1.0
 */
public class HpLowThanTrigger extends BaseTriggerSkill{
	private boolean isAtk;
	private double rate;//半分比

	public HpLowThanTrigger(boolean isAtk) {
		super(SkillTriggerType.HpLowThan);
		this.isAtk = isAtk;
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		Unit temp = isAtk? atk:def;
		return MathUtils.div(temp.getHpEngine().getHp(), temp.getMaxHp()) <= rate;
	}
	
	public void init(String parms,int lv) {
		this.rate = Double.parseDouble(parms);
	}
}
