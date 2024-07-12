package com.hm.leaderboards;

import cn.hutool.core.convert.Convert;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hm.model.tank.TankVo;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Getter;
import lombok.Setter;

/** 
 * @author 作者 xjt: 
 * @version 创建时间：2017年5月10日 上午10:34:43 
 * 类说明 
 */
@Getter
@Setter
public class LeaderboardInfo{
	private String id;//玩家id
	private int rank;//排名
	private double score;//分数
	private PlayerRankData playerRankData; 
	private GuildRankData guildRankData;

	
	private transient String rankName;
	private transient PlayerRedisData playerData;
	
	public LeaderboardInfo(String id, int rank, double score) {
		this.id = id;
		this.rank = rank;
		this.score = score;
	}

	public LeaderboardInfo(JsonElement el) {
		try {
			JsonObject leObj = el.getAsJsonObject();
			this.score = leObj.get("score").getAsDouble();
			this.id = leObj.get("userID").getAsString();
			this.rank = leObj.get("rank").getAsInt()+1;
			if(leObj.has("rankName")) {
				this.rankName = leObj.get("rankName").getAsString();
			}
			if(!isNpc()){
				this.playerRankData = new PlayerRankData(getPlayerData());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public String getId() {
		return id;
	}
	public int getRank() {
		return rank;
	}

	public double getScore() {
		return score;
	}
	
	public String getRankName() {
		return rankName;
	}
	
	public PlayerRedisData getPlayerData() {
		if(isNpc()) {
			return null;
		}
		if(this.playerData == null) {
			this.playerData = RedisUtil.getPlayerRedisData(getIntId());
		}
		return this.playerData;
	}
	
	public long getIntId() {
		if(isNpc()){
			return 0L;
		}
		return Convert.toLong(this.getId());
	}
	
	public GuildRankData getGuildRankData() {
		return guildRankData;
	}
	public void setGuildRankData(GuildRankData guildRankData) {
		this.guildRankData = guildRankData;
	}

	public PlayerRankData getPlayerRankData() {
		return playerRankData;
	}


	public boolean isNpc(){
		return id.startsWith("n");
	}
	
	//分数取整
	public void scoreToLong() {
		this.score = (long)score;
	}

}
