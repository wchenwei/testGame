package com.hm.action.player.biz;

import com.google.common.collect.Lists;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.annotation.Broadcast;
import com.hm.config.GameConstants;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.servercontainer.troop.TroopServerContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class CombatBiz extends NormalBroadcastAdapter {
	@Resource
	private TankBiz tankBiz;
	@Resource
	private TroopBiz troopBiz;


	@Broadcast(ObservableEnum.TroopChange)
	public void troopChange(ObservableEnum observableEnum, Player player, Object... argv){
		countTroopCombat(player);
	}

	@Broadcast({ObservableEnum.TankAdd, ObservableEnum.TankCombatChange})
	public void tankCombatChange(ObservableEnum observableEnum, Player player, Object... argv){
		countCombat(player);
		countTroopCombat(player);
	}

	@Broadcast(ObservableEnum.PlayerLoginSuc)
	public void playerLogin(ObservableEnum observableEnum, Player player, Object... argv){
		RedisTypeEnum.PlayerLastLoginTime.put(player.getServerId(), player.getId()+"" ,player.playerBaseInfo().getLastLoginDate().getTime()+"");
	}
	

	//计算玩家战力（该方法只在登录时进行同步）
	public void countPlayerCombat(Player player){
		countCombat(player);
		if(player.getPlayerDynamicData().getTroopCombat()==0){//只有部队战力为0时才重新计算同步
			countTroopCombat(player);
		}
	}
	
	//重新计算玩家部队战力
	public void countTroopCombat(Player player) {
		long oldCombat = player.getPlayerDynamicData().getTroopCombat();
		long newCombat = TroopServerContainer.of(player).getWorldTroopByPlayer(player)
		.stream().flatMap(e -> e.getTroopArmy().getTankList().stream())
		.map(e -> player.playerTank().getTank(e.getId()))
		.filter(Objects::nonNull)
		.mapToLong(e -> e.getCombat()).sum();
		
		if(newCombat!=oldCombat){
			player.getPlayerDynamicData().changeTroopCombat(newCombat);
			player.notifyObservers(ObservableEnum.TroopCombatChange);
		}
	}
	
	//计算最强战力
	public void countCombat(Player player) {
		try {
			long totalCombat = player.playerTank().getTankList().stream().mapToLong(e -> e.getCombat()).sum();
			log.error(player.getId()+"战力变化:"+totalCombat);
		} catch (Exception e2) {
		}
		
		long oldCombat = player.getPlayerDynamicData().getCombat();
		List<Tank> tankList = tankBiz.getTankListTopCombat(player, 5,true);
		long combat = tankList.stream().mapToLong(e -> e.getCombat()).sum();
		if(oldCombat == combat) {
			return;
		}
		player.getPlayerDynamicData().setCombat(combat);
		if(tankList.size()>0){
			player.getPlayerDynamicData().changeMaxCombatTank(tankList.get(0));
		}
		player.notifyObservers(ObservableEnum.MaxCombatChange, oldCombat, combat);
	}

	// 活跃玩家战力之和
	public long getServerActivePlayerTotalCombat(int serverId, long startTime, int limit){
		List<Long> activePlayers = getPlayersByLastLoginTime(serverId, startTime);
		return HdLeaderboardsService.getInstance().getGameRankByPlayerIds(serverId, RankType.Combat.getRankName(), activePlayers)
				.stream()
				.sorted(Comparator.comparing(LeaderboardInfo::getScore).reversed())
				.limit(limit)
				.mapToLong(leaderboardInfo -> (long) leaderboardInfo.getScore()).sum();
	}

	// 根据开始时间获得活跃玩家id
	public List<Long> getPlayersByLastLoginTime(int serverId, long startTime){
		Map<String, String> map = RedisTypeEnum.PlayerLastLoginTime.getAllVal(serverId);
		List<Long> playerIdList = Lists.newArrayList();
		long delTime = System.currentTimeMillis() - 7*GameConstants.DAY;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			long time = Long.parseLong(entry.getValue());
			if(time < delTime) {
				//删除确定流失的玩家
				RedisTypeEnum.PlayerLastLoginTime.del(serverId,entry.getKey());
				continue;
			}
			if(time > startTime) {
				playerIdList.add(Long.parseLong(entry.getKey()));
			}
		}
		return playerIdList;
	}

}
