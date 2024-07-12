package com.hm.model.activity.kfactivity;

import com.google.common.collect.Lists;
import com.hm.libcore.util.string.StringUtil;
import com.hm.libcore.mongodb.MongoUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseKfServerGroup {
	@Id
	private String id;
	protected List<Integer> serverIds = Lists.newArrayList();
	private String url;
	
	public BaseKfServerGroup(List<Integer> serverIds,String url) {
		this.id = StringUtil.list2Str(serverIds, "_");
		this.serverIds = serverIds;
		this.url = url;
	}
	
	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
	
	public static <T extends BaseKfServerGroup> T findFitKfServerGroup(int serverId,Class<T> entityClass) {
		Query query = new Query();
		Criteria criteria = Criteria.where("serverIds").in(serverId);
		query.addCriteria(criteria);
		return MongoUtils.getLoginMongodDB().queryOne(query, entityClass);
	}
	public static <T extends BaseKfServerGroup> List<T> findKfServerGroup(String url,Class<T> entityClass) {
		Query query = new Query();
		Criteria criteria = Criteria.where("url").is(url);
		query.addCriteria(criteria);
		return MongoUtils.getLoginMongodDB().query(query, entityClass);
	}
	public static <T extends BaseKfServerGroup> List<T> getAllServerGroup(Class<T> entityClass) {
		return MongoUtils.getLoginMongodDB().queryAll(entityClass);
	}
	
	public static <T extends BaseKfServerGroup> void dropColl(Class<T> entityClass) {
		MongoUtils.getLoginMongodDB().dropCollection(entityClass);
	}
}
