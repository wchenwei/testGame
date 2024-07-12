package com.hm.war.sg.bear;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.TransHurtBuff;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.HurtEvent;
import com.hm.war.sg.unit.HurtHpResult;
import com.hm.war.sg.unit.Unit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HurtBear extends Bear{
	private long hurt;
	private int addType;
	private long skillId;//所受技能id 如果=0 就是普通攻击 >0 是技能攻击
	private int funcId;
	private int atkTimes = 1;//普攻子弹次数
	
	//受致命连接影响
	private boolean isLinkHurt = true;
	//伤害无视护盾
	private boolean ignoreShield;

	public HurtBear(int atkId, long endFrame, long hurt,long skillId,int funcId) {
		super(atkId, endFrame);
		this.hurt = hurt;
		this.skillId = skillId;
		this.funcId = funcId;
	}

	@Override
	public Event createEvent(Unit unit) {
		if(unit.getUnitBuffs().haveWardOffBuff(this.ignoreShield)) {
			//有护盾和无敌 变成格挡
			this.addType = AtkAddType.WardOff.getType();
		}
		return new HurtEvent(unit, hurt, addType,getAtkId(),skillId,funcId,atkTimes);
	}

	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		if(this.hurt == 0) {
			return true;
		}
		boolean isHolyHurt = this.addType == AtkAddType.HolyHurt.getType();
		if(!isHolyHurt) {
			//获取增伤/减伤百分比
			double hurtAddRate = unit.getUnitBuffs().getBuffSumValue(UnitBufferType.HurtEffectBuff);
			if(this.hurt >= unit.getHp()*0.1) {
				hurtAddRate += unit.getUnitBuffs().getBuffSumValue(UnitBufferType.HurtHp10EffectBuff);
				if(this.hurt >= unit.getHp()*0.5) {
					hurtAddRate += unit.getUnitBuffs().getBuffSumValue(UnitBufferType.HurtHp80EffectBuff);
				}
			}
			hurtAddRate = Math.max(-0.9, hurtAddRate);
			this.hurt += this.hurt*hurtAddRate;
			//最终减伤
			this.hurt -= this.hurt * unit.getUnitAttr().getDoubleValue(TankAttrType.FinalReduceAtkPer);
			//最终增伤
			this.hurt += this.hurt * unit.getUnitAttr().getDoubleValue(TankAttrType.FinalAddAtkPer);

			if (doCheckTranHurtBuff(frame, unit)) {
				return false;
			}
		}
		HurtHpResult hurtHpResult = unit.getHpEngine().reduceHp(frame,getAtkId(),this.hurt,this.isLinkHurt,this.skillId,ignoreShield);
		if(hurtHpResult != null) {
			this.hurt = hurtHpResult.getHurt();
		}
		return true;
	}
	
	public void setAtkType(AtkAddType type) {
		this.addType = type.getType();
	}


	/**
	 * 转移伤害变成持续伤害
	 *
	 * @param unit
	 * @return
	 */
	public boolean doCheckTranHurtBuff(Frame frame, Unit unit) {
        if (this.hurt >= unit.getMaxHp() * 0.1d) {//超过10%
            TransHurtBuff buffers = (TransHurtBuff) unit.getUnitBuffs().getFirstBaseBufferByType(UnitBufferType.TransHurtBuff);
            if (buffers == null) {
                return false;
            }
            if (buffers.getHurtVal() == 10) {
                buffers.tranHurt(frame, unit, unit, hurt, 9322);
                return true;
            }
            if (this.hurt >= unit.getMaxHp() * 0.3d && buffers.getHurtVal() == 30) {//超过30%
                buffers.tranHurt(frame, unit, unit, hurt, 9322);
				return true;
			}
		}
		return false;
	}
}
