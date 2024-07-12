package com.hm.action.http;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.cache.PlayerCacheManager;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import lombok.Data;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Data
public class ServerChangeCamp {
	@Id
	private int serverId;
	private String info;
	
	@Transient
	private transient Map<Integer,Integer> campMap = Maps.newHashMap();
	
	public int getChangeCamp(int campId) {
		return this.campMap.getOrDefault(campId, campId);
	}

	public void init() {
		for (String camps : info.split(",")) {
			String[] cc = camps.split(":");
			campMap.put(Integer.parseInt(cc[0]), Integer.parseInt(cc[1]));
		}
	}
	
	public static Map<Integer,ServerChangeCamp> getServerChangeCamp() {
		MongodDB mongoDb = MongoUtils.getLoginMongodDB();
		Map<Integer,ServerChangeCamp> campMap = Maps.newHashMap();
		for (ServerChangeCamp serverChangeCamp : mongoDb.queryAll(ServerChangeCamp.class)) {
			serverChangeCamp.init();
			campMap.put(serverChangeCamp.getServerId(), serverChangeCamp);
		}
		return campMap;
	}
	
	 public static void changeCamp(MongoCollection<Document> collPlayer) {
		 Map<Integer,ServerChangeCamp> campMap = getServerChangeCamp();
		 if(campMap.isEmpty()) {
			 return;
		 }
        // 批量改玩家名
        List<WriteModel<Document>> writeModelList = Lists.newArrayList();
		 Consumer<Document> updateNameBlock = document -> {
            Integer serverId = document.getInteger("serverId");
            int oldCamp = document.getInteger("camp");
            ServerChangeCamp serverChangeCamp = campMap.get(serverId);
            if(serverChangeCamp == null) {
            	return;
            }
            int newCamp = serverChangeCamp.getChangeCamp(oldCamp);
            if(oldCamp == newCamp) {
            	return;
            }
            long playerId = document.getInteger("_id");
            PlayerCacheManager.getInstance().removePlayerCache(playerId);
            Document f = new Document("_id", playerId);
            Document update = new Document("$set", new Document("camp", newCamp));
            UpdateOneModel<Document> model = new UpdateOneModel<>(f, update);
            writeModelList.add(model);
            if (writeModelList.size() == 200) {
                collPlayer.bulkWrite(writeModelList);
                writeModelList.clear();
            }
        };
        // { "name":{"$concat": [  {$substr:['$createServerId', 0, 5] }, "_", "$name" ]} }
        List<? extends Bson> pipeline = Arrays.asList(
                new Document("$project", new Document("serverId", "$serverId").append("camp", "$camp"))
        );
        collPlayer.aggregate(pipeline).forEach(updateNameBlock);
        if (!writeModelList.isEmpty()) {
            collPlayer.bulkWrite(writeModelList);
        }
    }
	 
	public static void main(String[] args) {
		 ServerChangeCamp serverChangeCamp = new ServerChangeCamp();
		 serverChangeCamp.setServerId(27);
		 serverChangeCamp.setInfo("2:3,3:2");
		 MongoUtils.getLoginMongodDB().save(serverChangeCamp);
//		getServerChangeCamp();
	}
}
