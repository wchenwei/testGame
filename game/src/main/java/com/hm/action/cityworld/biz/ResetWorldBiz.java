package com.hm.action.cityworld.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.excel.WorldLvConfig;
import com.hm.config.excel.temlate.WorldUpgradeTemplate;
import com.hm.container.PlayerContainer;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerWorldLvData;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class ResetWorldBiz {
	@Resource
	private WorldCityBiz worldCityBiz;
	@Resource
	private WorldLvConfig worldLvConfig;

	
	/**
	 * 柏林战之后检查
	 * @param serverId
	 */
	public void doBerlinWarEnd(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerWorldLvData serverWorldLvData = serverData.getServerWorldLvData();
		if(serverWorldLvData.getType() == 0) {
			serverWorldLvData.openWorldLv();
			serverData.broadServerUpdate();
		}
	}
	
	/**
	 * 每小时检查世界重置
	 * @param serverId
	 */
	public void doResetWorld(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerWorldLvData serverWorldLvData = serverData.getServerWorldLvData();
		if(serverWorldLvData.getType() == 0) {
			return;
		}
		if(DateUtil.getCsWeek() == 7 && DateUtil.thisHour(true) == 20) {
//			worldCityBiz.clearWorldCity(serverId);
			//计算当前世界等级
			int serverLv = serverData.getServerStatistics().getServerLv();
			int newWorldLv = worldLvConfig.calCurWorldLv(serverLv);
			if(serverWorldLvData.getWorldLv() < newWorldLv) {
				serverWorldLvData.setWorldLv(serverWorldLvData.getWorldLv()+1);
				serverWorldLvData.save();
			}
			broadWorldLvChange(serverWorldLvData);
		}
	}
	
	public void broadWorldLvChange(ServerWorldLvData serverWorldLvData) {
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldLvChange);
		serverMsg.addProperty("serverWorldLvData", serverWorldLvData);
		PlayerContainer.broadPlayer(serverWorldLvData.getContext().getServerId(), serverMsg);
	}
	
	/**
	 * 答题经验加成
	 * @param player
	 * @param itemList
	 * @return
	 */
	public List<Items> calAnswerItemAdd(Player player,List<Items> itemList) {
		WorldUpgradeTemplate worldTemplate = getWorldUpgradeTemplate(player.getServerId());
		if(worldTemplate == null) {
			return itemList;
		}
		if(worldTemplate.getAnswer_product() <= 0) {
			return itemList;
		}
		List<Items> cloneList = ItemUtils.createCloneItems(itemList);
		for (Items items : cloneList) {
			if(items.isPlayerExp()) {
				items.addCountRate(worldTemplate.getAnswer_product());
			}
		}
		return cloneList;
	}
	

	
	public double getCityAddRate(int serverId) {
		WorldUpgradeTemplate worldTemplate = getWorldUpgradeTemplate(serverId);
		if(worldTemplate == null) {
			return 0d;
		}
		return worldTemplate.getCity_product();
	}
	
	public double getDailyTaskExpAdd(Player player) {
		WorldUpgradeTemplate worldTemplate = getWorldUpgradeTemplate(player.getServerId());
		if(worldTemplate == null) {
			return 0;
		}
		return worldTemplate.getDaily_task_add();
	}
	
	public WorldUpgradeTemplate getWorldUpgradeTemplate(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerWorldLvData serverWorldLvData = serverData.getServerWorldLvData();
		if(serverWorldLvData.getType() == 0) {
			return null;
		}
		return worldLvConfig.getWorldUpgradeTemplate(serverWorldLvData.getWorldLv());
	}
}
