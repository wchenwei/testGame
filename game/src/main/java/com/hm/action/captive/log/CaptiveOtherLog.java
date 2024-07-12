package com.hm.action.captive.log;

import com.hm.enums.CaptiveTankLogType;

public class CaptiveOtherLog extends AbstractCaptiveLog{
	private String name;
	private int tankId;
	
	
	public CaptiveOtherLog(String name,int tankId) {
		super(CaptiveTankLogType.CaptiveOther);
		this.name = name;
		this.tankId = tankId;
	}
}
