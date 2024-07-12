package com.hm.action.player;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.PlayerFunctionConfig;
import com.hm.config.excel.WarCraftConfig;
import com.hm.config.excel.temlate.WarCraftSkillTemplate;
import com.hm.config.excel.templaextra.WarCraftSkillUpgradeTemplateImpl;
import com.hm.config.excel.templaextra.WarCraftTemplateImpl;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**  
 * ClassName: PlayerWarCraftAction. <br/>  
 * Function: 兵法接口. <br/>  
 * date: 2020年02月05日 上午11:00:13 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Action
public class PlayerWarCraftAction extends AbstractPlayerAction {
	
	@Resource
	private WarCraftConfig warCraftConfig;
	@Resource
    private ItemBiz itemBiz;
	@Resource
	private PlayerFunctionConfig playerFunctionConfig;
	
	
	/**
	 * @param player
	 * @param msg
	 * 兵法书升级
	 * #msg:2378,endLv=10
	 */
	@MsgMethod(MessageComm.C2S_WarCraft_Lvup)
	public void lvup(Player player, JsonMsg msg) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.WarCraft)) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		int endLv = msg.getInt("endLv");//要升到多少级
		int maxLv = warCraftConfig.getWarCraftMaxLv();
		//技能本身的等级
		int lv = player.playerWarcraft().getLv();
		if(endLv>maxLv||lv>=endLv){
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return; 
		}
		
		List<Items> cost = ItemBiz.createItemList(warCraftConfig.getLvUpCost(lv+1, endLv));
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.WarCraft_lvUpdate)){
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		player.playerWarcraft().lvUpdate(player, endLv);
		player.notifyObservers(ObservableEnum.WarCraftLvup, endLv, lv, cost);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_WarCraft_Lvup, true);
		
	}
	
	/**
	 * @param player
	 * @param msg
	 * 兵法书技能升级
	 * #msg:2380,type=1,endLv=5
	 */
	@MsgMethod(MessageComm.C2S_WarCraft_Skill)
	public void skillLvup(Player player, JsonMsg msg) {
		int type = msg.getInt("type");
		int endLv = msg.getInt("endLv");//要升到多少级
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.WarCraft)) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		WarCraftTemplateImpl warCraft = warCraftConfig.getWarCraftByLv(player.playerWarcraft().getLv()+1);
		WarCraftSkillTemplate warCraftSkill = warCraftConfig.getWarCraftSkill(type);
		if(null==warCraftSkill || (null!=warCraft && warCraftSkill.getBook()>warCraft.getBook())) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}

		
		/*WarCraftSkillTemplate warCraftSkill = warCraftConfig.getWarCraftSkill(type);
		if(null==warCraftSkill) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}*/
		int nowSkillLv = player.playerWarcraft().getSkillLv(type);
		WarCraftSkillUpgradeTemplateImpl nextSkill = warCraftConfig.getWarCraftSkillUp(warCraftSkill.getSkill_id(), endLv);
		if(endLv<=nowSkillLv || null==nextSkill) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		List<Items> cost = itemBiz.createItemList(warCraftConfig.getSkillLvUpCost(nowSkillLv, warCraftSkill.getSkill_id(), endLv));
		if (!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.WarCraft_SkillLvUpdate)) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
		player.playerWarcraft().skillUpdate(player, type, nextSkill.getLevel());
		player.notifyObservers(ObservableEnum.WarCraftSkillLvup, warCraftSkill, nextSkill, type, nowSkillLv, nextSkill.getLevel(), cost);
		player.sendMsg(MessageComm.S2C_WarCraft_Skill, nextSkill.getLevel());
		player.sendUserUpdateMsg();
	}
	
}



