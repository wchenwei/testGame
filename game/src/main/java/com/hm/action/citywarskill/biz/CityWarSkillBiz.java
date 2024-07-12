package com.hm.action.citywarskill.biz;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.CityWarSkillConfig;
import com.hm.config.excel.temlate.CityWarSkillTemplate;
import com.hm.config.excel.templaextra.CityWarSkillBoxTemplateImpl;
import com.hm.config.excel.templaextra.CityWarSkillUpgradeTemplateImpl;
import com.hm.enums.CityWarSkillEnum;
import com.hm.enums.StatisticsType;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 城战技能
 * @ClassName:  WarCitySkillBiz   
 * @Description:
 * @author: zxj
 * @date:   2020年6月1日 下午5:25:52
 */
@Biz
public class CityWarSkillBiz implements IObserver{
	
	@Resource
	private CityWarSkillConfig cityWarSkillConfig;
	
	/**
	 * 校验是否能领取奖励
	 * @Title: checkCanReward   
	 * @Description: 
	 * @param player
	 * @param boxTemplate
	 * @return: boolean      
	 * @throws
	 */
	public boolean checkCanReward(Player player, CityWarSkillBoxTemplateImpl boxTemplate) {
		if(player.playerCityWarSkill().isReward(boxTemplate.getId())) {
			return false;
		}
		StatisticsType statType = CityWarSkillEnum.getType(boxTemplate.getSkill_id()).getStatisticsType();
		//已经获取次数
		return player.getPlayerStatistics().getLifeStatistics(statType)>=boxTemplate.getNum();
	}
	
	/**
	 * 判断是否可以升级
	 * @Title: checkCanUpgrade   
	 * @Description: 
	 * @param player
	 * @param skillUpgrade
	 * @return      
	 * @return: boolean      
	 * @throws
	 */
	public boolean checkCanUpgrade(Player player, CityWarSkillUpgradeTemplateImpl skillUpgrade) {
		int skillId = skillUpgrade.getSkill_id();
		int nextLv = player.playerCityWarSkill().getSkillLv(skillId)+1;
		return skillUpgrade.getSkill_lv()==nextLv 
				&& player.playerMission().getOpenCity()>=skillUpgrade.getUnlock_city();
	}


	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.TroopAdvance, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TroopRetreat, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PvpOneByOneLaunch, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.RecaptureCity, this);
	}
	/**
	 * 获取偷袭，突进的技能，以及等级
	 * @Title: getSkillAndLv   
	 * @Description: 
	 * @param player
	 * @param skillType
	 * @return      
	 * @return: Map<Integer,Integer>(技能id，等级)      
	 * @throws
	 */
	public Map<Integer, Integer> getSkillAndLv(Player player, CityWarSkillEnum skillType) {
		Map<Integer, Integer> result = Maps.newHashMap();
		Map<Integer, Integer> skillMap = player.playerCityWarSkill().getSkillMap();
		skillMap.forEach((skillId, lv)->{
			CityWarSkillUpgradeTemplateImpl upgradeTemplate = cityWarSkillConfig.getSkillUpgradeTemplate(skillId, lv);
			if(null!=upgradeTemplate && skillType.getType()==skillId
					&& upgradeTemplate.getUpgrade_buff()>0) {
				result.put(upgradeTemplate.getUpgrade_buff(), lv);
			}
		});
		return result;
	}
	/**
	 * 获取“撤退”的加血
	 * @Title: getSkillAddHp   
	 * @Description: 
	 * @param player
	 * @return      
	 * @return: int      
	 * @throws
	 */
	public int getSkillAddHp(Player player) {
		int skillLv = player.playerCityWarSkill().getSkillLv(CityWarSkillEnum.TroopRetreat.getType());
		if(skillLv<=0) {
			return 0;
		}
		CityWarSkillUpgradeTemplateImpl upgradeTemplate = cityWarSkillConfig.getSkillUpgradeTemplate(CityWarSkillEnum.TroopRetreat.getType(), skillLv);
		return null==upgradeTemplate?0:upgradeTemplate.getUpgrade_effect();
	}
	

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case TroopAdvance:
				player.getPlayerStatistics().addLifeStatistics(StatisticsType.TroopAdvance);
				break;
			case TroopRetreat:
				player.getPlayerStatistics().addLifeStatistics(StatisticsType.TroopRetreat);
				break;
			case PvpOneByOneLaunch:
				player.getPlayerStatistics().addLifeStatistics(StatisticsType.PvpOneByOneLaunch);
				break;
			case RecaptureCity:
				int cityId = Integer.parseInt(argv[0].toString());
				this.checkOpenSkill(player, cityId);
				break;
			default:
				break;
		}
	}
	/**
	 * 校验玩家是否要开启城战技能
	 * @Title: checkOpenSkill   
	 * @Description: 
	 * @param player
	 * @param cityId      
	 * @return: void      
	 * @throws
	 */
	public void checkOpenSkill(Player player, int cityId) {
		List<CityWarSkillTemplate> skillList = cityWarSkillConfig.getSkillTemplate();
		skillList.forEach(e->{
			if(cityId>=e.getUnlock_city() && player.playerCityWarSkill().getSkillLv(e.getId())<=0) {
				CityWarSkillUpgradeTemplateImpl skillUpgrade =cityWarSkillConfig.getSkillUpgradeTemplate(e.getId(), 1);
				player.playerCityWarSkill().upgrade(skillUpgrade);
			}
		});
	}
}












