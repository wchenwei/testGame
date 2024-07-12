package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 最大血量
 * @author siyunlong  
 * @date 2018年10月9日 下午7:48:59 
 * @version V1.0
 */
public class MaxHpTrigger extends BaseTriggerSkill{

	public MaxHpTrigger() {
		super(SkillTriggerType.DefAtkMaxHp);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		return def.getMaxHp() < atk.getMaxHp();
	}
}
