package com.hm.timerjob;

import com.hm.action.cmq.CmqMsg;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.util.TimeUtils;
import com.hm.action.cmq.CmqBiz;
import com.hm.container.PlayerContainer;
import com.hm.message.CmqMessageComm;
import com.hm.server.GameServerManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 
 * @Description: 在线玩家检查
 * @author xjt  
 * @date 2018年7月19日17:24:56 
 * @version V1.0
 */
@Service
public class OperateStaticsTimeJob {


	//每5分钟保存一次当前在线人数
	@Scheduled(cron="0 0/5 *  * * ? ")
	public void doJob() {
		if(!ServerStateCache.serverIsRun()) {
			return;
		}
		String nowDay=TimeUtils.formatSimpeTime(new Date());//日期
		String time = TimeUtils.ToStringHour(new Date());//时间
		GameServerManager.getInstance().getServerIdList().forEach(serverId ->{
			int onlineNum = PlayerContainer.getOnlinePlayersByServerId(serverId).size();
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Online_Statistics);
			cmqMsg.addProperty("serverId", serverId);
			cmqMsg.addProperty("days", nowDay);
			cmqMsg.addProperty("time", time);
			cmqMsg.addProperty("onlineNum", onlineNum);
			CmqBiz.sendDefaultMessage(serverId, cmqMsg);
		});
	}
	
}
