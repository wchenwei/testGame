package com.hm.db;

import com.hm.model.player.Player;
import com.hm.libcore.mongodb.MongodDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 跨服获取player信息要过滤的字段
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/6/7 11:50
 */
public class KFPlayerFiledBuilder {
    /**
     * 获取过滤的玩家资源
     *
     * @return
     */
    public static BasicDBObject buildPlayerFilterFiled() {
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定返回的字段
        fieldsObject.put("playerActivity", false);
        fieldsObject.put("playerDailyTask", false);
        fieldsObject.put("playerBattle", false);
        fieldsObject.put("playerGame", false);
        return fieldsObject;
    }

    public static Player getPlayerFromKF(MongodDB mongo, long playerId) {
        DBObject dbObject = new BasicDBObject();
        BasicDBObject fieldsObject = KFPlayerFiledBuilder.buildPlayerFilterFiled();
        Query query = new BasicQuery(dbObject.toString(), fieldsObject.toString());
        query.addCriteria(Criteria.where("_id").is(playerId));
        return mongo.queryOne(query, Player.class);
    }
}
