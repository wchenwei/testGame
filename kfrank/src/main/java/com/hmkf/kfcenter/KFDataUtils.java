package com.hmkf.kfcenter;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hmkf.db.KfDBUtils;

public class KFDataUtils {
    public static final String IdName = "center";

    public static MongodDB getMongoDB() {
        return MongoUtils.getMongodDB(KfDBUtils.KFSportsName);
    }

    public static void saveOrUpdate(KfCenterData data) {
        getMongoDB().update(data);
    }

    public static <T> T getPlayerFromDB(String id, Class<T> entityClass) {
        MongodDB mongo = getMongoDB();
        if (mongo == null) {
            return null;
        }
        return mongo.get(id, entityClass);
    }

    public static KfCenterData getKfCenterData() {
        return getPlayerFromDB(IdName, KfCenterData.class);
    }

}
