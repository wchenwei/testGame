package com.hm.redis;

import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "titlemanage")
public class NamingServer {
    @Id
    private int serverId;
    @Field("name")
    private String name;
    @Field("showMsg")
    private String info;
    @Field("expireTime")
    private long endTime;

    public boolean checkTime() {
        return this.endTime > System.currentTimeMillis();
    }

    public static Map<Integer, NamingServer> queryAll() {
        MongodDB mongo = MongoUtils.getLoginMongodDB();
        Query query = new Query(Criteria.where("expireTime").gte(System.currentTimeMillis()));
        query.limit(Integer.MAX_VALUE);
        Map<Integer, NamingServer> nameMap = Maps.newHashMap();
        for (NamingServer namingServer : mongo.query(query, NamingServer.class)) {
            nameMap.put(namingServer.getServerId(), namingServer);
        }
        return nameMap;
    }

}
