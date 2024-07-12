package com.hm.action.kfseason.vo;

import com.hm.model.activity.kfseason.server.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SeasonTopVo {
	@Id
	private int id;
	private long startTime;
	private long endTime;
	private KFSeasonServer winServer;//冠军服务器
	private SesaonGodWarPlayer godWarPlayer;//战神玩家信息
	
	public SeasonTopVo(int groupId,int seasonId) {
		KFSeasonTopLog topLog = KFSeasonTopLog.getTopLog(seasonId, groupId);
		KFSeason topSeason = KFSeasonUtil.getSeasonFromCache(seasonId);
		this.id = seasonId;
		this.startTime = topSeason.getStartTime();
		this.endTime = topSeason.getEndTime();
		if(topLog != null) {
			this.winServer = topLog.getWinServer();
			this.godWarPlayer = topLog.getGodWarPlayer();
		}
	}
}
