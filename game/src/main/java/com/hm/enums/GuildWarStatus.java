package com.hm.enums;

import com.hm.timerjob.WarStateModel;
import lombok.Getter;

@Getter
public enum GuildWarStatus {
	None(0,"noe"),//未开启
	Running(1,"正在运行"),//正在打
	WaitCal(2,"等待结算"),//已经开启 没有开战

	;
	private GuildWarStatus(int type, String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	private String desc;

	public WarStateModel build(long endTime) {
		return new WarStateModel(type,endTime);
	}
}
