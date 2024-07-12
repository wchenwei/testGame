package com.hm.war.sg.buff;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.TankAttrType;
import com.hm.util.RandomUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.event.CleanEvent;
import com.hm.war.sg.event.EventType;
import com.hm.war.sg.event.ShowEvent;
import com.hm.war.sg.event.SkillReleaseEvent;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnitBuffs {
	private List<BaseBuffer> buffList = Lists.newArrayList();
	private SkillPreBuff skillPre;
	private transient Unit unit;
	
	public UnitBuffs(Unit unit) {
		this.unit = unit;
	}
	/**
	 * 删除此坦克产生的光环
	 * @param unitId
	 */
	public void removeBufferByUnit(Frame frame, int unitId) {
		List<Integer> delFuncId = Lists.newArrayList();
		for (int i = buffList.size()-1; i >= 0; i--) {
			BaseBuffer buff = buffList.get(i);
			if(buff.getDelBuffType() == 1 && buff.getBuffUnitId() == unitId) {
				buffList.remove(i);
				delFuncId.add(buff.getFuncId());
			}
		}
		if(CollUtil.isNotEmpty(delFuncId)) {
			frame.addEvent(new CleanEvent(this.unit.getId(), 0, delFuncId));
		}
	}
	public List<BaseBuffer> removeBufferByKind(BuffKind buffKind) {
		List<BaseBuffer> removeBuffs = Lists.newArrayList();
		for (int i = buffList.size()-1; i >= 0; i--) {
			BaseBuffer buff = buffList.get(i);
			if(!buff.isForoverBuff() && buff.getBuffKind() == buffKind) {
				removeBuffs.add(buffList.remove(i));
			}
		}
		return removeBuffs;
	}
	public List<BaseBuffer> removeBufferByEffectId(int effectId) {
		List<BaseBuffer> removeBuffs = Lists.newArrayList();
		for (int i = buffList.size()-1; i >= 0; i--) {
			BaseBuffer buff = buffList.get(i);
			if(buff.getEffectId() == effectId) {
				removeBuffs.add(buffList.remove(i));
			}
		}
		return removeBuffs;
	}
	
	public List<BaseBuffer> removeSendBuff(UnitBufferType buffType,int sendId) {
		List<BaseBuffer> removeBuffs = Lists.newArrayList();
		for (int i = buffList.size()-1; i >= 0; i--) {
			BaseBuffer buff = buffList.get(i);
			if(buff.getType() == buffType && buff.getBuffSendId() == sendId) {
				removeBuffs.add(buffList.remove(i));
			}
		}
		return removeBuffs;
	}

	public List<BaseBuffer> removeBuffer(UnitBufferType buffType) {
		return removeBuffer(buffType, -1);
	}

	public List<BaseBuffer> removeBuffer(UnitBufferType buffType, int delNum) {
		List<BaseBuffer> removeBuffs = Lists.newArrayList();
		for (int i = buffList.size()-1; i >= 0; i--) {
			BaseBuffer buff = buffList.get(i);
			if(!buff.isForoverBuff() && buff.getType() == buffType) {
				removeBuffs.add(buffList.remove(i));
				if (delNum > 0 && removeBuffs.size() >= delNum) {
					return removeBuffs;
				}
			}
		}
		return removeBuffs;
	}
	
	public void removeBuff(BaseBuffer buff) {
		this.buffList.remove(buff);
	}
	
	public void checkBuffOver(Frame frame) {
		for (int i = buffList.size()-1; i >= 0; i--) {
			if(buffList.get(i).isOver(frame.getId())) {
				buffList.remove(i);
			}
		}
		List<BaseBuffer> tempList = Lists.newArrayList(buffList);
		for (int i = tempList.size()-1; i >= 0; i--) {
			tempList.get(i).doEffectBuff(frame, unit);
		}
	}
	
	public List<BaseBuffer> getNotConditionBuffList() {
		return buffList.stream().filter(e -> e.getBuffTriggerCondition() == null).collect(Collectors.toList());
	}
	
	public BaseBuffer getFirstBaseBufferByType(UnitBufferType bufferType) {
		return buffList.stream().filter(e -> e.getType() == bufferType && e.isFitCondition(this.unit)).findFirst().orElse(null);
	}
	public boolean haveBuff(UnitBufferType buffType) {
		if(buffType == UnitBufferType.SkillPreBuff) {
			return skillPre != null;
		}
		return this.buffList.stream().anyMatch(e -> e.getType() == buffType && e.isFitCondition(this.unit));
	}
	public boolean haveBuff(List<UnitBufferType> buffTypes) {
		return this.buffList.stream().anyMatch(e -> buffTypes.contains(e.getType()) && e.isFitCondition(this.unit));
	}
	
	public List<BaseBuffer> getBuffList(UnitBufferType buffType) {
		return this.buffList.stream().filter(e -> e.getType() == buffType && e.isFitCondition(this.unit)).collect(Collectors.toList());
	}
	//获取buff的总值
	public double getBuffSumValue(UnitBufferType buffType) {
		return this.buffList.stream().filter(e -> e.getType() == buffType && e.isFitCondition(this.unit)).mapToDouble(BaseBuffer::getValue).sum();
	}
	
	/**
	 * 是否可以反伤
	 * @return
	 */
	public boolean isCanBackHurt() {
		return !this.buffList.stream()
				.anyMatch(e -> (e.getType() == UnitBufferType.NoDeath || e.getType() == UnitBufferType.InvincibleBuff) && e.isFitCondition(this.unit));
	}
	
	/**
	 * 是否有格挡buff
	 * @return
	 */
	public boolean haveWardOffBuff(boolean ignoreShield) {
		return 
				this.buffList.stream()
				.anyMatch(e -> (e.getType() == UnitBufferType.InvincibleBuff || !ignoreShield && e.getType() == UnitBufferType.ShieldBuff)
						&& e.isFitCondition(this.unit));
	}
	
	/**
	 * 判断是否可添加buff
	 * @param buffType
	 * @return
	 */
	public boolean isCanAddBuff(Frame frame,UnitBufferType buffType) {
		if(buffType == UnitBufferType.StunBuff) {
			if(unit.getUnitBuffs().haveBuff(UnitBufferType.NotStunBuff)) {
				frame.addEvent(new ShowEvent(this.unit, EventType.NoStunShow));
				return false;
			}
			if(unit.getUnitBuffs().isTriggerRateBuff(UnitBufferType.RateNotStunBuff)) {
				frame.addEvent(new ShowEvent(this.unit,EventType.NoStunShow));
				return false;
			}
		}else if(buffType == UnitBufferType.SilentBuff) {
			if(isTriggerRateBuff(UnitBufferType.RateNoSilentBuff)) {
				frame.addEvent(new ShowEvent(this.unit,EventType.NoSilentShow));
				return false;
			}
		}else if(UnitBufferType.isShieldBuff(buffType)) {
			if(unit.getUnitBuffs().haveBuff(UnitBufferType.NoShieldBuff)) {
				return false;
			}
		}else if(buffType == UnitBufferType.FixedBodyBuff) {
			if(isTriggerRateBuff(UnitBufferType.RateNoFixedBody)) {
				frame.addEvent(new ShowEvent(this.unit,EventType.NoFixBodyShow));
				return false;
			}
		}
		return true;
	}
	
	public void addBuff(BaseBuffer buff) {
		if(buff.getType() == UnitBufferType.SkillPreBuff) {
			this.skillPre = (SkillPreBuff)buff;
			return;
		}
		if(buff.isCleanSkillPre()) {
			this.skillPre = null;//眩晕和沉默 清除技能前摇
		}
		this.buffList.add(buff);
	}

	public SkillPreBuff getSkillPre() {
		return skillPre;
	}
	
	public void setSkillPre(SkillPreBuff skillPre) {
		this.skillPre = skillPre;
	}
	
	public boolean replaceSkillBuff(Frame frame,BaseBuffer newBuff) {
		//判断是否可以叠加
		if(newBuff.isOverlying()) {
			return true;
		}
		BaseBuffer oldBuff = getFirstBaseBufferByType(newBuff.getType());
		if(oldBuff == null) {
			return true;
		}
		if(!oldBuff.isForoverBuff() && oldBuff.isCanReplace(frame,newBuff)) {
			this.buffList.remove(oldBuff);
			return true;
		}
		return false;
	}
	
	/**
	 * 计算属性buff技能最大叠加层数
	 * @param funcId
	 * @param attrType
	 * @param maxTimes
	 */
	public void resetSkillBuffOverTimes(int funcId,long maxTimes,UnitBufferType buffType) {
		if(maxTimes <= 0) {
			return;
		}
		long curNum = buffList.stream().filter(e -> e.getType() == buffType && e.getFuncId() == funcId).count();
		if(curNum >= maxTimes) {//大于最大叠加层数，删除第一层
			for (int i = 0; i < buffList.size(); i++) {
				BaseBuffer buff = buffList.get(i);
				if(buff.getFuncId() == funcId && buff.getType() == buffType) {
					buffList.remove(i);
					return;
				}
			}
		}
	}
	
	/**
	 * 判断是否触发概率buff
	 * @param buffType
	 * @return
	 */
	public boolean isTriggerRateBuff(UnitBufferType buffType) {
		double rate = this.buffList.stream().filter(e -> e.getType() == buffType && e.isFitCondition(this.unit))
				.mapToDouble(e -> e.getValue()).sum();
		return RandomUtils.randomIsRate(rate);
	}
	
	/**
	 * 是否闪避普通攻击
	 * @return
	 */
	public boolean isDodgeNormalAtk() {
		if(isTriggerRateBuff(UnitBufferType.RateDodgeBuff)) {
			return true;
		}
		for (BaseBuffer baseBuffer : this.buffList) {
			if(baseBuffer.getType() == UnitBufferType.DodgeBuff && baseBuffer.isFitCondition(this.unit)) {
				DodgeBuff dodgeBuff = (DodgeBuff)baseBuffer;
				if(dodgeBuff.checkDodge()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Map<TankAttrType,Double> calBuffUnitAttr() {
		Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
		for (BaseBuffer baseBuffer : this.buffList) {
			baseBuffer.calBuffUnitAttr(this.unit,attrMap);
		}
		return attrMap;
	}
	
	public long calBuffMaxHp() {
		return (long)buffList.stream().filter(e -> e instanceof AttrBuff)
					     .map(e -> (AttrBuff)e)
					     .filter(e -> e.getAttrType() == TankAttrType.HP)
					     .mapToDouble((e -> e.getValue()))
					     .sum();
	}
	
	//不能释放技能
	public boolean isNotRealseSkill() {
		return this.buffList.stream()
				.anyMatch(e -> e.isCleanSkillPre() && e.isFitCondition(this.unit) || e.getType() == UnitBufferType.TauntBuff);
	}
	
	/**
	 * 队友死亡后处理我自己的buff
	 * @param frame
	 * @param deathId
	 */
	public void doUnitDeathForMe(Frame frame,int deathId) {
		List<BaseBuffer> linkBuffList = getBuffList(UnitBufferType.HurtLink);
		for (BaseBuffer baseBuffer : linkBuffList) {
			HurtLinkBuff hurtLinkBuff = (HurtLinkBuff)baseBuffer;
			if(hurtLinkBuff.checkLinkDeath(deathId)) {
				//致命连接者死亡后,重新寻找新的连接对象
				List<Unit> lifeList = this.unit.getDefGroup().getLifeUnit();
				if(lifeList.size() > 0) {
					Unit luckUnit = RandomUtils.randomEle(lifeList);
					hurtLinkBuff.changeLinkIds(deathId, luckUnit.getId());
					frame.addEvent(new SkillReleaseEvent(this.unit, hurtLinkBuff.getSkillId(), Lists.newArrayList(hurtLinkBuff.getLinkIds()), 0));
				}
			}
		}
	}
}
