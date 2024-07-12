package com.hm.db;

import cn.hutool.core.collection.CollUtil;
import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ActivityUtils {
	public final static String TableName = "activity";
	public static void saveOrUpdate(AbstractActivity activity) {
		MongodDB mongo = MongoUtils.getMongodDB(activity.getServerId());
		mongo.save(activity,TableName);
	}
	
	public static AbstractActivity getActivity(int serverId,String id) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		return mongo.get(id, AbstractActivity.class,TableName);
	}
	
	public static void delete(AbstractActivity activity) {
		MongodDB mongo = MongoUtils.getMongodDB(activity.getServerId());
		mongo.remove(activity,TableName);
	}
	
	public static boolean haveActivity(int serverId,ActivityType type) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		Criteria criteria = Criteria.where("type").is(type.getType());
		Query query = new Query(criteria);
		query.limit(1);
		List<AbstractActivity> list = mongo.query(query, AbstractActivity.class,TableName);
		return CollUtil.isNotEmpty(list);
	}
	
	public static List<AbstractActivity> getActivityList(int serverId){
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		Criteria criteria = new Criteria().orOperator(
				Criteria.where("endTime").gt(System.currentTimeMillis()),
				Criteria.where("endTime").lt(0));
		Query query = new Query(criteria);
		query.limit(Integer.MAX_VALUE);
		query.with(Sort.by(Direction.DESC, "startTime"));//从大到小排序
		return mongo.query(query, AbstractActivity.class,TableName);
	}
	
}
