/**  
 * Project Name:SLG_GameHot.
 * File Name:PlayerSetAction.java  
 * Package Name:com.hm.action.player  
 * Date:2018年6月28日上午11:00:13  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.action.player;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.StatisticsType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.CustomItem;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**  
 * ClassName: PlayerSetAction. <br/>  
 * Function: 玩家设置的接口. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年6月28日 上午11:00:13 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Action
public class PlayerSetAction extends AbstractPlayerAction {
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	
	@MsgMethod(MessageComm.C2S_Player_Set)
	public void set(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		int state = msg.getInt("state");
		player.playerSet().setState(id, state);
		player.notifyObservers(ObservableEnum.ChangePlayerSet, id);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Player_Set,SysConstant.YES);
	}
	
	
	@MsgMethod(MessageComm.C2S_CreatePlayerCostom)
	public void createPlayerCostom(Player player, JsonMsg msg) {
		String name = msg.getString("name");
		String tanks = msg.getString("tanks");
		if(StrUtil.isEmpty(name) || StrUtil.isEmpty(tanks)) {
			return;
		}
		if(player.playerCustomTanks().getSize() >= commValueConfig.getCommValue(CommonValueType.CustomTankMaxNum)) {
			return;
		}
		player.playerCustomTanks().addCustomItem(new CustomItem(name, tanks));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_CreatePlayerCostom);
	}
	
	@MsgMethod(MessageComm.C2S_RemovePlayerCostom)
	public void removePlayerCostom(Player player, JsonMsg msg) {
		int index = msg.getInt("index");
		player.playerCustomTanks().removeCustomItem(index);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_RemovePlayerCostom);
	}
	
	@MsgMethod(MessageComm.C2S_ChangePlayerCostomName)
	public void changePlayerCostomName(Player player, JsonMsg msg) {
		int index = msg.getInt("index");
		String name = msg.getString("name");
		if(StrUtil.isEmpty(name)) {
			return;
		}
		player.playerCustomTanks().changeCustomItemName(index,name);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_ChangePlayerCostomName);
	}
	
	@MsgMethod(MessageComm.C2S_IosUpdateReward)
	public void iosUpdateReward(Player player, JsonMsg msg) {
		if(player.getPlayerStatistics().getLifeStatistics(StatisticsType.IosUpdateReward) > 0) {
			player.sendMsg(MessageComm.S2C_IosUpdateReward);
			return;
		}
		List<Items> itemList = commValueConfig.getListItem(CommonValueType.IosUpdateReward);
		if(CollUtil.isEmpty(itemList)) {
			player.sendMsg(MessageComm.S2C_IosUpdateReward);
			return;
		}
		itemBiz.addItem(player, itemList, LogType.IosUpdateRward);
		
		player.getPlayerStatistics().addLifeStatistics(StatisticsType.IosUpdateReward);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_IosUpdateReward);
		serverMsg.addProperty("itemList", itemList);
		player.sendMsg(serverMsg);
	}
}















