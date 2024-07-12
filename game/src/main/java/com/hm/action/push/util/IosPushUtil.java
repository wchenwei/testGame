/**  
 * Project Name:SLG_GameHot.
 * File Name:IosPustUtil.java  
 * Package Name:com.hm.action.push.util  
 * Date:2018年6月29日上午11:22:05  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */
package com.hm.action.push.util;

import com.hm.action.push.conf.PushConfig;
import com.hm.config.PushSetConfig;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**  
 * ClassName: IosPustUtil. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年6月29日 上午11:22:05 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Slf4j
public class IosPushUtil {
	

	private static PushNotificationManager pushManager = null;
	
	private static String certifileFile = "config/";
	
	//发送苹果推送
	public static void pushMsg(String token, String content) {
		if(token.contains("null") || token.length() < 10) {
			return;
		}
		PushNotificationPayload payLoad = new PushNotificationPayload();
		payLoad.addAlert(content); // 消息内容
		payLoad.addBadge(PushSetConfig.badge); // iphone应用图标上小红圈上的数值
		
		Device device = new BasicDevice();
		device.setToken(token);
		try {
			PushedNotification notification = getPushManager().sendNotification(device, payLoad, true);
			if(!notification.isSuccessful()) {
//				log.error(String.format("推送苹果玩家失败：%s", JSON.toJSONString(notification)));;
			}
		} catch (Exception e) {
//			log.error(String.format("苹果发送消息异常：%s====%s", content, token) , e);
		}
	}
	//初始化推送的manager，这里暂时没有关闭的地方（）
	public static PushNotificationManager getPushManager() throws Exception {
        if(null==pushManager){
            synchronized (IosPushUtil.class){
            	boolean pushEv = PushConfig.isIosPushEv();
            	String pass = PushConfig.getCertificatePassword();
            	String filename = PushConfig.getCertificateFile();
            	String filePath = IosPushUtil.class.getResource("/").toURI().getPath()+certifileFile+filename;
            	log.info(String.format("推送环境配置：%s;%s;%s", pushEv, pass, filePath));
                if(null==pushManager){
                	pushManager = new PushNotificationManager();
        			pushManager.initializeConnection(
        					new AppleNotificationServerBasicImpl(filePath, pass, true));
                }
            }
        }
		return pushManager;
	}
	
}











