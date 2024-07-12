package com.hm.model.serverpublic.serverpower;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilitaryPolicy {
	private int id;//开放期数
	private long expLimit;//获得经验上限
	private long endTime;//结束时间
	public void military(long expLimit) {
		this.id++;
		this.expLimit = expLimit;
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		this.endTime = System.currentTimeMillis()+commValueConfig.getCommValue(CommonValueType.PresidentPower_Policy_Time)*GameConstants.HOUR;
	}
	public boolean isInTime() {
		return this.endTime>System.currentTimeMillis();
	}
	public void clearData(){
		this.id = 0;
		this.expLimit = 0;
		this.endTime = 0;
	}

}
