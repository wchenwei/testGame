package com.hm.action.login;

import cn.hutool.core.util.StrUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.commander.biz.AircraftCarrierBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildFactoryBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.guild.command.GuildCommandBiz;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.login.vo.LoadPlayerVO;
import com.hm.action.loginmsg.LoginMsgBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.recharge.RechargeBiz;
import com.hm.action.sys.SysFacade;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.cache.PlayerCacheManager;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.GmPlayerUtils;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.handler.SessionUtil;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.action.BaseAction;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.ISession;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.log.PlayerLogBiz;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.servercontainer.idcode.IdCodeItemContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Action
public class LoginAction extends BaseAction {
	@Resource
	private SysFacade sysFacade;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private LoginBiz loginBiz;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private PlayerLogBiz playerLogBiz;
	@Resource
	private ActivityBiz activityBiz;
	@Resource
	private RechargeBiz rechargeBiz;
	@Resource
	private TankBiz tankBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private GuildTechBiz guildTechBiz;
	@Resource
	private GuildCommandBiz guildCommandBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private GuildFactoryBiz guildFactoryBiz;
	@Resource
	private AircraftCarrierBiz aircraftCarrierBiz;
	@Resource
	private LoginMsgBiz loginMsgBiz;
	@Resource
	private TroopBiz troopBiz;

	@Override
	public void registerMsg() {
		this.registerMsg(MessageComm.C2S_InitLogin);
		this.registerMsg(MessageComm.C2S_CreatePlayer);
		this.registerMsg(MessageComm.C2S_LoadPlayer);
		this.registerMsg(MessageComm.S2C_LeavePlayer);
		this.registerMsg(MessageComm.C2S_Player_Heart);
	}

	@Override
	public void doProcess(JsonMsg msg, HMSession session) {
		switch (msg.getMsgId()) {
		case MessageComm.C2S_InitLogin:
			initLogin(session, msg);
			break;
		case MessageComm.C2S_CreatePlayer:
			createPlayer(session, msg);
			break;
		case MessageComm.C2S_LoadPlayer:
			loadPlayer(session, msg);
			break;
		case MessageComm.S2C_LeavePlayer:
			leavePlayer(session, msg);
			break;
		case MessageComm.C2S_Player_Heart:
			heart(session, msg);
			break;
		}
	}
	
	public void initLogin(HMSession session, JsonMsg msg) {
		try {
			int serverId = msg.getInt("serverId");
			String bindIdCode = msg.getString("bindIdCode");
			//如果serverid小于0，则会返回错误，这个是必须的。
			if (serverId <= 0) {
				session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SERVER_CHOISE);
				return;
			}
			int dbServerId = GameServerManager.getInstance().getDbServerId(serverId);
			LoginKeyMode loginKey = LoginKeyMode.buildLoginKeyMode(msg);
			long uid = loginKey.getUid();
			long playerId = 0;
			String imei = msg.getString("imei");
			if (imei == null) {
				imei = playerId + "";
			}
			Player simplePlayer = PlayerUtils.getPlayerIdAndIdCode(uid, serverId,dbServerId);
			playerId = simplePlayer==null?0:simplePlayer.getId();
			ServerData serverData = ServerDataManager.getIntance().getServerData(dbServerId);
			if(serverData == null) {
				log.error("服务器错误:"+dbServerId);
				session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SERVER_CLOSE);
				return;
			}
			JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_InitLogin);
			serverMsg.addProperty("time", System.currentTimeMillis());
			if (playerId <= 0) {
				// 玩家还没有注册过,重新注册
				//根据规则，判断是否需要对阵营选择，给予一定的奖励。
				serverMsg.addProperty("playerId", 0);
				serverMsg.addProperty("state", 0);
				int codeState = loginBiz.isNormalSocket(session)?getIdCodeState(dbServerId,serverId,0,"",bindIdCode,session.getRemoteIp(),playerId):IdCodeFilterState.Normal.getType();
				serverMsg.addProperty("idCodeState", ServerConfig.getInstance().isIdCodeSwitch()?codeState:IdCodeFilterState.Normal.getType());
				session.sendMsg(serverMsg);
				return;
			}
			// 内存查找
			Player player = PlayerContainer.getPlayer(playerId);
			serverMsg.addProperty("playerId", playerId);
			serverMsg.addProperty("state", 1);
			// 如果在内存,就断线重连
			if (player != null) {
				if (player.isOnline()) {// 断开现有连接
					ISession hmSession = player.getSession();
					Object sessionkeyobj = hmSession.getAttribute("sessionKey");
					String sessionkey = new String();
					if (sessionkeyobj != null) {
						sessionkey = String.valueOf(sessionkeyobj);
					} else {
						player.notifyObservers(ObservableEnum.PlayerReLogin, false);
						session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SESSIONKEY_ERROR);
						return;
					}
					if (!sessionkey.equals(loginKey.getLoginKey())) {// sessionkey不一致，不是游戏内重连，属于异地登陆
						sysFacade.sendLeavePlayer(player, LeaveOnlineType.RELOGIN);
					}
				}
				boolean isReconnect = msg.getBoolean("isReconnect");
				if (isReconnect) {
					serverMsg.addProperty("state", 2);
					session.setAttribute("playerId", playerId);
					player.setSession(session);
					player.notifyObservers(ObservableEnum.PlayerReLogin, true);
				}
			}
			int idCodeState = getIdCodeState(simplePlayer.getServerId(),simplePlayer.getCreateServerId(),simplePlayer.getPlayerVipInfo().getVipLv(),simplePlayer.getIdCode(),bindIdCode,session.getRemoteIp(),playerId);
			serverMsg.addProperty("idCodeState", ServerConfig.getInstance().isIdCodeSwitch()?idCodeState:IdCodeFilterState.Normal.getType());
			session.setAttribute(SessionKeyType.SessionKey, loginKey);
			session.sendMsg(serverMsg);
		} catch (Exception e) {
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SYS_ERROR);
			log.error("登录初始化失败", e);
		}
	}
	
	/**
	 * heart:心跳
	 * @param
	 * @param msg 使用说明
	 */
	public void heart(HMSession session, JsonMsg msg) {
		Player player = SessionUtil.getPlayer(session);
		if(player != null) {
			player.playerTemp().setLastMsgTime(System.currentTimeMillis());
		}
		session.sendMsg(MessageComm.S2C_Heart,System.currentTimeMillis());
	}

	/**
	 * 创建角色
	 * 
	 * @param session
	 * @param
	 */
	@MsgMethod(MessageComm.C2S_CreatePlayer)
	public void createPlayer(HMSession session, JsonMsg msg) {
		try {
			LoginKeyMode loginKey = LoginKeyMode.buildLoginKeyMode(msg);
			long uid = loginKey.getUid();
			int channelId = loginKey.getChannelId();
			int createServer = msg.getInt("serverId");
			if (createServer <= 0) {
				session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SERVER_CHOISE);
				return;
			}
			//真实服务器id
			int serverId = GameServerManager.getInstance().getDbServerId(createServer);
			if(PlayerUtils.playerIsExist(uid, createServer,serverId)) {
				session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SYS_ERROR);
				return;
			}
			//查询该服务器本账号拥有的角色个数
			List<Long> ids = PlayerUtils.getPlayerByUid(uid, serverId);
			int roleLimit = commValueConfig.getCommValue(CommonValueType.IdCodeBindLimit);
			if (ServerConfig.getInstance().isRoleNumLimit() && ids.size() >= roleLimit) {
				log.error(serverId+"创建角色出错超过允许上限");
				session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.Role_Limit);
				return;
			}


            String imei = msg.getString("imei");
			Player player = playerBiz.createPlayer(uid, createServer, serverId,channelId, imei, GameConstants.DEFAULT_ICON_ID);
			if(player == null) {
				log.error(serverId+"创建角色出错超过最大人数");
				session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.Server_Is_Full);
				return;
			}
			player.setSession(session);
			PlayerContainer.addPlayer2Map(player);
			PlayerCacheManager.getInstance().addPlayerToCache(player);
			session.setAttribute(SessionKeyType.SessionKey, loginKey);
            session.setAttribute("isNewPlayer", true);
			// 注册成功返回
			JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_CreatePlayer);
			serverMsg.addProperty("playerId", player.getId());
			serverMsg.addProperty("state", 1);
			session.sendMsg(serverMsg);
			playerLogBiz.logRegister(player);//记录注册日志
		} catch (Exception e) {
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SYS_ERROR);
			log.error("创建角色出错", e);
		}
	}
	
	public void loadPlayer(HMSession session, JsonMsg msg) {
		try {
			long playerId = msg.getInt("playerId");
			//imei
			String imei = msg.getString("imei");
			String ip = session.getRemoteIp();
			String idCode = msg.getString("idCode");
			String bindIdCode = msg.getString("bindIdCode");
			LoadPlayerVO loadPlayerVO = new LoadPlayerVO();
			Player player = PlayerUtils.getPlayer(playerId);
			if (player == null) {
				loadPlayerVO.setState(2);// 玩家不存在
				session.sendMsg(MessageComm.S2C_LoadPlayer, loadPlayerVO);
				return;
			}
			LoginKeyMode loginKey = (LoginKeyMode)session.getAttribute(SessionKeyType.SessionKey);
			if (player.getUid() != loginKey.getUid()) {
				log.error("============非法加载角色，角色id" + playerId + "账号uid" + loginKey.getUid());
				session.close();
				return;
			}
			// 检查是否是黑名单
			if (player.playerStatus().isStatus(PlayerStatusType.NotLogin)) {
				session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.BANNEDPLAYER);
				return;
			}
			
			session.setAttachment(playerId);
			session.setAttribute("heart", System.currentTimeMillis());
			session.setAttribute("playerId", playerId);
			player.setSession(session);
			player.playerFix().setLoginChannelId(loginKey.getChannelId());
			player.playerBaseInfo().setImei(imei);
			player.playerTemp().setLastMsgTime(System.currentTimeMillis());
			player.playerTemp().initClientParm(msg);
            //如果玩家的serverId和真实serverId不一致则修改为真实serverId
            int dbId = ServerUtils.getServerId(player.getId());
            if (player.getServerId() != dbId) {
                player.setServerId(dbId);
            }
			//如果是H5不进行idCode校验
			if(loginBiz.isNormalSocket(session)){
				
				if(ServerConfig.getInstance().isIdCodeSwitch()&&(StrUtil.isBlank(idCode)||StrUtil.isBlank(bindIdCode))){
					sysFacade.sendLeavePlayer(session, LeaveOnlineType.SERVER);
					loginBiz.doLoginOut(player);
					return;
				}
				//检查唯一码
				boolean flag = playerBiz.checkPlayerIdCode(player, idCode, bindIdCode,ip);
				if(!flag&&ServerConfig.getInstance().isIdCodeSwitch()){//校验未通过则踢出玩家
					sysFacade.sendLeavePlayer(session, LeaveOnlineType.SERVER);
					loginBiz.doLoginOut(player);
					return;
				}
			}
			//检查玩家fix信息是否有修改
			player.playerFix().initMsg(session,msg);
			// 用户过期时间、合服清理用户用
			playerBiz.checkExpireTime(player);
			// 登录检查
			boolean isDayFirstLogin = loginBiz.doPlayerLogin(player);
			// 加载返回
			JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_LoadPlayer);
			serverMsg.addProperty("dayFirstLogin",isDayFirstLogin);
			guildBiz.fillGuildData(player, serverMsg);
			fillServerData(player, serverMsg);//加载服务器数据
			player.fillMsg(serverMsg, true);
			player.sendMsg(serverMsg);
			//加入在线列表
			PlayerContainer.addPlayer2Map(player);
			//加入ip在线列表
			IdCodeContainer.of(player).addPlayerByIp(ip, playerId);
			//加载玩家坦克
			tankBiz.loadTankList(player);
			troopBiz.checkTroopIndex(player);
			player.sendWorldTroopMessage();//发送玩家部队信息
			//加载玩家邮件
			mailBiz.loadMailList(player);
			//发送活动信息列
			activityBiz.sendActivityList(player);
			//重发充值弹窗，清除为弹窗的充值记录
			rechargeBiz.reSendRechargeWarn(player);
			//重新发送部落信息
			guildBiz.sendPlayerGuildVo(player);
			//发送部落战术信息
			guildTechBiz.sendPlayerGuildTactics(player);
			guildCommandBiz.sendGuildCommandUpdate(player);
			//更新部落最后登录时间
			guildBiz.updateLoginTime(player);
            //发送玩家武器信息
            guildFactoryBiz.sendArmsPosChange(player);
            //发送玩家飞机阵型信息
            aircraftCarrierBiz.sendPlayerAirFormation(player);
			loginMsgBiz.checkLoginMsg(player);

            //更新排行榜
            HdLeaderboardsService.getInstance().updatePlayerInfo(player);
            RedisUtil.updateRedisPlayer(player);
            playerLogBiz.logLogin(player);//记录登录日志

            if (session.containsAttrKey("isNewPlayer")) {
                player.notifyObservers(ObservableEnum.PlayerCreate);
            }
            //登录成功事件
            player.notifyObservers(ObservableEnum.PlayerLoginSuc);
        } catch (Exception e) {
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg_Login, SysConstant.SYS_ERROR);
			log.error("加载角色出错", e);
		}
	}
	public void leavePlayer(HMSession session, JsonMsg msg) {
		Player player = SessionUtil.getPlayer(session);
		loginBiz.doLoginOut(player);
	}

	private void fillServerData(Player player, JsonMsg serverMsg) {
		//聊天地址
		int serverId = player.getServerId();
		serverMsg.addProperty("ServerNowTime", System.currentTimeMillis());
		serverMsg.addProperty("Server2020Time", GameConstants.Server2020Time);
		player.playerTemp().setGm(GmPlayerUtils.isGm(player.getId()));
        serverMsg.addProperty("isGm", player.playerTemp().isGm());
		
		ServerDataManager.getIntance().getServerData(serverId).fillMsg(serverMsg);
	}
	/**
	 * 
	 * @param
	 * @param ip
	 * @return -1表示该玩家放行，-2该ip下用户登录数已经超过限制不能登录，-3表示角色已经绑定，>=0代表可以绑定该idCode的剩余数量
	 */
	private int getIdCodeState(int serverId,int createServerid,int vipLevel,String myIdCode,String idCode,String ip,long playerId){
		//ip过滤和唯一码过滤都关闭则放行
		if(!ServerConfig.getInstance().isIdCodeSwitch()&&!ServerConfig.getInstance().isIpFilterSwitch()){
			return IdCodeFilterState.Normal.getType();
		}
		int vipLimit = commValueConfig.getCommValue(CommonValueType.VipFilterLimit);
		int ipLoginLimit = commValueConfig.getCommValue(CommonValueType.IpLoginLimit);
		int idCodeBindLimit = commValueConfig.getCommValue(CommonValueType.IdCodeBindLimit);
		//vip等级大于=2放过
		if(vipLevel>=vipLimit){
			return IdCodeFilterState.Normal.getType();
		}
		IdCodeItemContainer idCodeItemContainer = IdCodeContainer.of(serverId);
		if(ServerConfig.getInstance().isIpFilterSwitch()){//开放ip过滤则检查该ip下人数是否达到上限
			int onlineNum = idCodeItemContainer.getIpNum(ip);
			if(onlineNum>=ipLoginLimit){
				return IdCodeFilterState.IpLimit.getType();
			}
		}
		if(!ServerConfig.getInstance().isIdCodeSwitch()){
			return IdCodeFilterState.Normal.getType();
		}
		if(!ServerConfig.getInstance().isIdCodeSwitch()){
			//未开启登录验证，如果自身绑定了唯一码则返回-3
			if(StrUtil.isNotBlank(myIdCode)){
				return IdCodeFilterState.Binded.getType();
			}
		}else{
			//已开启登录验证则判断 
			//playerId<=0则state必定为0
			int state = idCodeItemContainer.getIdCodeInfoState(myIdCode, createServerid,playerId);
			if(StrUtil.isNotBlank(myIdCode)&&state==1){
				return IdCodeFilterState.Binded.getType();
			}
		}
		return Math.max(0, idCodeBindLimit-idCodeItemContainer.getIdCodeInfoNum(idCode,createServerid));
	}
}

