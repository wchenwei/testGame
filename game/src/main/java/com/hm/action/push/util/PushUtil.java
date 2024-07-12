package com.hm.action.push.util;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.push.PushItem;
import com.hm.config.excel.PushConfigExcel;
import com.hm.config.excel.temlate.PushMessageTemplate;
import com.hm.enums.PushType;
import com.hm.enums.SysType;
import com.hm.model.player.Player;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class PushUtil {

	public static void sendMessage(Player player, PushType pushType) {
		PushConfigExcel msgConf = SpringUtil.getBean(PushConfigExcel.class);
		PushMessageTemplate msgTemp = msgConf.getPushMessageTemplate(pushType.getType());
		//在线的，或者配置里面关闭的，不在发送，
		if(player.isOnline()) {
			return;
		}
		//配置出错
		if(null==msgTemp) {
			log.error(String.format("推送的配置错误：%s", pushType.getType()));
			return;
		}
		//生成推送的内容
		String contentMsg = msgTemp.getMessage_txt();
		String type = player.playerFix().getSysType();
		//检验系统版本
        if(StrUtil.equals(SysType.IOS.getType(), type)) {
        	//发送ios通知
        	IosPushUtil.pushMsg(player.playerFix().getIosToken(), contentMsg);
		}else {
			//发送android通知
			AndroidPushUtil.sendAndroidMsg(player.getUid(),player.getChannelId(), msgTemp.getMessage_title(), contentMsg);
		}
	}
	
	public static void sendPush(PushItem pushItem,PushMessageTemplate msgTemp) {
		if(StrUtil.equals(SysType.IOS.getType(), pushItem.getSysType())) {
        	//发送ios通知
        	if(StringUtil.isNullOrEmpty(pushItem.getIosToken())) {
        		log.error(String.format("苹果推送失败，isotoken为空：%s", pushItem.getPlayerId()));
        	}else {
        		IosPushUtil.pushMsg(pushItem.getIosToken(), msgTemp.getMessage_txt());
        	}
		}else {
			//发送android通知
			AndroidPushUtil.sendAndroidMsg(pushItem.getUid(), pushItem.getChannleId(), msgTemp.getMessage_title(), msgTemp.getMessage_txt());
		}
	}
}










