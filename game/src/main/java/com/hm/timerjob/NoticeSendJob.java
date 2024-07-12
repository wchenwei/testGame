/**  
 * Project Name:SLG_GameHot.
 * File Name:NoticeSendJob.java  
 * Package Name:com.hm.timerjob  
 * Date:2018年4月9日上午10:21:18  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.timerjob;

import com.hm.action.notice.NoticeBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.enums.NoticeTypeEnum;
import com.hm.model.notice.NewNotice;
import com.hm.servercontainer.notice.NoticeItemContainer;
import com.hm.servercontainer.notice.NoticeServerContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**  
 * ClassName: NoticeSendJob. <br/>  
 * Function: 发送广播的job. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月9日 上午10:21:18 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Slf4j
@Service
public class NoticeSendJob {
	@Resource
	private NoticeBiz noticeBiz;
	

	@Resource
	private LanguageCnTemplateConfig langeConfig;
	
	//一分钟时间
	private static long time1 = GameConstants.MINUTE;
	//活动发送的次数
	public static int activiteTimes = 10;
	
	
	@Scheduled(cron="0/10 * * * * ?")  
	public void sendJob() {
		Map<String, NewNotice> map = NoticeServerContainer.getNoticeMap();
		//System.out.println("================sendJob================"+map.keySet().size());
		for(String key : map.keySet()) {
			NewNotice notice = map.get(key);
			if(notice.getNextTime()>new Date().getTime()) {
				continue;
			}
			modifyTsNotice(key, notice);
			sendSysNotice(notice);
		}
	}
	
	/**  
	 * modifyTsNotice:(修改活动广播在容器中的信息). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 *
	 * @author zxj  
	 * @param notice  使用说明
	 *
	 */
	public void modifyTsNotice(String key, NewNotice notice) {
		NoticeItemContainer itemContainer = NoticeServerContainer.of(notice);
		if(itemContainer ==null) {
			log.error("系统异常，没有找到NoticeItemContainer：" + notice.getServerId());
		}
		//判断次数，是否需要移除
		NoticeTypeEnum type = NoticeTypeEnum.getAddType(notice.getType());
		switch (type) {
			case  SYSTS:
				if(notice.getTimes()>=notice.getMaxTimes()) {
					itemContainer.removeNoticeMap(notice.getId());
				}
				break;
			case  SYSTSBETWEENTIME:
				if(notice.getNextTime()>notice.getEndTime()) {
					itemContainer.removeNoticeMap(notice.getId());
				}
				break;
		}
		
		notice.setTimes(notice.getTimes()+1);
		long addTime = 0;
		if(notice.getInterval()<1) {
			addTime = NoticeSendJob.time1;
		}else {
			addTime = notice.getInterval()*GameConstants.MINUTE;
		}
		notice.setNextTime(notice.getNextTime()+addTime);
		notice.saveDB();
	}

	/**
	 * 
	 * addNotice:(新增一个系统消息，需要保持，需要发送到聊天，跟广播). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 *  
	 * @author zxj  
	 * @param notice  使用说明
	 *
	 */
	public void sendSysNotice(NewNotice notice){
		NoticeTypeEnum type = NoticeTypeEnum.getAddType(notice.getType());
		//超过结束时间，不再发
		if(NoticeTypeEnum.SYSTSBETWEENTIME==type && System.currentTimeMillis()>notice.getEndTime()) {
			return;
		}
		
		//给在线用户发送广播信息,ChatMsgType.Notice，不需要playerid
		noticeBiz.sendSysNoticeAndChat(0, notice);
	}
}


