
package com.hm.container;

import com.hm.action.push.PushItem;
import com.hm.action.push.util.PushUtil;
import com.hm.config.excel.PushConfigExcel;
import com.hm.config.excel.temlate.PushMessageTemplate;
import com.hm.enums.PushType;
import com.hm.model.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @Description: 推送信息
 * @author siyunlong  
 * @date 2019年1月21日 下午3:35:07 
 * @version V1.0
 */
@Slf4j
@Service
public class PushContainer{
	@Resource
    private PushConfigExcel pushConfig;

	
	//所有登录过的推送信息
	private Map<Long,PushItem> pushMap= new ConcurrentHashMap<>();
	
	public void addPush(Player player) {
		pushMap.put(player.getUid(), new PushItem(player));
	}
	
	public void broadPush(PushType pushType,int serverId) {
		PushMessageTemplate msgTemp = pushConfig.getPushMessageTemplate(pushType.getType());
		if(msgTemp == null) {
			return;
		}
		for (PushItem pushItem : pushMap.values()) {
			if(PlayerContainer.isOnline(pushItem.getPlayerId())) {
				continue;
			}
			if(serverId > 0 && pushItem.getServerId() != serverId) {
				continue;
			}
			PushUtil.sendPush(pushItem, msgTemp);
		}
	}
	
	public void broadPush(PushType pushType) {
		broadPush(pushType, -1);
	}
}

