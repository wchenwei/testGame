package com.hm.action.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.http.gm.MergeServerBean;
import com.hm.action.http.merge.MergeServerCompensateBiz;
import com.hm.action.kf.kfexpedition.KfExpeditionScoreUtils;
import com.hm.action.mail.biz.MailBiz;
import com.hm.cache.PlayerCacheManager;
import com.hm.config.TitleConfig;
import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.templaextra.TitleTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerGroup;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.main.CreateServerFactory;
import com.hm.model.battle.BaseBattle;
import com.hm.model.battle.TowerBattle;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.item.Items;
import com.hm.model.notice.NewNotice;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.redis.troop.TroopRedisUtils;
import com.hm.redis.type.RedisConstants;
import com.hm.redis.util.RedisDbUtil;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.servercontainer.idcode.IdCodeInfo;
import com.hm.servercontainer.idcode.IdCodeItemContainer;
import com.hm.servercontainer.worldbuild.PlayerWorldBuild;
import com.hm.util.HFUtils;
import com.hm.util.PubFunc;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service("mergeserver.do")
public class MergeServerAction {
    @Resource
	private CmqBiz cmqBiz;
    @Resource
	private MergeServerCompensateBiz mergeServerCompensateBiz;
    @Resource
    private CommanderConfig commanderConfig;
    @Resource
    private MailConfig mailConfig;
	@Resource
    private MailBiz mailBiz;

	@Resource
    private TitleConfig titleConfig;


    public void mergeServer(HttpSession session) {
        int serverId = Integer.parseInt(session.getParams("serverId"));
        MergeContants.isMergeRunning = true;
        log.error("==============合服：重新加载服务器id列表===================");
        GameServerManager.getInstance().init();
        log.error("==============合服：记录服务器数据===================");
        mergeServerCompensateBiz.mergeServerPre(serverId);
        log.error("==============合服：记录服务器数据完毕===================");
        
        log.error("==============合服：玩家===================");
        doPlayer(serverId);
        log.error("==============合服：玩家完毕===================");

        log.error("==============合服：开启合服活动===================");
        createMergeActivity(serverId);
        log.error("==============合服：开启合服活动完毕===================");
        try {
        	log.error("==============合服：延迟3秒===================");
			Thread.sleep(3000L);
			log.error("==============合服：延迟3秒===================");
		} catch (Exception e) {
		}

        try {
        	log.error("==============合服：删除跨服远征积分===================");
        	KfExpeditionScoreUtils.removeMergeServer(serverId);
        	log.error("==============合服：删除跨服远征积分===================");
		} catch (Exception e) {
			e.printStackTrace();
		}
        //通知跨服服務器
        log.error("==============合服：通知跨服務器：" + sendKfReloadServer(serverId));
        
        log.error("==============合服：服务器补偿发放===================");
        mergeServerCompensateBiz.mergeServerAfter(serverId);
        MergeContants.isMergeRunning = false;
        log.error("==============合服：服务器补偿发放完毕==================="+MergeContants.isMergeRunning);
        session.Write("1");
    }


    private void createMergeActivity(int serverId) {
        HFUtils.calServerLv(serverId);
        ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
        int serverLv = serverData.getServerStatistics().getServerLv();
        serverData.getServerMergeData().setAvgLv(serverLv);
        serverData.getServerMergeData().setDate(System.currentTimeMillis());
        serverData.getServerMergeData().addMergeTimes();
        serverData.getServerCampData().loadNewMark();//重置荣誉排行
        serverData.getServerMergeData().save();
        ActivityServerContainer.of(serverId).createMergeServerActivity();
    }

    public void doPlayer(int serverId) {
        MongoTemplate mongoTemplate = MongoUtils.getServerMongoTemplate(serverId);
        if (mongoTemplate == null) {
            return;
        }
        MongoCollection<Document> collPlayer = mongoTemplate.getCollection("player");
        MongoCollection<Document> collGuild = mongoTemplate.getCollection("guild");

        CreateServerFactory.stopServer(serverId);
        //============================================================================
        // drop collections
        String[] dropColl = new String[]{"GuildWar", "BattleRecord", "Mail", "warHelper", "worldCity", "worldTroop", "FightDataRecord", "camp", "newNotice", "playerWorldBuild"};
        for (String collName : dropColl) {
            log.error("drop collection " + collName + " begin");
            mongoTemplate.dropCollection(collName);
            log.error("drop collection " + collName + " end");
        }
        log.error("drop redis world  begin");
        RedisMapperUtil.deleteAll(serverId, WorldCity.class);
        log.error("drop redis world  end");
        log.error("drop redis troop  begin");
        RedisMapperUtil.deleteAll(serverId, WorldTroop.class);
        log.error("drop redis troop  begin");
        RedisMapperUtil.deleteAll(serverId, PlayerWorldBuild.class);
        RedisMapperUtil.deleteAll(serverId, NewNotice.class);
        // 清理用户
        log.error("deletePlayers begin");
        deletePlayers(collPlayer);
        log.error("deletePlayers end");

        log.error("renamePlayer begin");
        renamePlayer(collPlayer);
        log.error("renamePlayer end");

        log.error("removePlayerFields begin");
        removePlayerFields(collPlayer);
        log.error("removePlayerFields end");
        // 批量重命名guildName
        log.error("renameGuild begin");
        renameGuild(collGuild);
        log.error("renameGuild end");
        //删除部落字段
        log.error("removeGuildFields begin");
        removeGuildFields(collGuild);
        log.error("removeGuildFields end");

        try {
			sendMergeServerStatistics(serverId);
		} catch (Exception e) {
			log.error("往统计服发送失败：",e);
		}

        // 更新用户、部落里的serverId字段
        collPlayer.updateMany(new Document("serverId", new Document("$ne", serverId)),
                new Document("$set", new Document("serverId", serverId))
        );
        collGuild.updateMany(new Document("serverId", new Document("$ne", serverId)),
                new Document("$set", new Document("serverId", serverId))
        );

        CreateServerFactory.createServer(serverId);

        log.error("rebuildRedisRank begin");
        rebuildRedisRank(mongoTemplate, collPlayer);
        log.error("rebuildRedisRank end");

        // 清理部分serverData
        ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
        serverData.getTitleData().doMergeServerForTitle();
        serverData.getServerPresidentPower().clearData();
        serverData.getServerWorldBuildData().resetData();
        serverData.getServerAllianceData().clearData();
        serverData.save();

        // 清理cache里用户信息
        PlayerCacheManager.getInstance().clearAllCache();
    }

    /**
     * mongo db 数据同步到redis, 重新加载用户排行榜
     *
     * @param mongoTemplate
     * @param collPlayer
     */
    private void rebuildRedisRank(MongoTemplate mongoTemplate, MongoCollection<Document> collPlayer) {
    	List<String> playerIds = Lists.newArrayList();
        Date day60 = new Date(DateUtil.getProDays(new Date(), 60));
        Document filter = new Document("playerBaseInfo.lastLoginDate", new Document("$gt", day60));
        for (Document document : collPlayer.find(filter)) {
            Player player = mongoTemplate.getConverter().read(Player.class, document);
            if (player != null) {
            	try {
            		//发放克隆奖励
                    doPlayerMergerReward(player);
                    //处理玩家称号
                    doPlayerTitle(player);

                    //同步redis
                    RedisUtil.updateRedisPlayer(player);
                    //重新加载排行榜
                    HdLeaderboardsService.getInstance().updatePlayerInfo(player);
                    HdLeaderboardsService.getInstance().updatePlayerRankOverride(player, RankType.PlayerMainBattle, player.playerMission().getFbId());
                    BaseBattle baseBattle = player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
                    if(baseBattle != null) {
                    	HdLeaderboardsService.getInstance().updatePlayerRankOverride(player, RankType.TowerRank, ((TowerBattle)baseBattle).getMaxHistory());
                    }
                    //TODO 排行榜处理移动到合并排行榜(短期的，不可能涉及玩家被删除的排行榜)
                    //判断今天是否登录
//                    if(DateUtil.isSameDay(new Date(), player.playerBaseInfo().getLastLoginDate())) {
//                    	reloadShortRank(player);
//                    	reloadRepairTrainRank(player);
//                    }
//                    reloadArmyPressRank(player);
                    if(player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Arena)) {
                    	HdLeaderboardsService.getInstance().initArenaPlayer(player, RankType.Arena);
                    }
                    playerIds.add(player.getId()+"");
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
        
        //删除部队redis
        TroopRedisUtils.doMergeServerForPlayer(playerIds);
    }

    //重新加载坦克训练排行榜
    private void reloadRepairTrainRank(Player player) {
    	try {
    		long score = player.playerRepairTrain().getScore();
    		if(score>0){
    			HdLeaderboardsService.getInstance().updatePlayerRankOverrideForTime(player, RankType.RepairTrain, score);
    		}
		} catch (Exception e) {
		}
    }
    

    /**
     * 删除用户某些字段
     *
     * @param collPlayer
     */
    private void removePlayerFields(MongoCollection<Document> collPlayer) {
        Document unsetFields = new Document();
        unsetFields
//        .append("playerTitle.usingTitleId", "")
        .append("playerMail", "")
        .append("playerGuild.guildTaskPos", "")
        ;
        // remove some function id
        Document functions = new Document("playerFunction.functionIds",
                new Document("$in", Arrays.asList(
                        //PlayerFunctionType.Arena.getType(),
                        PlayerFunctionType.TrumpArena.getType())
                )
        );
        Document update = new Document("$unset", unsetFields);
        update.append("$pull", functions);

        UpdateResult updateResult = collPlayer.updateMany(new Document(), update);
    }
    
    //清理无用唯一码
    public void deleteIdCodeInfo(int serverId,List<Long> delPlayerIds){
    	//清理唯一码信息
        try {
			if(!delPlayerIds.isEmpty()){
				IdCodeItemContainer  container =  IdCodeContainer.of(serverId);
				List<IdCodeInfo> idCodeInfos = RedisMapperUtil.queryListAll(serverId, IdCodeInfo.class);;
				for(IdCodeInfo idCodeInfo:idCodeInfos){
					List<Long> copyDeletePlayerId = Lists.newArrayList(delPlayerIds);
					List<Long> ids = Lists.newArrayList(idCodeInfo.getPlayerMap().keySet());
					if(CollUtil.isEmpty(ids)){
						//该唯一码信息中没有任何玩家信息则从container和数据库中删除
						container.delIdCodeInfo(idCodeInfo);
						continue;
					}
					//取集合中的交集（即该唯一码中被删除的玩家id）
					copyDeletePlayerId.retainAll(ids);
					//此时copyDeletePlayerId中的数据为该唯一码中被删除的玩家
					if(!CollUtil.isEmpty(copyDeletePlayerId)){//该唯一码信息中有玩家已被删除(合服清用户)
						idCodeInfo.del(copyDeletePlayerId);
                        idCodeInfo.saveDB();
						//删除后再次检查该码中是否还有用户
						if(idCodeInfo.getPlayerMap().size()<=0){
							//该唯一码信息中没有任何玩家信息则从container和数据库中删除
							container.delIdCodeInfo(idCodeInfo);
						}
					}
				}
			}
		} catch (Exception e) {
		}
    }
    
    //删除部落表中的字段
    private void removeGuildFields(MongoCollection<Document> collGuild) {
        Document unsetFields = new Document();
        unsetFields
                .append("guildTask", "")
                .append("guildReqs", "");

        Document update = new Document("$unset", unsetFields);
        UpdateResult updateResult = collGuild.updateMany(new Document(), update);
    }

    /**
     * 部落改名
     *
     * @param collGuild
     */
    private void renameGuild(MongoCollection<Document> collGuild) {
        Map<Integer, String> serverNameInfo = MongoUtils.serverNameInfo();
        List<WriteModel<Document>> writeModelGuildList = Lists.newArrayList();
        Consumer<Document> updateNameBlockGuild = document -> {
            Integer serverId = document.getInteger("serverId");
            String guildName = document.getString("guildName");

            // 如果部落名字已经有[xxxx]前缀，则不再改名,保留原来前缀
            if (HFUtils.haveMergeServerPrefix(guildName)) {
                return;
            }

            String checkName = HFUtils.checkName(guildName, serverId, serverNameInfo);
            if (guildName.equals(checkName)) {
                return;
            }
            Document f = new Document("_id", document.getInteger("_id"));
            Document update = new Document("$set", new Document("guildBaseInfo.guildName", checkName));
            writeModelGuildList.add(new UpdateOneModel<>(f, update));
            if (writeModelGuildList.size() == 200) {
                collGuild.bulkWrite(writeModelGuildList);
                writeModelGuildList.clear();
            }
        };

        List<? extends Bson> pipelineGuild = Arrays.asList(
                new Document("$match", new Document("guildBaseInfo.guildName", new Document("$exists", true))),
                new Document("$project", new Document("guildName", "$guildBaseInfo.guildName").append("serverId", "$serverId"))
        );
        collGuild.aggregate(pipelineGuild).forEach(updateNameBlockGuild);

        if (!writeModelGuildList.isEmpty()) {
            collGuild.bulkWrite(writeModelGuildList);
        }
    }

    /**
     * 玩家改名
     *
     * @param collPlayer
     */
    private void renamePlayer(MongoCollection<Document> collPlayer) {
        Map<Integer, String> serverNameInfo = MongoUtils.serverNameInfo();

        // 批量改玩家名
        List<WriteModel<Document>> writeModelList = Lists.newArrayList();
        Consumer<Document> updateNameBlock = document -> {
            Integer createServerId = document.getInteger("createServerId");
            String playerName = document.getString("name");

            String checkName = HFUtils.checkName(playerName, createServerId, serverNameInfo);
            if (playerName.equals(checkName)) {
                return;
            }

            Document f = new Document("_id", document.getInteger("_id"));
            Document update = new Document("$set", new Document("name", checkName));
            UpdateOneModel<Document> model = new UpdateOneModel<>(f, update);
            writeModelList.add(model);
            if (writeModelList.size() == 200) {
                collPlayer.bulkWrite(writeModelList);
                writeModelList.clear();
            }
        };

        // { "name":{"$concat": [  {$substr:['$createServerId', 0, 5] }, "_", "$name" ]} }
        List<? extends Bson> pipeline = Arrays.asList(
                new Document("$project", new Document("name", "$name").append("createServerId", "$createServerId"))
        );
        collPlayer.aggregate(pipeline).forEach(updateNameBlock);

        if (!writeModelList.isEmpty()) {
            collPlayer.bulkWrite(writeModelList);
        }
    }

    /**
     * 清理用户
     *
     * @param collPlayer
     */
    private List<String> deletePlayers(MongoCollection<Document> collPlayer) {
        Document filter = new Document();
        filter.append("$and", Arrays.asList(
                new Document("expireTime", new Document("$lt", System.currentTimeMillis())),
                new Document("expireTime", new Document("$ne", -1L))
                )
        );

        Document projection = new Document("_id", 1);

        List<String> deletePlayerId = Lists.newArrayList();
        Consumer<Document> processBlock = document -> deletePlayerId.add(String.valueOf(document.getInteger("_id")));

        collPlayer.find(filter).projection(projection).forEach(processBlock);

        if (!deletePlayerId.isEmpty()) {
            log.error("delete player from redis " + deletePlayerId.size());

            String rdbKey = String.format("%s_%s", MongoUtils.getGameDBName(), "player");
            Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);

            final int size = deletePlayerId.size();
            final int pageSize = 200;
            for (int i = 0; i < size; i += pageSize) {
                List<String> page = deletePlayerId.subList(i, Math.min(size, i + pageSize));
                Long num = jedis.hdel(rdbKey, page.toArray(new String[0]));
                log.error("delete " + num + " player from redis(player)");
            }

            DeleteResult deleteResult = collPlayer.deleteMany(filter);
            log.error("delete player from mongo. size = " + deleteResult.getDeletedCount());
            RedisDbUtil.returnResource(jedis);
            //删除部队redis
            TroopRedisUtils.doMergeServerForPlayer(deletePlayerId);
        }
        return deletePlayerId;
    }
    //清理无用唯一码
    public void clearIdCodeInfo(List<String> deletePlayerId,int serverId){
    	//清理唯一码信息
        if(!deletePlayerId.isEmpty()){
        	List<IdCodeInfo> idCodeInfos = RedisMapperUtil.queryListAll(serverId, IdCodeInfo.class);;
        	List<Long> deleteIds = deletePlayerId.stream().map(t ->Long.parseLong(t)).collect(Collectors.toList());
        	idCodeInfos.forEach(t ->{
        		List<Long> copyDeletePlayerId = Lists.newArrayList(deleteIds);
        		List<Long> ids = Lists.newArrayList(t.getPlayerMap().keySet());
        		copyDeletePlayerId.retainAll(ids);
        		if(!copyDeletePlayerId.isEmpty()){//有需要删除的
        			t.del(copyDeletePlayerId);
        			t.saveDB();
        		}
        	});
        }
    }
    public static boolean sendKfReloadServer(int serverId) {
        ServerGroup serverGroup = GameServerManager.getInstance().getServerGroup(serverId);
        if (serverGroup == null) {
            return true;
        }
        try {
            String url = "http://" + serverGroup.getHttpServerurl() + "?action=kfope.do&m=reloadServerGroup";
            String result = HttpUtil.get(url, 3000);
            return StrUtil.equals(result, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * sendMergeServerStatistics:(往统计服，发送修改阵营信息). <br/>  
     * @author zxj  
     * @param serverIdin  使用说明
     */
	public static void sendMergeServerStatistics(int serverIdin) {
 		List<ServerInfo> listServerInfo = GameServerManager.getMergeServerInfo(serverIdin);
		if (null==listServerInfo || listServerInfo.size()==0) {
			return;
		}
		Map<Integer, ServerChangeCamp> campMap = ServerChangeCamp.getServerChangeCamp();
		
		List<MergeServerBean> result = Lists.newArrayList();
		listServerInfo.forEach(e->{
			String info = null;
			if(null!=campMap && !campMap.isEmpty()) {
				info = null==campMap.get(e.getServer_id())?null:campMap.get(e.getServer_id()).getInfo();
			}
			MergeServerBean mergeServer = new MergeServerBean();
			mergeServer.setServerid(e.getServer_id());
			mergeServer.setTarServerid(serverIdin);
			mergeServer.setCampMsg(info);
			result.add(mergeServer);
		});
		if(CollectionUtil.isNotEmpty(result)) {
			CmqBiz temp = SpringUtil.getBean(CmqBiz.class);
			temp.sendMergeServerMsg(result);
		}
	}
	
	/**
	 * 检查玩家未发放奖励
	 * @param player
	 */
	public void doPlayerMergerReward(Player player) {
		try {
			//发放克隆奖励
			boolean isChange = doPlayerCloneReward(player);
			
			if(isChange) {
				player.saveNowDB();
			}
		} catch (Exception e) {
			log.error(player.getId()+"合服奖励检查出错",e);
		}
	}
	
	public void doPlayerTitle(Player player) {
		try {
			int usingTitleId = player.playerTitle().getUsingTitleId();
			if(usingTitleId <= 0) {
				return;
			}
			TitleTemplate titleTemplate = titleConfig.getTitleTemplate(usingTitleId);
			if(titleTemplate != null && titleTemplate.isSystemOnly()) {
				player.playerTitle().useTitle(0);
				player.saveNowDB();
			}
		} catch (Exception e) {
			log.error(player.getId()+"处理玩家称号出错",e);
		}
	}
	
	
	private boolean doPlayerCloneReward(Player player) {
		try {
			long totalExp = player.playerCloneTroops().getTotalExp(commanderConfig.getMilitaryExtraTemplate(player).getGhost_reward());
			if(totalExp > 0) {
				Items item = new Items(PlayerAssetEnum.EXP.getTypeId(), totalExp, ItemType.CURRENCY);
				//发送邮件奖励
				mailBiz.sendSysMail(player, MailConfigEnum.CloneTroopReturn, Lists.newArrayList(item));
				return true;
			}
		} catch (Exception e) {
			log.error(player.getId()+"克隆奖励检查出错",e);
		}
		return false;
    }
	
	public void mergeServerPre(HttpSession session) {
        int serverId = Integer.parseInt(session.getParams("serverId"));
        mergeServerCompensateBiz.mergeServerPre(serverId);
        log.error("==============合服前数据备份：完成===================");
        session.Write("sucess");
	}
	
	public void mergeServerAfter(HttpSession session) {
        int serverId = Integer.parseInt(session.getParams("serverId"));
        mergeServerCompensateBiz.mergeServerAfter(serverId);
        log.error("==============合服前数据发放奖励：完成===================");
        session.Write("sucess");
	}
	
	public void mergeServerOld(HttpSession session) {
		MergeServerCompensateBiz.mergeServerOld();
        log.error("==============合服初始数据完成===================");
        session.Write("sucess");
	}
	
	public void mergeForPlayer(HttpSession session) {
		long playerId = PubFunc.parseInt(session.getParams("playerId"));
		Player player = PlayerUtils.getPlayer(playerId);
		doPlayerMergerReward(player);
	}

}
