package com.hm.servercontainer.notice;

import com.google.common.collect.Maps;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.model.notice.NewNotice;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

import java.util.Map;

public class NoticeServerContainer{
	@Getter
	private static ContainerMap<NoticeItemContainer> serverMap =
			new ContainerMap<>(serverId -> new NoticeItemContainer(serverId));

	public static NoticeItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static NoticeItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}

	public static Map<String, NewNotice> getNoticeMap() {
		Map<String, NewNotice> allNoticeMap = Maps.newConcurrentMap();
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			NoticeItemContainer noticeItemContainer = NoticeServerContainer.of(serverId);
			if(null!= noticeItemContainer) {
				allNoticeMap.putAll(noticeItemContainer.noticeMap);
			}
		});
		return allNoticeMap;
	}
}







