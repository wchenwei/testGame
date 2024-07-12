/**  
 * Project Name:SLG_GameHot.
 * File Name:AndroidPushUtil.java  
 * Package Name:com.hm.action.push.util  
 * Date:2018年7月2日下午6:03:49  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.action.push.util;

import com.hm.config.excel.temlate.PushConfigTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**  
 * ClassName: AndroidPushUtil. <br/>  
 * Function: 安卓推送的工具类. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年7月2日 下午6:03:49 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Slf4j
public class AndroidPushUtil {
//	private static Map<Integer, XingeApp> xingeAppMap= Maps.newHashMap();

	//发送android通知
	public static void sendAndroidMsg(long uid,int channelId, String titleMsg, String contentMsg) {
//		Message message = new Message();
//
//        //离线存储时间，0表示默认3天
//        message.setExpireTime(0);
//        message.setTitle(titleMsg);
//        message.setContent(contentMsg);
//        message.setType(Message.TYPE_NOTIFICATION);
//        message.setMultiPkg(1);
//        XingeApp xingeApp = getXinge(channelId);
//        if(xingeApp == null) {
//        	return;
//        }
//		JSONObject result = xingeApp.pushSingleAccount(0, ""+uid, message);
//		if(0!=result.getInt("ret_code")) {
//			log.error(String.format("玩家推送失败，%s:%s", uid, JSON.toJSONString(message)));
//		}
	}
	
	public static void sendAllAndroidMsg(String titleMsg, String contentMsg) {
//		Message message = new Message();
//        //离线存储时间，0表示默认3天
//        message.setExpireTime(0);
//        message.setTitle(titleMsg);
//        message.setContent(contentMsg);
//        message.setMultiPkg(1);
//        message.setType(Message.TYPE_NOTIFICATION);
//        xingeAppMap.values().forEach(e -> e.pushAllDevice(0, message));
	}
	
//	public static XingeApp getXinge(int channelId) {
//		return xingeAppMap.getOrDefault(channelId, xingeAppMap.get(0));
//	}
	
	public static void initApp(List<PushConfigTemplate> channelList) {
//		Map<Integer, XingeApp> tempAppMap = Maps.newConcurrentMap();
//		for (PushConfigTemplate configTemplate : channelList) {
//			tempAppMap.put(configTemplate.getChannelId(), new XingeApp(configTemplate.getSecretId(), configTemplate.getAccessKey()));
//		}
//		if(!channelList.isEmpty() && !tempAppMap.containsKey(0)) {
//			PushConfigTemplate configTemplate = channelList.get(0);
//			tempAppMap.put(0, new XingeApp(configTemplate.getSecretId(), configTemplate.getAccessKey()));
//		}
//		xingeAppMap = tempAppMap;
	}

}





