package com.hm.db;

import com.hm.model.srvtypemgr.ServerTypeManager;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SrvTMgrUtils {

    public static Set<String> getMarks() {
        MongodDB mongo = MongoUtils.getLoginMongodDB();
        Query query = new Query();
        query.limit(Integer.MAX_VALUE);
        List<ServerTypeManager> list = mongo.queryAll(ServerTypeManager.class);
        Set<String> marks = new HashSet<>();
        list.stream().map(ServerTypeManager::getMarks).forEach(marks::addAll);

        return marks;
    }
}
