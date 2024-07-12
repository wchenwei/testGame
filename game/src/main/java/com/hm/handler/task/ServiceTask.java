/**
 * 
 */
package com.hm.handler.task;

import com.hm.libcore.actor.IRunner;
import com.hm.libcore.msg.Router;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.message.MessageComm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * Title: ServiceTask.java Description:业务线程池处理的任务 Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * 
 * @author 叶亮
 * @date 2015年8月6日 下午1:45:40
 * @version 1.0
 */
@Slf4j
public class ServiceTask implements Runnable  , IRunner {


	private HMSession session;
	private HMRequest req;

	public ServiceTask(HMSession session, HMRequest req) {
		this.session = session;
		this.req = req;
	}

	@Override
	public void run() {
		debugRecv();
		try {
			int msgId = this.req.getMsgId();
			if(msgId == MessageComm.C2S_Player_Heart) {
				Router.getInstance().process(req, session);
			}else{
				synchronized (this.session) {
					long start = System.currentTimeMillis();
					Router.getInstance().process(req, session);
					long runTime = System.currentTimeMillis() - start;
					if(runTime>200) {
						log.info("msgID:"+msgId+"  cost:" + (System.currentTimeMillis() - start) + "ms");
					}
				}
			}
		} catch (Throwable e) {
			log.error("msgID:"+this.req.getMsgId()+" execute task thread:" + Thread.currentThread().getName() + " error", e);
		}

	}
	
	private void debugRecv(){
		if(ServerConfig.getInstance().isDebug()){
			if(this.req.getMsgId() != MessageComm.C2S_Player_Heart) {
				long playerId = 0;
				if(session.getAttribute("playerId") != null){
					playerId = (long)session.getAttribute("playerId");
				}
				log.info(session.getRemoteAddress()+" playerId="+playerId+" getMsg:// msgID:"+this.req.getMsgId()+"=" + this.req.getData().toStringUtf8());
			}
		}
	}
	@Override
	public Object runActor() {
		debugRecv();

		int msgId = this.req.getMsgId();
		long start = System.currentTimeMillis();
		Router.getInstance().process(req, session,false);
		long runTime = System.currentTimeMillis() - start;
		if(runTime>200) {
			log.info("msgID:"+msgId+"  cost:" + (System.currentTimeMillis() - start) + "ms");
		}
		return null;
	}
}
