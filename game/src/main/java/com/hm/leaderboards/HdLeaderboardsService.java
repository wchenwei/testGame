package com.hm.leaderboards;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hm.libcore.leaderboards.*;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.RankType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.mq.redis.RedisRankSender;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**   
 * @Description: 排行榜
 * @author siyunlong  
 * @date 2016年8月18日 上午10:05:29 
 * @version V1.0   
 */
@Slf4j
public class HdLeaderboardsService extends LeaderboardsService{
	private static final HdLeaderboardsService Instance = new HdLeaderboardsService();
	private static final int PageNum = 10;//每页的条数
	public static HdLeaderboardsService getInstance() {
		return Instance;
	}

	public void init() {
		log.info("排行榜按照cmq启动");
		RedisRankSender rankSender = new RedisRankSender();
		rankSender.startProducer();
		setAbstractRankCmq(rankSender);
	}
	/**
	 * 更新玩家排行榜信息
	 * @param player
	 */
	public void updatePlayerInfo(Player player) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(player);
		long combat = player.getPlayerDynamicData().getTroopCombat();
		putDescriptor.AddScore(RankType.Combat.getRankName(player), combat);
		PutScores(putDescriptor);
//			updatePlayerKFRank(player);
	}

	/**
	 * 更新以服务器类型为大组的排行
	 * @param player
	 */
	public void updatePlayerKFRank(Player player) {
		boolean buildFuc = player.playerBuild().isUnlockExtort();
		boolean tradeFuc = player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TradeStock);
		if(!(buildFuc || tradeFuc)) {
			return;
		}
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		if(serverData == null || serverData.getGameServerType() == null) {
			return;
		}
		long combat = player.getPlayerDynamicData().getCombat();
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(serverData.getGameServerType().getId(),player.getId()+"");
		if(buildFuc) {
			putDescriptor.AddScore(RankType.BuildCombat.getRankName(player), combat);
		}
		if(tradeFuc) {
			putDescriptor.AddScore(RankType.TradeCombatKF.getRankName(player), combat);
		}
		PutScores(putDescriptor);
	}
	
//	
//	/**
//	 * 更新玩家最强坦克排行榜信息
//	 * @param player
//	 */
//	public void updatePlayerMaxCombatTank(Player player) {
//		long combat = player.getPlayerDynamicData().getMaxTankCombat();
//		updatePlayerRankOverride(player, RankType.TankCombat, combat);
//	}

    /**
     * zadd 覆盖旧score
     * @param player
     * @param rankType
     * @param value
     */
	public void updatePlayerRankOverride(BasePlayer player,RankType rankType,double value) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(player);
		putDescriptor.AddScore(rankType.getRankName(player), value);
		PutScores(putDescriptor);
	}

	/**
	 * zadd 覆盖旧score 数值按照时间二次排序，数值相同先到的在前
	 * @param playerId
	 * @param serverId
	 * @param rankName
	 * @param value
	 */
	public void updatePlayerRankOverrideForTime(long playerId,int serverId,String rankName,long value) {
		updatePlayerRankOverride(playerId, serverId, rankName, DateUtil.parseTimeDouble(value));
	}

	/**
	 * zadd 覆盖旧score
	 * @param player
	 * @param rankType
	 * @param value
	 */
	public void updatePlayerRankOverride(long playerId,int serverId,String rankName,double value) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(serverId,playerId+"");
		putDescriptor.AddScore(rankName, value);
		PutScores(putDescriptor);
	}

	/**
	 * 数值按照时间二次排序，数值相同先到的在前
	 * @param player
	 * @param rankType
	 * @param value
	 */
	public void updatePlayerRankOverrideForTime(BasePlayer player,RankType rankType,long value) {
		updatePlayerRankOverride(player, rankType, DateUtil.parseTimeDouble(value));
	}

	//再原有排名数据基础+此数据
	public void updatePlayerRankForAdd(Player player,RankType rankType,double add) {
		updatePlayerRankForAdd(player, rankType.getRankName(player), add);
	}
	public void updatePlayerRankForAdd(Player player,String rankName,double add) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(player);
		putDescriptor.AddScore(rankName, add);
		PutScoresForAdd(putDescriptor);
	}
	/**
	 * 按照时间作第二排序更新玩家排行分数
	 * add 是转换的时间值
	 * int oldScore = (int)getOldScore;
	 * int newScore = add + oldScore;
	 * @param player
	 * @param rankName
	 * @param add
	 */
	public void updatePlayerRankForTimeAdd(Player player,RankType rankType,long add) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(player);
		putDescriptor.AddScore(rankType.getRankName(player), DateUtil.parseTimeDouble(add));
		PutScoresForTimeAdd(putDescriptor);
	}
	public void updatePlayerRankForCombatAdd(int serverId,long playerId,String rankName,long combat,long add) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(serverId,playerId+"");
		putDescriptor.AddScore(rankName, LeaderboardBiz.toCombatScore(add, combat));
		PutScoresForTimeAdd(putDescriptor);
	}
	
	//初始化玩家竞技场排行
	public boolean initArenaPlayer(BasePlayer player, RankType rankType) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(player);
		int value = -PrimaryKeyWeb.getIntPrimaryKey(rankType.getRankName(), player.getServerId());
		putDescriptor.AddScore(rankType.getRankName(player), value);
		return PutScoresSync(putDescriptor);
	}
	
	public void exchangeArena(int serverId, RankType rankType, String pid, String oid) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankType.getRankName());
		exchangeScore(getRankDescriptor, pid, oid);
	}
	
	//获取排行信息
	public List<LeaderboardInfo> getGameRanks(RankType rankType,Player player,int pageNo) {
		return getGameRank(player.getServerId(), rankType.getRankName(player), pageNo);
	}

	//获取玩家自己的排名
	public long getPlayerRank(RankType rankType,BasePlayer player) {
		return getRank(player.getServerId(), rankType.getRankName(player), player.getId()+"");
	}

	//获取排行榜指定排名的
	public List<LeaderboardInfo> getGameRank(RankType rankType,Player player,List<Integer> ranks) {
		ranks = ranks.stream().map(e->e-1).collect(Collectors.toList());//每个变量都-1
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(player.getServerId()), rankType.getRankName(player));
		return parseList(getRankByRanks(getRankDescriptor, ranks));
	}

	//获取排行榜指定排名的
	public List<LeaderboardInfo> getGameRank(RankType rankType, int serverId, List<Integer> ranks) {
		ranks = ranks.stream().map(e->e-1).collect(Collectors.toList());//每个变量都-1
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankType.getRankName());
		return parseList(getRankByRanks(getRankDescriptor, ranks));
	}
	public List<String> getGameRankByAround(int serverId,String rankName, String playerId,int aroundNum) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		String json = getRankByAround(getRankDescriptor, playerId,aroundNum);
		return GSONUtils.FromJSONString(json, new TypeToken<List<String>>(){}.getType());
	}
	//获取指定排名的玩家id
	public long getGameRank(RankType rankType,Player player,int rank){
		LeaderboardInfo info = getGameRank(rankType,player,Lists.newArrayList(rank)).stream().findAny().orElse(null);
		return info==null?-1L:info.getIntId();
	}
	//获取排行榜指定排名的
	public long getGameRank(RankType rankType, int serverId, int rank) {
		LeaderboardInfo info = getGameRank(rankType,serverId,Lists.newArrayList(rank)).stream().findAny().orElse(null);
		return info==null?-1L:info.getIntId();
	}
	public List<LeaderboardInfo> getGameRank(RankType rankType,Player player,int start,int end) {
		return getGameRank(player.getServerId(),rankType.getRankName(player), start, end);
	}
	
	public List<LeaderboardInfo> getMaxCombatRank(int serverId,String rankName,List<String> userIds) {
		List<Integer> ranks = Lists.newArrayList(0,1,2,3,4,5,6,7,8,9,99);
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		return parseList(getRankByRanksAndUserIds(getRankDescriptor, ranks,userIds));
	}
	
	public List<LeaderboardInfo> getGameRankThanScore(int serverId,String rankName,long minScore,long maxScore) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		return parseList(getRankThanScore(getRankDescriptor, minScore,maxScore));
	}
	public List<Integer> getUserIdsThanScore(int serverId,String rankName,long minScore,long maxScore) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		List<Integer> userIds = getUserIdsByRankTankScore(getRankDescriptor, minScore, maxScore);
		return userIds;
	}
	public List<LeaderboardInfo> getGameRankByPlayerIds(int serverId,String rankName,Collection<Long> playerIds) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		return parseList(getRankByIds(getRankDescriptor, playerIds));
	}
	//获取指定排行玩家排名信息
	public LeaderboardInfo getLeaderboardInfo(BasePlayer player,RankType rankType) {
		List<LeaderboardInfo> tempList = getGameRankByPlayerIds(player.getServerId(), rankType.getRankName(player), Lists.newArrayList(player.getId()));
		if(CollUtil.isNotEmpty(tempList)) {
			return tempList.get(0);
		}
		return null;
	}
	public LeaderboardInfo getLeaderboardInfo(BasePlayer player,int serverId,String rankName) {
		List<LeaderboardInfo> tempList = getGameRankByPlayerIds(serverId, rankName, Lists.newArrayList(player.getId()));
		if(CollUtil.isNotEmpty(tempList)) {
			return tempList.get(0);
		}
		return null;
	}
	
	
	public long getRank(int serverId,String rankName,String playerId) {
		long rank = GetRank(createTableName(serverId,rankName), playerId);
		return rank >= 0 ? rank+1:rank;
	}
	/**
	 * 获取排行总条数
	 * @param rankName
	 * @return
	 */
	public long getRankTotal(int serverId,String rankName) {
		return GetRecordsCount(createTableName(serverId,rankName));
	}
	
	public static String createTableName(int serverId,String rankName) {
		return createLeaderMark(serverId)+":"+rankName;
	}
	
//	public void clearRank(int serverId,List<String> ranks) {
//		renameGameSet(serverId,ranks);
//	}
	
	/**
	 * 改变排行榜
	 * 例如 score为类,执行此方法后
	 * uk:score 积分排行榜
	 * 1,先把uk:score:pre 重命名为 uk:score:20160816,防止第二名表名冲突
	 * 2,uk:score 重命名为uk:score:pre
	 * @param leaderName
	 */
	public void renameGameSet(int serverId,List<String> lbs) {
//		String addSuffix = TimeUtils.getYesterday();
//		LeaderboardRenameDescriptor renameDescriptor = new LeaderboardRenameDescriptor(createLeaderMark(serverId), "", addSuffix);
//		onlineRenameSetList(renameDescriptor,lbs);
		RankRedisUtils.renameRankList(serverId, lbs);
	}  
	public void renameGameSet(int serverId,RankType type) {
		renameGameSet(serverId, Lists.newArrayList(type.getRankName()));
	}
	
	//充值排行数值全部为0
	public boolean resetRankToScore(int serverId,RankType type,int resetScore) {
		ResetRankToScore resetRankToScore = new ResetRankToScore(createLeaderMark(serverId), type.getRankName(),resetScore);
		return resetRankToScore(resetRankToScore);
	}
	
	/**
	 * 删除keys
	 * @param keys
	 */
	public void delGameKeys(int serverId,List<String> keys) {
		LeaderboardDelKeys delKeys = new LeaderboardDelKeys(createLeaderMark(serverId));
		delKeys(delKeys, keys);
	}
	
	//删除玩家排行
	public boolean delRankPlayer(int serverId,RankType rankType,long playerId) {
		LeaderboardPutDescriptor temp = new LeaderboardPutDescriptor(createTableName(serverId, rankType.getRankName()), playerId+"");
		return DelScores(temp);
	}
	
	//=========================全服排方法===========================================
	public void updateAllServerPlayerRankOverride(String rankName,long id,double value) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(0,id+"");
		putDescriptor.AddScore(rankName, value);
		PutScores(putDescriptor);
	}
	public void updateKfServerPlayerRankOverride(int markId,String rankName,long playerId,double value) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(markId,playerId+"");
		putDescriptor.AddScore(rankName, value);
		PutScores(putDescriptor);
	}
	public void updateKfRankOverrideTime(String rankName,long playerId,long add) {
		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(0,playerId+"");
		putDescriptor.AddScore(rankName, DateUtil.parseTimeDouble(add));
		PutScores(putDescriptor);
	}
	public List<LeaderboardInfo> getAllServerGameRanks(String rankName,int pageNo) {
		return getGameRank(0,rankName, pageNo);
	}
	
	public LeaderboardInfo getAllServerLeaderboardInfo(String rankName,long id) {
		List<LeaderboardInfo> tempList = getGameRankByPlayerIds(0, rankName, Lists.newArrayList(id));
		if(CollUtil.isNotEmpty(tempList)) {
			return tempList.get(0);
		}
		return null;
	}
	//获取全服玩家自己的排名
	public long getAllServerPlayerRank(RankType rankType,long playerId) {
		return getAllServerPlayerRank(rankType.getRankName(), playerId+"");
	}
	
	public long getAllServerPlayerRank(String rankName,String playerId) {
		long rank = GetRank(createTableName(0,rankName), playerId);
		return rank >= 0 ? rank+1:rank;
	}
	/**
	 * 获取排名
	 * @param rankName 排行榜名
	 * @param start 开始排名
	 * @param end 结束排名
	 * @return
	 */
	public List<LeaderboardInfo> getAllServerGameRank(String rankName,int start,int end) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(0), rankName);
		String result = getRanks(getRankDescriptor, start-1, end-1);
		return parseList(result);
	}

	/**
	 * 获取排行榜大小
	 *
	 * @param rankType
	 * @return
	 */
	public long getAllServerRankTotal(RankType rankType) {
		return getRankTotal(0, rankType.getRankName());
	}
	//=========================全服排方法===========================================

	/**
	 * 获取排名
	 * @param rankName 排行榜名
	 * @param start 开始排名
	 * @param end 结束排名
	 * @return
	 */
	public List<LeaderboardInfo> getGameRank(int serverId,String rankName,int start,int end) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		String result = getRanks(getRankDescriptor, start-1, end-1);
		return parseList(result);
	}
	
	public LeaderboardInfo getGameRankByRank(int serverId,String rankName,int rank) {
		List<LeaderboardInfo> topList = getGameRank(serverId, rankName, rank, rank);
		if(topList.size() > 0) {
			return topList.get(0);
		}
		return null;
	}
	
	public List<LeaderboardInfo> getGameRank(int serverId,String rankName,int start,int end,List<Long> playerIds) {
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		String result = getRanksAndPlayerIds(getRankDescriptor, start-1, end-1,playerIds);
		return parseList(result);
	}
	
	public ArrayListMultimap<String, LeaderboardInfo> getGameRank(int serverId,List<RankRange> rankRanges) {
		LeaderboardGetRankForNames getRankDescriptor = new LeaderboardGetRankForNames(createLeaderMark(serverId), rankRanges);
		String result = getRanks(getRankDescriptor);
		return parseMap(result);
	}
	public Map<String,Integer> getPlayerRankByList(int serverId,long playerId,List<String> rankNames) {
		LeaderboardGetPlayerRankForList temp = new LeaderboardGetPlayerRankForList(createLeaderMark(serverId), playerId, rankNames);
		String result = getPlayerRankByList(temp);
		return parseRankMap(result);
	}
	public List<LeaderboardInfo> getGameRank(int serverId,String rankName,int pageNo) {
		return getGameRankWithPage(serverId, rankName, pageNo, PageNum);
	}
	
	public List<LeaderboardInfo> getGameRankWithPage(int serverId,String rankName,int pageNo,int pageSize) {
		int start = pageSize*(pageNo-1);
		int end = pageSize*pageNo-1;
		LeaderboardGetRank getRankDescriptor = new LeaderboardGetRank(createLeaderMark(serverId), rankName);
		String result = getRanks(getRankDescriptor, start, end);
		return parseList(result);
	}
	
	public List<LeaderboardInfo> parseList(String result) {
		List<LeaderboardInfo> leaderboardInfos = Lists.newArrayList();
		try {
			if("{}".equals(result) || "null".equals(result)) {
				return leaderboardInfos;
			}
			JsonArray jsonArray = GSONUtils.FromJSONString(result, JsonArray.class);
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement el = jsonArray.get(i);
				LeaderboardInfo leaderboardInfo = new LeaderboardInfo(el);
				leaderboardInfos.add(leaderboardInfo);
			}
		} catch (Exception e) {
			log.error("getGameRank出错"+result, e);
		}
		return leaderboardInfos;
	}
	private ArrayListMultimap<String, LeaderboardInfo> parseMap(String result) {
		ArrayListMultimap<String, LeaderboardInfo> rankMap = ArrayListMultimap.create();
		for (LeaderboardInfo ldb : parseList(result)) {
			rankMap.put(ldb.getRankName(), ldb);
		}
		return rankMap;
	}
	private Map<String,Integer> parseRankMap(String result) {
		Map<String,Integer> rankMap = Maps.newConcurrentMap();
		try {
			if("{}".equals(result) || "null".equals(result)) {
				return rankMap;
			}
			JsonArray jsonArray = GSONUtils.FromJSONString(result, JsonArray.class);
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement el = jsonArray.get(i);
				JsonObject leObj = el.getAsJsonObject();
				int rank = leObj.get("rank").getAsInt();
				rank = rank >= 0 ? rank+1:rank;
				rankMap.put(leObj.get("rankName").getAsString(), rank);
			}
		} catch (Exception e) {
			log.error("getGameRank出错"+result, e);
		}		
		return rankMap;
	}
	public static String createLeaderMark(int serverId) {
		return MongoUtils.getGameDBName() + serverId;
	}
	
	//更新排行榜方法
	public void updateRankOverride(HdLeaderboardPutDescriptor putDescriptor) {
		PutScores(putDescriptor);
	}
	/**
	 */
	public static void main(String[] args) {
		HdLeaderboardsService service = HdLeaderboardsService.getInstance();
		System.err.println("==============");
		System.err.println(service.getGameRankByAround(22, "arena", "n_19", 10));
//		HdLeaderboardPutDescriptor putDescriptor = new HdLeaderboardPutDescriptor(6,600009+"");
//		putDescriptor.AddScore("testrank", 2);
//		System.err.println(service.PutScoresSync(putDescriptor));
//		System.err.println("-------------");
//		service.updateAllServerPlayerRankOverride("allServerRecharge", 1, 100);
//		service.updateAllServerPlayerRankOverride("allServerRecharge", 2, 100);
//		service.updatePlayerRankOverrideForTime(PlayerUtils.getPlayerFromDB(600009), RankType.Recharge, 64800);
//		
//		List<LeaderboardInfo> rankList = service.getAllServerGameRanks("allServerRecharge", 1);
//		for (LeaderboardInfo leaderboardInfo : rankList) {
//			System.err.println(GSONUtils.ToJSONString(leaderboardInfo));
//		}
//		boolean isSuc = service.resetRankToScore(new ResetRankToScore("Zslggame6", "CityKillTank:pre",100));
//		System.err.println(isSuc);
//		System.err.println(service.delRankPlayer(6, RankType.OverallWarRank, 600067));
	}
}
