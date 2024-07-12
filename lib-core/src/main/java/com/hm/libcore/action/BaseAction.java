
package com.hm.libcore.action;

import com.hm.libcore.msg.Router;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: BaseAction.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014年11月6日 下午2:03:46
 * @version 1.0
 */
@Slf4j
public abstract class BaseAction implements IAction {

	
	public void registerMsg(int msgId) {
		Router.getInstance().registMsg(msgId, this);
	}
	
}

