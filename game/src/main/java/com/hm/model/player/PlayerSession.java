package com.hm.model.player;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.soketserver.ISession;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.message.MessageComm;
import org.springframework.data.annotation.Transient;

public abstract class PlayerSession extends DBEntity<Long>{
	@Transient
	private transient ISession session;//玩家连接通道
	
	public ISession getSession() {
		return session;
	}
	public void setSession(ISession session) {
		this.session = session;
	}
	public boolean isOnline() {
		return session != null && session.isConnect();
	}
	
	public void sendErrorMsg(int code, String msg) {
		if(isOnline()) {
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg,code,msg);
		}
	}
	public void sendErrorMsg(int code) {
		if(isOnline()) {
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg,code);
		}
	}
	public void sendNormalErrorMsg(int msgId) {
		if(isOnline()) {
			session.sendNormalErrorMsg(msgId);
		}
	}
	
	public void sendMsg(JsonMsg msg) {
		if(isOnline()) {
			session.sendMsg(msg);
		}
	}
	public void sendMsg(int msgId) {
		if(isOnline()) {
			session.sendMsg(JsonMsg.create(msgId));
		}
	}
	public void sendMsg(int msgId,Object obj) {
		if(isOnline()) {
			session.sendMsg(msgId, obj);
		}
	}

	public void write(HMProtobuf.HMResponse response) {
		session.write(response);
	}

	public void clearSession() {
		if(session != null) {
			this.session.clearSession();
			this.session = null;
		}
	}
}
