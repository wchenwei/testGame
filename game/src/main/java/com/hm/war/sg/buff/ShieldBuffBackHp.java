package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;
import com.hm.war.sg.UnitAtkAction;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 护盾buf
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class ShieldBuffBackHp extends ShieldBuff{
	private long backHurt;//反弹的伤害
	private double backRate;
	
	public ShieldBuffBackHp(long endFrame,long value,Object confValue) {
		super(endFrame,value);
		this.backHurt = value;
		this.backRate = (double)confValue;
	}
	
	@Override
	public void doRemoveAction(Frame frame,Unit unit) {
		//给对方全场造成伤害
		List<Unit> defList = unit.getDefGroup().getLifeUnit();
		for (Unit def : defList) {
			long trueHurt = UnitAtkAction.calSkillHurt(unit, def, backHurt*backRate);
			HurtBear hurtBear = new HurtBear(unit.getId(), frame.getId()+2, trueHurt, getSkillId(), getFuncId());
			def.addBear(frame, hurtBear);
		}
	}
	
	@Override
	public boolean isOverlying() {
		return true;
	}
	
}
