/**
 * 
 */
package com.hm.action.sys;

import com.hm.action.login.biz.LoginBiz;
import com.hm.libcore.annotation.Facder;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.enums.LeaveOnlineType;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;

import javax.annotation.Resource;

/**
 * Title: SysFacade.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年7月22日 上午10:09:25
 * @version 1.0
 */
@Facder("SysFacade")
public class SysFacade {
	private boolean isClose = false;
	@Resource
	private LoginBiz loginBiz;
	
	public void changeServerState(boolean isClose){
		this.isClose = isClose;
	}
	
	public boolean getServerState(){
		return this.isClose;
	}
	
	
	public void sendLeavePlayer(HMSession session,LeaveOnlineType type) {
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_LeaveOnline);
		msg.addProperty("type", type.getType());
		session.sendMsg(msg);
	}

	public void sendLeavePlayer(Player player, LeaveOnlineType type) {
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_LeaveOnline);
		msg.addProperty("type", type.getType());
		player.sendMsg(msg);

		loginBiz.doLoginOut(player);
	}
}
