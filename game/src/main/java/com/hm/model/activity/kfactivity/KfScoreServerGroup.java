package com.hm.model.activity.kfactivity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.string.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "KfScoreServerGroup")
public class KfScoreServerGroup {
	@Id
	private String id;
	private List<Integer> serverIds = Lists.newArrayList();
	private String url;
	
	public KfScoreServerGroup(List<Integer> serverIds,String url) {
		this.id = StringUtil.list2Str(serverIds, "_");
		this.serverIds = serverIds;
		this.url = url;
	}
	
	public static KfScoreServerGroup findFitKfScoreServerGroup(int serverId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("serverIds").in(serverId);
		query.addCriteria(criteria);
		return MongoUtils.getLoginMongodDB().queryOne(query, KfScoreServerGroup.class);
	}
	
	public static List<KfScoreServerGroup> findFitKfScoreServerGroup(String url) {
		Query query = new Query();
		Criteria criteria = Criteria.where("url").is(url);
		query.addCriteria(criteria);
		return MongoUtils.getLoginMongodDB().query(query, KfScoreServerGroup.class);
	}

	public static Map<Integer,KfScoreServerGroup> getAllKfScoreServerGroupForServer() {
		Map<Integer,KfScoreServerGroup> resultMap = Maps.newHashMap();
		for (KfScoreServerGroup kfScoreServerGroup : MongoUtils.getLoginMongodDB().queryAll(KfScoreServerGroup.class)) {
			for (Integer serverId : kfScoreServerGroup.getServerIds()) {
				resultMap.put(serverId,kfScoreServerGroup);
			}
			System.out.println("pre server:"+ GSONUtils.ToJSONString(kfScoreServerGroup));
		}
		return resultMap;
	}
	
	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
	
	public static void dropColl() {
		MongoUtils.getLoginMongodDB().dropCollection(KfScoreServerGroup.class);
	}
}
