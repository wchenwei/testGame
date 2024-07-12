package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.BattleType;
import com.hm.enums.BuffType;
import com.hm.model.buff.Buff;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家buff信息
 * @author siyunlong  
 * @date 2018年1月8日 下午4:36:39 
 * @version V1.0
 */
public class PlayerBuffer extends PlayerDataContext{
	private ConcurrentHashMap<Integer, Buff> buffMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, PveLoseBuff> pveMap = new ConcurrentHashMap<>();

	
	/**
	 * 重置buff时间
	 * @param buffType
	 * @param buffSecond buff时间
	 */
	public void resetBuff(BuffType buffType,long buffSecond,double buffValue) {
		Buff buff = new Buff(buffType);
		buff.addBufferSecond(buffSecond);
		buff.setValue(buffValue);
		this.buffMap.put(buffType.getType(), buff);
		SetChanged();
	}
	
	public boolean haveBuff(BuffType buffType) {
		return getBuffByType(buffType) != null;
	}
	
	public double getBuffValue(BuffType buffType) {
		Buff buff = getBuffByType(buffType);
		return buff != null ? buff.getValue():0;
	}

	
	public Buff getBuffByType(BuffType buffType) {
		Buff buff = this.buffMap.get(buffType.getType());
		if(buff != null && buff.isOver()) {
			clearBuff(buffType);
			return null;
		}
		return buff;
	}
	
	public void clearBuff(BuffType buffType) {
		this.buffMap.remove(buffType.getType());
		SetChanged();
	}

	public PveLoseBuff getPveBuff(BattleType type) {
		return this.pveMap.get(type.getType());
	}
	public void removePveBuff(BattleType type) {
		this.pveMap.remove(type.getType());
		SetChanged();
	}
	public void addPveBuff(PveLoseBuff loseBuff) {
		this.pveMap.put(loseBuff.getType(),loseBuff);
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		// 清理过期buff
		if (buffMap.values().removeIf(Buff::isOver)) {
			SetChanged();
		}
		msg.addProperty("playerBuffs", Lists.newArrayList(buffMap.values()));
	}
	
	
}
