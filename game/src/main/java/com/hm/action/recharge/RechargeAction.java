package com.hm.action.recharge;

import com.hm.action.AbstractPlayerAction;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerRecharge;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
import java.util.List;

@Action
public class RechargeAction extends AbstractPlayerAction{
	@Resource
	private RechargeBiz rechargeBiz;

	
	@MsgMethod(MessageComm.C2S_FreeRecharge_Test)
	public void freeCharge(Player player,JsonMsg msg) {
		if(!ServerConfig.getInstance().getUseGmOrder(player.getServerId())) {//是否可以用GM指令
			return;
		}
		int rechargeId = msg.getInt("id");
		rechargeBiz.rewardPlayer(player, rechargeId);
	}
	
	@MsgMethod(MessageComm.C2S_DayGift)
	public void receiveDayCard(Player player,JsonMsg msg) {
		int type = msg.getInt("type");
		PlayerRecharge playerRecharge = player.getPlayerRecharge();
		if(player.getPlayerRecharge().isRewardKaType(type)){
			player.sendErrorMsg(SysConstant.Day_Card_Received);
			//重复领取
			return;
		}
		if(!playerRecharge.haveYueka(type)){
			//没有购买月卡或周卡
			player.sendErrorMsg(SysConstant.Day_Card_Have_Not);
			return;
		}
		List<Items> rewards = rechargeBiz.receiveDayCard(player, type);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_DayGift, rewards);
	}

	@MsgMethod(MessageComm.C2S_CreateCPOrder)
	public void createCPOrder(Player player, JsonMsg msg) {
		String productid = msg.getString("productid");
		PayMsg payMsg = new PayMsg(player,productid);
		payMsg.saveDB();

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_CreateCPOrder);
		retMsg.cloneMsg(msg);
		retMsg.addProperty("orderId",payMsg.getId());
		player.sendMsg(retMsg);
	}

}
