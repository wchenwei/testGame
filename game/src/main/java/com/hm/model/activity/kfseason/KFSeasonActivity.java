package com.hm.model.activity.kfseason;

import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.kfseason.server.KFSeason;
import com.hm.model.player.BasePlayer;
import lombok.Data;

/**
 * @Description: 跨服赛季活动
 * @author siyunlong  
 * @date 2020年12月9日 上午10:06:09 
 * @version V1.0
 */
@Data
public class KFSeasonActivity extends AbstractActivity{
	private boolean isOpen;//是否开启
	
	private int seasonId;//赛季id
	private long seasonStartTime;
	private long seasonEndTime;
	
	public KFSeasonActivity() {
		super(ActivityType.KFSeason);
	}
	
	@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		return !isOpen;
	}
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	
	public void loadNewSeason(KFSeason kFSeason) {
		this.seasonId = kFSeason.getId();
		this.seasonStartTime = kFSeason.getStartTime();
		this.seasonEndTime = kFSeason.getEndTime();
	}
}
