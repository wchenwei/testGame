package com.hm.action.captive.log;

import com.hm.enums.CaptiveTankLogType;

public class CaptiveMeLog extends AbstractCaptiveLog{
	private String name;
	private int tankId;
	
	
	public CaptiveMeLog(String name,int tankId) {
		super(CaptiveTankLogType.CaptiveMe);
		this.name = name;
		this.tankId = tankId;
	}
}
