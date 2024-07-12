package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 普攻次数
 * @author siyunlong  
 * @date 2018年10月9日 下午7:48:59 
 * @version V1.0
 */
public class NormalAtkBeiTrigger extends BaseTriggerSkill{
	private int interval;

	public NormalAtkBeiTrigger() {
		super(SkillTriggerType.NormalAtkBei);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		long atkNum = atk.getAtkEngine().getNormalAtkNum();
		return atkNum > 0 && interval > 0 && atkNum%interval == 0;
	}
	
	public void init(String parms,int lv) {
		this.interval = Integer.parseInt(parms);
	}
}
