package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;

/**
 * @Description: 玩家等级相关
 * @author siyunlong  
 * @date 2017年12月15日 上午10:53:24 
 * @version V1.0
 */
public class PlayerLevel extends PlayerDataContext{
	private int lv;//玩家等级
	private long lvExp;//玩家当前等级经验
	private int warGodLv; //战神等级
	private int rewardLv;//玩家已领取的奖励的等级
	private boolean isSendReward;//是否发放奖励
	
	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
		SetChanged();
	}
	/**  
	 * lvUp:升级方法 
	 *  
	 * @author zqh  
	 * @param player
	 * @param maxLv 最大等级
	 * @return  boolean 是否升级
	 *
	 */
	public boolean lvUp(Player player,int maxLv) {
		if (this.lv >= maxLv){
            this.lv = maxLv;
            return false;
        }
		this.lv++;
		SetChanged();
		return true;
	}

	public int getWarGodLv() {
		return warGodLv;
	}

	public void setWarGodLv(int warGodLv) {
		this.warGodLv = warGodLv;
		SetChanged();
	}

	public int getRewardLv() {
		return rewardLv;
	}
	public void syncRewardLv() {
		this.rewardLv = this.lv;
		SetChanged();
	}

	public long getLvExp() {
		return lvExp;
	}

	public void setLvExp(long lvExp) {
		this.lvExp = lvExp;
		SetChanged();
	}
	
	public boolean isSendReward() {
		return isSendReward;
	}

	public void setSendReward(boolean isSendReward) {
		this.isSendReward = isSendReward;
		SetChanged();
	}

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("playerLevel", this);
	}

}
