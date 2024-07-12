package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.RecoverMpBear;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 护盾破裂后增加mp
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class ShieldBuffAddMp extends ShieldBuff{
	private double mpRate;//增加最大蓝量的百分比

    public ShieldBuffAddMp(long endFrame, long value, Object confValue) {
		super(endFrame,value);
        this.mpRate = (double) confValue;
	}
	
	@Override
	public void doRemoveAction(Frame frame,Unit unit) {
		//给对方全场造成伤害
		List<Unit> defList = unit.getMyGroup().getLifeUnit();
		for (Unit def : defList) {
			double addMp = def.getMpEngine().getMaxMp()*mpRate;
			if(addMp > 0) {
				def.addBear(frame, new RecoverMpBear(def.getId(), addMp, frame.getId(), 0));
			}
		}
	}
	
	@Override
	public boolean isOverlying() {
		return true;
	}
	
}
