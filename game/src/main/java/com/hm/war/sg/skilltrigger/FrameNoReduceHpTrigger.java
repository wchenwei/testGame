package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: x帧数内血量没有减少触发
 * @author siyunlong  
 * @date 2020年4月10日 下午3:37:28 
 * @version V1.0
 */
public class FrameNoReduceHpTrigger extends BaseTriggerSkill{
	private int count;
	
	public FrameNoReduceHpTrigger() {
		super(SkillTriggerType.FrameNoReduceHpTrigger);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		if(frame == null) {
			return false;
		}
		return frame.getId() - atk.getHpEngine().getLastReduceHpFrame() >= this.count;
	}
	
	public void init(String parms,int lv) {
		this.count = Integer.parseInt(parms);
	}
}
