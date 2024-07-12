package com.hm.libcore.msg;

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.gson.GSONUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 消息日志处理器
 * @author siyunlong  
 * @date 2019年9月17日 上午10:35:32 
 * @version V1.0
 */
@Slf4j
public class MsgLogUtils {
	public static boolean isShowMsgLog = false;

	static {
		try {
			isShowMsgLog = ServerConfig.getInstance().isDebug();
		} catch (Exception e) {
		}
		log.error("消息日志展示:"+isShowMsgLog);
	}
	public static void showMsgLog(long playerId, DefaultMsg clientMsg) {
		if(!isShowMsgLog) {
			return;
		}
		if(2304 == clientMsg.getMsgId()) {
			return;
		}
		try {
			log.info(playerId+"_"+clientMsg.getMsgId()+GSONUtils.ToJSONString(clientMsg.getParamMap()));
		} catch (Exception e) {
		}
	}
}
