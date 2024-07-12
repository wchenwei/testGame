package com.hm.action.kf.vo;

import lombok.Data;

@Data
public class KfExpeditionServerVo {
	private int serverId;
	private int type;
	
	public KfExpeditionServerVo(int serverId, int type) {
		super();
		this.serverId = serverId;
		this.type = type;
	}
}
