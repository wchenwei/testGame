/**
 * 
 */
package com.hm.libcore.handler;

import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.protobuf.HMProtobuf;

/**
 * Title: RequestHandler.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014年11月3日 下午2:59:44
 * @version 1.0
 */
public abstract class RequestHandler {
	public abstract void messageReceived(HMSession session, HMProtobuf.HMRequest msg);
	public abstract void sessionClosed(HMSession session);
	public abstract void sessionCreated(HMSession session);
	public abstract void sessionIdel(HMSession session);
	public abstract void exceptionCaught(HMSession session) throws Exception;
}
