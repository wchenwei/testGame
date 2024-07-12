package com.hm.model.tankLimitAdapter;

import com.hm.enums.TankLimitType;

public class TankLimitFactory {
	//A=1:1,2;
	public static BaseTankLimitAdapter createProhibitAdapter(TankLimitType tankLimitType,String prohibits) {
		switch(tankLimitType){
			case TankTypeProhibit:
			case TankCampProhibit:
			case TankRareProhibit:
				return new TankFilterAdapter(tankLimitType,prohibits);
		}
		return null;
	}
	
}
