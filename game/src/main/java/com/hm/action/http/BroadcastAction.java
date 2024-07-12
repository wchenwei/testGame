/**  
 * Project Name:SLG_GameHot.
 * File Name:BroadcasetAction.java  
 * Package Name:com.hm.action.http  
 * Date:2018年5月17日上午9:17:56  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.action.http;

import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.model.notice.NewNotice;
import com.hm.model.player.Player;
import com.hm.observer.IObservable;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.notice.NoticeItemContainer;
import com.hm.servercontainer.notice.NoticeServerContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import com.hm.libcore.annotation.Action;
import org.springframework.stereotype.Service;

import java.util.Map;

/**  
 * ClassName: BroadcastAction. <br/>  
 * Function: 处理gm发过来的http请求，发送广播信息. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年5月17日 上午9:17:56 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Slf4j
@Service("broadcast.do")
public class BroadcastAction implements IObservable{
	

	private final static String SUCC = "succ"; 
	private final static String Error = "error"; 

	
	//发送系统广播
	public void sendGmBroadcast(HttpSession session) {
		Map<String, String> params = session.getParams();
		try {
			int type = Integer.parseInt(params.get("type"));
			//localhost:8096/?action=http.do&m=insertNewNotice&content=测试数据&startTime=2018-04-08 12:12:12&endTime=2018-04-09 14:20:20&type=100
			this.notifyChanges(ObservableEnum.getObservableEnum(type), null, params);
			session.Write(BroadcastAction.SUCC);
		} catch (Exception e) {
			log.error("发送广播消息失败：", e);
			session.Write(BroadcastAction.Error);
		}
	}
	
	public void delGmBroadcast(HttpSession session) {
		Map<String, String> params = session.getParams();
		
		int id = Integer.parseInt(params.get("broadcastid"));
		Map<String, NewNotice> map = NoticeServerContainer.getNoticeMap();
		for(String key : map.keySet()) {
			try {
				NewNotice notice = map.get(key);
				if(notice.getBroadcastid()==id) {
					NoticeItemContainer itemContainer = NoticeServerContainer.of(notice);
					if(null!=itemContainer) {
						itemContainer.removeNoticeMap(notice.getId());
					}else {
						log.error("系统异常，没有找到NoticeItemContainer：" + notice.getServerId());
					}
				}
			} catch (Exception e) {
				log.error("删除广播消息失败：", e);
				session.Write(BroadcastAction.Error);
			}
		}
		session.Write(BroadcastAction.SUCC);
		
	}
	
	

	/**  
	 * TODO 简单描述该方法的实现功能（可选）.  
	 * @see IObservable#notifyChanges(ObservableEnum, Player, Object[])
	 */
	@Override
	public void notifyChanges(ObservableEnum observableEnum, Player player, Object... argv) {
		ObserverRouter.getInstance().notifyObservers( observableEnum, player, argv );
	}
}
