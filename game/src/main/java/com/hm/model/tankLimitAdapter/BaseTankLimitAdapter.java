package com.hm.model.tankLimitAdapter;

import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.TankConfig;
import com.hm.enums.TankLimitType;
import com.hm.war.sg.setting.TankSetting;
import lombok.Data;

import java.util.List;

@Data
public abstract class BaseTankLimitAdapter {
	private TankLimitType type;
	private List<Integer> params;
	public abstract boolean isFit(TankSetting tankSetting);
	
	public boolean isFit(int tankId) {
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		if(tankSetting==null){
			return false;
		}
		return isFit(tankSetting);
	}

	public BaseTankLimitAdapter(TankLimitType type,String prohibits) {
		super();
		this.params = StringUtil.splitStr2IntegerList(prohibits, ",");
		this.type = type;
	}
	
}
