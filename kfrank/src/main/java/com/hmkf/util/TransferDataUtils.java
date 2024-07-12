package com.hmkf.util;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.util.ServerUtils;
import com.hmkf.db.KfDBUtils;
import com.hmkf.leaderboard.KfLeaderBiz;
import com.hmkf.model.KFPlayer;

public class TransferDataUtils {
    public static void allChange(KFPlayer kfPlayer) {
        kfPlayer.getLevelPlayer().SetChanged();
        kfPlayer.getLevelPlayerInfo().SetChanged();
    }

    public static void transPlayerFromDb(String fromDb, List<Integer> typeIds, int toTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameTypeId").in(typeIds));
        List<KFPlayer> players = MongoUtils.getMongodDB(fromDb).query(query, KFPlayer.class);
        for (KFPlayer kfPlayer : players) {
            kfPlayer.setGameTypeId(toTypeId);
            allChange(kfPlayer);
            kfPlayer.save();

            KfLeaderBiz.updatePlayerRank(kfPlayer);
            System.err.println(kfPlayer.getId() + "转移完成:" + toTypeId);
        }
    }

    public static void transPlayer(List<Integer> typeIds, int toTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameTypeId").in(typeIds));
        List<KFPlayer> players = KfDBUtils.getMongoDB().query(query, KFPlayer.class);
        for (KFPlayer kfPlayer : players) {
            kfPlayer.setGameTypeId(toTypeId);
            kfPlayer.save();

            KfLeaderBiz.updatePlayerRank(kfPlayer);
            System.err.println(kfPlayer.getId() + "转移完成:" + toTypeId);
        }
    }

    public static void transPlayerFromDb(String fromDb, List<Integer> serverIds, int fromTypeId, int toTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameTypeId").in(fromTypeId));
        List<KFPlayer> players = MongoUtils.getMongodDB(fromDb).query(query, KFPlayer.class);
        for (KFPlayer kfPlayer : players) {
            int serverId = ServerUtils.getCreateServerId(kfPlayer.getId());
            if (serverIds.contains(serverId)) {
//				kfPlayer.setGameTypeId(toTypeId);
//	    		kfPlayer.save();
//	    		
//	    		KfLeaderBiz.updatePlayerRank(kfPlayer);
//	    		System.err.println(kfPlayer.getId()+"转移完成:"+toTypeId);
                System.err.println(kfPlayer.getId() + "转移");
            }
        }
    }

    public static void transPlayerFromDb(List<Integer> serverIds, int fromTypeId, int toTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameTypeId").in(fromTypeId));
        List<KFPlayer> players = KfDBUtils.getMongoDB().query(query, KFPlayer.class);
        for (KFPlayer kfPlayer : players) {
            int serverId = ServerUtils.getCreateServerId(kfPlayer.getId());
            if (serverIds.contains(serverId)) {
//				kfPlayer.setGameTypeId(toTypeId);
//	    		kfPlayer.save();
//	    		
//	    		KfLeaderBiz.updatePlayerRank(kfPlayer);
//	    		System.err.println(kfPlayer.getId()+"转移完成:"+toTypeId);
                System.err.println(kfPlayer.getId() + "转移");
            }
        }
    }

    public static void main(String[] args) {
        //在49.233.70.122
//		transPlayer(Lists.newArrayList(6,7,10,13,14), 4);


    }
}
