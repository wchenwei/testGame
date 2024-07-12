package com.hm.war.sg.buff;

import com.google.common.collect.Lists;
import com.hm.war.sg.Frame;
import com.hm.war.sg.event.RecoverEvent;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 吸取buf
 * @author siyunlong  
 * @date 2018年10月9日 下午3:02:16 
 * @version V1.0
 */
public class DrawBuff extends BaseBuffer{
	private List<Integer> defList = Lists.newArrayList();
	private long value;
	
	private int drawType;
	private int interval;
	private long count;//次数
	private long nextEffectFrame;
	
	
	public DrawBuff(List<Integer> defList, long value, int type,
			int interval, long count, long nextEffectFrame,BuffKind buffKind,int funcId) {
		super(UnitBufferType.DrawBuff, -1, buffKind);
		this.defList = defList;
		this.value = value;
		this.drawType = type;
		this.interval = interval;
		this.count = count;
		this.nextEffectFrame = nextEffectFrame;
		setFuncId(funcId);
	}

	@Override
	public boolean isOver(long curFrame) {
		return count <= 0;
	}
	
	@Override
	public void doEffectBuff(Frame frame,Unit unit) {
		if(frame.getId() >= this.nextEffectFrame) {
			this.count --;
			this.nextEffectFrame += interval;
			
			if(drawType == 0) {
				addHp(frame, unit);
			}else{
				addMp(frame, unit);
			}
		}
	}
	
	private void addHp(Frame frame,Unit unit) {
		int sumAdd = 0;
		for (int defId : defList) {
			Unit def = unit.getDefGroup().getUnitById(defId);
			if(def == null || def.isDeath()) {
				continue;
			}
			long add = Math.min(this.value, def.getHp());
			sumAdd += add;
			def.getHpEngine().reduceHp(frame, unit.getId(), add,true,this.funcId,false);
			//不绑定特效
			frame.addEvent(new RecoverEvent(def, -add,0,getFuncId()));
		}
		if(sumAdd != 0) {
			unit.getHpEngine().addHp(sumAdd);
			frame.addEvent(new RecoverEvent(unit, sumAdd,0,getFuncId()));
		}
	}
	
	private void addMp(Frame frame,Unit unit) {
		int sumAdd = 0;
		for (int defId : defList) {
			Unit def = unit.getDefGroup().getUnitById(defId);
			if(def == null || def.isDeath()) {
				continue;
			}
			double add = Math.min(this.value, def.getMpEngine().getMp());
			if(def.getMpEngine().reduceMp(frame,getFuncId(),-add)) {
				sumAdd += add;
			}
		}
		if(sumAdd != 0) {
			if(unit.getMpEngine().addMpForSkill(frame,sumAdd)) {
				frame.addEvent(new RecoverEvent(unit, 0,sumAdd,getFuncId()));
			}
		}
	}
}
