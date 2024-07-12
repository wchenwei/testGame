package com.hm.action.troop.biz;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.cityworld.biz.WorldDeclareWarBiz;
import com.hm.action.commander.biz.CommanderBiz;
import com.hm.action.commander.biz.EquipmentBiz;
import com.hm.action.guild.biz.GuildCityFightBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.serverData.ServerPowerBiz;
import com.hm.action.troop.vo.MovePlayerVo;
import com.hm.action.troop.vo.WorldTroopVo;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.PlayerLevelConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.MilitaryExtraTemplate;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.guild.tactics.TheSuckerTatics;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerTank;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankVo;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.redis.tank.PlayerTankBind;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.ItemConstant;
import com.hm.sysConstant.SysConstant;
import com.hm.util.MathUtils;
import com.hm.war.sg.troop.TankArmy;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class TroopBiz {
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private PlayerLevelConfig playerLevelConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private WorldDeclareWarBiz worldDeclareWarBiz;
	@Resource
	private GuildTechBiz guildTechBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
    private EquipmentBiz equipmentBiz;
	@Resource
    private CommanderBiz commanderBiz;
	@Resource
    private ServerPowerBiz serverPowerBiz;
	@Resource
    private WorldTroopBiz worldTroopBiz;
	@Resource
    private WorldBuildBiz worldBuildBiz;
	@Resource
	private GuildCityFightBiz guildCityFightBiz;
	@Resource
	private CommanderConfig commanderConfig;

	public static ArrayList<TankArmy> createTankArmys(String armys,Player player,boolean isFullHp) {
		ArrayList<TankArmy> tankList = Lists.newArrayList();
		for (String str : armys.split(",")) {
			TankArmy tankArmy = new TankArmy(Integer.parseInt(str.split(":")[0]), Integer.parseInt(str.split(":")[1]),player);
			tankList.add(tankArmy);
			if(isFullHp) {
				tankArmy.setFullHp();
			}
		}
		return tankList;
	}
	
	/**
	 * 检查坦克状态是否可以创建部队
	 * @param player
	 * @param troopId
	 * @param tankArmys
	 * @return
	 */
	public boolean checkTankArmyState(Player player,String troopId,List<TankArmy> tankArmys) {
		List<String> playerTroopIds = Lists.newArrayList(player.playerTroops().getTroopIdList());
		if(StrUtil.isNotEmpty(troopId) && !playerTroopIds.remove(troopId)) {
			return false;
		}
		//当前我的部队
		List<WorldTroop> worldTroops = TroopServerContainer.of(player).getWorldTroops(playerTroopIds);
		for (TankArmy tankArmy : tankArmys) {
			//当前部队都不包含此坦克
			if(worldTroops.stream().anyMatch(e -> e.getTroopArmy().isContainTankId(tankArmy.getId()))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断部队状态是否可以编译
	 * @param player
	 * @param worldTroop
	 * @return
	 */
	public boolean worldTroopIsCanEditor(Player player,WorldTroop worldTroop) {
		//空闲状态,并且不再采集中
		return worldTroop.getState() == TroopState.None.getType()
				&& !worldTroopBiz.isTroopIsBusy(worldTroop);
	}
	
	/**
	 * 部队派往世界
	 * @param player
	 * @param worldTroop
	 */
	private boolean addWorldTroopToWorld(Player player,WorldTroop worldTroop) {
		//获取部落
		WorldCity worldCity = findReviveWorldCity(player, worldTroop);
		if(worldCity == null) {
			return false;
		}
		//部队加入此城
		return worldBiz.troopEnterCity(player, worldTroop, worldCity.getId());
	}
	
	private WorldCity findReviveWorldCity(Player player,WorldTroop worldTroop) {
		worldTroop.setWorldType(WorldType.Normal);
		//进入我方主城
		int mainCityId = getTroopReviveCityId(player);
		return WorldServerContainer.of(player).getWorldCity(mainCityId);
	}
	
	//获取玩家部队的复活城市
	public int getTroopReviveCityId(Player player) {
		return GameConstants.PeaceId;
	}
	
	public boolean addWorldTroopToWorldAndUpdate(Player player,WorldTroop worldTroop) {
//		boolean isInCity = addWorldTroopToWorld(player, worldTroop);
//		if(!isInCity) {
//			addWorldTroopToPeaceCity(player, worldTroop);
//		}
//		return isInCity;
		addWorldTroopToPeaceCity(player, worldTroop);
		return false;
	}
	
	public void addWorldTroopToPeaceCity(Player player,WorldTroop worldTroop) {
		worldTroop.setCityId(GameConstants.PeaceId);//默认到和平城
		worldTroop.saveDB();
		sendWorldTroopUpdate(player, worldTroop);
		log.error(worldTroop.getId()+"进入柏林:  状态："+worldTroop.getState());
	}

	public void checkTroopIndex(Player player){
		List<WorldTroop> worldTroops = TroopServerContainer.of(player).getWorldTroopByPlayer(player);
		worldTroops.stream().filter(e -> e.getTroopInfo().getIndex() <= 0).forEach(e -> removeWorldTroop(e, player));
	}

	/**
	 * 删除部队
	 * @param player
	 * @param worldTroop
	 */
	public void removeWorldTroop(WorldTroop worldTroop,Player player) {
		WorldCity worldCity = WorldServerContainer.of(worldTroop.getServerId()).getWorldCity(worldTroop.getCityId());
		if(worldCity != null) {
			if(!worldCity.removeTroopAndSave(worldTroop.getId())) {
				log.error(worldTroop.getId()+"删除失败:解散删除在"+worldCity.getId()+" 删除失败!");
			}
			//广播城市部队变化
			worldBiz.broadWorldCityTroopChange(worldCity);
		}
		//改变坦克血量
		changeHpToTank(worldTroop,player);
		player.playerTroops().removeTroopId(worldTroop.getId());
		//删除部队
		TroopServerContainer.of(worldTroop.getServerId()).removeWorldTroop(worldTroop.getId());
		//检查其余所有部队index
		checkPlayerTroopIndex(player);
		player.notifyObservers(ObservableEnum.TroopChange);
		player.sendUserUpdateMsg();
		//发送客户端添加队伍
		player.sendWorldTroopMessage();
	}
	//更新部队的坦克血量到坦克库里
	public void changeHpToTank(WorldTroop worldTroop,Player player){
		List<Integer> tankIds = Lists.newArrayList();
		worldTroop.getTroopArmy().getTankList().forEach(t -> {
			tankIds.add(t.getId());
			player.playerTank().changeHp(t.getId(),t.getHp());
		});
		player.notifyObservers(ObservableEnum.TankHpUpdate, tankIds);
	}
	
	/**
	 * 广播部队移动
	 * @param worldTroop
	 */
	public void worldTroopMoveBroad(WorldTroop worldTroop,int startId,int nextId) {
		Player player = PlayerUtils.getPlayer(worldTroop);
		broadTroopMoveMsg(player, worldTroop,startId, nextId);
//		WorldCity targetCity = WorldServerContainer.of(worldTroop.getServerId()).getWorldCity(nextId);
//		if(targetCity.hasFight()){//下一个目标城市有战斗则广播给所有玩家
//
//		}else{
//			sendTroopMoveMsgToPlayer(player, worldTroop, startId, nextId);
//		}
	}
	//发送部队移动信息给
	public void sendTroopMoveMsgToPlayer(Player player,WorldTroop worldTroop,int startId,int nextId) {
		sendTroopMoveMsg(player, worldTroop, startId, nextId, 0,false);
	}
	//广播给当前服务器
	public void broadTroopMoveMsg(Player player,WorldTroop worldTroop,int startId,int nextId) {
		sendTroopMoveMsg(player, worldTroop, startId, nextId, 0,true);
	}
	public void sendTroopMoveMsg(Player player,WorldTroop worldTroop,int startId,int nextId,int moveType,boolean broardServer) {
		JsonMsg msg = new JsonMsg(MessageComm.S2C_WorldTroopMove);
		MovePlayerVo playerVo = new MovePlayerVo();
		playerVo.load(player);
		msg.addProperty("playerVo", playerVo);
		msg.addProperty("troopId", worldTroop.getId());
		msg.addProperty("troopSpeed", worldTroop.getTroopWay().getTroopSpeed());
		msg.addProperty("startCityId", startId);
		msg.addProperty("endCityId", nextId);
		msg.addProperty("moveType", moveType);
		if(broardServer) {
			int serverId = worldTroop.getKFServerId() > 0
					?worldTroop.getKFServerId():worldTroop.getServerId();
			//判断终点城池id
			int finalEndCityId = worldTroop.getTroopWay().getEndCityId();
			if(finalEndCityId <= 0) finalEndCityId = nextId;

			for (Player onlinePlayer : PlayerContainer.getGamePlayerMap().values()) {
				if(onlinePlayer.isInServerWorld(serverId)) {
					if(onlinePlayer.playerTemp().isWorldView()
						|| finalEndCityId == nextId && nextId == onlinePlayer.playerTemp().getCurCityId()
						|| moveType > 0) {
						onlinePlayer.sendMsg(msg);
					}
				}
			}
		}else{
			player.sendMsg(msg);
		}
	}
	
	
	public void sendWorldTroopUpdate(Player player,WorldTroop worldTroop) {
		Player troopPlayer = player.getId() == worldTroop.getPlayerId()?
				player:PlayerUtils.getOnlinePlayer(worldTroop.getPlayerId());
		if(troopPlayer != null) {
			JsonMsg msg = JsonMsg.create(MessageComm.S2C_UpdateWorldTroop);
			msg.addProperty("worldTroop", new WorldTroopVo(troopPlayer,worldTroop));
			troopPlayer.sendMsg(msg);
		}
	}
	public void sendWorldTroopUpdate(WorldTroop worldTroop) {
		Player player = PlayerUtils.getOnlinePlayer(worldTroop.getPlayerId());
		if(player != null) {
			sendWorldTroopUpdate(player, worldTroop);
		}
	}
	
	//检查路线是否可以通过
	public boolean canAdoptWays(Player player,WorldTroop worldTroop,List<Integer> wayList) {
		List<Integer> ways = Lists.newArrayList(wayList);
		//判断路径是有有未解锁
		if(ways.stream().anyMatch(e -> !isUnlockCity(player, e))) {
			return false;//有未解锁的路径
		}
//		return cityConfig.isFitWays(ways);
		return true;
	}


	public boolean isCanAdvance(Player player, WorldTroop worldTroop, int cityId, boolean sendErrorCodeMsg) {
		if (!isUnlockCity(player, cityId)) {//没有解锁，不能突进
			return false;
		}
		if (!cityConfig.isConnect(worldTroop.getCityId(), cityId)) {//不相连不能突进
			return false;
		}
		//处于战斗等待状态才能突进
		if (worldTroop.getState() != TroopState.FightWait.getType()) {
			if (sendErrorCodeMsg) {
				player.sendErrorMsg(SysConstant.WorldTroop_Not_Advance_TroopState);
			}
			return false;
		}
		WorldCity nowCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
		//有路障不能突进
//		if(nowCity.getCityStatus().haveCityStatus(CityStatusType.Roadblock)) {
//			player.sendErrorMsg(SysConstant.WorldTroop_Roadblock);
//			return false;
//		}
		WorldCity nextWorldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if (!isEnterCityFight(player, nextWorldCity)) {
			if (sendErrorCodeMsg) {
				player.sendErrorMsg(SysConstant.WorldTroop_Not_Advance_City);
			}
			return false;
		}
		int curAdvanceBei = getCurAdvanceBei(nowCity, player);
		//城市中我方部队比地方部队多才能突进
		if (!checkTroopCanAdvance(nowCity, player, worldTroop.getId(), curAdvanceBei)) {
			//不能突进！！
			if (sendErrorCodeMsg) {
				player.sendMsg(MessageComm.S2C_AdvanceFail, curAdvanceBei);
			}
			return false;
		}
		return true;
	}

	//检查部队是否可以突进
	public boolean isCanAdvance(Player player, WorldTroop worldTroop, int cityId) {
		return isCanAdvance(player, worldTroop, cityId, true);
	}
	
	//突进到城市
	public boolean advanceToCity(Player player,WorldTroop worldTroop,WorldCity worldCity,int nextCityId) {
		if(!worldCity.removeTroop(worldTroop.getId())) {
			log.error(worldTroop.getId()+"删除失败:突进在"+worldCity.getId()+"删除失败");
			return false;
		}
		//广播城市部队变化
		worldBiz.broadWorldCityTroopChange(worldCity);
		List<Integer> wayList = Lists.newArrayList(nextCityId);
		//加入起点
		wayList.add(0, worldCity.getId());
		//部队开始行进
		stratMove(worldTroop,wayList);
		//设置部队的突进时间
		int effectMinute = commValueConfig.getCommValue(CommonValueType.TroopAdvanceSkill);
		worldTroop.getTroopTemp().setAdvanceSkillTime(System.currentTimeMillis()+effectMinute*GameConstants.MINUTE);
		//保存城上的部队信息
		worldCity.saveDB();
		sendWorldTroopUpdate(player, worldTroop);
		
		player.notifyObservers(ObservableEnum.TroopAdvance);
		player.sendUserUpdateMsg();
		return true;
	}
	//获取突进最小倍数
	public int getCurAdvanceBei(WorldCity nowCity,Player player) {
		//突进判断部队数量
		int maxBei = commValueConfig.getCommValue(CommonValueType.AdvanceMinBei);
		TheSuckerTatics theSuckerTatics = (TheSuckerTatics)guildTechBiz.getGuildCityTactics(player, GuildTacticsType.TheSucker, nowCity.getId());
		if(theSuckerTatics != null) {
			maxBei -= theSuckerTatics.getReduceBei();
		}
		//计算科技加成
//		maxBei -= (int)guildTechBiz.getGuildTecAdd(player, GuildTecFunEnum.TroopAdvance);
		return Math.max(maxBei, 1);
	}
	//该部队是否可以在本城市突进(只判断己方部队是否大于敌方部队)
	public boolean checkTroopCanAdvance(WorldCity nowCity,Player player,String troopId,int cuBei) {
		int atkSize = nowCity.getTroopSize(AtkDefType.ATK);
		int defSize = nowCity.getTroopSize(AtkDefType.DEF);
		AtkDefType atkDefType = nowCity.getAtkDefType(troopId);
		//己方部队数量
		int myTroopSize = atkDefType == AtkDefType.ATK?atkSize:defSize;
		//地方部队数量
		int enemyTroopSize = atkDefType == AtkDefType.ATK?defSize:atkSize;
		return myTroopSize >= enemyTroopSize*cuBei;
	}

	public boolean isCanRetreat(Player player, WorldTroop worldTroop, int cityId){
		return isCanRetreat(player, worldTroop, cityId, false);
	}

	//检查部队是否可以撤退
	public boolean isCanRetreat(Player player, WorldTroop worldTroop, int cityId, boolean isAuto) {
		//处于战斗等待状态才能突进
		if(worldTroop.getState()!=TroopState.FightWait.getType()){
			if (!isAuto) {
				player.sendErrorMsg(SysConstant.WorldTroop_Not_Retreat_TroopState);
			}
			return false;
		}
		if(!isUnlockCity(player, cityId)){//没有解锁，不能突进
			return false;
		}
		if(!cityConfig.isConnect(worldTroop.getCityId(), cityId)){//不相连不能突进
			return false;
		}
		//只能撤往我方的和平城市
		WorldCity nextWorldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if(isEnterCityFight(player, nextWorldCity)){
			if (!isAuto) {
				player.sendErrorMsg(SysConstant.WorldTroop_Not_Retreat_City);
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 发生战斗-> 突进
	 * 进入和平城 -> 撤退
	 * 是否进入城池发生战斗 
	 * @param player
	 * @param worldCity
	 * @return
	 */
	public boolean isEnterCityFight(Player player,WorldCity worldCity) {
//		if(worldCity.getCityStatus().haveCityStatus(CityStatusType.Roadblock)) {
//			return false;
//		}
		if(worldDeclareWarBiz.isEnemyGuild(player, worldCity)) {
			return true;//是敌对方就战斗
		}
		if(worldCity.hasFight() && guildCityFightBiz.isFriendCity(player, worldCity)) {
			return true;//友方有战斗
		}
		return false;
	}

	public boolean isEnterCityFightForMove(Player player,WorldTroop worldTroop,WorldCity worldCity) {
		if(isEnterCityFight(player,worldCity)) {
			LinkedList<Integer> wayList = worldTroop.getTroopWay().getWayList();
			//判断是否经过港口城池
			if(worldCity.isPortCity() && wayList.size() > 1) {
				//判断城内部队的数量 如果<3 进城就不能出来了 直接战斗
				boolean isEnemy = worldDeclareWarBiz.isEnemyGuild(player, worldCity);
				return worldCity.getTroopSize(isEnemy?AtkDefType.ATK:AtkDefType.DEF) < 3;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 改变部队状态
	 * @param worldCity
	 * @param troopIds
	 * @param state
	 */
	public void changeCityTroopState(WorldCity worldCity,List<BaseCityFightTroop> troopList,TroopState state) {
		troopList.stream().forEach(troopId -> changeCityTroopState(worldCity, troopId, state));
	}
	public void changeCityTroopState(WorldCity worldCity,BaseCityFightTroop troop,TroopState state) {
		if(troop.isNpcTroop() || troop.isCloneTroop()) {
			//npc部队改变
			troop.changeState(state);
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(worldCity).getWorldTroop(troop.getId());
		if(worldTroop != null && worldTroop.getState() != state.getType()) {
			if(worldCity.getId() != worldTroop.getCityId()) {
				log.error(worldTroop.getId()+"部队不在同一城市  =:"+worldCity.getId()+"状态："+worldTroop.getState()+" ="+worldTroop.getCityId());
				worldCity.removeTroop(troop.getId());
				return;
			}
			if(state == TroopState.Death) {
				worldTroop.getTroopArmy().troopDeath();//血量清0
				worldTroop.setCityId(GameConstants.PeaceId);//先飘在空中
				worldTroop.getTroopTemp().doDeath();
				worldTroop.getTroopArmy().updateLastRecoverTime();
			}
			worldTroop.changeState(state);
			worldTroop.saveDB();
			sendWorldTroopUpdate(worldTroop);
		}
	}
	
	//部队开始行进
	public void stratMove(WorldTroop worldTroop,List<Integer> wayList) {
		worldTroop.changeState(TroopState.Move);//改为行进状态
		worldTroop.getTroopWay().clear();
		worldTroop.getTroopWay().setStartCityId(worldTroop.getCityId());
		worldTroop.getTroopWay().setWayList(Lists.newLinkedList(wayList));
//		worldTroop.saveDB();
	}
	//检查出战部队是否合法（只检查禁止出站的阵营和兵种）
	public boolean checkFinghtTank(Player player,String tankIds, String prohibitCamp, String prohibitArms) {
		List<Integer> tanks =StringUtil.splitStr2IntegerList(tankIds, ",");
		return checkFightTank(player, tanks, prohibitCamp, prohibitArms);
	}
	public static boolean checkFinghtTank(Player player,String tankIds) {
		List<Integer> tanks =StringUtil.splitStr2IntegerList(tankIds, ",");
		return checkFightTank(player, tanks);
	}
	public static boolean checkFightTank(Player player,List<Integer> tankIds){
		return tankIds.stream().mapToInt(tankId -> tankId).noneMatch(tankId ->{
			Tank tank = player.playerTank().getTank(tankId);
			return tank == null||tank.getMainTank()>0;
		});
	}
	/**
	 * 检查本服逻辑 
	 * 检查部队是否包含被sss绑定的坦克列表
	 * @param player
	 * @param tanks
	 * @return
	 */
	public static boolean checkGameFightTankArmy(Player player,List<TankArmy> tanks){
		return tanks.stream().mapToInt(t -> t.getId()).noneMatch(tankId ->{
			Tank tank = player.playerTank().getTank(tankId);
			return tank == null||tank.getMainTank()>0;
		});
	}
	/**
	 * 检查跨服服逻辑 
	 * 检查部队是否包含被sss绑定的坦克列表
	 * @param player
	 * @param tanks
	 * @return
	 */
	public static boolean checkKFFightTankArmy(long playerId,List<TankArmy> tanks){
		PlayerTankBind playerTankBind = PlayerTankBind.getPlayerTankBind(playerId);
		if(playerTankBind == null) {
			return true;
		}
		List<Integer> beBinds = playerTankBind.getBeBindTankList();
		return tanks.stream().mapToInt(t -> t.getId()).noneMatch(tankId ->beBinds.contains(tankId));
	}

	public boolean checkFightTank(Player player, List<Integer> tankIds, String prohibitCamp, String prohibitArms) {
		return tankIds.stream().mapToInt(tankId -> tankId).noneMatch(tankId ->{
			Tank tank = player.playerTank().getTank(tankId);
			return tank == null || tankConfig.isProhibit(tankId, prohibitCamp, prohibitArms)||tank.getMainTank()>0;
		});
	}
	
	public void recalTroopRecoveSecondHp(Player player,WorldTroop worldTroop) {
		worldTroop.getTroopTemp().setSecondAdd(0);
		calTroopRecoveSecondHp(player, worldTroop);
	}
	/**
	 * 计算部队每秒回血量
	 * @param player
	 * @param worldTroop
	 * @return
	 */
	public long calTroopRecoveSecondHp(Player player,WorldTroop worldTroop) {
		long troopSecondAdd = worldTroop.getTroopTemp().getSecondAdd();
		if(troopSecondAdd > 0) {
			return troopSecondAdd;//部队回血
		}
		long addHp = commValueConfig.getCommValue(CommonValueType.TroopRecoverHp);
		//主城回血提升20%
		double addRate = 0;
		//计算部落科技增加
		addRate += guildTechBiz.getGuildTecAdd(player, GuildTecFunEnum.ArmyReHp);
		//计算装备光环增加
		addRate += equipmentBiz.getEquipHpCircle(player);
		//座驾增加
		addRate += commanderBiz.getCarHpAddRate(player);
		//世界建筑
		addRate += worldBuildBiz.getWorldBuildAddValue(player.getServerId(), WorldBuildAddType.TroopRecover);
		//计算单车额外的额外百分比
		addRate += calTroopForTankRecoverRate(player, worldTroop);
		
		addHp += MathUtils.mul(addHp,addRate);
		//每秒最多回复2%
		long maxAddHp = (int)(calTroopTotalHp(player, worldTroop.getTroopArmy().getTankList())*0.02d);
		troopSecondAdd = Math.min(addHp, maxAddHp);
		worldTroop.getTroopTemp().setSecondAdd(troopSecondAdd);
		return troopSecondAdd;
	}
	
	/**
	 * 检查部队的索引
	 * @param player
	 */
	public void checkPlayerTroopIndex(Player player) {
		List<WorldTroop> troopList = TroopServerContainer.of(player).getWorldTroopByPlayer(player);
		for (WorldTroop worldTroop : troopList) {
			int newIndex = player.playerTroops().getTroopIndex(worldTroop.getId());
			if(worldTroop.getTroopInfo().getIndex() != newIndex) {
				worldTroop.getTroopInfo().setIndex(newIndex);
				worldTroop.saveDB();
			}
		}
	}
	//停止移动
	public void stopMove(Player player,WorldTroop worldTroop) {
		try {
			worldTroop.lockTroop();
			LinkedList<Integer> ways = worldTroop.getTroopWay().getWayList();
			if(ways.size()<2){
				return;
			}
			LinkedList<Integer> newWays = Lists.newLinkedList();
			newWays.add(ways.getFirst());
			worldTroop.getTroopWay().setWayList(newWays);
			worldTroop.saveDB();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			worldTroop.unlockTroop();
		}
	}
	
	/**
	 * 选择所需回复的坦克
	 * @param player
	 * @param worldTroop
	 * @return
	 */
	public int choiceRecoverTankId(Player player,WorldTroop worldTroop) {
		//判断俘虏
		List<Integer> beCaptiveTankIds = player.playerCaptive().getBeCaptiveTank();
		/**
		 * 1,过滤满血
		 * 2,按照剩余血量百分比排序
		 * 3,找出最大血量
		 */
		List<TankArmy> armyList = worldTroop.getTroopArmy().getTankList().stream()
		.filter(e -> !beCaptiveTankIds.contains(e.getId()))
		.filter(e -> !e.isFullHp())
		.sorted((TankArmy t1,TankArmy t2) -> {
			Tank tank1 = player.playerTank().getTank(t1.getId());
			Tank tank2 = player.playerTank().getTank(t2.getId());
			if(tank1 == null) {
				return -1;
			}
			if(tank2 == null) {
				return -1;
			}
			long maxHp1 = (long)tank1.getTotalAttr(TankAttrType.HP);
			long maxHp2 = (long)tank2.getTotalAttr(TankAttrType.HP);
			return MathUtils.div(t1.getHp(), maxHp1) > MathUtils.div(t2.getHp(), maxHp2)?-1:1;
		}).collect(Collectors.toList());
		if(armyList.isEmpty()) {
			//全部加满了，复活了
			return -1;
		}
		return armyList.get(0).getId();
	}

	/**
	 * 获取部队油耗
	 * @param player
	 * @param tankList
	 * @return
	 */
	public long getTroopSpendOil(Player player,List<TankArmy> tankList) {
		return tankList.stream().mapToLong(e -> {
			Tank tank = player.playerTank().getTank(e.getId());
			if(tank == null) {
				return 0;
			}
			return tank.getOil();
		}).sum();
	}
	//获取部队回满血的石油消耗
	public long getRecoveryCost(Player player,List<TankArmy> recoverList) {
		long spendOil = recoverList.stream().mapToLong(e -> {
			Tank tank = player.playerTank().getTank(e.getId());
			if(tank == null) {
				return 0;
			}
			long maxHp = (long)tank.getTotalAttr(TankAttrType.HP);
			return (long)(Math.min(1, MathUtils.div(maxHp-e.getHp(), maxHp))*tank.getOil());
		}).sum();
		return spendOil;
	}
	//计算部队恢复的最大血量
	public long calTroopRecoveHp(Player player, List<TankArmy> recoverList) {
		long lastTotalHp = recoverList.stream().mapToLong(e -> {
			Tank tank = player.playerTank().getTank(e.getId());
			if(tank == null) {
				return 0;
			}
			long maxHp = (long)tank.getTotalAttr(TankAttrType.HP);
			return Math.max(0, maxHp-e.getHp());
		}).sum();
		return lastTotalHp;
	}
	public long calTroopTotalHp(Player player, List<TankArmy> recoverList) {
		return recoverList.stream().mapToLong(e -> {
			Tank tank = player.playerTank().getTank(e.getId());
			if(tank == null) {
				return 0;
			}
			return (long)tank.getTotalAttr(TankAttrType.HP);
		}).sum();
	}
	
	public long calTanksTotalHp(Player player, List<Integer> tankIds) {
		return tankIds.stream().mapToLong(e -> {
			Tank tank = player.playerTank().getTank(e);
			if(tank == null) {
				return 0;
			}
			return (long)tank.getTotalAttr(TankAttrType.HP);
		}).sum();
	}

	//获取部队回满血所需时间
	public long calTroopRecoveTime(Player player, WorldTroop worldTroop,
			List<TankArmy> recoverList) {
		long addSecondHp = calTroopRecoveSecondHp(player, worldTroop);
		long lastTotalHp =  calTroopRecoveHp(player, recoverList);
		return (long)Math.ceil(MathUtils.div(lastTotalHp, addSecondHp));
	}
	
	//玩家是否解锁此城池
	public boolean isUnlockCity(Player player,int cityId) {
		return player.playerMission().isUnlockCity(cityId);
	}
	
	public void movePlayerTroopToBerlin(WorldTroop worldTroop) {
		try {
			worldTroop.lockTroop();
			if(worldTroop.getState() != TroopState.Death.getType()) {
				worldTroop.changeState(TroopState.None);
				worldTroop.setCityId(GameConstants.PeaceId);
				worldTroop.getTroopWay().clear();
				worldTroop.saveDB();
			}
		} finally {
			worldTroop.unlockTroop();
		}
	}
	
	//获取玩家的所有部队信息
	public Map<Integer,List<TankVo>> getTroops(Player player){
		Map<Integer,List<TankVo>> troops = Maps.newConcurrentMap();
		List<String> troopIds = player.playerTroops().getTroopIdList();
		for(String troopId:troopIds){
			WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
			if(worldTroop!=null){
				List<TankVo> tanks = worldTroop.getTroopArmy().getTankList().stream().map(t ->{
					Tank tank = player.playerTank().getTank(t.getId());
					TankVo tankVo = tank.createTankVo();
					tankVo.setIndex(t.getIndex());
					return tankVo;
				}).collect(Collectors.toList());
				troops.put(worldTroop.getTroopInfo().getIndex(), tanks);
			}
		}
		return troops;
	}
	
	/**
	 * 计算玩家移动速度
	 * @param player
	 */
	public int calPlayerWorldMoveSpeed(Player player) {
		int moveSpeed = commValueConfig.getCommValue(CommonValueType.TroopMoveSpeed);
		//减去科技加成
		moveSpeed -= (int)guildTechBiz.getGuildTecAdd(player, GuildTecFunEnum.TroopMoveSpeed);
		//判断总统特权的制裁
		if(serverPowerBiz.isBePunished(player.getId())) {
			moveSpeed += commValueConfig.getCommValue(CommonValueType.PresidentPower_Troop);
		}
		return moveSpeed;
	}
	
	public Items calPlayerCloneItem(Player player) {
		if(player.playerBag().getItemCount(ItemConstant.CloneItem) > 0) {
			return new Items(ItemConstant.CloneItem, 1, ItemType.ITEM);
		}
		int costGold = commValueConfig.getCommValue(CommonValueType.CloneTroopCost);
		if(player.getPlayerRecharge().haveSeasonVip()){
			costGold = (int) (costGold * Convert.toDouble(commValueConfig.getStrValue(CommonValueType.SeasonVipCard_Buf3),1d));
		}
		return new Items(PlayerAssetEnum.Gold.getTypeId(), costGold, ItemType.CURRENCY);
	}
	
	/**
	 * 计算部队5只坦克单车回复速度加成
	 * @param player
	 * @param worldTroop
	 * @return
	 */
	public static double calTroopForTankRecoverRate(Player player,WorldTroop worldTroop) {
		PlayerTank playerTank = player.playerTank();
		return worldTroop.getTroopArmy().getTankList().stream().map(e -> playerTank.getTank(e.getId()))
		.filter(Objects::nonNull).mapToDouble(e -> e.getTotalAttr(TankAttrType.TroopRecoverHp))
		.sum();
	}
	/**
	 * 根据玩家复活所需时间计算本次复活需要消耗的锤子数
	 * @param haveNum
	 * @param totalSecond
	 * @param oneItemSecond
	 * @return
	 */
	public long getTroopFullHpNeedItemNum(long totalSecond,long oneItemSecond) {
		return Math.max(1, (long)Math.ceil(MathUtils.div(totalSecond, oneItemSecond,4)));
	}
	
	
}


