package com.hm.model.guild.command;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.GuildCommandType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AbstractGuildCommand {
	private int type;
	private long endTime;//到期时间
	
	public AbstractGuildCommand(GuildCommandType commandType) {
		int minute = SpringUtil.getBean(CommValueConfig.class).getCommValue(CommonValueType.GuildComandMinute);
		this.type = commandType.getType();
		this.endTime = System.currentTimeMillis() + minute*GameConstants.MINUTE;
	}

	public int getType() {
		return type;
	}

	public long getEndTime() {
		return endTime;
	}
	
	public boolean isOver() {
		return System.currentTimeMillis() > this.endTime;
	}
	
	public boolean isFitCityId(int cityId) {
		return false;
	}
}
