package com.hm.action.http;

import cn.hutool.core.thread.ThreadUtil;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.sys.SysFacade;
import com.hm.actor.ActorDispatcherType;
import com.hm.actor.OnceQueueTaskType;
import com.hm.cache.PlayerCacheManager;
import com.hm.chsdk.event2.pack.CHPackEventBiz;
import com.hm.container.PlayerContainer;
import com.hm.enums.LeaveOnlineType;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.handler.ServerStateCache.ServerState;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.log.ali.AliLogProducerUtil;
import com.hm.main.CreateServerFactory;
import com.hm.model.player.Player;
import com.hm.mongoqueue.MongoQueue;
import com.hm.mq.msg.MqManager;
import com.hm.servercontainer.worldbuild.WorldBuildTroopServerContainer;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("http.do")
public class HttpRequestHandlerAction {

	@Resource
    private LoginBiz loginBiz;
	@Resource
	private SysFacade sysFacade;
	

	public void stopServer(HttpSession session) {
		//关闭消息接受
		ServerStateCache.changeServerState(ServerState.Close);
		MongoQueue.closeQueue();
		//把所有玩家踢下线
		log.error("==============踢出所有玩家==============");
		for (Player player : PlayerContainer.getOnlinePlayers()) {
			sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
		}
		CHPackEventBiz.stopServer();
		ActorDispatcherType.stopAll();
		OnceQueueTaskType.stopAll();

		ThreadUtil.sleep(5000);

		WorldBuildTroopServerContainer.saveAll();
		PlayerCacheManager.getInstance().clearAllCache();
		//关闭mq
		log.info("stop mq");
		MqManager.getInstance().shutdown();
		AliLogProducerUtil.close();

		session.Write("==========game server is closed========");
		log.error("==============服务器正常关闭==============");
	}
	
	
	public void stopOneServer(HttpSession session) {
        List<Integer> serverIdList = StringUtil.splitStr2IntegerList(session.getParams().get("serverId"), ",");
		//把所有玩家踢下线
        for (Integer serverId : serverIdList) {
            for (Player player : PlayerContainer.getOnlinePlayersByServerId(serverId)) {
                sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
            }
		}
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
        for (Integer serverId : serverIdList) {
            CreateServerFactory.stopServer(serverId);
            log.error("==========game server is closed is" + serverId + "========");
            log.error(serverId + "==============服务器正常关闭==============");
        }
        session.Write("==========game server is closed========:" + GSONUtils.ToJSONString(serverIdList));
	}
}


