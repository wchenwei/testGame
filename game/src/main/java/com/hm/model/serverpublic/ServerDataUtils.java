package com.hm.model.serverpublic;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.mission.biz.PlayerMissionBiz;
import com.hm.config.CityConfig;
import com.hm.config.excel.templaextra.CityTemplate;

public class ServerDataUtils {
	public static int calBaseLv(int serverId) {
    	CityConfig cityConfig = SpringUtil.getBean(CityConfig.class);
		CityTemplate cityTemplate = cityConfig.getCityById(1);
		//max[0， min（昨日基础等级 +1，玩家平均等级 - 小城差值 - 小城初始值）]
		ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics();
		int serverLv = serverStatistics.getServerLv();
		int yesLv = serverStatistics.getBaseNpcLv();
		return Math.max(0, Math.min(yesLv + 1, serverLv-cityTemplate.getNpcLv()-cityTemplate.getNpc_level_diff()));
	}
	
	public static void checkFirstBaseLv(ServerData serverData) {
		ServerStatistics serverStatistics = serverData.getServerStatistics();
		if(serverStatistics.getBaseNpcLv() <= 0 && serverStatistics.getServerLv() > 0) {
			WorldBiz worldBiz = SpringUtil.getBean(WorldBiz.class);
			worldBiz.calFirstBaseLv(serverData.getServerId());
		}
		if(serverStatistics.getMaxMissionId() <= 0) {
			PlayerMissionBiz playerMissionBiz = SpringUtil.getBean(PlayerMissionBiz.class);
			serverStatistics.setMaxMissionId(playerMissionBiz.getPlayerMaxMissionId(serverData.getServerId()));
			serverStatistics.save();
		}
	}
	
}
