package com.hm.action.resBack;

import cn.hutool.core.collection.CollectionUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.resBack.biz.ResBackBiz;
import com.hm.config.excel.ResBackConfig;
import com.hm.enums.LogType;
import com.hm.enums.ResBackType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.ResBackMode;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class ResBackAction extends AbstractPlayerAction{
	@Resource
	private ResBackBiz resBackBiz;
	@Resource
	private ResBackConfig resBackConfig;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private ItemBiz itemBiz;
	@MsgMethod(MessageComm.C2S_ResBack)
	public void back(Player player,JsonMsg msg) {
		int id = msg.getInt("id");//召回的类型id
		int type = msg.getInt("type");//0-普通召回 1-金砖召回
		ResBackType resBackType = ResBackType.getBackType(id);
		if(resBackType==null){
			return;
		}
		ResBackMode mode = player.playerResBack().getResBackMode(resBackType);
		if(mode==null||mode.getCount()<=0){
			return;
		}
		if(!player.playerResBack().isReceive(resBackType)){
			//已找回
			return;
		}
		List<Items> cost = resBackBiz.getResBackCost(player,id,type);
		if(CollectionUtil.isEmpty(cost)){
			return;
		}
		if(!itemBiz.checkItemEnough(player, cost)){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		List<Items> rewards = resBackBiz.getBackReward(player, resBackType,type);
		if(CollectionUtil.isEmpty(rewards)){
			//找不到该奖励
			return;
		}
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.ResBack.value(id))){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		itemBiz.addItem(player, rewards, LogType.ResBack.value(id));
		player.playerResBack().resBack(id);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_ResBack, rewards);
	}
	
	@MsgMethod(MessageComm.C2S_ResBackAll)
	public void backAll(Player player,JsonMsg msg) {
		int type = msg.getInt("type");//0-普通召回 1-金砖召回
		List<ResBackType> backTypes = player.playerResBack().getBackModes();
		List<Items> cost = resBackBiz.getResBackCost(player, backTypes,type);
		if(CollectionUtil.isEmpty(cost)){
			return;
		}
		if(!itemBiz.checkItemEnough(player, cost)){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		List<Items> rewards = resBackBiz.getBackReward(player, backTypes,type);
		if(CollectionUtil.isEmpty(rewards)){
			//找不到该奖励
			return;
		}
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.ResBack)){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		itemBiz.addItem(player, rewards, LogType.ResBack);
		player.playerResBack().resBackAll(backTypes);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_ResBackAll, rewards);
	}
}
