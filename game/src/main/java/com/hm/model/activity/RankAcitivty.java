package com.hm.model.activity;

import com.hm.enums.ActivityState;
import com.hm.enums.ActivityType;
import com.hm.leaderboards.activity.RankLine;
import com.hm.leaderboards.activity.RankList;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @Description: 排行榜活动基类
 * @author siyunlong  
 * @date 2018年5月28日 下午1:26:24 
 * @version V1.0
 */
public abstract class RankAcitivty extends AbstractActivity{
	private transient RankList rankList;

	public RankAcitivty(ActivityType type,int maxLine) {
		super(type);
		this.rankList = new RankList(maxLine);
	}

	public RankAcitivty(ActivityType type) {
		super(type);
	}
	
	/**
	 * 获取玩家的排行
	 * @param playerId
	 * @return
	 */
	public int getRank(long playerId) {
		return this.rankList.getRank(playerId);
	}
	
	/**
	 * 更新玩家排行
	 * @param player
	 */
	public boolean updateRank(Player player,long rankValue) {
		return this.rankList.addLine(new RankLine(player,rankValue));
	}
	
	
	@Override
	public boolean checkCondition(BasePlayer player,int id) {
		if(getState() != ActivityState.CalOver.getType()) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<Items> getRewardItems(BasePlayer player, int id) {
		return null;
	}

	
}
