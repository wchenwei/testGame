
package com.hm.libcore.action;


import com.hm.libcore.msg.Router;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

public abstract class BaseKFAction implements IAction {
	@Getter
	private static final org.slf4j.Logger log
			= org.slf4j.LoggerFactory.getLogger(BaseKFAction.class);

	public void registerMsg(int msgId) {
		Router.getInstance().registMsg(msgId, this);
	}


}

