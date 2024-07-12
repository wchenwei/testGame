package com.hm.db;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.action.player.biz.PlayerNameBiz;
import com.hm.cache.KFPlayerCacheManager;
import com.hm.cache.PlayerCacheManager;
import com.hm.container.PlayerContainer;
import com.hm.enums.StatisticsType;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.util.ServerUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayerUtils {

    public static Player getPlayer(WorldTroop worldTroop) {
        return getPlayer(worldTroop.getPlayerId());
    }

    public static Player getPlayer(long playerId) {
        Player player = getOnlinePlayer(playerId);
        if (player != null) {
            return player;
        }
        if(GameServerManager.getInstance().isServerMachinePlayer(playerId)) {
            return PlayerCacheManager.getInstance().getPlayer(playerId);
        }else{
            return KFPlayerCacheManager.getInstance().getKFPlayer(playerId);
        }
    }
    
    public static Player getOnlinePlayer(long playerId) {
        return PlayerContainer.getPlayer(playerId);
    }
    
    /**
     * 从在线或google_cache里获取玩家信息.获取不到返回null,不从数据库读取
     * @param playerId
     * @return
     */
    public static Player getPlayerFromOnlineOrCache(long playerId) {
    	Player player = getOnlinePlayer(playerId);
        if (player != null) {
            return player;
        }
        return PlayerCacheManager.getInstance().getPlayerOrNull(playerId);
    }

    public static Player getPlayerFromDB(long playerId) {
    	int serverId = ServerUtils.getServerId(playerId);
        MongodDB mongo = MongoUtils.getMongodDB(serverId);
        if(mongo == null) {
        	System.err.println("mongo is err:"+playerId);
        	return null;
        }
        return mongo.get(playerId, Player.class);
    }

    public static Player getPlayerFromKF(long playerId) {
        if(GameServerManager.getInstance().isServerMachinePlayer(playerId)) {
            return getPlayer(playerId);
        }
        return KFPlayerCacheManager.getInstance().getKFPlayer(playerId);
    }
    
    public static long getPlayerTotalCount(int serverId) {
    	MongodDB mongo = MongoUtils.getMongodDB(serverId);
    	return mongo.count("player");
    }
    
    /**
     * 获取服务器全部玩家个别信息
     * @param crieria 过滤条件
     * @param sort 排序
     * @param pageSize 不需要分页的话填0
     * @param pageNo 不需要分页的话填0
     * @return
     */
    public static List<Player> getPlayerSimple(int serverId,Criteria crieria,Sort sort,int pageSize,int pageNo){
    	MongodDB mongo = MongoUtils.getMongodDB(serverId);
    	DBObject dbObject = new BasicDBObject();
    	BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        fieldsObject.put("name", true);
        fieldsObject.put("playerLevel.lv", true);
        fieldsObject.put("playerStatistics.lifeStatistics."+StatisticsType.RECHARGE.getType(), true);
        fieldsObject.put("playerBaseInfo.createDate", true);
        fieldsObject.put("playerBaseInfo.lastLoginDate", true);
        fieldsObject.put("playerStatistics.lifeStatistics."+StatisticsType.LOGIN_DAYS.getType(), true);
        fieldsObject.put("playerMission.missionId", true);
        fieldsObject.put("channelId", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        query.addCriteria(crieria);
        if(pageNo!=0&&pageSize!=0){
        	query.with(PageRequest.of(pageNo-1, pageSize,sort));
        }
        return mongo.query(query, Player.class);
    }

    /**
     * 同步整个服用户到redis
     *
     * @param serverId
     */
    public static void syncPlayerToRedis(int serverId) {
        MongoTemplate mongoTemplate = MongoUtils.getServerMongoTemplate(serverId);
        if (mongoTemplate == null) {
            return;
        }
        for (Document document : mongoTemplate.getCollection("player").find()) {
            Player player = mongoTemplate.getConverter().read(Player.class, document);
            if (player != null) {
                RedisUtil.updateRedisPlayer(player);
            }
        }
    }

    /**
     * 获取该服所有重复的玩家名字
     *
     * @param serverId
     * @return
     */
    public static List<String> findAllRepeatPlayerName(int serverId) {
        List<String> nameList = Lists.newArrayList();
        MongoTemplate mongoTemplate = MongoUtils.getServerMongoTemplate(serverId);
        if (mongoTemplate == null) {
            return nameList;
        }

        MongoCollection<Document> collection = mongoTemplate.getCollection("player");

        Consumer<Document> processBlock = document -> {
            if (document.containsKey("name")) {
                nameList.add(document.getString("name"));
            }
        };

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$group", new Document()
                                .append("_id", "$name")
                                .append("count", new Document()
                                        .append("$sum", 1.0)
                                )
                        ),
                new Document()
                        .append("$match", new Document()
                                .append("count", new Document()
                                        .append("$gt", 1.0)
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("_id", 0.0)
                                .append("name", "$_id")
                        )
        );

        collection.aggregate(pipeline).forEach(processBlock);
        return nameList;
    }

    public static List<Player> getPlayerByName(int serverId, String name) {
        MongodDB mongo = MongoUtils.getMongodDB(serverId);
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("name", name);
        BasicDBObject fieldsObject = new BasicDBObject();
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        return mongo.query(query, Player.class);
    }
    
    public static void saveOrUpdate(BasePlayer player) {
        MongodDB mongo = MongoUtils.getMongodDB(player.getServerId());
        mongo.update(player);
    }
    public static List<Player> getStatistic(int serverId,Criteria criteria){
    	MongodDB mongo = MongoUtils.getMongodDB(serverId);
    	DBObject dbObject = new BasicDBObject();
    	BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("playerCurrency.value", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        query.addCriteria(criteria);
        return mongo.query(query, Player.class);
    }
    public static boolean checkName(String name, int serverId) {
//        //检查当前在线用户
//        boolean haveSame = PlayerContainer.getOnlinePlayersByServerId(serverId).stream().anyMatch(e -> StrUtil.equals(e.getName(), name));
//        if (haveSame) {
//            return false;
//        }
//        MongodDB mongo = MongoUtils.getMongodDB(serverId);
//        DBObject dbObject = new BasicDBObject();
//        dbObject.put("name", name);  //查询条件
//        BasicDBObject fieldsObject = new BasicDBObject();
//        //指定返回的字段
//        fieldsObject.put("_id", true);
//        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
//        return !mongo.isExists(query, Player.class);
        return !SpringUtil.getBean(PlayerNameBiz.class).checkNameRepeat(serverId,name);
    }
    
    public static Player getInviteCodePlayer(String inviteCode, int serverId) {
        MongodDB mongo = MongoUtils.getMongodDB(serverId);
        DBObject dbObject = new BasicDBObject();
        dbObject.put("inviteCode", inviteCode);  //查询条件
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        return mongo.queryOne(query, Player.class);
    }
    
	public static long getPlayerId(long uid, int serverId,int dbId) {
        MongodDB mongo = MongoUtils.getMongodDB(dbId);
        DBObject dbObject = new BasicDBObject();
        dbObject.put("uid", uid);  //查询条件
        dbObject.put("createServerId", serverId);  //查询条件
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        Player player = mongo.queryOne(query, Player.class);
        if (player != null) {
            return player.getId();
        }
        return 0;
    }
	//查询uid下的playerId
	public static List<Long> getPlayerByUid(long uid, int serverId) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
        DBObject dbObject = new BasicDBObject();
        dbObject.put("uid", uid);  //查询条件
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        List<Player> players = mongo.query(query, Player.class);
        return players.stream().map(e -> e.getId()).collect(Collectors.toList());
	}
	public static Player getPlayerIdAndIdCode(long uid, int serverId,int dbId) {
        MongodDB mongo = MongoUtils.getMongodDB(dbId);
        DBObject dbObject = new BasicDBObject();
        dbObject.put("uid", uid);  //查询条件
        dbObject.put("createServerId", serverId);  //查询条件
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        fieldsObject.put("createServerId", true);
        fieldsObject.put("idCode", true);
        fieldsObject.put("serverId", true);
        fieldsObject.put("playerVipInfo", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        return mongo.queryOne(query, Player.class);
    }
	public static boolean playerIsExist(long uid, int createServerId,int dbId) {
		MongodDB mongo = MongoUtils.getMongodDB(dbId);
        Query query = Query.query(Criteria.where("uid").is(uid).and("createServerId").is(createServerId));
        return mongo.isExists(query, Player.class);
	}
	
	
	public static List<Long> getPlayerIdByCreateServerId(int serverId,int createServerId) {
        MongodDB mongo = MongoUtils.getMongodDB(serverId);
        DBObject dbObject = new BasicDBObject();
        dbObject.put("createServerId", createServerId);  //查询条件
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        List<Player> players = mongo.query(query, Player.class);
        return players.stream().map(e -> e.getId()).collect(Collectors.toList());
    }
	
	//获取远征错误的玩家
	public static List<Long> getPlayerExpeditionError(int serverId){
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
        DBObject dbObject = new BasicDBObject();
        dbObject.put("playerExpedition.id", 0);  //查询条件
        dbObject.put("playerLevel.lv",  new BasicDBObject("$gte", 30));  //查询条件
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        List<Player> players = mongo.query(query, Player.class);
        return players.stream().map(e -> e.getId()).collect(Collectors.toList());
	}
	
	//获取武器状态错误的玩家
	public static List<Long> getPlayerArmsError(int serverId){
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
        DBObject dbObject = new BasicDBObject();
        dbObject.put("playerArms.state", 1);  //查询条件
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        List<Player> players = mongo.query(query, Player.class);
        return players.stream().map(e -> e.getId()).collect(Collectors.toList());
	}

    public static List<Long> getPlayerIdByName(int serverId, String name) {
        MongodDB mongo = MongoUtils.getMongodDB(serverId);
        DBObject dbObject = new BasicDBObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        query.addCriteria(Criteria.where("name").regex(name));
        List<Player> players = mongo.query(query, Player.class);
        return players.stream().map(e -> e.getId()).collect(Collectors.toList());
    }
    /**
     * 获取所有玩家的名字
     * @param serverId
     * @return
     */
    public static List<Player> getPlayerName(int serverId) {
        MongodDB mongo = MongoUtils.getMongodDB(serverId);
        return getPlayerName(mongo);
    }
    public static List<Player> getPlayerName(MongodDB mongo) {
        DBObject dbObject = new BasicDBObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("_id", true);
        fieldsObject.put("name", true);
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        return mongo.query(query, Player.class);
    }

    public static Set<String> getPlayerNameForStr(MongodDB mongo) {
        MongoTemplate template = mongo.getMongoTemplate();
        final Set<String> nameList = Sets.newConcurrentHashSet();
        template.getCollection("player").distinct("name", String.class)
                .forEach((Consumer<? super String>) e -> nameList.add(e));
        return nameList;
    }
}
