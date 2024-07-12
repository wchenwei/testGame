/**  
 * Project Name:SLG_GameHot.
 * File Name:NoticeBiz.java  
 * Package Name:com.hm.action.notice  
 * Date:2018年4月3日上午11:21:39  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */

package com.hm.action.notice;

import com.hm.libcore.annotation.Biz;
import com.hm.config.CityConfig;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.enums.NoticeTypeEnum;
import com.hm.model.notice.NewNotice;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.notice.NoticeServerContainer;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.Map;

/**  
 * ClassName: NoticeBiz. <br/>  
 * Function: 广播消息. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月3日 上午11:21:39 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Slf4j
@Biz
public class NoticeSysBiz implements IObserver{

	@Resource
	private LanguageCnTemplateConfig langeConfig;
	@Resource
	private NoticeBiz noticeBiz;
	@Resource
	private CityConfig cityConfig;
	
	/**
	 * 系统后台发的广播
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		NoticeTypeEnum noticeEnum = NoticeTypeEnum.getAddType(observableEnum.getEnumId());
		if(noticeEnum == null || !noticeEnum.isCon()){
			return;
		}
		try {
			String contentData = "";
			NewNotice newNotice = null;
			switch (observableEnum) {
				case  SYSTS:
					Map<String, String> params = (Map<String, String>) argv[0];
					newNotice = new NewNotice(params);
					contentData = noticeEnum.getSysStr(player,observableEnum, newNotice.getContentBase());
					break;
				case  SYSTSBETWEENTIME:
					Map<String, String> params2 = (Map<String, String>) argv[0];
					newNotice = new NewNotice(params2);
				/*
				 * {broadcastid=63, endTimeStr=1577933233000, action=broadcast.do,
				 * startTimeStr=1577932631000, interval=1, type=230, m=sendGmBroadcast,
				 * serverId=1, content=555555}
				 */
					newNotice.setStartTime(Long.parseLong(params2.get("startTimeStr")));
					newNotice.setNextTime(Long.parseLong(params2.get("startTimeStr")));
					newNotice.setEndTime(Long.parseLong(params2.get("endTimeStr")));
					contentData = noticeEnum.getSysStr(player,observableEnum, newNotice.getContentBase());
					break;
					
			}
			if(newNotice == null || StringUtil.isNullOrEmpty(contentData)) {
				return;
			}
			newNotice.setContent(contentData);
			newNotice.setType(noticeEnum);
			addAndSave(newNotice.getServerId(),newNotice);
			
		} catch (Exception e) {
			log.error("发送系统广播时发生异常", e);
		}
	}
	
	/**  
	 * TODO 简单描述该方法的实现功能（可选）.  
	 * @see IObserver#registObserverEnum()
	 */
	@Override
	public void registObserverEnum() {
		 ObserverRouter.getInstance().registObserver(ObservableEnum.SYSTS, this);
		 ObserverRouter.getInstance().registObserver(ObservableEnum.SYSTSBETWEENTIME, this);
	}
	
	public void addAndSave(int serverId,NewNotice newNotice) {
		//加入到容器中
		NoticeServerContainer.of(serverId).addNoticeMap(newNotice);
	}

}















