package com.hm.action.http.merge;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.WorldBuildConfig;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.*;
import com.hm.rediscenter.MongoRedisUtils;
import com.hm.server.GameServerManager;
import com.hm.util.ItemUtils;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class MergeServerCompensateBiz {
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private MailBiz mailBiz;
	@Resource
    private WorldBuildConfig worldBuildConfig;
	@Resource
    private ActivityMergeServerBiz activityMergeServerBiz;

	/**
	 * 和服删除前处理
	 * @param serverId
	 */
	public void mergeServerPre(int serverId) {
		List<Integer> serverList = GameServerManager.getMergeServerInfo(serverId)
				.stream().map(e -> e.getServer_id()).collect(Collectors.toList());
		//找出包含serverList的记录
		List<MergeServerRecord> recordList = getMergeServerRecord(serverList);
		List<Integer> recordIdList = recordList.stream().map(e -> e.getServerId()).collect(Collectors.toList());
		List<Integer> newList = serverList.stream().filter(e -> !recordIdList.contains(e)).collect(Collectors.toList());
		newList.remove(Integer.valueOf(serverId));

		//构建主服和服
		buildMergeServer(serverId);
		//构建合并的服务器
		newList.forEach(e -> buildMergeServer(e));
	}

    public void mergeServerAfter(int serverId) {
        List<Integer> serverList = GameServerManager.getMergeServerInfo(serverId)
                .stream().map(e -> e.getServer_id()).collect(Collectors.toList());
        //找出包含serverList的记录
        List<MergeServerRecord> recordList = getMergeServerRecord(serverList);

        //当前主服
        MergeServerRecord mainServerRecord = recordList.stream().filter(e -> e.getServerId() == serverId).findFirst().orElse(null);
        //新合并的服务器
        List<MergeServerRecord> newRecordList = recordList.stream()
                .filter(e -> e.getServerId() != serverId)
                .filter(e -> !e.isReward()).collect(Collectors.toList());
        //发邮件
        List<Items> titleItems = commValueConfig.getListItem(CommonValueType.MergeRewardTitle);
        List<Items> oneServerItems = commValueConfig.getListItem(CommonValueType.MergeRewardServer);
        //发称号补偿
        //最强王者取消
        Set<Long> titlePlayers = mainServerRecord.getTitlePlayers();
        for (MergeServerRecord mergeServerRecord : newRecordList) {
            titlePlayers.addAll(mergeServerRecord.getTitlePlayers());
        }
        mailBiz.sendSysMail(serverId, titlePlayers, MailConfigEnum.MergeRewardTitle, titleItems);
        //发放最强王称号取消奖励
        doDelKingTitle(serverId, mainServerRecord, newRecordList);

        log.error("合服邮件：发送玩家称号补偿:"+GSONUtils.ToJSONString(titlePlayers));

        List<MergeServerRecord> allServers = Lists.newArrayList(newRecordList);
        allServers.add(mainServerRecord);
        //找出最早的服务器
        MergeServerRecord firstOpenServer = mainServerRecord;
//						MergeServerRecord firstOpenServer = allServers.stream()
//								.min(Comparator.comparingLong(MergeServerRecord::getOpenLong)).orElse(null);
//						if(firstOpenServer == null) {
//							firstOpenServer = mainServerRecord;
//						}
        for (MergeServerRecord mergeServerRecord : allServers) {
            if(mergeServerRecord.getOpenDate().getTime() <= firstOpenServer.getOpenDate().getTime()) {
                continue;
            }
            long day = DateUtil.betweenDay(mergeServerRecord.getOpenDate(), firstOpenServer.getOpenDate(), true);
            if(day <= 0 || mergeServerRecord.getServerId() == firstOpenServer.getServerId()) {
                continue;
            }
            day = Math.min(30, day);//最多30天
            List<Items> serverItems =  ItemUtils.calItemExtraAdd(oneServerItems, (day-1));
            //找出所有服务器的玩家id
            List<Long> playerIds = getPlayerIdByCreateServerId(mergeServerRecord.getServerId());
            filerPlayerIds(playerIds, serverId);//过滤已经删除的玩家id
            Set<Long> serverPlayerIds = Sets.newHashSet(playerIds);
            mailBiz.sendSysMail(serverId, serverPlayerIds, MailConfigEnum.MergeRewardServer, serverItems);
            log.error(mergeServerRecord.getServerId()+"合服邮件：发送服务器补偿,天数:"+day);
        }
        //计算最新的worldLv
        calNewWorldLv(serverId, allServers);

        for (MergeServerRecord mergeServerRecord : newRecordList) {
            mergeServerRecord.setReward(true);
            mergeServerRecord.saveDB();
        }
        //删除主服
        MongoUtils.getLoginMongodDB().delete(serverId, MergeServerRecord.class);

        //处理活动和服
        List<Integer> newServerIdsList = newRecordList.stream().map(e -> e.getServerId()).collect(Collectors.toList());
        newServerIdsList.remove(Integer.valueOf(serverId));
        activityMergeServerBiz.doActivityMegerServer(serverId, newServerIdsList);
        //处理合服redis
        MongoRedisUtils.megrge(serverId,newServerIdsList);
        //处理合服排行榜
        doMergeServerRank(serverId, newServerIdsList);

    }

    private void doMergeServerRank(int serverId, List<Integer> newServerIdsList) {
        //合并大军压境排行榜
        RankRedisUtils.mergeServerRank(serverId, newServerIdsList, RankType.ArmyPressBorder.getRankName());
        RankRedisUtils.mergeServerRank(serverId, newServerIdsList, RankType.Shooting.getRankName());
        RankRedisUtils.mergeServerRank(serverId, newServerIdsList, RankType.RepairTrain.getRankName());
    }

	public void doDelKingTitle(int serverId,MergeServerRecord mainServerRecord,List<MergeServerRecord> subList) {
		try {
			List<Items> titleItems = commValueConfig.getListItem(CommonValueType.KingTitleDel);
			long mainId = mainServerRecord.getTitlePlayerId(PlayerTitleType.KING);
			Set<Long> ids = subList.stream().map(e -> e.getTitlePlayerId(PlayerTitleType.KING))
								.filter(e -> e != mainId)
								.collect(Collectors.toSet());
			if(CollUtil.isNotEmpty(ids)) {
				ids.removeIf(e -> PlayerUtils.getPlayer(e) == null);
				log.error("合服邮件：最强王者取消:"+GSONUtils.ToJSONString(ids));
				mailBiz.sendSysMail(serverId, ids, MailConfigEnum.MergeRewardKingTitle, titleItems);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void calNewWorldLv(int serverId,List<MergeServerRecord> allServers) {
		try {
			ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);

			int maxWorldLv = allServers.stream().mapToInt(e -> e.getWorldLv()).max().orElse(0);
			ServerWorldLvData serverWorldLvData = serverData.getServerWorldLvData();
			if(serverWorldLvData.getWorldLv() < maxWorldLv) {
				serverWorldLvData.setWorldLv(maxWorldLv);
				serverWorldLvData.save();
				log.error(serverId+"最新世界等级="+maxWorldLv);
			}
			
			//==================重新计算世界建筑等级=============================
			long maxWorldBuildExp = allServers.stream().mapToLong(e -> e.getWorldBuildExp()).max().orElse(0);
			ServerWorldBuildData serverWorldBuildData = serverData.getServerWorldBuildData();
			if(serverWorldBuildData.getExp() < maxWorldBuildExp) {
				serverWorldBuildData.setExp(maxWorldBuildExp);
				serverWorldBuildData.setLv(worldBuildConfig.calNewLv(maxWorldBuildExp));
				serverWorldBuildData.save();
				log.error(serverId+"最新世界建筑等级="+serverWorldBuildData.getLv());
			}
			//===================重新计算初始npc等级====================================
			int minNpcLv = allServers.stream().mapToInt(e -> e.getBaseNpcLv()).min().orElse(0);
			ServerStatistics serverStatistics = serverData.getServerStatistics();
			if(serverStatistics.getBaseNpcLv() > minNpcLv) {
				serverStatistics.setBaseNpcLv(minNpcLv);
				serverStatistics.save();
				log.error(serverId+"最新NPC等级="+minNpcLv);
			}
			for (MergeServerRecord mergeServerRecord : allServers) {
				serverData.getServerFunction().addAllLock(mergeServerRecord.getFunctions());
				log.error(serverId+"重新加载功能解锁");
			}
            //重新加载大军压境全服地图数据,如果合服后开启了大军压境功能且主服没有大军压境的地图数据则重新生成一份
            if (serverData.getServerFunction().isServerUnlock(ServerFunctionType.EliminatePlay.getType()) && serverData.getServerEliminate().isEmpty()) {
                serverData.getServerEliminate().initCells();
            }
			serverData.save();
		} catch (Exception e) {
			log.error(serverId+"和服重新计算世界等级出错",e);
		}
	}

	/**
	 * 处理之前的数据
	 */
	public static void mergeServerOld() {
		List<ServerInfo> serverList = MongoUtils.getLoginMongodDB().queryAll(ServerInfo.class);
		List<Integer> serverIds = serverList.stream()
				.filter(e -> e.getDb_id() > 0 && e.getDb_id() != e.getServer_id())
				.map(e -> e.getServer_id())
				.collect(Collectors.toList());
		for (int serverId : serverIds) {
			MergeServerRecord serverRecord = new MergeServerRecord();
			serverRecord.setServerId(serverId);
			serverRecord.setTitleData(Maps.newHashMap());
			serverRecord.setOpenDate(new Date());
			serverRecord.setReward(true);
			serverRecord.saveDB();
		}
	}
	
	public static List<MergeServerRecord> getMergeServerRecord(List<Integer> serverList) {
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").in(serverList);
    	query.addCriteria(criteria);
    	return MongoUtils.getLoginMongodDB().query(query, MergeServerRecord.class);
	}
	
	public static List<Long> getPlayerIdByCreateServerId(int createServerId) {
		// 最后一次登录时间时间小于60天
		Date day60 = new Date(DateUtil.getProDays(new Date(), 60));
		Document filter = new Document("playerBaseInfo.lastLoginDate", new Document("$gt", day60));
		filter.append("serverId", createServerId);

		MongoCollection<Document> player = MongoUtils.getGameMongodDB(createServerId).getMongoTemplate().getCollection("player");
		List<Long> r = Lists.newArrayList();
		Consumer<Document> processBlock = doc -> r.add(Convert.toLong(doc.get("_id")));
		player.find(filter).projection(new Document("_id", 1)).forEach(processBlock);
        return r;
    }
	
	public static MergeServerRecord buildMergeServer(int serverId) {
		MergeServerRecord mergeServer = new MergeServerRecord();
		mergeServer.setServerId(serverId);
		//记录称号
		MongodDB mongo = MongoUtils.getGameMongodDB(serverId);
		ServerData serverData = mongo.get(ServerDataDbUtils.ID, ServerData.class);
		mergeServer.setTitleData(serverData.getTitleData().getAllTitle());
//		long lastMergeDate = serverData.getServerMergeData().getDate();
//		if(lastMergeDate > 0) {
//			mergeServer.setOpenDate(new Date(lastMergeDate));
//		}else{
//			
//		}
		mergeServer.setWorldBuildExp(serverData.getServerWorldBuildData().getExp());
		mergeServer.setWorldLv(serverData.getServerWorldLvData().getWorldLv());
		//全部按照开服时间去算
		mergeServer.setOpenDate(serverData.getServerOpenData().getFirstOpenDate());
		mergeServer.setBaseNpcLv(serverData.getServerStatistics().getBaseNpcLv());
		mergeServer.setFunctions(serverData.getServerFunction().getFunctions());
		mergeServer.saveDB();
		
		log.error("合服前数据备份：["+serverId+"]备份完成");
		return mergeServer;
	}
	public static void filerPlayerIds(List<Long> playerIds,int serverId) {
		MongodDB mongo = MongoUtils.getGameMongodDB(serverId);
		for (int i = playerIds.size()-1; i >= 0; i--) {
			Query query = new Query();
			Criteria criteria = Criteria.where("_id").is(playerIds.get(i));
	    	query.addCriteria(criteria);
	    	if(!mongo.isExists(query, Player.class)) {
	    		playerIds.remove(i);
	    	}
		}
	}
	
	public static void main(String[] args) {
		DateTime beginDate = new DateTime("2019-07-05 09:00:00","yyyy-MM-dd HH:mm:ss");
		DateTime endDate = new DateTime("2019-08-24 08:00:00","yyyy-MM-dd HH:mm:ss");
		System.err.println(DateUtil.betweenDay(endDate, beginDate, true));
		//		mergeServerOld();
		
//		List<Long> playerIds = Lists.newArrayList(600009,500012);
//		filerPlayerIds(playerIds, 6);
//		System.err.println(playerIds);
	}
}
