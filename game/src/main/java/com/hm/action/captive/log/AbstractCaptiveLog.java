package com.hm.action.captive.log;

import com.hm.enums.CaptiveTankLogType;

public abstract class AbstractCaptiveLog {
	private int type;

	public AbstractCaptiveLog(CaptiveTankLogType type) {
		this.type = type.getType();
	}
}
