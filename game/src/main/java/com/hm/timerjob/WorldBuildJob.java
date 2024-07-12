package com.hm.timerjob;

import com.google.common.collect.Lists;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.config.excel.WorldBuildConfig;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerWorldBuildData;
import com.hm.servercontainer.worldbuild.PlayerWorldBuild;
import com.hm.servercontainer.worldbuild.WorldBuildTroop;
import com.hm.servercontainer.worldbuild.WorldBuildTroopItemContainer;
import com.hm.servercontainer.worldbuild.WorldBuildTroopServerContainer;
import com.hm.timerjob.server.GameTimerManager;
import com.hm.timerjob.server.WorldBuildQuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 世界建筑
 * @author siyunlong  
 * @date 2019年10月16日 下午9:20:51 
 * @version V1.0
 */
@Slf4j
@Service
public class WorldBuildJob {


	@Resource
	private WorldBuildBiz worldBuildBiz;
	@Resource
	private WorldBuildConfig worldBuildConfig;
	
	public void doJob(int serverId) {
		WorldBuildTroopItemContainer troopContainer = WorldBuildTroopServerContainer.of(serverId);
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(serverId)
				.getServerWorldBuildData();
		if(serverWorldBuildData.isOver()) {
			GameTimerManager.getInstance().checkAndRemoveJob(serverId, WorldBuildQuartzJob.class);
			worldBuildOver(serverId);
			troopContainer.clearData();
			return;
		}
		for (PlayerWorldBuild playerWorldBuild : troopContainer.getPlayerMap().values()) {
			try {
				doWorldBuildTroop(playerWorldBuild,serverWorldBuildData);
			} catch (Exception e) {
				log.error(playerWorldBuild.getId()+"世界建筑任务检查出错!", e);
			}
		}
	}
	
	
	/**
	 * 广播任务结束
	 * @param serverId
	 */
	public void worldBuildOver(int serverId) {
		for (Map.Entry<Long, PlayerWorldBuild> entry : WorldBuildTroopServerContainer.of(serverId).getPlayerMap().entrySet()) {
			//遣返所有
			PlayerWorldBuild playerWorldBuild = entry.getValue();
			worldBuildBiz.backWorldBuildTroop(playerWorldBuild, playerWorldBuild.getWorldBuildTroopList(),true);
		}
	}
	
	public void doWorldBuildTroop(PlayerWorldBuild playerWorldBuild,ServerWorldBuildData serverWorldBuildData) {
		int[] campMainCitys = serverWorldBuildData.getMainCitys();
		int secondAdd = serverWorldBuildData.getSecondAdd();
		int maxScore = serverWorldBuildData.getMaxScore();
		
		List<WorldBuildTroop> removeList = Lists.newArrayList();
		for (WorldBuildTroop worldBuildTroop : playerWorldBuild.getWorldBuildTroopList()) {
			playerWorldBuild.addCampScores(worldBuildTroop.getCampId(), secondAdd);
			worldBuildTroop.addTroopCount();
			if(worldBuildTroop.isOver() || campMainCitys[worldBuildTroop.getCampId()-1] != worldBuildTroop.getCityId()
					|| playerWorldBuild.getCampScore(worldBuildTroop.getCampId()) >= maxScore) {
				playerWorldBuild.removeBuildTroop(worldBuildTroop.getId());
				removeList.add(worldBuildTroop);
			}
		}
		if(!removeList.isEmpty()) {
			playerWorldBuild.saveDB();//保存
			worldBuildBiz.backWorldBuildTroop(playerWorldBuild, removeList,false);
		}
	}
}
