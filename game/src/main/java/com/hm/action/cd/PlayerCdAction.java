package com.hm.action.cd;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.cd.biz.PlayerCdBiz;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;

@Action
public class PlayerCdAction extends AbstractPlayerAction{
	@Resource
	private PlayerCdBiz playerCdBiz;
	
	
	@MsgMethod(MessageComm.C2S_PlayerCd)
	public void requestPlayerCd(Player player,JsonMsg msg){
		//检查cd是否到时
		player.getPlayerCDs().checkPlayerCd();
		//测试


		player.sendUserUpdateMsg();
	}
}
