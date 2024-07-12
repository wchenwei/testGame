package com.hm.action.mission;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.mission.biz.PlayerMissionBoxBiz;
import com.hm.config.MissionBoxConfig;
import com.hm.config.MissionConfig;
import com.hm.enums.ActionType;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Action
public class PlayerMissionBoxAction extends AbstractPlayerAction{
	@Resource
	private MissionBoxConfig missionBoxConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MissionConfig missionConfig;
	@Resource
	private PlayerMissionBoxBiz playerMissionBoxBiz;
	@Resource
	private LogBiz logBiz;

	//查看当前宝箱奖励
	@MsgMethod(MessageComm.C2S_PlayerFB_ShowBox)
	public void C2S_PlayerFB_ShowBox(Player player,JsonMsg msg){
		Map<Integer,Double> itemMap= missionBoxConfig.getMissionBoxItems(player);

		List<Items> itemsList = playerMissionBoxBiz.buildBoxItemList(player,itemMap);

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFB_ShowBox);
		retMsg.addProperty("boxTime",player.playerMissionBox().getBoxTime());
		retMsg.addProperty("itemList",itemsList);
		player.sendMsg(retMsg);
	}

	@MsgMethod(MessageComm.C2S_PlayerFB_GetBox)
	public void C2S_PlayerFB_GetBox(Player player,JsonMsg msg){
		Map<Integer,Double> itemMap= missionBoxConfig.getMissionBoxItems(player);
		List<Items> itemsList = playerMissionBoxBiz.buildBoxItemList(player,itemMap);
		if(CollUtil.isEmpty(itemsList)) {
			player.sendMsg(MessageComm.S2C_PlayerFB_GetBox);
			return;
		}
		itemBiz.addItem(player,itemsList, LogType.MissionBox);

		player.notifyObservers(ObservableEnum.MissionBox);

		player.playerMissionBox().updateBoxTime();
		player.playerMissionBox().setItemMap(missionBoxConfig.lastFloat(itemMap));
		player.sendUserUpdateMsg();
		logBiz.addPlayerActionLog(player, ActionType.MissionBox, player.getId()+"");
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFB_GetBox);
		retMsg.addProperty("boxTime",player.playerMissionBox().getBoxTime());
		retMsg.addProperty("itemList",itemsList);
		player.sendMsg(retMsg);
	}

	// 查看离线宝箱
	@MsgMethod(MessageComm.C2S_PlayerFB_ShowOffLineBox)
	public void showOffLineBox(Player player,JsonMsg msg){
		List<Items> itemsList = playerMissionBoxBiz.getOffLineBoxItemList(player);
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFB_ShowOffLineBox);
		retMsg.addProperty("offLineTime",player.playerMissionBox().getOffLineBoxTime());
		retMsg.addProperty("itemList",itemsList);
		player.sendMsg(retMsg);
	}

	// 领取离线宝箱奖励
	@MsgMethod(MessageComm.C2S_PlayerFB_GetOffLineBox)
	public void getOffLineBox(Player player,JsonMsg msg){
		List<Items> itemsList = playerMissionBoxBiz.getOffLineBoxItemList(player);
		if(CollUtil.isEmpty(itemsList)) {
			player.sendMsg(MessageComm.S2C_PlayerFB_GetOffLineBox);
			return;
		}
		itemBiz.addItem(player,itemsList, LogType.MissionOffLineBox);
		player.notifyObservers(ObservableEnum.MissionOffLineBox);
		player.playerMissionBox().clearOffLineTime();
		player.sendUserUpdateMsg();

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFB_GetOffLineBox);
		retMsg.addProperty("itemList",itemsList);
		player.sendMsg(retMsg);
	}

}
