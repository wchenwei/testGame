package com.hm.action.kf.kfexpedition;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class KfExpeditionServerResult {
	private int serverId;
	private int mailType;
	private int winServerId;
	private int failServerId;
	private Map<Integer,String> playerMap = Maps.newHashMap();
	
	public KfExpeditionServerResult(int serverId,int winServerId,int failServerId) {
		this.serverId = serverId;
		this.winServerId = winServerId;
		this.failServerId = failServerId;
	}
	
	
}
