package com.hm.action;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import com.hm.action.kfgame.KFMessageIDS;
import com.hm.handler.SessionUtil;
import com.hm.libcore.action.BaseAction;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.util.UpdateLockUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
@Slf4j
public abstract class AbstractPlayerAction extends BaseAction{
	@Override
	public void doProcess(JsonMsg clientMsg, HMSession session) {
		Player player = SessionUtil.getPlayer(session);
		if(player == null){
			log.error("玩家不存在:处理"+clientMsg.getMsgId()+"出错");
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg,SysConstant.SESSIONKEY_ERROR);
			return;
		}
		if(KFMessageIDS.isSendKF(player,clientMsg)) {
			return;
		}
		long now = System.currentTimeMillis();
		try {
			player.playerTemp().setLastMsgTime(now);
			doProcess(clientMsg, player);
		} catch (Exception e) {
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg,SysConstant.SESSIONKEY_ERROR);
			log.error("处理"+clientMsg.getMsgId()+"出错", e);
		}
//		finally {
//			MsgLogUtils.showMsgLog(player.getId(), clientMsg);
//		}
	}
	private MethodAccess access;
	private Map<Integer,Integer> msgMethodMap = Maps.newHashMap();
	
	@Override
	public final void registerMsg() {
		initMethodAccess();
	}
	
	private void initMethodAccess() {
		this.access = MethodAccess.get(getClass());
		for (Method method : getClass().getDeclaredMethods()) {
			MsgMethod msgMethod = method.getAnnotation(MsgMethod.class);
			if(msgMethod != null) {
				int index = access.getIndex(method.getName(), Player.class, JsonMsg.class);
				int msgId = msgMethod.value();
				registerMsg(msgId);//注册消息
				this.msgMethodMap.put(msgId, index);
			}
		}
	}
	
	public final void doProcess(JsonMsg clientMsg,Player player) {
		int msgId = clientMsg.getMsgId();
		if(UpdateLockUtil.isLockUpdate(msgId)){
			return;
		}
		if(msgMethodMap.containsKey(msgId)) {
			int index = msgMethodMap.get(msgId);
			access.invoke(this, index, player, clientMsg);
			return;
		}
	}
	
}
