package com.hm.model.activity.kfactivity;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.util.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "kfInfo")
public class KfServerInfo {
	@Id
	private String id;
	private int type;
	private String url;
	private int weight;//权重
	private String outUrl;//对外映射

	@Transient
	private transient int groupNum;

	public static List<KfServerInfo> getKfServerInfo(int type) {
		Query query = new Query();
		Criteria criteria = Criteria.where("type").is(type)
				.and("weight").gt(0);
		query.addCriteria(criteria);
		query.with(Sort.by(Direction.DESC, "weight"));
		return MongoUtils.getLoginMongodDB().query(query, KfServerInfo.class);
	}

	public static List<KfServerInfo> getAllKfServerInfo() {
		return MongoUtils.getLoginMongodDB().queryAll(KfServerInfo.class);
	}

	public static String randomKfUrlByWeight(int type) {
		List<KfServerInfo> kfList = getKfServerInfo(type);
		if (kfList.isEmpty()) {
			return null;
		}
		return randomKfUrlByWeight(kfList);
	}

	public static String randomKfUrlByWeight(List<KfServerInfo> kfList) {
		Map<String,Integer> weightMap = Maps.newHashMap();
		for (KfServerInfo kfServerInfo : kfList) {
			if(kfServerInfo.getWeight() > 0) {
				weightMap.put(kfServerInfo.getUrl(), kfServerInfo.getWeight());
			}
		}
		if(CollUtil.isNotEmpty(weightMap)) {
			return RandomUtils.buildWeightMeta(weightMap).random();
		}
		return RandomUtils.randomEle(kfList).getUrl();
	}
	
	public void addGroupNum() {
		this.groupNum ++;
	}

	public String getClientUrl() {
		String[] urls = url.split("#");
		return urls[0] + ":" + urls[1];
	}
}
