package com.hm.action.push;

import com.hm.model.player.Player;
import lombok.Data;

/**
 * @Description: 推送信息
 * @author siyunlong  
 * @date 2019年1月21日 下午3:39:45 
 * @version V1.0
 */
@Data
public class PushItem {
	private long playerId;
	private long uid;
	private int channleId;
	private int serverId;
	//=====================
	private String sysType;
	private String iosToken;
	
	public PushItem(Player player) {
		this.playerId = player.getId();
		this.serverId = player.getServerId();
		this.uid = player.getUid();
		this.channleId = player.getChannelId();
		this.sysType = player.playerFix().getSysType();
		this.iosToken = player.playerFix().getIosToken();
	}
}
