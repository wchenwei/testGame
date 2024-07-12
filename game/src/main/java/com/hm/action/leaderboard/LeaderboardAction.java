package com.hm.action.leaderboard;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.RankType;
import com.hm.leaderboards.GuildRankData;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.leaderboards.PlayerRankData;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerTroopVo;
import com.hm.model.tank.TankVo;
import com.hm.redis.PlayerRedisData;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Action
public class LeaderboardAction extends AbstractPlayerAction {
	@Resource
	private TroopBiz troopBiz;
	
	@MsgMethod(MessageComm.C2S_GameRank)
	public void getGameRank(Player player,JsonMsg msg){
		int type = msg.getInt("type");
		int pageNo = msg.getInt("pageNo");
		pageNo = Math.max(1, pageNo);
		RankType rankType = RankType.getTypeByIndex(type);
		List<LeaderboardInfo> rankList = HdLeaderboardsService.getInstance().getGameRanks(rankType, player, pageNo);
		rankList.forEach(e -> loadTempInfo(player,rankType, e));//加载部落信息
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_GameRank);
		serverMsg.addProperty("type", type);
		serverMsg.addProperty("pageNo", pageNo);
		serverMsg.addProperty("ranks", rankList);
		if(pageNo == 1) {
			LeaderboardInfo myRankData = HdLeaderboardsService.getInstance().getLeaderboardInfo(player, rankType);
			if(myRankData != null) { 
				loadTempInfo(player,rankType, myRankData);
				serverMsg.addProperty("myRankData", myRankData);
			}
		}
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_MyRank)
	public void getMyRank(Player player,JsonMsg msg){
		int type = msg.getInt("type");
		long myRank = HdLeaderboardsService.getInstance().getPlayerRank(RankType.getTypeByIndex(type), player);
		player.sendMsg(MessageComm.S2C_MyRank,myRank);
	}
	
	private void loadTempInfo(Player player,RankType rankType,LeaderboardInfo leaderInfo) {
		PlayerRedisData playerData = leaderInfo.getPlayerData();
		if(playerData != null) {
			Guild guild = GuildContainer.of(player).getGuild(playerData.getGuildId());
			if(guild != null) {
				leaderInfo.setGuildRankData(new GuildRankData(guild,playerData.getId()));
			}
		};
		//判断是否有排行不取整数
		if(RankType.isScoreToInt(rankType)){
			leaderInfo.scoreToLong();
		}
	}
	
	@MsgMethod(MessageComm.C2S_TroopRank_Info)
	public void getPlayerVo(Player player, JsonMsg msg) {
		long playerId = msg.getInt("playerId");
		Player tarPlayer = PlayerUtils.getPlayer(playerId);
		Map<Integer,List<TankVo>> toops = troopBiz.getTroops(tarPlayer);
		Guild guild = null;
		if(tarPlayer.getGuildId()>0) {
			guild = GuildContainer.of(tarPlayer.getServerId()).getGuild(tarPlayer.getGuildId());
		}
		PlayerTroopVo playerVo = new PlayerTroopVo(tarPlayer,guild,toops);
		//@TODO 玩家信息的排行榜等信息 
		playerVo.setDw(1);
		playerVo.setRank(2);
		
		long tarPvpRank = HdLeaderboardsService.getInstance().getPlayerRank(RankType.Arena, tarPlayer);
		playerVo.setPvpRank(tarPvpRank);
		long tarCombatRank = HdLeaderboardsService.getInstance().getPlayerRank(RankType.Combat, tarPlayer);
		playerVo.setCombatRank(tarCombatRank);
		player.sendMsg(MessageComm.S2C_TroopRank_Info, playerVo);
	}
}
