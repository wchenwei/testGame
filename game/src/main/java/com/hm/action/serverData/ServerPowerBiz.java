package com.hm.action.serverData;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.player.PlayerBiz;
import com.hm.action.serverData.vo.PunishVo;
import com.hm.config.CityConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.PlayerContainer;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerPresidentPower;
import com.hm.model.serverpublic.serverpower.MilitaryPolicy;
import com.hm.model.serverpublic.serverpower.Punish;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.util.ServerUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Biz
public class ServerPowerBiz implements IObserver{

	@Resource
	private CityConfig cityConfig;
	@Resource
	private PlayerBiz playerBiz;

	@Resource
	private CommValueConfig commValueConfig;
	
	public boolean isBigCity(List<Integer> cityIds){
		List<Integer> bigCityIds = cityConfig.getBigCityIds();
		for(int cityId:cityIds){
			if(!bigCityIds.contains(cityId)){
				return false;
			}
		}
		return true;
	}

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.CityBattleResult, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryPolicy, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		switch (observableEnum) {
			case CityBattleResult:
				calBattleResult(player, (int)argv[0], (int)argv[1],(boolean)argv[2],(WorldCity)argv[3],(String)argv[4]);
				break;
			case MilitaryPolicy:
				doServerMilitaryPolicy(player,(int)argv[0]);
				break;
		}
	}
	
	
	public void clearPunish(Player player,Object... argv) {
		int serverId = player!=null?player.getServerId():Integer.parseInt(argv[0].toString());
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		serverData.getServerPresidentPower().clearPunish();
		serverData.save();
		broadPowerChange(serverId);
	}
	
	//广播总统特权相关变化
	public void broadPowerChange(int serverId){
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_ServerPowerChange);
		serverMsg.addProperty("ServerPresidentPower", serverData.getServerPresidentPower());
		PlayerContainer.broadPlayer(serverId, serverMsg);
	}
	//是否是中心城市
	public boolean isCenterCity(int serverId,int cityId){
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		return serverPresidentPower.isCenterCity(cityId);
	}
	
	//获取政策经验加成上限
	public long getPolicyAddExpLimit(int serverId){
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		return serverPresidentPower.getExpLimit();
	}
	
	/**
	 * 战斗结束计算总统特权经验
	 * @param player
	 * @param killTankNum
	 * @param deathTankNum
	 * @param isWin
	 * @param worldCity
	 * @param troopId
	 */
	private void calBattleResult(Player player,int killTankNum,int deathTankNum,boolean isWin,WorldCity worldCity,String troopId) {
		if(player == null || isWin) {
			return;
		}
		int serverId = player.getServerId();
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		MilitaryPolicy militaryPolicy = serverPresidentPower.getMilitaryPolicy();
		if(militaryPolicy==null || !militaryPolicy.isInTime()) {
			return;
		}
		if(militaryPolicy.getId() != player.playerPresidentData().getMark()) {
			player.playerPresidentData().resetDay(militaryPolicy.getId());
		}
		//本次最多获取经验
		long maxExp = Math.max(serverPresidentPower.getExpLimit()-player.playerPresidentData().getExp(), 0);
		if(maxExp <= 0) {
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			return;
		}
		List<Integer> tankIds = worldTroop.getTroopArmy().getTankList().stream().map(e -> e.getId()).collect(Collectors.toList());
		long totalOil = player.playerTank().getTankTotalOil(tankIds);
		long addExp = (long)(totalOil*commValueConfig.getDoubleCommValue(CommonValueType.PresidentPower_ExpRate));
		addExp = Math.min(addExp, maxExp);
		player.playerPresidentData().addExp(addExp);
		playerBiz.addPlayerExp(player, addExp, LogType.PresidentExp);
	}
	
	/**
	 * 检查玩家总统特权经验
	 * @param player
	 */
	public void checkPlayerPresidentExp(Player player) {
		int serverId = player.getServerId();
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		MilitaryPolicy militaryPolicy = serverPresidentPower.getMilitaryPolicy();
		if(militaryPolicy==null) {
			return;
		}
		if(militaryPolicy.getId() != player.playerPresidentData().getMark()) {
			player.playerPresidentData().resetDay(militaryPolicy.getId());
		}
	}
	
	/**
	 * 处理总统新颁布的政策
	 * @param player
	 */
	public void doServerMilitaryPolicy(Player player,int newId) {
		for (Player temp : PlayerContainer.getOnlinePlayersByServerId(player.getServerId())) {
			temp.playerPresidentData().resetDay(newId);
			temp.sendUserUpdateMsg();
		}
	}
	
	//玩家是否处于被制裁中
	public boolean isBePunished(long playerId){
		int serverId = ServerUtils.getServerId(playerId);
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		if(serverData == null) {
			return false;
		}
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		return serverPresidentPower.isBePunished(playerId);
	}
	//是否处于可以设置中心城市的时间
	public boolean isInCenterCitySetTime() {
		int hour = DateUtil.hour(new Date(), true);
		if(hour>=8&&hour<23){
			return true;
		}
		return false;
	}

	public PunishVo createPunishVo(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		Punish punish = serverPresidentPower.getPunish();
		if(punish==null){
			return new PunishVo();
		}
		return punish.createVo();
	}
}
