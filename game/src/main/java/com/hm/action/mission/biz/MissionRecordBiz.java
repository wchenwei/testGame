package com.hm.action.mission.biz;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.FarWinBattleConfig;
import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.BattlePveRecordType;
import com.hm.enums.RankType;
import com.hm.kfchat.cache.KFPlayerRedisCache;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerStatistics;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.PlayerRedisData;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.mission.MissionRecord;
import com.hm.servercontainer.record.PveMRecord;
import com.hm.servercontainer.world.WorldItemContainer;
import com.hm.servercontainer.world.WorldServerContainer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Biz
public class MissionRecordBiz implements IObserver{

	@Resource
	private MissionConfig missionConfig;
	@Resource
	private FarWinBattleConfig farWinBattleConfig;
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FightMission, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ClearnceMission, this);
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		if(observableEnum == ObservableEnum.FightMission){
			checkMissionRecord(player, argv);
			doCityFirstMission(player, argv);
			return;
		}
		if(observableEnum == ObservableEnum.ClearnceMission){
			doClearnceMission(player, argv);
			return;
		}
	}
	
	public void checkMissionRecord(Player player,Object... argv) {
		String missionId = (int)argv[0]+"";
		long combat = (long)argv[1];
		String tanks = (String)argv[2];
		
		MissionRecord record = WorldServerContainer.of(player).getMissionRecord(missionId);
		record.checkTopRecord(player, combat, tanks);
	}

	public void doCityFirstMission(Player player,Object... argv) {
		int missionId = (int)argv[0];
		MissionExtraTemplate template = missionConfig.getMission(missionId);
		if(template == null || CollUtil.isEmpty(template.getRecordRewardList())) {
			return;
		}
		String recordId = BattlePveRecordType.MainMission.buildRecordId(template.getUnlock_city());
		PveMRecord record = WorldServerContainer.of(player).getPveMRecord(recordId);
		if(record != null) {
			return;
		}
		record = new PveMRecord(player,recordId);
		record.setMid(missionId);
		record.save();
		WorldServerContainer.of(player).addPveMRecord(record);

		//广播给玩家
		changePveRecordChange(player,BattlePveRecordType.MainMission.getType(),template.getUnlock_city());
	}

	public void doClearnceMission(Player player,Object... argv) {
		int battleType = (int)argv[0];
		int id = (int)argv[1];
		BattlePveRecordType recordType = BattlePveRecordType.getRecordType(battleType,id);
		if(recordType == null || CollUtil.isEmpty(recordType.getRecordItems(id))) {
			return;
		}

		String recordId = recordType.buildRecordId(id);
		PveMRecord record = WorldServerContainer.of(player).getPveMRecord(recordId);
		if(record != null) {
			return;
		}

		if(!loadBattleTypeMax(player,recordType,id)) {
			//检查所有<id 的记录是否有记录 没有记录就补上
			return;
		}

		record = new PveMRecord(player,recordId);
		record.setMid(id);
		record.setType(recordType.getType());
		record.save();
		WorldServerContainer.of(player).addPveMRecord(record);
		//广播给玩家
		changePveRecordChange(player,recordType.getType(),id);
	}


	public void missionRecordOldToBattle(int serverId,Map<String, MissionRecord> missonMap,Map<String, PveMRecord> pveMap) {

		int maxCityId = 0;
		for (MissionRecord value : missonMap.values()) {
			int id = Convert.toInt(value.getId(),0);
			MissionExtraTemplate template = missionConfig.getMission(id);
			if(template == null || CollUtil.isEmpty(template.getRecordRewardList())) {
				continue;
			}
			PlayerRedisData playerRedisData = KFPlayerRedisCache.getRedisPlayer(value.getFirstRecord().getPlayerId());
			if(playerRedisData == null) {
				;playerRedisData = KFPlayerRedisCache.getRedisPlayer(value.getMinCombatRecord().getPlayerId());
			}
			if(playerRedisData == null) {
				System.out.println("mrecord not find:"+serverId+"_"+id);
				continue;
			}
			String recordId = BattlePveRecordType.MainMission.buildRecordId(template.getUnlock_city());
			PveMRecord record = new PveMRecord(playerRedisData,serverId,recordId);
			record.setMid(id);
			record.save();
			pveMap.put(record.getId(),record);

			maxCityId = Math.max(template.getUnlock_city(),maxCityId);
		}
		ServerDataManager.getIntance().getServerData(serverId).getServerStatistics()
				.changePveRecordMax(BattlePveRecordType.MainMission.getType(),maxCityId);
	}


	public void changePveRecordChange(Player player,int type,int maxId) {
		ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerStatistics();
		serverStatistics.changePveRecordMax(type,maxId);

		//收到次消息就展示红点
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Battle_PveRecordMaxChange);
		serverMsg.addProperty("type", type);
		serverMsg.addProperty("maxId", maxId);
		player.sendMsg(serverMsg);
	}


	public boolean loadBattleTypeMax(Player player, BattlePveRecordType recordType, int maxId) {
		if(recordType.getBattleType() == null) {
			return false;
		}
		ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerStatistics();
		if(serverStatistics.getPveRecordMaxId(recordType.getType()) >= maxId) {
			return false;
		}
		int maxLogId = 0;
		WorldItemContainer container = WorldServerContainer.of(player);
		List<Integer> idList = recordType.getSortIdList();
		for (int id : idList) {
			if(id <= maxId) {
				String recordId = recordType.buildRecordId(id);
				PveMRecord record = container.getPveMRecord(recordId);
				if(record == null) {
					record = new PveMRecord(player,recordId);
					record.setMid(id);
					record.setType(recordType.getType());
					record.save();

					container.addPveMRecord(record);
				}
				maxLogId = Math.max(maxLogId,id);
			}
		}
		changePveRecordChange(player,recordType.getType(),maxLogId);
		return true;
	}
	
	public void checkTowerBattleOldData() {
		for (int serverId : GameServerManager.getInstance().getServerIdList()) {
			List<LeaderboardInfo> tempList = RankRedisUtils.getRankListForLeader(serverId, RankType.TowerRank.getRankName(),1,1);
			if(tempList.isEmpty()) {
				continue;
			}
			LeaderboardInfo top = tempList.get(0);
			Player player = PlayerUtils.getPlayer(top.getIntId());
			if(player == null) {
				continue;
			}
			int id = (int)top.getScore();

			loadBattleTypeMax(player,BattlePveRecordType.TowerBattle,id);
		}
	}
}
