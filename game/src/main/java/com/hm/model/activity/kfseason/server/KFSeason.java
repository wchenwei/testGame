package com.hm.model.activity.kfseason.server;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

/**
 * @Description: 跨服赛季
 * @author siyunlong  
 * @date 2020年12月9日 上午10:09:55 
 * @version V1.0
 */
@NoArgsConstructor
@Data
@Document(collection = "KFSeason")
public class KFSeason {
	@Transient
	public static final int SeasonMonth = 2;
	
	@Id
	private int id;
	private long startTime;
	private long endTime;
	
	public KFSeason(int id, long startTime, long endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void save() {
		System.err.println(id+"开始时间:"+DateUtil.formatDate(new Date(this.startTime)));
		System.err.println(id+"结束时间:"+DateUtil.formatDate(new Date(this.endTime)));
		MongoUtils.getLoginMongodDB().save(this);
	}
	
	
	public static KFSeason getKFSeasonTime(int id) {
		MongodDB mongo = MongoUtils.getLoginMongodDB();
		return mongo.get(id, KFSeason.class);
	}
	public static KFSeason getKFSeasonTime() {
		MongodDB mongo = MongoUtils.getLoginMongodDB();
		Criteria criteria = Criteria.where("startTime").lte(System.currentTimeMillis())
				.and("endTime").gt(System.currentTimeMillis());
		Query query = new Query(criteria);
		return mongo.queryOne(query, KFSeason.class);
	}
	
	public static void loadFirstKFSeasonTime(int totalNum) {
		long startTime = DateUtil.beginOfMonth(new Date()).getTime();
		for (int i = 1; i <= totalNum; i++) {
			long endTime = DateUtil.offset(new Date(startTime), DateField.MONTH, SeasonMonth).getTime();
			System.err.println(i+"="+new DateTime(startTime).toDateStr()+":"+new DateTime(endTime).toDateStr());
			new KFSeason(i, startTime, endTime).save();
			startTime = endTime;
		}
	}
	
	public static void reloadSnum(int totalNum) {
		//本期还是第3期  6月22结束
		MongoUtils.getLoginMongodDB().dropCollection(KFSeason.class);
		loadFirstKFSeasonTime(totalNum);
	}
	
	public static void main(String[] args) {
		reloadSnum(30);
		System.err.println(getKFSeasonTime());
	}

}

