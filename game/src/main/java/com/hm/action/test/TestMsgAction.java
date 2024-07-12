package com.hm.action.test;

import com.hm.libcore.action.BaseAction;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.message.MessageComm;
import com.hm.libcore.annotation.Action;

/**
 * @Description: 用于处理ios过审包
 * @author siyunlong  
 * @date 2019年8月17日 上午1:01:48 
 * @version V1.0
 */
@Action
public class TestMsgAction extends BaseAction{
	
	@Override
	public void registerMsg() {
		this.registerMsg(MessageComm.S2C_Test_Msg0);
		this.registerMsg(MessageComm.S2C_Test_Msg1);
		this.registerMsg(MessageComm.S2C_Test_Msg2);
		this.registerMsg(MessageComm.S2C_Test_Msg3);
		this.registerMsg(MessageComm.S2C_Test_Msg4);
		this.registerMsg(MessageComm.S2C_Test_Msg5);
		this.registerMsg(MessageComm.S2C_Test_Msg6);
		this.registerMsg(MessageComm.S2C_Test_Msg7);
		this.registerMsg(MessageComm.S2C_Test_Msg8);
		this.registerMsg(MessageComm.S2C_Test_Msg9);
	}
	
	@Override
	public void doProcess(JsonMsg msg, HMSession session) {
		doTestMsg(session, msg);
	}
	
	public void doTestMsg(HMSession session, JsonMsg msg) {
		JsonMsg serverMsg = JsonMsg.create(msg.getMsgId()); 
		serverMsg.getParamMap().putAll(msg.getParamMap());
		session.sendMsg(serverMsg);
	}
}
