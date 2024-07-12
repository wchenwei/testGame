package com.hm.action.tank.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.excel.templaextra.StarUnlockTemplateImpl;
import com.hm.enums.TankRecastType;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankSkillConfig;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.observer.ObservableEnum;
import com.hm.war.sg.skillnew.Skill;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Biz
public class TankTechBiz {
	
	@Resource
	private TankSkillConfig tankSkillConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private TankConfig tankConfig;
	
	/**
	 * getTechAttr:(获取坦克科技的属性加成信息). <br/>  
	 * @author zxj  
	 * @param player
	 * @param tank
	 * @return  使用说明
	 */
	public TankAttr getTechAttr(Player player, Tank tank) {
		TankAttr tankAttr = new TankAttr();
		return tankAttr;
	}


	/**
	 * getTankStarSkill:(获取坦克星级的技能信息). <br/>
	 * @author zxj  
	 * @param tank
	 * @return  使用说明
	 */
	public List<Skill> getTankStarSkill(Tank tank) {
		List<Skill> skillList = Lists.newArrayList();
		StarUnlockTemplateImpl tankStarTemplate = tankConfig.getTankStarTemplate(tank.getStar());
		for(int i=1; i<= tankStarTemplate.getMaxPosition(); i++) {
			int skillId = tankConfig.getTankSetting(tank.getId()).getStarSkill(i);
			//技能开启后，默认1级，不能升级
			if(skillId>0) {
				skillList.add(new Skill(1, tankSkillConfig.getSkillSetting(skillId)));
			}
		}
		return skillList;
	}

	public Map<Integer, Integer> getTankStarSkillMap(int tankId, int star){
		Map<Integer, Integer> skillMap = Maps.newHashMap();
		if (star <= 0){
			return skillMap;
		}
		StarUnlockTemplateImpl tankStarTemplate = tankConfig.getTankStarTemplate(star);
		for(int i=1; i<= tankStarTemplate.getMaxPosition(); i++) {
			int skillId = tankConfig.getTankSetting(tankId).getStarSkill(i);
			//技能开启后，默认1级，不能升级
			if(skillId>0) {
				skillMap.put(skillId, 1);
			}
		}
		return skillMap;
	}

	public boolean canReset(Tank tank){
		return Arrays.stream(TankRecastType.values()).anyMatch(e -> e.checkRecast(tank));
	}

	public List<Items> getTankResetItems(Tank tank){
		List<Items> itemsList = Arrays.stream(TankRecastType.values()).map(e -> e.recastItems(tank)).flatMap(Collection::stream).collect(Collectors.toList());
		return ItemUtils.mergeItemList(itemsList);
	}

	public void doReset(Player player, Tank tank){
		for (TankRecastType recastType : TankRecastType.values()) {
			recastType.doRecast(tank);
		}
		player.playerTank().SetChanged();
		player.notifyObservers(ObservableEnum.TankRecast, tank.getId());
	}

}





