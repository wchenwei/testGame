package com.hm.leaderboards;

import com.hm.libcore.leaderboards.LeaderboardPutDescriptor;
import com.hm.model.player.BasePlayer;

/**   
 * @author siyunlong  
 * @date 2016年8月17日 下午2:46:45 
 * @version V1.0   
 */
public class HdLeaderboardPutDescriptor extends LeaderboardPutDescriptor{
	public HdLeaderboardPutDescriptor(BasePlayer player) {
		super(HdLeaderboardsService.createLeaderMark(player.getServerId()), player.getId()+"");
//		AddDimension( "lv", player.playerLevel().getLv() );
	}
	
	public HdLeaderboardPutDescriptor(int serverId,String userId) {
		super(HdLeaderboardsService.createLeaderMark(serverId), userId);
	}
	
}
