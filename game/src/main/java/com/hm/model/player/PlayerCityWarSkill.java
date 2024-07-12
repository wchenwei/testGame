package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.excel.templaextra.CityWarSkillBoxTemplateImpl;
import com.hm.config.excel.templaextra.CityWarSkillUpgradeTemplateImpl;

import java.util.List;
import java.util.Map;

/**
 * 城战技能模块
 * @ClassName:  PlayerCityWarSkill   
 * @Description:
 * @author: zxj
 * @date:   2020年6月2日 下午3:56:05
 */
public class PlayerCityWarSkill extends PlayerDataContext{
	//任务奖励领取情况（类型：等级）
	private List<Integer> rewardReceive = Lists.newArrayList();
	//城战技能等级（类型：等级）
	private Map<Integer, Integer> skillMap = Maps.newHashMap();
	
	/**
	 * 记录玩家已经领取奖励
	 * @Title: addRewardRecord   
	 * @Description: 
	 * @param boxTemplate      
	 * @return: boolean      
	 * @throws
	 */
	public boolean addRewardRecord(CityWarSkillBoxTemplateImpl boxTemplate) {
		if(!rewardReceive.contains(boxTemplate.getId())) {
			rewardReceive.add(boxTemplate.getId());
			SetChanged();
			return true;
		}
		return false;
	}
	//升级城战技能
	public void upgrade(CityWarSkillUpgradeTemplateImpl skillUpgrade) {
		skillMap.put(skillUpgrade.getSkill_id(), skillUpgrade.getSkill_lv());
		SetChanged();
	}
	
	public Map<Integer, Integer> getSkillMap() {
		return skillMap;
	}
	
	public int getSkillLv(int skillId) {
		return skillMap.getOrDefault(skillId, 0);
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("playerCityWarSkill", this);
	}
	public boolean isReward(int skillId) {
		return rewardReceive.contains(skillId);
	}
}
