package com.hm.action.cmq;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hm.action.http.gm.MergeServerBean;
import com.hm.actor.ActorDispatcherType;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.StatisticsType;
import com.hm.enums.TankRareType;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.TimeUtils;
import com.hm.log.LogBiz;
import com.hm.message.CmqMessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Equipment;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.tank.Driver;
import com.hm.model.tank.Tank;
import com.hm.mq.game.GameMqConfig;
import com.hm.mq.game.IMqProxy;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.util.ServerUtils;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
@Biz
public class CmqBiz implements IObserver{
	@Resource
	private LogBiz logBiz;
	@Resource
	private TankConfig tankConfig;

	public static IMqProxy cmqProxy = IMqProxy.createMqProxy();


	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ClearnceMission, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SuperWeaponLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CarLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CarModelIcon, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.VipLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankStarUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.DriverLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GoldStatistics, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GoldDaySurplus, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.JoinCamp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.BindIdCode, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankEvolveStarUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.WeekPointAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerEquipChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MagicReform, this);

		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLoginOut, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLoginZero, this);

		ObserverRouter.getInstance().registObserver(ObservableEnum.CarModelUnLock, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CarModelStarUp, this);

		ObserverRouter.getInstance().registObserver(ObservableEnum.Agent, this);

		ObserverRouter.getInstance().registObserver(ObservableEnum.TankSpecialLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankLv, this);
		//兵法升级
		ObserverRouter.getInstance().registObserver(ObservableEnum.WarCraftLvup, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.WarCraftSkillLvup, this);

		ObserverRouter.getInstance().registObserver(ObservableEnum.MasteryLvUp, this);

		ObserverRouter.getInstance().registObserver(ObservableEnum.BindPhone, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
					   Object... argv) {
		switch(observableEnum){
			case PlayerLevelUp:
				logBiz.addPlayerLevelLog(player);
				sendPlayerLevelUp(player);
				break;
			case ClearnceMission:
				int battleType = Integer.parseInt(argv[0].toString());
				int missionId = Integer.parseInt(argv[1].toString());
				long combat = Long.parseLong(argv[2].toString());
				int result = Integer.parseInt(argv[3].toString());
				logBiz.addPlayerBattleLog(player, battleType,missionId,combat,result);
				sendPlayerClearnceMission(player,missionId);
				break;
			case SuperWeaponLv:
				sendPlayerSuperWeaponLv(player);
				break;
			case MilitaryLvUp:
				sendPlayerMilitaryLv(player);
				break;
			case CarLv:
				if(Boolean.parseBoolean(argv[0].toString())){
					sendPlayerCarLv(player);
				}
				break;
			case CarModelIcon:
				int icon = Integer.parseInt(argv[0].toString());
				sendPlayerCarModelIcon(player,icon);
				break;
			case VipLevelUp:
				sendPlayerVipLvUp(player);
				break;
			case TankAdd:
			case TankStarUp:
			case DriverLv:
			case TankEvolveStarUp:
			case MagicReform:
			case TankSpecialLvUp:
			case TankLv:
				int tankId = Integer.parseInt(argv[0].toString());
				Tank tank = player.playerTank().getTank(tankId);
				if (tank == null) {
					return;
				}

				Driver driver = tank.getDriver();
				int driverLv = driver == null ? 0 : driver.getLv();

				int star = tank.getStar();
				TankRareType type = TankRareType.getType(tankConfig.getTankSetting(tankId).getRare());
				// 统计S,SS,和A级5星的
				if (type == TankRareType.SR || type == TankRareType.SSR ||type==TankRareType.SSSR) {
					sendPlayerTank(player, tank, star, driverLv);
				} else if (type == TankRareType.R && star >= 5) {
					sendPlayerTank(player, tank, star, driverLv);
				}
				break;
			case GoldStatistics:
				int currencyId = Integer.parseInt(argv[0].toString());
				if(currencyId!=CurrencyKind.Gold.getIndex()&&currencyId != CurrencyKind.SysGold.getIndex()){
					break;
				}
				long num = Long.parseLong(argv[1].toString());
				int statisticsType = Integer.parseInt(argv[2].toString());//1-收入 0-支出
				sendGoldStatistics(player,currencyId,num,statisticsType);
				break;
			case GoldDaySurplus:
				sendGoldDaySurplus(argv);
				break;
			case BindIdCode :
				sendIdCode(player);
				break;
			case WeekPointAdd :
				int weekPoint = Integer.parseInt(argv[0].toString());
				sendWeekPoint(player, weekPoint);
				break;
			case PlayerEquipChange :
				List<Equipment> equ = (List<Equipment>) argv[0];
				sendEquip(player, equ);
				break;
			case PlayerLoginOut :
				sendLoginOut(player);
				break;
			case PlayerLoginZero :
				sendLoginZero(player);
				break;
			case CarModelUnLock :
				//sendCarModelData sendAgentData
				int carModelId1 = Integer.parseInt(argv[0].toString());
				sendCarModelDataAdd(player, carModelId1);
				break;
			case CarModelStarUp :
				int carModelId2 = Integer.parseInt(argv[0].toString());
				sendCarModelDataUpdate(player, carModelId2);
				break;
			case Agent :
				int agentId = Integer.parseInt(argv[0].toString());
				String actType = argv[1].toString();
				sendAgentData(player, agentId, actType);
				break;
			//CarModelUnLock CarModelStarUp Agent active training
			case WarCraftLvup:
				sendWarCraftLvup(player);
				break;
			case WarCraftSkillLvup:
				sendWarCraftSkillLvup(player);
				break;
			case MasteryLvUp:
				int id = Integer.parseInt(argv[0].toString());
				sendMasteryLvUp(player, id);
				break;
			case BindPhone:
				bindPhone(player);
				break;

			default:
				break;
		}
	}

	private void sendIdCode(Player player) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_BindIdCode);
		int state = ServerConfig.getInstance().isIdCodeSwitch()?1:0;
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("idCode", StrUtil.isBlank(player.getIdCode())?"-1":player.getIdCode());
		cmqMsg.addProperty("state", state);
		cmqMsg.addProperty("createServerId", player.getCreateServerId());
		sendDefaultMessage(player, cmqMsg);
	}

	private void sendGoldDaySurplus(Object[] argv) {
		int serverId = Integer.parseInt(argv[0].toString());
		long sysGolds = Long.parseLong(argv[1].toString());
		long golds = Long.parseLong(argv[2].toString());
		long oil = Long.parseLong(argv[3].toString());
		long cash = Long.parseLong(argv[4].toString());
		long num = Long.parseLong(argv[5].toString());
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Gold_DaySurplus);
		cmqMsg.addProperty("serverId", serverId);
		cmqMsg.addProperty("sysGolds", sysGolds);
		cmqMsg.addProperty("golds", golds);
		cmqMsg.addProperty("oil", oil);
		cmqMsg.addProperty("cash", cash);
		cmqMsg.addProperty("activeNum", num);
		cmqMsg.addProperty("date", TimeUtils.formatSimpeTime(new Date()));
		sendDefaultMessage(serverId, cmqMsg);
	}

	private void sendGoldStatistics(Player player, int currencyId, long num,
									int statisticsType) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Gold_Statistics);
		cmqMsg.addProperty("serverId", player.getServerId());
		cmqMsg.addProperty("itemId", currencyId);
		cmqMsg.addProperty("num", num);
		cmqMsg.addProperty("type", statisticsType);
		cmqMsg.addProperty("date", TimeUtils.formatSimpeTime(new Date()));
		sendDefaultMessage(player, cmqMsg);
	}

	private void sendPlayerVipLvUp(Player player) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Player_VipLvUp);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("vipLv", player.getPlayerVipInfo().getVipLv());
		sendDefaultMessage(player, cmqMsg);
	}

	private void sendPlayerCarModelIcon(Player player,int icon) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Player_CarModelIcon);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("carModelIcon", icon);
		sendDefaultMessage(player, cmqMsg);
	}

	private void sendPlayerCarLv(Player player) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Player_Mounts_LvUp);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("carLv", player.playerCommander().getCarLv());
		sendDefaultMessage(player, cmqMsg);
	}

	private void sendPlayerMilitaryLv(Player player) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Player_MilitaryRank_LvUp);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("militaryLv", player.playerCommander().getMilitaryLv());
		sendDefaultMessage(player, cmqMsg);
	}

	private void sendPlayerSuperWeaponLv(Player player) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Player_SuperWeapon_LvUp);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("superWeaponLv", player.playerCommander().getSuperWeaponLv());
		sendDefaultMessage(player, cmqMsg);
	}

	private void sendPlayerClearnceMission(Player player, int missionId) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_PlayerMission);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("missionId", missionId);
		sendDefaultMessage(player, cmqMsg);
	}

	public void sendPlayerLevelUp(BasePlayer player){
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_PlayerLevelUp);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("lv", player.playerLevel().getLv());
		cmqMsg.addProperty("createTime", player.playerBaseInfo().getCreateDate().getTime());
		sendDefaultMessage(player, cmqMsg);

		this.sendPlatformMsg(cmqMsg);
	}

	private void sendPlayerTank(Player player, Tank tank, int star, int driverLv) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_PlayerTankSync);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("tankId", tank.getId());
		cmqMsg.addProperty("star", star);
		cmqMsg.addProperty("driverLv", driverLv);
		cmqMsg.addProperty("tankLv", tank.getLv());
		cmqMsg.addProperty("evolvestar", tank.getEvolveStar());
		cmqMsg.addProperty("reformLv", tank.getTankMagicReform().getReformLv());
		cmqMsg.addProperty("bigSkillId", tank.getTankMagicReform().getBigSkillId());
		cmqMsg.addProperty("specialLv", tank.getTankSpecial().getLv());
		sendDefaultMessage(player, cmqMsg);
	}
	/**
	 *
	 * @param player
	 * @param
	 */
	public void sendPlayerLogin(BasePlayer player){
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_PlayerLogin);
		cmqMsg.addProperty("channel", player.getChannelId());
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("uid", player.getUid());
		cmqMsg.addProperty("playerName", player.getName());
		cmqMsg.addProperty("lv", player.playerLevel().getLv());
		cmqMsg.addProperty("imei", player.playerBaseInfo().getImei());
		cmqMsg.addProperty("loginTime",System.currentTimeMillis());
		cmqMsg.addProperty("serverId", player.getServerId());
		cmqMsg.addProperty("combat", player.getPlayerDynamicData().getCombat());
		cmqMsg.addProperty("createTime", player.playerBaseInfo().getCreateDate().getTime());
		cmqMsg.addProperty("createServerId", player.getCreateServerId());
		sendDefaultMessage(player, cmqMsg);

		this.sendPlatformMsg(cmqMsg);
	}
	/**
	 *
	 * @param player
	 */
	public void sendPlayerRegister(BasePlayer player){
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_PlayerRegister);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("lv", player.playerLevel().getLv());
		cmqMsg.addProperty("uid", player.getUid());
		cmqMsg.addProperty("channel", player.getChannelId());
		cmqMsg.addProperty("imei", player.playerBaseInfo().getImei());
		cmqMsg.addProperty("createTime", player.playerBaseInfo().getCreateDate().getTime());
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		//开服第几天创建的
		int createServerDay = (int) (DateUtil.betweenDay(serverData.getServerOpenData().getFirstOpenDate(), player.playerBaseInfo().getCreateDate(), true) + 1);
		cmqMsg.addProperty("createServerDay", createServerDay);
		cmqMsg.addProperty("serverId", player.getServerId());
		cmqMsg.addProperty("createServerId", player.getCreateServerId());
		sendDefaultMessage(player, cmqMsg);

		this.sendPlatformMsg(cmqMsg);
	}
	public void playerRecharge(Player player, long rmb, int testFlag,
							   RechargeGiftTempImpl rechargeGift, int paychannel, Map<String, String> params) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_PlayerRecharge);
		Map<String, Object> tempMap = Maps.newHashMap();
		tempMap.put("accountid", player.getUid());
		tempMap.put("serverid", player.getServerId());
		tempMap.put("roleid", player.getId());
		tempMap.put("channel", player.getChannelId());
		tempMap.put("recordtime", new Date());
		tempMap.put("amt", rmb);
		tempMap.put("goodid", rechargeGift.getId());
		tempMap.put("goodname", rechargeGift.getName());
		tempMap.put("levelno", player.playerLevel().getLv());
		tempMap.put("testflag", testFlag);
		tempMap.put("paychannel", paychannel);

		tempMap.put("orderid", params.getOrDefault("orderid","-1"));
		tempMap.put("suporderid", params.getOrDefault("suporderid","-1"));

		tempMap.put("chappid", params.getOrDefault("chappid","-1"));
		tempMap.put("chchannelid", params.getOrDefault("chchannelid","-1"));
		tempMap.put("chchannelapplyid", params.getOrDefault("chchannelapplyid","-1"));

		cmqMsg.addProperty("param",JSON.toJSONString(tempMap));
		sendDefaultMessage(player, cmqMsg);

		cmqMsg.addProperty("serverid", player.getServerId());
		this.sendPlatformMsg(cmqMsg);
	}
	/**
	 * sendReportPlayer:(举报其他用户，发送到统计服务). <br/>
	 * @author zxj
	 * @param player
	 * @param tarPlayerId
	 * @param content  使用说明
	 */
	public void sendReportPlayer(Player player, int tarPlayerId, String content) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_ReportTar);
		cmqMsg.addProperty("userid", player.getId());
		cmqMsg.addProperty("serverid", player.getServerId());
		cmqMsg.addProperty("taruserid", tarPlayerId);
		cmqMsg.addProperty("content", content);
		sendDefaultMessage(player, cmqMsg);
	}

	public void statisServerMsg(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		this.statisServerMsg(serverId, serverData);
	}

	public void statisServerMsg(int serverId, ServerData serverData) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Statis_Msg);
		cmqMsg.addProperty("firstopendate", serverData.getServerOpenData().getFirstOpenDate());
		cmqMsg.addProperty("lastopendate", serverData.getServerOpenData().getLastOpenDate());
		cmqMsg.addProperty("opennum", serverData.getServerOpenData().getOpenNum());
		cmqMsg.addProperty("serverid", serverData.getServerId());
		cmqMsg.addProperty("serverlv", serverData.getServerStatistics().getServerLv());
		cmqMsg.addProperty("openday", serverData.getServerStatistics().getOpenDay());
		sendDefaultMessage(serverId, cmqMsg);
	}

	public void sendMergeServerMsg(List<MergeServerBean> data){
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_MergeServer_Msg);
		cmqMsg.addProperty("data", data);
		sendDefaultMessage(0, cmqMsg);
	}

	public void sendUserInvite(long uid,long playerId) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_UserInvite);
		cmqMsg.addProperty("uid", uid);
		cmqMsg.addProperty("playerId", playerId);
		sendDefaultMessage(ServerUtils.getServerId(playerId), cmqMsg);
	}

	public void sendVoucher(Player player,String voucherId, String voucherSetName, List<Items> rewards) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Voucher);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("voucherid", voucherId);
		cmqMsg.addProperty("serverid", player.getServerId());
		cmqMsg.addProperty("vouchersetname", voucherSetName);
		cmqMsg.addProperty("rewards", JSON.toJSONString(rewards));
		sendDefaultMessage(player, cmqMsg);
	}
	//发送日常任务活跃点数
	private void sendWeekPoint(Player player, int weekPoint) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Player_WeekPoint);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("weekPoint", weekPoint);
		cmqMsg.addProperty("sendtime", new Date().getTime());
		sendDefaultMessage(player, cmqMsg);
	}

	//发送玩家装备更好
	private void sendEquip(Player player, List<Equipment> equ) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Player_EquipChange);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("channel", player.getChannelId());
		cmqMsg.addProperty("serverid", player.getServerId());
		cmqMsg.addProperty("listEquip", equ);
		cmqMsg.addProperty("sendtime", new Date().getTime());
		sendDefaultMessage(player, cmqMsg);
	}


	private void sendPlatformMsg(CmqMsg cmqMsg) {
		try {
			String platformQueueName = GameMqConfig.getInstance().getPlatformTopic();
			int serverid = 0;
			if(StringUtil.isNullOrEmpty(platformQueueName)) {
				return;
			}
			if(cmqMsg.containsKey("serverid")) {
				serverid = cmqMsg.getInt("serverid");
			}else if(cmqMsg.containsKey("playerId")) {
				serverid = ServerUtils.getServerId(cmqMsg.getInt("playerId"));
			}
			if(GameServerManager.getInstance().isTestServer(serverid)) {
				return;
			}
			ActorDispatcherType.Cmq.putTask(new IRunner() {
				@Override
				public Object runActor() {
					try {
						cmqProxy.sendMessage(platformQueueName, cmqMsg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//统计用户退出时的在线时间
	private void sendLoginOut(Player player) {
		this.sendLoginOut(player, System.currentTimeMillis());
	}
	//统计凌晨模拟用户登录时的消息
	private void sendLoginZero(Player player) {
		this.sendLoginOut(player, TimeUtils.getNowZero()-1);
	}

	private void sendLoginOut(Player player, long time) {
        try {
            //退出的时候更新数据
            sendPlayerLogin(player);
        } catch (Exception e) {
            log.error("用户退出异常：", e);
        }

        CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_LoginOut);
        cmqMsg.addProperty("playerId", player.getId());
        cmqMsg.addProperty("onlinetime", player.getPlayerStatistics().getTodayStatistics(StatisticsType.ONLINE_TIME));
        cmqMsg.addProperty("counttime", time);
        sendDefaultMessage(player, cmqMsg);
    }

	public static void sendDefaultMessage(BasePlayer player, CmqMsg cmqMsg) {
		sendDefaultMessage(player.getServerId(), cmqMsg);
	}

	public static void sendDefaultMessage(int serverid, CmqMsg cmqMsg) {
		try {
			if (serverid > 0 && GameServerManager.getInstance().isTestServer(serverid)) {
				return;
			}
			ActorDispatcherType.Cmq.putTask(new IRunner() {
				@Override
				public Object runActor() {
					cmqProxy.sendDefaultMessage(cmqMsg);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//发送统计数据
	public void sendStatisData(BasePlayer player) {
		try {
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_LOGINSTATIS);
			cmqMsg.addProperty("playerId", player.getId());
			//特工
			cmqMsg.addProperty("agent", player.playerAgent().getAllAgent());

			sendDefaultMessage(player, cmqMsg);
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}

	//发送座驾皮肤数据
	private void sendCarModelDataAdd(BasePlayer player, int carModelId) {
		try {
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_MODELSTAR_ADD);
			cmqMsg.addProperty("playerId", player.getId());
			cmqMsg.addProperty("modelstar", carModelId);
			sendDefaultMessage(player, cmqMsg);
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}
	//发送座驾皮肤数据
	private void sendCarModelDataUpdate(BasePlayer player, int carModelId) {
		try {
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_MODELSTAR_UPDATE);
			cmqMsg.addProperty("playerId", player.getId());
			cmqMsg.addProperty("modelstar", carModelId);
			sendDefaultMessage(player, cmqMsg);
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}
	//发送特工数据
	private void sendAgentData(BasePlayer player, int agentId, String type) {
		try {
			//active training
			CmqMsg cmqMsg = null;
			if("active".equals(type)) {
				cmqMsg = new CmqMsg(CmqMessageComm.S2S_AGENT_ADD);
			}else if("training".equals(type)) {
				cmqMsg = new CmqMsg(CmqMessageComm.S2S_AGENT_UPDATE);
			}
			if(null!=cmqMsg) {
				cmqMsg.addProperty("playerId", player.getId());
				cmqMsg.addProperty("agent", player.playerAgent().getAgent(agentId));
				sendDefaultMessage(player, cmqMsg);
			}
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}

	//sendWarCraftLvup sendWarCraftSkillLvup
	//玩家兵法
	private void sendWarCraftLvup(BasePlayer player) {
		try {
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_WarCraftLvup);
			cmqMsg.addProperty("playerId", player.getId());
			cmqMsg.addProperty("warCraftlv", player.playerWarcraft().getLv());
			sendDefaultMessage(player, cmqMsg);
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}
	//玩家兵法
	private void sendWarCraftSkillLvup(BasePlayer player) {
		try {
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_WarCraftSkillLvup);
			cmqMsg.addProperty("playerId", player.getId());
			cmqMsg.addProperty("skillmap", player.playerWarcraft().getSkillMap());
			sendDefaultMessage(player, cmqMsg);
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}

	//研修
	private void sendMasteryLvUp(BasePlayer player, int id) {
		try {
			if(id<=0) {
				return;
			}
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_MasteryLvUp);
			cmqMsg.addProperty("playerId", player.getId());
			cmqMsg.addProperty("index", id);
			cmqMsg.addProperty("data", player.playerMastery().getMastery(id).getLvs());
			sendDefaultMessage(player, cmqMsg);
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}

	private void bindPhone(Player player) {
		try {
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_BindPhone);
			cmqMsg.addProperty("playerId", player.getId());
			cmqMsg.addProperty("lv", player.playerLevel().getLv());
			cmqMsg.addProperty("viplv", player.getPlayerVipInfo().getVipLv());
			cmqMsg.addProperty("phone", player.playerBaseInfo().getBindPhone());
			cmqMsg.addProperty("createtime", new Date().getTime());

			//chappid chchannelid chapplyid chuserid
			cmqMsg.addProperty("chappid", player.playerFix().getChAppid());
			cmqMsg.addProperty("chchannelid", player.playerFix().getChChannelId());
			cmqMsg.addProperty("chapplyid", player.playerFix().getChannelApplyId());
			cmqMsg.addProperty("chuserid", player.playerFix().getAccount());

			sendDefaultMessage(player, cmqMsg);
		} catch (Exception e) {
			log.error("发送统计服异常：", e);
		}
	}

	public void sendMessage(String queueName, List<CmqMsg> msgs) {
		cmqProxy.sendMessage(queueName, msgs);
	}
}





