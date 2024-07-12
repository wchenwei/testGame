package com.hm.model.tankLimitAdapter;

import com.hm.enums.TankLimitType;
import com.hm.war.sg.setting.TankSetting;

public class TankFilterAdapter extends BaseTankLimitAdapter {

	public TankFilterAdapter(TankLimitType type,String prohibits) {
		super(type,prohibits);
	}
	@Override
	public boolean isFit(TankSetting tankSetting) {
		return this.getParams().contains(this.getType().getParam(tankSetting));
	}

}
