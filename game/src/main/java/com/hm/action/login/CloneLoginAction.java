package com.hm.action.login;

import com.hm.libcore.action.BaseAction;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.util.aes.CryptAES;
import com.hm.action.player.PlayerBiz;
import com.hm.db.PlayerUtils;
import com.hm.enums.IdCodeFilterState;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.util.ServerUtils;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.Map;

@Action
public class CloneLoginAction extends BaseAction {

	@Resource
	private PlayerBiz playerBiz;
	
	@Override
	public void registerMsg() {
		this.registerMsg(MessageComm.C2S_CloneLogin);
	}

	@Override
	public void doProcess(JsonMsg msg, HMSession session) {
		switch (msg.getMsgId()) {
			case MessageComm.C2S_CloneLogin:
				cloneInitLogin(session, msg);
				break;
		}
	}
	
	
	public void cloneInitLogin(HMSession session, JsonMsg msg) {
		int serverId = msg.getInt("serverId");//服务器id 
		long playerId = msg.getInt("playerId");//附身的玩家id
		
		if(!ServerConfig.getInstance().getUseGmOrder(serverId)){//能否使用Gm命令
			Map<String,Object> infoMap = ServerUtils.getServerInfoForMap(serverId);
			String whitelist = infoMap.getOrDefault("whitelist", "").toString();
			if(!whitelist.contains(session.getRemoteIp())) {
				return;
			}
		}
		Player player = PlayerUtils.getPlayer(playerId);
		if(player == null) {
			return;
		}
		String imei = player.playerBaseInfo().getImei();
		String idCode = player.getIdCode();
		String bindIdCode = idCode;
		String sessionKey = createSessionKey(player.getUid(), player.getChannelId());
//		player.playerTemp().setCloneLogin(true);
		session.setAttribute("sessionKey", sessionKey);
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_CloneLogin);
		serverMsg.addProperty("imei", imei);
		serverMsg.addProperty("state", 1);
		serverMsg.addProperty("idCode", idCode);
		serverMsg.addProperty("sessionKey", sessionKey);
		serverMsg.addProperty("bindIdCode", bindIdCode);
		serverMsg.addProperty("playerId", playerId);
		serverMsg.addProperty("idCodeState", IdCodeFilterState.Normal.getType());
		session.sendMsg(serverMsg);
	}
	public String createSessionKey(long uid,int channelId){
		 long timeout = System.currentTimeMillis() + 60 * 720 * 1000;//暂定1小时超时  
		 String key = uid + "," + timeout + "," + channelId;
		 return CryptAES.AES_Encrypt("de7d553fc14f2782", key);
	}
}

