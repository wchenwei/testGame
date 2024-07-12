package com.hm.action.cityworld.biz;

import com.google.common.collect.Lists;
import com.hm.action.cityworld.vo.JoinCityVo;
import com.hm.action.cityworld.vo.WorldCityVo;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildCityBiz;
import com.hm.action.guild.biz.GuildCityFightBiz;
import com.hm.action.guild.biz.GuildWorldBiz;
import com.hm.action.guild.vo.GuildSimpleVo;
import com.hm.action.guildwar.biz.WarRewardBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.biz.TroopFightBiz;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.AtkDefType;
import com.hm.enums.CityTroopType;
import com.hm.enums.NpcType;
import com.hm.enums.TroopState;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.*;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerStatistics;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.troop.TroopItemContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.AbstractPlayerFightTroop;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class WorldBiz {
	@Resource
	private NpcConfig npcConfig;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private TroopFightBiz troopFightBiz;
	@Resource
	private GuildWorldBiz guildWorldBiz;
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private WarRewardBiz warRewardBiz;
	@Resource
	private GuildCityFightBiz guildCityFightBiz;
	@Resource
	private GuildCityBiz guildCityBiz;


	
	public WorldCityVo createWorldCityVo(WorldCity worldCity) {
		WorldCityVo vo = new WorldCityVo(worldCity);
		if(worldCity.getBelongGuildId() > 0) {
			Guild guild = GuildContainer.of(worldCity).getGuild(worldCity.getBelongGuildId());
			if(guild != null) {
				vo.setFlag(guild.getGuildFlag().getFlagName());
				vo.setGuildName(guild.getGuildInfo().getGuildName());
			}
		}
		return vo;
	}
	
	/**
	 * 广播城池信息变化
	 * @param worldCity
	 */
	public void broadWorldCityUpdate(WorldCity worldCity) {
		if(worldCity != null) {
			JsonMsg jsonMsg = JsonMsg.create(MessageComm.WorldCityUpdate);
			jsonMsg.addProperty("city",createWorldCityVo(worldCity));
			broadWorldCityUpdate(worldCity,jsonMsg);
		}
	}
	public void broadWorldCityUpdateAndTroopSize(WorldCity worldCity) {
		if(worldCity != null) {
			JsonMsg jsonMsg = JsonMsg.create(MessageComm.WorldCityUpdate);
			jsonMsg.addProperty("city",createWorldCityVo(worldCity));
			jsonMsg.addProperty("atkSize", worldCity.getTroopSize(AtkDefType.ATK));
			jsonMsg.addProperty("defSize", worldCity.getTroopSize(AtkDefType.DEF));
			broadWorldCityUpdate(worldCity,jsonMsg);
		}
	}

	private void broadWorldCityUpdate(WorldCity worldCity,JsonMsg msg) {
		int serverId = worldCity.getServerId();
		PlayerContainer.getGamePlayerMap().values().stream()
				.filter(e -> e.playerTemp().isWorldView())
				.filter(e -> e.isInServerWorld(serverId))
				.forEach(e -> e.sendMsg(msg));
	}

	/**
	 * 广播城镇消息
	 * @param worldCity
	 * @param msg
	 */
	public void broadWorldCityMsg(WorldCity worldCity,JsonMsg msg) {
		int serverId = worldCity.getServerId();
		PlayerContainer.getGamePlayerMap().values().stream()
				.filter(e -> e.playerTemp().getCurCityId() == worldCity.getId())
				.filter(e -> e.isInServerWorld(serverId))
				.forEach(e -> e.sendMsg(msg));
	}
	/**
	 * 部队进入到城市
	 * @param player
	 * @param worldTroop
	 * @param cityId
	 */
	public boolean troopEnterCity(Player player,WorldTroop worldTroop,int cityId) {
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if(worldCity == null || !troopBiz.isUnlockCity(player, cityId)) {
			return false;//没有此城
		}
		boolean hasFight = worldCity.hasFight();//是否有战斗
		//部队进城
		troopIntoWorldCity(player, worldTroop, worldCity);
		//发送部队变化
		troopBiz.sendWorldTroopUpdate(player, worldTroop);
		//广播世界城池变化
		if(!hasFight && worldCity.hasFight()) {
			worldBiz.broadWorldCityUpdate(worldCity);
		}
		player.notifyObservers(ObservableEnum.TroopEnterCity, worldTroop, cityId);
		return true;
	}
	
	public void troopIntoWorldCity(Player player,WorldTroop worldTroop,WorldCity worldCity) {
		try {
			worldCity.lockWrite();
			boolean isFriendGuild = guildCityFightBiz.isFriendCity(player, worldCity);

			//友方部落或者宣战部落
			if((isFriendGuild || guildCityFightBiz.isEnemyCity(player, worldCity))) {
				worldCity.addTroop(player, worldTroop, isFriendGuild);
				log.error(worldTroop.getId()+"部队状态更新:"+worldTroop.getState()+" city="+worldCity.getId());
				//广播城市部队变化
				if(broadWorldCityTroopChange(worldCity)) {
					//广播进入城市
					AtkDefType atkType = isFriendGuild ? AtkDefType.DEF : AtkDefType.ATK;
					broadPlayerIntoCity(player, worldTroop ,worldCity, atkType,false);
				}
				worldCity.saveDB();
				//重新计算部队回血
				troopBiz.recalTroopRecoveSecondHp(player, worldTroop);
				worldTroop.saveDB();
				return;
			}
			////重新计算部队回血
			troopBiz.recalTroopRecoveSecondHp(player, worldTroop);
			worldTroop.changeState(TroopState.None);
			worldTroop.setCityId(worldCity.getId());
			worldTroop.saveDB();
		} finally {
			worldCity.unlockWrite();
		}
	}
	
	/**
	 * 重新生成城市npc
	 * @param worldCity
	 */
	public void resetNpc(WorldCity worldCity) {
		resetNpc(worldCity, true);
	}
	public void resetNpc(WorldCity worldCity,boolean isDelNpc) {
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		worldCity.getCityNpc().setNpcIndex(0);//npc索引重置
		createNpc(worldCity, cityTemplate.getNpcNum(),isDelNpc);
	}
	public void resetNpc(WorldCity worldCity,int npcNum) {
		createNpc(worldCity, npcNum,true);
	}
	//检查是否生成npc
	public void checkWorldCityNpcCreate(WorldCity worldCity) {
		if(worldCity.getCityNpc().isCanCreateNpc()) {
			long nextTime = System.currentTimeMillis() + 5*GameConstants.MINUTE;
			worldCity.getCityNpc().setNextCreateNpcTime(nextTime);
			CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
			if(cityTemplate.getNpcNum() > worldCity.getDefCityTroop().getNpcSize()) {
				createNpc(worldCity, 1, false);
			}
		}
	}
	public void createNpc(WorldCity worldCity,int npcNum,boolean isDelNpc) {
		//删除之前npc
		if(isDelNpc) {
			worldCity.getDefCityTroop().removeNpc(NpcType.CityDef);
		}
		final int guildId = worldCity.getBelongGuildId();
		int serverId = worldCity.getServerId();
		List<BaseCityFightTroop> npcList = npcConfig.randomNpcList(worldCity, npcNum).stream()
					.map(e -> new CityDefNpcTroop(e,serverId, guildId))
					.collect(Collectors.toList());
		worldCity.getDefCityTroop().addNpcToTroop(npcList);
		worldCity.saveDB();
	}
	
	
	/**
	 * 处理防守方胜利
	 * @param worldCity
	 */
	public void doWorldCityDefWin(WorldCity worldCity) {
		Player winPlayer = getPlayerByFightTroop(worldCity, troopFightBiz.getDefFightTroop(worldCity));
		//计算奖励
		warRewardBiz.calWinHonorReward(worldCity, winPlayer, AtkDefType.DEF);
		
		//清空保留状态
		clearTankRetainState(worldCity);
		worldCity.getCityFight().clearFightData();
		//改变当前所有防守部队的状态
		troopFightBiz.checkWorldCityTroopState(worldCity);
		//发送消息 防守方胜利 
		broadWorldCityFightResult(worldCity, AtkDefType.DEF,worldCity.getBelongGuildId());
		
		worldBiz.broadWorldCityUpdate(worldCity);//广播城池变化
		
		log.error(worldCity.getServerId()+"_"+worldCity.getId()+"城池战斗结束:防守成功"+worldCity.getBelongGuildId());
	}
	
	/**
	 * 攻击方胜利
	 * @param worldCity
	 * @param
	 */
	public void doWorldCityAtkWin(WorldCity worldCity,AbstractFightTroop atkFightTroop) {
		int oldGuildId = worldCity.getBelongGuildId();
		int winGuildId = 0;
		boolean guildIsMax = false;
		Player winPlayer = null;
		if(atkFightTroop.isNpc()) {//攻击获胜是npc处理
			BaseCityFightTroop winFightTroop = worldCity.getAtkCityTroop().getCityFightTroop(atkFightTroop.getId());
			if(winFightTroop != null && winFightTroop.getTroopType() == CityTroopType.NpcTroop.getType()) {
				NpcCityTroop npcCityTroop = (NpcCityTroop)winFightTroop;
				winGuildId = npcCityTroop.getGuildId();
			}
		}else{
			long playerId = ((AbstractPlayerFightTroop)atkFightTroop).getPlayerId();
			winPlayer = PlayerUtils.getPlayer(playerId);
			winGuildId = winPlayer.getGuildId();
			log.error(worldCity.getServerId()+"_"+worldCity.getId()+"玩家军团"+playerId+"_"+winGuildId);
			//判断军团城池是否超过上限
			guildIsMax = guildCityBiz.guildCityIsMax(winPlayer);
			if(guildIsMax) {
				log.error(worldCity.getServerId()+"_"+worldCity.getId()+"军团城池达到上限"+playerId+"_"+winGuildId);
				winGuildId = 0;
			}
		}
		//计算奖励
		warRewardBiz.calWinHonorReward(worldCity, winPlayer, AtkDefType.ATK);
		log.error(worldCity.getServerId()+"_"+worldCity.getId()+"城池战斗结束:攻占成功"+winGuildId);
		//清空保留状态
		clearTankRetainState(worldCity);
		worldCity.clearData();
		//把属于胜利方军团的部队移到防御方
		moveWinTroopToDef(worldCity, winGuildId);
		if(guildIsMax) {//把winFightTroop移到攻击队列的尾部
			worldCity.getAtkCityTroop().changeTroopForLast(atkFightTroop.getId());
		}
		//处理攻占城池
		guildWorldBiz.guildWinWorldCity(winPlayer, winGuildId, worldCity);
		worldBiz.broadWorldCityUpdate(worldCity);//广播城池变化
		//重新生成npc
		resetNpc(worldCity,false);
		//重新加载状态
		troopFightBiz.checkWorldCityTroopState(worldCity);
		//发送消息 攻击方胜利 
		broadWorldCityFightResult(worldCity, AtkDefType.ATK,atkFightTroop.getGuildId());
		//检查攻击方是否可以自动突进
//		warHelperBiz.checkTroopAdvanceForCityChange(worldCity);
		//发送攻占成功消息
		ObserverRouter.getInstance().notifyObservers(ObservableEnum.OccupyCity, winPlayer, worldCity, oldGuildId,false);
	}
	
	/**
	 * 广播战斗最后结算
	 * @param worldCity
	 * @param winType
	 */
	private void broadWorldCityFightResult(WorldCity worldCity,AtkDefType winType,int winGuildId) {
		boolean haveFight = worldCity.hasFight();
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_BroadWorldCityFightResulte);
		msg.addProperty("cityId", worldCity.getId());
		msg.addProperty("winType", winType.getType());
		msg.addProperty("hasNextFight", haveFight);
		msg.addProperty("defSize", worldCity.getTroopSize(AtkDefType.DEF));
		//获胜的军团信息
		if(winGuildId > 0) {
			Guild guild = GuildContainer.of(worldCity).getGuild(winGuildId);
			if(guild != null) {
				msg.addProperty("winGuild", new GuildSimpleVo(guild));
			}
		}
		if(!haveFight) {
			msg.addProperty("defTroopList", worldCity.getDefTroopForMoveVo());
		}

		broadWorldCityMsg(worldCity,msg);
	}
	
	/**
	 * 获取与胜利部落友盟部队，从攻击方删除到防守方
	 * @param worldCity
	 * @param winGuildId
	 * @return
	 */
	public void moveWinTroopToDef(WorldCity worldCity,int winGuildId) {
		//把当前防御方剩余的部队移动到攻击方 ps:处理防守方中处于单挑战斗中,胜利后到攻击队列
		worldCity.getAtkCityTroop().addTroopList(Lists.newArrayList(worldCity.getDefCityTroop().getTroopList()));

		TroopItemContainer troopContainer = TroopServerContainer.of(worldCity);
		List<BaseCityFightTroop> winTroopIds = Lists.newArrayList();
		List<BaseCityFightTroop> removeTroopIds = Lists.newArrayList();
		List<BaseCityFightTroop> atkTroopList = worldCity.getAtkCityTroop().getTroopList();
		int serverId = worldCity.getServerId();
		
		//过滤出胜利方部队
		atkTroopList.stream().forEach(temp -> {
			if(temp.getTroopType() == CityTroopType.PlayerTroop.getType()) {
				WorldTroop worldTroop = troopContainer.getWorldTroop(temp.getId());
				if(worldTroop != null) {
					Player player = PlayerUtils.getPlayer(worldTroop);
					if(player.getGuildId() <= 0) {
						//不是敌方
						removeTroopIds.add(temp);
						//进入观看状态
						worldTroop.changeState(TroopState.None);
						worldTroop.setCityId(0);//飘在空中
						worldTroop.saveDB();
						troopBiz.sendWorldTroopUpdate(player, worldTroop);
					}else if(guildCityFightBiz.isFriendGuild(player, winGuildId)) {
						winTroopIds.add(temp);
					}
				}
			}else if(temp.getTroopType() == CityTroopType.NpcTroop.getType()) {
				NpcCityTroop npcTroop = (NpcCityTroop)temp;
				if(guildCityFightBiz.isFriendGuild(serverId, winGuildId, npcTroop.getGuildId())) {
					winTroopIds.add(temp);
				}
			}else if(temp.getTroopType() == CityTroopType.ClonePlayer.getType()) {
				ClonePlayerTroop clonePlayerTroop = (ClonePlayerTroop)temp;
				Player player = PlayerUtils.getPlayer(clonePlayerTroop.getPlayerId());
				if(guildCityFightBiz.isFriendGuild(player, winGuildId)) {
					winTroopIds.add(temp);
				}
			}
		});
		atkTroopList.removeAll(winTroopIds);
		atkTroopList.removeAll(removeTroopIds);
		worldCity.getAtkCityTroop().SetChanged();
		//加入到防守部队
		worldCity.getDefCityTroop().loadTroopList(winTroopIds);
	}
	
	/**
	 * 广播城镇部队数量变化
	 * @param worldCity
	 */
	public boolean broadWorldCityTroopChange(WorldCity worldCity) {
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_BroadWorldCityTroopChange);
		msg.addProperty("cityId", worldCity.getId());
		msg.addProperty("atkSize", worldCity.getTroopSize(AtkDefType.ATK));
		msg.addProperty("defSize", worldCity.getTroopSize(AtkDefType.DEF));
		broadWorldCityMsg(worldCity,msg);
		return true;
	}
	
	/**
	 * 广播玩家进入城战
	 * @param player
	 * @param worldCity
	 */
	public void broadPlayerIntoCity(Player player,WorldTroop worldTroop,WorldCity worldCity,AtkDefType type,boolean isClone) {
		Player troopPlayer = player.getId() == worldTroop.getPlayerId()?
				player:PlayerUtils.getPlayer(worldTroop);
		
		Guild guild = guildBiz.getGuild(troopPlayer);
		JsonMsg msg = JsonMsg.create(MessageComm.C2S_PlayerIntoCity);
		JoinCityVo vo = new JoinCityVo(troopPlayer,guild);
		vo.setAtkType(type.getType());
		if(isClone) {
			vo.name = vo.name+"-镜像";
		}
		msg.addProperty("playerVo", vo);
		broadWorldCityMsg(worldCity,msg);
	}
	
	public Player getPlayerByFightTroop(WorldCity worldCity,AbstractFightTroop fightTroop) {
		if(fightTroop == null) {
			return null;
		}
		if(fightTroop instanceof AbstractPlayerFightTroop) {
			return PlayerUtils.getPlayer(((AbstractPlayerFightTroop)(fightTroop)).getPlayerId());
		}
		return null;
	}

	/**
	 * 清空战斗双方的保留状态
	 * @param worldCity
	 */
	public void clearTankRetainState(WorldCity worldCity) {
		WarResult warResult = worldCity.getCityFight().getWarResult();
		if(warResult != null) {
			warResult.getAtk().getFightTroop().clearRetainState();
			warResult.getDef().getFightTroop().clearRetainState();
		}
	}
	
	/**
	 * 获取城池中可战斗部队列表 (过滤掉单挑部队)
	 * @param worldCity
	 * @param type
	 * @return
	 */
	public long getWorldCityTroopSize(WorldCity worldCity,AtkDefType type) {
		return worldCity.getCityTroop(type.getType()).getTroopList().stream()
				.filter(troop -> troopFightBiz.getCityFightTroopState(worldCity, troop) != TroopState.PvpOneByOne.getType())
				.count();		
	}
	
	public boolean worldCityHasFight(WorldCity worldCity) {
		return worldCity.getCityTroop(AtkDefType.ATK.getType()).getTroopList().stream()
				.anyMatch(troop -> troopFightBiz.getCityFightTroopState(worldCity, troop) != TroopState.PvpOneByOne.getType());
	}
	
	public boolean checkTroopIsFightState(WorldCity worldCity,AtkDefType type) {
		List<BaseCityFightTroop> troopIds = worldCity.getCityTroop(type.getType()).getTroopList();
		if(troopIds.size() < GameConstants.LockTroopNum) {
			return true;
		}
		int fightNum = 0;
		for (int i = 0; i < troopIds.size(); i++) {
			if(troopFightBiz.getCityFightTroopState(worldCity, troopIds.get(i)) != TroopState.PvpOneByOne.getType()) {
				fightNum ++;
				if(fightNum >= GameConstants.LockTroopNum) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取当前城池的npc等级
	 * @param worldCity
	 * @return
	 */
	@Deprecated
	public int getCityNpcLvOld(WorldCity worldCity) {
		ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(worldCity.getServerId()).getServerStatistics();
		int serverLv = serverStatistics.getServerLv();
		int addNpcLv = serverStatistics.getOpenDay();
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		int divLv = cityTemplate.getNpc_level_diff();
		int npcLv = Math.min(cityTemplate.getNpcLv()+addNpcLv, serverLv-divLv);
		return Math.max(cityTemplate.getNpcLv(), npcLv);
	}
	/**
	 * 获取当前城池的npc等级
	 * @param worldCity
	 * @return
	 */
	public int getCityNpcLv(WorldCity worldCity) {
		ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(worldCity.getServerId()).getServerStatistics();
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		return cityTemplate.getNpcLv() + serverStatistics.getBaseNpcLv();
	}
	public void calFirstBaseLv(int serverId) {
		ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics();
		WorldCity worldCity = WorldServerContainer.of(serverId).getWorldCity(1);
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		int firstBaseLv = getCityNpcLvOld(worldCity) - cityTemplate.getNpcLv();
		serverStatistics.setBaseNpcLv(firstBaseLv);
		serverStatistics.save();
	}


	// 获取攻占该城池 不算挂机的玩家集合
	public Set<Long> getAtkCityPlayerIds(WorldCity worldCity){
		TroopItemContainer troopContainer = TroopServerContainer.of(worldCity);
		return worldCity.getAtkCityTroop().getTroopList().stream().map(baseCityFightTroop -> {
			boolean isPlayerTroop = baseCityFightTroop instanceof PlayerCityTroop;
			if(!isPlayerTroop){
				return null;
			}
			String troopId = baseCityFightTroop.getId();
			WorldTroop worldTroop = troopContainer.getWorldTroop(troopId);
			if(worldTroop == null){
				return null;
			}
			Player player = PlayerUtils.getPlayer(worldTroop);
			if(player == null){
				return null;
			}
			return player.getId();
		}).filter(Objects::nonNull).collect(Collectors.toSet());
	}
}
