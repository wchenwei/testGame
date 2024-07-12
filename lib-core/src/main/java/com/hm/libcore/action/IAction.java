
package com.hm.libcore.action;

import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.soketserver.handler.HMSession;

/**
 * Title: IAction.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014年11月6日 下午2:02:21
 * @version 1.0
 */
public interface IAction {
	void registerMsg();
	void doProcess(JsonMsg clientMsg, HMSession session);
}

