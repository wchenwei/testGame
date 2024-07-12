package com.hm.servercontainer.notice;

import com.google.common.collect.Maps;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.notice.NewNotice;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class NoticeItemContainer extends ItemContainer{

	public Map<String, NewNotice> noticeMap = Maps.newConcurrentMap();//广播信息，主要用于系统gm的广播缓存
	public NoticeItemContainer(int serverId) {
		super(serverId);
	}
	
	@Override
	public void initContainer() {
		try {
			log.info( "加载公告开始:" +this.getServerId());
			RedisMapperUtil.queryListAll(getServerId(), NewNotice.class).forEach(notice -> this.noticeMap.put(notice.getId(), notice));
			log.info( "加载公告结束:" +this.getServerId() );
		} catch (Exception e) {
			log.error("公告加载出错:"+this.getServerId(), e);
		}
	}
	
	public void addNoticeMap(NewNotice notice) {
		notice.setServerId(getServerId());
		this.noticeMap.put( notice.getId(), notice );
		notice.saveDB();
	}
	public void removeNoticeMap(String key) {
		NewNotice notice = this.noticeMap.remove(key);
		if(notice != null) {
			notice.delete();
		}
	}
	public NewNotice getNewNoticeById(String id) {
		return this.noticeMap.get( id );
	}
	
}
