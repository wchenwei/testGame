package com.hm.action.citywarskill;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.citywarskill.biz.CityWarSkillBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CityWarSkillConfig;
import com.hm.config.excel.templaextra.CityWarSkillBoxTemplateImpl;
import com.hm.config.excel.templaextra.CityWarSkillUpgradeTemplateImpl;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * 城战技能信息
 * @ClassName:  CityWarSkillAction   
 * @Description:
 * @author: zxj
 * @date:   2020年6月1日 下午5:28:25
 */
@Action
public class CityWarSkillAction extends AbstractPlayerAction{
	
	@Resource
	private ItemBiz itemBiz;
	
	@Resource
	private CityWarSkillConfig cityWarSkillConfig;
	
	@Resource
	private CityWarSkillBiz cityWarSkillBiz;
	
	/**
	 * 玩家偷袭、突进和撤退次数，任务奖励
	 * @Title: intoWorld   
	 * @Description: 
	 * @param player
	 * @param msg      
	 * @return: void      
	 * @throws
	 */
	@MsgMethod(MessageComm.C2S_CityWarSkill_Reward)
	public void intoWorld(Player player,JsonMsg msg){
		//领取类型
		int id = msg.getInt("id");
		CityWarSkillBoxTemplateImpl boxTemplate = cityWarSkillConfig.getCityWarSkillBox(id);
		if(null==boxTemplate) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		boolean result = cityWarSkillBiz.checkCanReward(player, boxTemplate);
		if(!result) {
			player.sendErrorMsg(SysConstant.Activity_ConditionsNot);
			return;
		}
		if(player.playerCityWarSkill().addRewardRecord(boxTemplate)) {
			List<Items> itemList = boxTemplate.getRewardItems();
			itemBiz.addItem(player, itemList, LogType.CityWarSkillReward);
			player.sendUserUpdateMsg();
			player.sendMsg(MessageComm.S2C_CityWarSkill_Reward, itemList);
			return;
		}
		player.sendErrorMsg(SysConstant.PARAM_ERROR);
	}
	
	/**
	 * 城战技能升级
	 * @Title: cityWarSkillLvup   
	 * @Description: 
	 * @param player
	 * @param msg      
	 * @return: void      
	 * @throws
	 */
	@MsgMethod(MessageComm.C2S_CityWarSkill_Lvup)
	public void cityWarSkillLvup(Player player,JsonMsg msg){
		//领取类型
		int id = msg.getInt("id");
		CityWarSkillUpgradeTemplateImpl skillUpgrade = cityWarSkillConfig.getSkillUpgradeTemplate(id);
		//上一个等级的信息（默认开启1级，所以此处skillUpgrade.getSkill_lv()，大于等于二）
		CityWarSkillUpgradeTemplateImpl beforeUpgrade = cityWarSkillConfig.getSkillUpgradeTemplate(skillUpgrade.getSkill_id(), skillUpgrade.getSkill_lv()-1);
		if(null==skillUpgrade || null==beforeUpgrade) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		
		boolean result = cityWarSkillBiz.checkCanUpgrade(player, skillUpgrade);
		if(!result) {
			player.sendErrorMsg(SysConstant.Activity_ConditionsNot);
			return;
		}
		//消耗取上一个等级的，消耗信息
		List<Items> itemList = beforeUpgrade.getCostItems();
		if(!itemBiz.checkItemEnoughAndSpend(player, itemList, LogType.CityWarSkillCost)){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		};
		player.playerCityWarSkill().upgrade(skillUpgrade);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_CityWarSkill_Lvup);
	}
	
	
}






