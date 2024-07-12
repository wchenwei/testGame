package com.hm.action.http;

import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.kf.kfexpedition.KfExpeditionBiz;
import com.hm.action.mission.biz.PlayerMissionBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.ServerFunctionConfig;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.server.GameServerManager;
import com.hm.util.PubFunc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("serverFunction.do")
public class ServerFunctionAction {
	@Resource
	private PlayerMissionBiz playerMissionBiz;
	@Resource
	private ServerFunctionConfig serverFunctionConfig;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private KfExpeditionBiz kfExpeditionBiz;
	@Resource
	private GuildBiz guildBiz;
	
	

	public void serverFunctionOpenCheck(HttpSession session) {
		GameServerManager.getInstance().getServerIdList().forEach(serverId ->{
			try{
				checkServerFunctionFoServer(serverId);
				session.Write("sucess");
			}catch(Exception e){
				log.error(serverId+"检查服务器功能解锁出现异常!!!!!", e);
				session.Write("fail");
			}
		});
    }

	public void checkServerFunctionFoServer(int serverId) {
		log.error("================serverId:" + serverId + ":检查服务器功能是否开启开始=================");
		int maxOpenCityId = playerMissionBiz.getPlayerMaxCity(serverId);
		int maxGuildLv = guildBiz.getGuildLvMax(serverId);
		int maxPlayerLv = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics().getMaxPlayerLv();
		serverFunctionConfig.checkFunctionOpen(serverId, maxPlayerLv, maxOpenCityId, maxGuildLv);
		log.error("================serverId:" + serverId + ":检查服务器功能是否开启结束=================");
	}

	
	public void reloadKfExpediton(HttpSession session) {
		try {
			int serverId = PubFunc.parseInt(session.getParams("serverId"));
			if(serverId == 0) {
				GameServerManager.getInstance().getServerIdList().forEach(dbId -> {
					log.error("================serverId:"+dbId+":检查服务器跨服检查开始=================");
					kfExpeditionBiz.calOldServerKf(dbId);
					log.error("================serverId:"+dbId+":检查服务器跨服检查结束=================");
				});
			}else{
				kfExpeditionBiz.calOldServerKf(serverId);
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void doServerOpen(HttpSession session) {
		serverFunctionOpenCheck(session);
		reloadKfExpediton(session);
		session.Write("sucess");
	}


}
