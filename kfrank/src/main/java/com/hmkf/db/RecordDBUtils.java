package com.hmkf.db;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.collect.Lists;
import com.hm.model.war.FightDataRecord;
import com.hmkf.model.kfwarrecord.KfWarRecord;

public class RecordDBUtils {
    public static List<KfWarRecord> getKfWarRecord(long playerId) {
        Query query = new Query();
//    	query.addCriteria(Criteria.where("areaId").is(serverAreaId).and("groupId").is(groupId));
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("atkId").is(playerId), Criteria.where("defId").is(playerId));
        query.addCriteria(criteria);
        query.with(Sort.by(Direction.DESC, "addTime"));//从大到小排序

        query.limit(30);
        return KfDBUtils.getMongoDB().query(query, KfWarRecord.class);
    }

    public static List<KfWarRecord> getKfWarRecord(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        return KfDBUtils.getMongoDB().query(query, KfWarRecord.class);
    }

    public static KfWarRecord getKfWarRecord(String id) {
        return KfDBUtils.getMongoDB().get(id, KfWarRecord.class);
    }

    public static List<FightDataRecord> getFightDataRecord(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        return KfDBUtils.getMongoDB().query(query, FightDataRecord.class, "FightDataRecord");
    }


    public static void main(String[] args) {
        List<FightDataRecord> recordList = getFightDataRecord(Lists.newArrayList("fj", "fk"));
        System.err.println(recordList.size());
    }
}
