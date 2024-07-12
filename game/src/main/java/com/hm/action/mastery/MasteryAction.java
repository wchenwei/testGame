package com.hm.action.mastery;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.MasteryConfig;
import com.hm.config.excel.templaextra.MasteryLvUpCostTemplate;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Mastery;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class MasteryAction extends AbstractPlayerAction {
	@Resource
	private MasteryConfig masterConfig;
	@Resource
	private ItemBiz itemBiz;
	
	@MsgMethod(MessageComm.C2S_Mastery_LvUp)
    public void lvUp(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		int index = msg.getInt("index");
		Mastery mastery = player.playerMastery().getMastery(id);
		if(!mastery.isCanLvUp(index)){
			player.sendErrorMsg(SysConstant.Mastery_Lv_Up_Not);
			//不满足升级条件
			return;
		}
		int lv = player.playerMastery().getLv(id, index);
		int maxLv = masterConfig.getMaxLv();
		if(lv==maxLv){
			//超出最大等级不能再升级
			return;
		}
		MasteryLvUpCostTemplate template = masterConfig.getCostTemplate(lv);
		List<Items> cost = template.getCostItems();
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Mastery.value(lv))){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		player.playerMastery().lvUp(id, index);
		player.notifyObservers(ObservableEnum.MasteryLvUp, id);
		player.notifyObservers(ObservableEnum.CHMasteryLvUp, id, index, lv, player.playerMastery().getLv(id, index), cost);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Mastery_LvUp);
	}
}
