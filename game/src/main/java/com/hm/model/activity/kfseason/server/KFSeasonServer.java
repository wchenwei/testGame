package com.hm.model.activity.kfseason.server;

import com.google.common.collect.Maps;
import com.hm.enums.KfType;
import com.hm.redis.ServerNameCache;
import com.hm.redis.mode.AbstractRedisHashMode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description: 服务器赛季数据
 * @author siyunlong  
 * @date 2020年12月9日 上午10:47:26 
 * @version V1.0
 */
@NoArgsConstructor
@Data
public class KFSeasonServer extends AbstractRedisHashMode{
	private int id;//服务器id
	private int score;//赛季积分
	private String name;//服务器名字
	private long lastTime;

	private int seasonId;
	private int groupId;//分组id
	private Map<Integer,ServerSeasonKfItem> kfMap = Maps.newHashMap();
	
	public KFSeasonServer(int id,int groupId,int seasonId) {
		this.id = id;
		this.groupId = groupId;
		this.name = ServerNameCache.getServerName(id);
		this.seasonId = seasonId;
	}
	
	public void loadName() {
		this.name = ServerNameCache.getServerName(id);
	}
	public void addServerScore(KfType kfType,int add,boolean isWin) {
		ServerSeasonKfItem kfItem = getServerSeasonKfItem(kfType);
		this.score += add;
		this.lastTime = System.currentTimeMillis();
		kfItem.addScore(add, isWin);
	}
	public ServerSeasonKfItem getServerSeasonKfItem(KfType kfType) {
		ServerSeasonKfItem item = this.kfMap.get(kfType.getType());
		if(item == null) {
			item = buildServerSeasonKfItem(kfType);
			this.kfMap.put(kfType.getType(), item);	
		}
		return item;
	}
	public static ServerSeasonKfItem buildServerSeasonKfItem(KfType kfType) {
		return new ServerSeasonKfItem();
	}

	@Override
	public String buildFiledKey() {
		return id+"";
	}
	@Override
	public String buildHashKey() {
		return buildHashKey(seasonId);
	}
	public static String buildHashKey(int seasonId) {
		return "KFSeasonServer"+seasonId;
	}
}
