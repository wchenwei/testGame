package com.hm.war.sg.buff;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.skilltrigger.BaseTriggerSkill;
import com.hm.war.sg.unit.Unit;
import lombok.Data;

import java.util.Map;

@Data
public abstract class BaseBuffer {
	private UnitBufferType type;
	protected long endFrame;
	private BuffKind buffKind;
	protected int funcId;//所属技能functionid
	protected int skillId;//技能id
	private double value;
	protected BaseTriggerSkill buffTriggerCondition;//buff的生效条件
	private int buffUnitId;//谁给我上的buff
	private int delBuffType;//0-不删除  1-释放者死亡后删除
	private int effectId;//效果id

	public BaseBuffer(UnitBufferType type,long endFrame,BuffKind buffKind) {
		this.type = type;
		this.endFrame = endFrame;
		this.buffKind = buffKind;
	}

	public boolean isOver(long curFrame) {
		return endFrame > 0 && curFrame >= endFrame;
	}
	
	public boolean isForoverBuff() {
		return endFrame < 0;
	}
	

	public void setBuffKind(BuffKind buffKind) {
		this.buffKind = buffKind;
	}
	
	public int getBuffUnitId() {
		return buffUnitId;
	}

	public void setBuffUnitId(int buffUnitId) {
		this.buffUnitId = buffUnitId;
	}

	//是否技能失效
	public boolean isCleanSkillPre() {
		return false;
	}
	/**
	 * 是否是此坦克的所属的永久光环
	 * @param unitId
	 * @return
	 */
	public boolean isBelongUnitForverBuff(int unitId) {
		return false;
	}
	
	/**
	 * 是否能替换技能，用于眩晕，沉默，护盾
	 * @param newBuff
	 * @return
	 */
	public boolean isCanReplace(Frame frame,BaseBuffer newBuff) {
		return false;
	}
	
	public void calBuffUnitAttr(Unit unit,Map<TankAttrType,Double> attrMap) {
		
	}
	
	/**
	 * 获取buff的发出者
	 * @return
	 */
	public int getBuffSendId() {
		return -1;
	}
	
	/**
	 * 处理buff参数的影响 用于 持续回复/伤害
	 */
	public void doEffectBuff(Frame frame,Unit unit) {
		
	}
	
	public void setBuffTriggerCondition(BaseTriggerSkill buffTriggerCondition) {
		this.buffTriggerCondition = buffTriggerCondition;
	}
	
	public BaseTriggerSkill getBuffTriggerCondition() {
		return buffTriggerCondition;
	}

	/**
	 * 是否满足条件
	 * @return
	 */
	public boolean isFitCondition(Unit unit) {
		return buffTriggerCondition == null || buffTriggerCondition.isCanTrigger(unit, unit,getBuffSendId());
	}
	
	/**
	 * 是否可叠加,数值叠加
	 * @return
	 */
	public boolean isOverlying() {
		return false;
	}
	//破裂时触发
	public void doRemoveAction(Frame frame,Unit unit) {
		
	}

}
