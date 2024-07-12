package com.hm.war.sg.skilltrigger;

import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 蓝量触发
 * @author siyunlong  
 * @date 2018年10月9日 下午7:48:59 
 * @version V1.0
 */
public class MpTrigger extends BaseTriggerSkill{
	private boolean isAtk;
	private int type;//0-低于  1-高于
	private double rate;//半分比

	public MpTrigger(boolean isAtk,int type) {
		super(SkillTriggerType.SelfMpLow);
		this.isAtk = isAtk;
		this.type = type;
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		Unit temp = isAtk? atk:def;
		if(type == 0) {
			return MathUtils.div(temp.getMpEngine().getMp(), temp.getMpEngine().getMaxMp()) <= rate;
		}else{
			return MathUtils.div(temp.getMpEngine().getMp(), temp.getMpEngine().getMaxMp()) > rate;
		}
	}
	
	public void init(String parms,int lv) {
		this.rate = Double.parseDouble(parms);
	}
}
