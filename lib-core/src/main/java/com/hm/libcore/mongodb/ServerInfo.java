package com.hm.libcore.mongodb;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.serverConfig.GameServerMachine;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Data
public class ServerInfo {
    private int server_id;
    private String name;
    private int db_id;//合服的id
    private int server_groupid;
    private int server_typeid;
    private int servermachineId;
    private boolean testServerFlag = false;
    private int openstate;



    @Transient
    private transient GameServerMachine serverMachine;

    public boolean isTestServer() {
        return testServerFlag;
    }
    public int getType() {
        return server_typeid;
    }

    public boolean isOpen() {
        return openstate == 0;
    }

    public static ServerInfo getServerInfoFromDB(int serverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("server_id").is(serverId));
        List<ServerInfo> resultList = MongoUtils.getLoginMongodDB().query(query, ServerInfo.class);
        if(CollUtil.isNotEmpty(resultList)) {
            return resultList.get(0);
        }
        return null;
    }
}
