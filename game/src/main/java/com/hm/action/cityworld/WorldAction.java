package com.hm.action.cityworld;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.cityworld.biz.WorldCityBiz;
import com.hm.action.cityworld.vo.FightTroopVo;
import com.hm.action.cityworld.vo.SMovePlayerVo;
import com.hm.action.cityworld.vo.WorldCityVo;
import com.hm.action.kfgame.DKFServerBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.client.ClientTroop;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.config.GameConstants;
import com.hm.db.WarResultUtils;
import com.hm.enums.AtkDefType;
import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.ServerInfoCache;
import com.hm.message.InnerMessageComm;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.CityFight;
import com.hm.model.cityworld.Pvp1v1;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.fight.FightProxy;
import com.hm.model.guild.Guild;
import com.hm.model.player.KFPServerUrl;
import com.hm.model.player.Player;
import com.hm.model.war.FightDataRecord;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.rpc.GameRpcManager;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.StringUtil;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.PlayerTroop;
import com.hm.war.sg.troop.WorldCityNpcTroop;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Action
public class WorldAction extends AbstractPlayerAction {
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private WorldBuildBiz worldBuildBiz;
	@Resource
	private WorldCityBiz worldCityBiz;
	@Resource
	private DKFServerBiz dkfServerBiz;

	@MsgMethod(MessageComm.C2S_IntoKFWorld)
	public void C2S_IntoKFWorld(Player player,JsonMsg msg){
		dkfServerBiz.intoKFWorld(player,71);
	}
	
	@MsgMethod(MessageComm.C2S_IntoWorld)
	public void intoWorld(Player player,JsonMsg msg){
		player.playerTemp().setWorldType(WorldType.Normal);

		List<WorldCityVo> cityList = WorldServerContainer.of(player).getWorldCitys(WorldType.Normal)
				.stream().map(e -> worldBiz.createWorldCityVo(e)).collect(Collectors.toList());
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_IntoWorld);
		serverMsg.addProperty("cityList", cityList);
		player.sendMsg(serverMsg);
		player.sendWorldTroopMessage();
		
		worldBuildBiz.sendPlayerWorldBuildTroop(player);
	}
	
	@MsgMethod(MessageComm.C2S_LeaveWorld)
	public void leaveWorld(Player player,JsonMsg msg){
		player.playerTemp().leaveWorld();
	}

	@MsgMethod(MessageComm.C2S_WorldCityFightCurFrame)
	public void getWorldCityFightCurFrame(Player player,JsonMsg msg){
		int cityId = msg.getInt("cityId");
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if(!worldCity.hasFight()) {
			return;
		}
		CityFight cityFight = worldCity.getCityFight();
		if(cityFight == null) {
			return;//没有战斗不能进入观看
		}
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldCityFightCurFrame);
		serverMsg.addProperty("curFrame", cityFight.getWarResult().getRunningFrame());
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_WorldCityFightData)
	public void getWorldCityFightData(Player player,JsonMsg msg){
		int cityId = msg.getInt("cityId");
		
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		//判断PVE是否解锁此城市
		if(!troopBiz.isUnlockCity(player, cityId)) {
			player.sendErrorMsg(SysConstant.LockCity);
			return;//没有战斗不能进入观看
		}
		player.playerTemp().setCurCityId(worldCity.getId());

		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldCityFightData);
		serverMsg.addProperty("cityId", worldCity.getId());
		serverMsg.addProperty("atkSize", worldCity.getTroopSize(AtkDefType.ATK));
		serverMsg.addProperty("defSize", worldCity.getTroopSize(AtkDefType.DEF));

		if(!worldCity.hasFight()) {
			//添加防守第一只部队信息
			List<SMovePlayerVo> voList = worldCity.getDefTroopForMoveVo();
			serverMsg.addProperty("defTroopList", voList);
			player.sendMsg(serverMsg);
			return;//没有战斗
		}
		CityFight cityFight = worldCity.getCityFight();
		WarResult warResult = cityFight.getWarResult();
		if(warResult == null) {
			player.sendMsg(serverMsg);
			return;//没有战斗
		}
		serverMsg.addProperty("warResult", warResult);
		serverMsg.addProperty("startFrame", warResult.getRunningFrame());

		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_LeaveCity)
	public void leaveCity(Player player,JsonMsg msg) {
		player.playerTemp().setCurCityId(0);
	}
	

	
	@MsgMethod(MessageComm.C2S_WorldCityTroopList)
	public void worldCityTroopList(Player player,JsonMsg msg){
		final int cityId = msg.getInt("cityId");
		int pageNum = msg.getInt("pageNum");
		int pageCount = msg.getInt("pageCount");
		int[] typeList = StringUtil.strToIntArray(msg.getString("types"), ",");
		
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		//城池所属部落
		Map<Integer,List<FightTroopVo>> typeMap = Maps.newHashMap();
		int skip = (pageNum-1)*pageCount;
		for (int type : typeList) {
			List<BaseCityFightTroop> troopList = Lists.newArrayList(worldCity.getCityTroop(type).getTroopList());
			List<FightTroopVo> worldTroopList = troopList.stream()
					.skip(skip).limit(pageCount)
					.map(e -> e.createFightTroopVo(worldCity))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			typeMap.put(type, worldTroopList);
		}
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldCityTroopList);
		serverMsg.addProperty("atkWorldTroopList", typeMap.get(AtkDefType.ATK.getType()));
		serverMsg.addProperty("defWorldTroopList", typeMap.get(AtkDefType.DEF.getType()));
		player.sendMsg(serverMsg);
	}
	
	
	@MsgMethod(MessageComm.C2S_Pvp1v1FightData)
	public void getPvp1v1FightData(Player player,JsonMsg msg){
		String troopId = msg.getString("troopId");
		if(!player.playerTroops().isContain(troopId)) {
			player.sendErrorMsg(SysConstant.WorldCity_NotFight);
			player.sendNormalErrorMsg(MessageComm.S2C_Pvp1v1FightData);
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null || worldTroop.getState() != TroopState.PvpOneByOne.getType()) {
			player.sendErrorMsg(SysConstant.WorldCity_NotFight);
			player.sendNormalErrorMsg(MessageComm.S2C_Pvp1v1FightData);
			return;
		}
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
		if(worldCity == null) {
			player.sendErrorMsg(SysConstant.WorldCity_NotFight);
			player.sendNormalErrorMsg(MessageComm.S2C_Pvp1v1FightData);
			return;
		}
		Pvp1v1 pvp = worldCity.getCityFight().getPvp1v1(troopId);
		if(pvp == null) {
			player.sendErrorMsg(SysConstant.WorldCity_NotFight);
			player.sendNormalErrorMsg(MessageComm.S2C_Pvp1v1FightData);
			return;
		}
		player.playerTemp().setCurCityId(0);
		WarResult warResult = pvp.getWarResult();
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Pvp1v1FightData);
		serverMsg.addProperty("warResult", warResult);
		serverMsg.addProperty("startFrame", warResult.getRunningFrame());
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_WarResult_Record)
	public void getWarResultRecord(Player player,JsonMsg msg){
		if(!GameConstants.FightRecordDB) {
			player.sendErrorMsg(SysConstant.WarResult_Record_NotHave);
			return;
		}
		String recordId = msg.getString("id");
		FightDataRecord battleRecord = WarResultUtils.getFightDataRecord(player.getServerId(), recordId);
		if(battleRecord == null) {
			player.sendErrorMsg(SysConstant.WarResult_Record_NotHave);
			return;
		}
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WarResult_Record);
		serverMsg.addProperty("warResult", battleRecord.getResult());
		player.sendMsg(serverMsg);
	}
	
	
	@MsgMethod(MessageComm.C2S_GuidWarData)
	public void guidWarData(Player player,JsonMsg msg){
		// 构建玩家部队
		ClientTroop clientTroop = ClientTroop.buildFull(player, msg);
		PlayerTroop atkTroop = new PlayerTroop(player.getId(), "guild");
		atkTroop.loadClientTroop(clientTroop);
		//获取npc
		int[] npcList = {102001,102002,102003};
		int npcIndex = msg.getInt("index");
		WorldCityNpcTroop npcTroop = new WorldCityNpcTroop(npcList[npcIndex]);
		
		WarResult warResult = new FightProxy().doFight(atkTroop, npcTroop);
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_GuidWarData);
		serverMsg.addProperty("battleRecord", warResult);
		serverMsg.addProperty("npcIndex", npcIndex);
		player.sendMsg(serverMsg);
	}

	@MsgMethod(MessageComm.C2S_GiveUp_City)
	public void C2S_GiveUp_City(Player player,JsonMsg msg){
		//判断军团权限
		Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
		if(guild == null || !guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		int cityId = msg.getInt("cityId");
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if(worldCity.getBelongGuildId() != guild.getId()) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		worldCityBiz.giveUpCity(player,worldCity);

		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_GiveUp_City);
		serverMsg.addProperty("cityId", cityId);
		player.sendMsg(serverMsg);
	}

}
