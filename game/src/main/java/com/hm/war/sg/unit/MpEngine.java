package com.hm.war.sg.unit;

import com.hm.enums.TankAttrType;
import com.hm.enums.WillSkillAddType;
import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.SettingManager;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.NImmunityEvent;
import com.hm.war.sg.event.RecoverEvent;

public class MpEngine {
	private transient Unit unit;
	public double mp;//当前蓝量
	//上次蓝量回复的帧
	protected transient long nextRecoveMpFrame;
	
	public MpEngine(Unit unit) {
		this.unit = unit;
	}
	
	//计算蓝量回复
	public void calLastRecoveMpFrame(Frame frame) {
		if(this.nextRecoveMpFrame == 0) {
			this.nextRecoveMpFrame = 10;
			return;
		}
		if(frame.getId() > this.nextRecoveMpFrame) {
			this.nextRecoveMpFrame += 10;
			//没10帧回复蓝量
			double secondAddHp = SettingManager.getInstance().getSecondMp();
			double recoverAddd = this.unit.getUnitAttr().getDoubleValue(TankAttrType.MpRecover);
			secondAddHp = secondAddHp + MathUtils.mul(secondAddHp,recoverAddd);
			boolean isAddMp = addMp(frame,secondAddHp);
			if(isAddMp) {
				frame.addEvent(new RecoverEvent(unit, 0,secondAddHp,0));//添加event
			}
		}
	}
	
	public double getMp() {
		return  this.mp;
	}
	
	public double getMaxMp() {
		return this.unit.getSetting().getMaxMp();
	}
	public double calTrueAddMp(double addMp) {
		if(addMp <= 0) {
			return addMp;
		}
		double mpRate = this.unit.getUnitBuffs().getBuffSumValue(UnitBufferType.ReduceMpBuff);
		addMp += addMp*mpRate;
		return addMp;
	}

	//所有回蓝
	public boolean addMp(Frame frame,double add) {
		if(add == 0) {
			return false;
		}
		if(add < 0) {
			//减少蓝
			return reduceMp(frame,0,add);
		}
		if(this.unit.getUnitBuffs().haveBuff(UnitBufferType.NotAddMpBuff)) {
			return false;//有不回蓝buff
		}
		add = calTrueAddMp(add);//检查mp增加减少buff
		if(add <= 0) {
			return false;
		}
		this.mp = Math.min(this.mp+add, getMaxMp());
		this.mp = Math.max(this.mp, 0);
		//取小数点
		this.mp = MathUtils.round(this.mp, 2);
		return true;
	}
	
	//技能回蓝
	public boolean addMpForSkill(Frame frame,double add) {
		if(add > 0 && this.unit.getUnitBuffs().haveBuff(UnitBufferType.NotAddMpSkillBuff)) {
			return false;//有不回蓝buff
		}
		return addMp(frame,add);
	}
	
	public void clearMp() {
		this.mp = 0;
	}
	public boolean isMpFull() {
		return this.mp >= getMaxMp();
	}

	public double getMpRate() {
		return MathUtils.div(this.mp, getMaxMp());
	}


	//减少mp
	public boolean reduceMp(Frame frame,int funcId,double val) {
		if(val >= 0) {
			return false;
		}
		if(unit.getUnitBuffs().isTriggerRateBuff(UnitBufferType.RateNoReduceMP)) {
			if(frame != null) {
				frame.addEvent(new NImmunityEvent(unit));
			}
			return false;//免疫减少mp
		}
		this.mp = Math.min(this.mp+val, getMaxMp());
		this.mp = Math.max(this.mp, 0);
		//取小数点
		this.mp = MathUtils.round(this.mp, 2);
		//不绑定特效
		if(funcId > 0 && frame != null) {
			frame.addEvent(new RecoverEvent(unit, 0,val,funcId));
		}

		return true;
	}

}
