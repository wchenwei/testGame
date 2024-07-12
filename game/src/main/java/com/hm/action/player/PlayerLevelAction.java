package com.hm.action.player;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.player.biz.PlayerLevelBiz;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
@Biz
public class PlayerLevelAction extends AbstractPlayerAction{
	@Resource
	private PlayerLevelBiz playerLevelBiz;
	//领取
	@MsgMethod(MessageComm.C2S_Player_LevelTarget)
	public void receiveLvReward(Player player, JsonMsg msg){
		int rewardLv = player.playerLevel().getRewardLv();
		if(rewardLv>=player.playerLevel().getLv()){
			player.sendErrorMsg(SysConstant.Lv_Reward_Received);
			return;
		}
		playerLevelBiz.checkLvReward(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Player_LevelTarget);
	}
}
