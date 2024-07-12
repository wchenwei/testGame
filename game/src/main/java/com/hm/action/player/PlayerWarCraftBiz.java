package com.hm.action.player;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.WarCraftConfig;
import com.hm.config.excel.temlate.WarCraftSkillTemplate;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankType;
import com.hm.model.player.Player;
import com.hm.model.tank.TankAttr;
import com.hm.war.sg.setting.SkillSetting;
import com.hm.war.sg.skillnew.Skill;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zxj
 * 2020年2月6日09:27:47
 * 兵法biz
 */
@Slf4j
@Biz
public class PlayerWarCraftBiz {
	@Resource
	private WarCraftConfig warCraftConfig;
	@Resource
	private TankSkillConfig tankSkillConfig;
	

	public void initWarCraftLv(Player player, Map<Integer, TankAttr> totalMap) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.WarCraft)){
			return;
		}
		int lv = player.playerWarcraft().getLv();
		for(TankType type:TankType.values()){
			TankAttr attr = totalMap.getOrDefault(type.getType(), new TankAttr());
			attr.addAttr(warCraftConfig.getTankAttrByLv(type.getType(), lv));
			totalMap.put(type.getType(), attr);
		}
	}
	
	/**
	 * 获取兵法技能-随机一个坦克添加
	 * @param player
	 * @return
	 */
	public List<Skill> getWarCraftSkillList(Player player) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.WarCraft)){
			return Lists.newArrayList();
		}
		List<Skill> skillList = Lists.newArrayList();
		Map<Integer, Integer> skillMap = player.playerWarcraft().getSkillMap();
		for (Map.Entry<Integer, Integer> entry : skillMap.entrySet()) {
			int skillType = entry.getKey();
			int skillLv = entry.getValue();
			WarCraftSkillTemplate warCraftSkill = warCraftConfig.getWarCraftSkill(skillType);
			if(skillLv > 0 && warCraftSkill != null) {
				int skillId = warCraftSkill.getSkill_id();
				SkillSetting skillSetting = tankSkillConfig.getSkillSetting(skillId);
				if(skillSetting != null) {
					skillList.add(new Skill(skillLv, skillSetting));
				}else{
					log.error("兵法技能不存在:"+skillType);
				}
			}
		}
		return skillList;
	}
	
}
