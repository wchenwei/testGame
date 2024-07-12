package com.hm.action;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.activity.ActivityServerConfBiz;
import com.hm.action.captive.CaptiveBiz;
import com.hm.action.captive.log.CaptiveMeLog;
import com.hm.action.captive.log.CaptiveOtherLog;
import com.hm.action.cityworld.WorldAction;
import com.hm.action.cityworld.biz.ResetWorldBiz;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.cityworld.biz.WorldCityBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.guild.biz.GuildWorldBiz;
import com.hm.action.guild.task.GuildTaskBiz;
import com.hm.action.guildwar.WarConstants;
import com.hm.action.http.KFAction;
import com.hm.action.http.biz.BroadServerBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.kf.KfBuildExtortUtils;
import com.hm.action.kf.KfObserverBiz;
import com.hm.action.kf.KfPlayerAction;
import com.hm.action.kf.kfexpedition.KfExpeditionScoreUtils;
import com.hm.action.kfgame.DKFServerBiz;
import com.hm.action.kfseason.KFSeasonBiz;
import com.hm.action.language.MailCustomContent;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.personalchat.FriendAction;
import com.hm.action.recharge.RechargeBiz;
import com.hm.action.smallgame.biz.SmallGameBiz;
import com.hm.action.sys.SysFacade;
import com.hm.action.task.biz.MainTaskBiz;
import com.hm.action.titile.TitleBiz;
import com.hm.action.troop.biz.TroopFightBiz;
import com.hm.action.warMake.WarMakesBiz;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.action.wx.WXBiz;
import com.hm.action.wx.WXSubsType;
import com.hm.chat.InnerChatFacade;
import com.hm.config.EquipmentConfig;
import com.hm.config.GameConstants;
import com.hm.config.TaskConfig;
import com.hm.config.TitleConfig;
import com.hm.config.excel.AdConfig;
import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.FishConfig;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.config.excel.temlate.TaskMainTemplateImpl;
import com.hm.config.excel.templaextra.AdTemplate;
import com.hm.config.excel.templaextra.PlayerArmExtraTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.message.MessageComm;
import com.hm.model.activity.PlayerActivityValue;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.agent.Agent;
import com.hm.model.battle.BaseBattle;
import com.hm.model.battle.TowerBattle;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.GuildTask;
import com.hm.model.item.Items;
import com.hm.model.player.*;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerWorldBuildData;
import com.hm.model.serverpublic.ServerWorldLvData;
import com.hm.model.task.MainTask;
import com.hm.model.task.daily.DailyTask;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.observer.TaskStatus;
import com.hm.redis.kftask.BaseKFTask;
import com.hm.redis.kftask.KFPlayerTaskCache;
import com.hm.redis.kftask.KFTaskEventType;
import com.hm.redis.kftask.KFTaskType;
import com.hm.redis.trade.PlayerStockRes;
import com.hm.redis.util.RedisUtil;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldItemContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.util.ItemUtils;
import com.hm.util.PubFunc;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
@Action
public class TestAction extends AbstractPlayerAction{
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private TroopFightBiz troopFightBiz;
	@Resource
	private RechargeBiz rechargeBiz;
	@Resource
	private WorldCityBiz worldCityBiz;
	@Resource
	private ActivityBiz activityBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private CaptiveBiz captiveBiz;
	@Resource
	private KFSeasonBiz seasonBiz;
	@Resource
	private SmallGameBiz smallGameBiz;
	@Resource
	private TitleConfig titleConfig;
    @Resource
    private ItemConfig itemConfig;
	@Resource
	private FishConfig fishConfig;
    @Resource
    private LoginBiz loginBiz;
    @Resource
    private SysFacade sysFacade;
    @Resource
    private CommanderConfig commanderConfig;

    /**
	 * gm指令给玩家增加物品
	 * @param player
	 * @param msg
	 */
	@MsgMethod ( MessageComm.C2S_Test_AddItems )
	public void addItem(Player player, JsonMsg msg){
		if(!ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
			return;
		}
		String itemStr = msg.getString("itemStr");
		//TODO gm测试
		itemBiz.checkItemTest(player, itemStr);

		//测试跨分支合并
	}
	
	@MsgMethod ( MessageComm.S2C_Test_Spend )
	public void spendItem(Player player, JsonMsg msg){
		if(!ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
			return;
		}
		String itemStr = msg.getString("itemStr");
		itemBiz.checkItemEnoughAndSpend(player, ItemUtils.str2DefaultItemList(itemStr), LogType.ReduceByGM);
		player.sendUserUpdateMsg();
	}


    public void UnlockAll(Player player){
        if(!ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
            return;
        }
        if(player.getServerId() != 999) {
            return;
        }
        player.playerLevel().setLv(70);
        player.playerLevel().setLvExp(0);

        player.playerGuide().changeGuide("1073741823", -1);
        player.playerGuide().skipGuide();

        player.playerCurrency().add(CurrencyKind.Cash,10000000);
        player.playerCurrency().add(CurrencyKind.Gold,10000000);
        player.playerCurrency().add(CurrencyKind.Oil,10000000);

        int missionId = 7501;
        player.playerMission().setMissionId(missionId);
        int openCity = player.playerMission().getRelOpenCity();
        for(int i=1;i<=openCity;i++){
            player.playerMission().checkArmory(i);
        }
        //触发一次解放城市信号来检查功能解锁
        player.notifyObservers(ObservableEnum.RecaptureCity, player.playerMission().getOpenCity());
        player.notifyObservers(ObservableEnum.PlayerLevelUp);
        player.sendMsg(MessageComm.S2C_SkipGuide);

        player.sendUserUpdateMsg();

        WorldAction worldAction = SpringUtil.getBean(WorldAction.class);
        worldAction.intoWorld(player,null);
    }
	
	@MsgMethod ( MessageComm.C2S_Test_Cmd )
	public void clonePlayer(Player player, JsonMsg msg){
		if(!ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
			return;
		}
		String cmd = msg.getString("key");
		log.error(player.getId()+"_"+player.getGuildId()+" gm:"+cmd);
		String action = cmd.split("#")[0];
		if("cloneplayer".equals(action)) {
			String value = cmd.split("#")[1];
			ClonePlayerUtils.clonePlayer(Long.parseLong(value), player.getId());
			return;
		} else if("wwclone".equals(action)) {
			String playerId = cmd.split("#")[1];
			Player clonePlayer = ClonePlayerUtils.toKfPlayer(playerId);
			if(clonePlayer != null) {
				ClonePlayerUtils.clonePlayer(clonePlayer, player);
			}else{
				player.sendErrorMsg(11111);
			}
			return;
		}else if("clonetroop".equals(action)) {
			String value = cmd.split("#")[1];
			int index = Integer.parseInt(value);
			String troopId = player.playerTroops().getTroopIdList().get(index);
			troopFightBiz.clonePlayerTroop(player, troopId);
			return;
		}else if("recharge".equals(action)) {
			String value = cmd.split("#")[1];
			int index = Integer.parseInt(value);
			rechargeBiz.rewardPlayer(player, index);
			return;
		}else if("clearworld".equals(action)) {
			worldCityBiz.clearWorldCity(player.getServerId());
		}else if("addbuff".equals(action)) {
			BuffType buffType = BuffType.getType(Integer.parseInt(cmd.split("#")[1]));
			player.playerBuffer().resetBuff(buffType, 3600, 0.1);
			player.sendUserUpdateMsg();
		}else if("test".equals(action)) {
			SpringUtil.getBean(KfPlayerAction.class).expeditionDeclare(player, msg);
		}
        else if("yz".equals(action)) {
            KfExpeditionActivity activity = (KfExpeditionActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KfExpeditionActivity);
            activity.setOpenType(2);
            KfExpeditionScoreUtils.checkFirstScore(player.getServerId());
            SpringUtil.getBean(KfObserverBiz.class).calServerKfCombat(player.getServerId());
            activity.saveDB();
            activityBiz.broadPlayerActivityUpdate(activity);
        }else if("title".equals(action)) {
			SpringUtil.getBean(TitleBiz.class).addTitle(player, PlayerTitleType.PRESIDENT);
		}else if("guildtech".equals(action)) {
			SpringUtil.getBean(GuildTechBiz.class).guildAddExp(player, 100000);
		}else if("server".equals(action)) {
			ServerWorldLvData serverWorldLvData = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerWorldLvData();
			int index = Integer.parseInt(cmd.split("#")[1]);
			if(index > 0) {
				serverWorldLvData.setWorldLv(serverWorldLvData.getWorldLv()+1);
				serverWorldLvData.save();
			}
			SpringUtil.getBean(ResetWorldBiz.class).broadWorldLvChange(serverWorldLvData);
		}else if("guildscore".equals(action)) {
			GuildBiz guildBiz = SpringUtil.getBean(GuildBiz.class);
			Guild guild = guildBiz.getGuild(player);
			GuildTask guildTask = guild.getGuildTask();
			guildTask.addScore(player.getId(), 100000);
			HdLeaderboardsService.getInstance().updatePlayerRankForAdd(player, RankType.PlayerGuildScore, 10000);
			guild.saveDB();
			SpringUtil.getBean(GuildTaskBiz.class).broadGuildScoreChange(guild);
		}else if("guildlv".equals(action)) {
			GuildBiz guildBiz = SpringUtil.getBean(GuildBiz.class);
			Guild guild = guildBiz.getGuild(player);
			guild.guildLevelInfo().setLv(guild.guildLevelInfo().getLv()+1);
			guild.saveDB();
		}else if("worldbuild".equals(action)) {
			ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
					.getServerWorldBuildData();
			serverWorldBuildData.resetData();
			SpringUtil.getBean(WorldBuildBiz.class).broadWorldBuildChange(serverWorldBuildData);
			player.playerWorldBuildData().dayReset();
			player.sendUserUpdateMsg();
		}else if("worldbuildexp".equals(action)) {
			ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
					.getServerWorldBuildData();
			SpringUtil.getBean(WorldBuildBiz.class).doServerWorldBuildAddExp(serverWorldBuildData, 999999);
		}else if("clearexpreward".equals(action)){
            player.playerLevel().setSendReward(false);
            player.sendUserUpdateMsg();
        }else if("cleartech".equals(action)){
            Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
            if(guild != null) {
                guild.guildTechnology().doDayReset();
                guild.saveDB();
            }
        }else if("buildred".equals(action)){
            KfBuildExtortUtils.playerAdd(player);
            KfBuildExtortUtils.sendPlayerChange(player);
        }else if("syncact".equals(action)){
            ActivityServerConfBiz activityServerConfBiz = SpringUtil.getBean(ActivityServerConfBiz.class);
            activityServerConfBiz.syncActivity(player.getServerId());
        }else if("resetBattle".equals(action)){
            int type = Integer.parseInt(cmd.split("#")[1]);
            BaseBattle battle = player.playerBattle().getPlayerBattle(type);
            battle.resetDay(player);
            player.playerBattle().SetChanged();
            player.sendUserUpdateMsg();
        }else if("broad".equals(action)){
            BroadServerBiz broadServerBiz = SpringUtil.getBean(BroadServerBiz.class);
            Map<String,String> parms = Maps.newHashMap();
            parms.put("test", "1阿萨德");
            broadServerBiz.broadAllServerMsg(AllServerBroadType.None, parms);
        }else if("sdk".equals(action)){
            player.notifyObservers(ObservableEnum.RechargeSdk, Maps.newHashMap());
        }else if("city".equals(action)){
            List<Integer> cityIds = StringUtil.splitStr2IntegerList(cmd.split("#")[1], ",");
            changeWorldCity(player, cityIds);
        }else if("testtroop".equals(action)){
            String troopId = cmd.split("#")[1];
            WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
            WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
            worldCity.getDefCityTroop().removeTroop(troopId);
            worldCity.getAtkCityTroop().addTroop(worldTroop);
        }else if("redmsg".equals(action)){
            InnerChatFacade innerChatFacade = SpringUtil.getBean(InnerChatFacade.class);
            innerChatFacade.saveAndSendRedPacket(player);
        }else if("addProsperity".equals(action)){
            int prosperity = Integer.parseInt(cmd.split("#")[1]);
            Guild guild = guildBiz.getGuild(player);
            if(guild!=null){
                guild.guildFactory().addProsperity(prosperity);
                guild.guildFactory().checkProsperity();
                guild.saveDB();
                guild.broadMemberGuildUpdate();
            }
        }else if("addGuildFactoryLv".equals(action)){
            int exp = Integer.parseInt(cmd.split("#")[1]);
            Guild guild = guildBiz.getGuild(player);
            if(guild!=null){
                guild.guildFactory().addExp(exp);
                guild.saveDB();
                guild.broadMemberGuildUpdate();
            }
        }else if("changeking".equals(action)) {
            long playerId = Long.parseLong(cmd.split("#")[1]);
            KFAction kfAction = SpringUtil.getBean("kf.do");
            kfAction.doServerKingTitle(player.getServerId(), playerId);
        }else if("beGuildLeader".equals(action)){
            Guild guild = guildBiz.getGuild(player);
            if(guild==null){
                return;
            }
            Player guildLeader = PlayerUtils.getPlayer(guild.getGuildInfo().getLeaderId());
            boolean result = guildBiz.transfer(guildLeader, guild, player);
            guildBiz.sendGuildMember(player);
        }else if("captive".equals(action)){
            int index = PubFunc.parseInt(cmd.split("#")[1])-1;
            List<WorldTroop> worldTroops = TroopServerContainer.of(player).getWorldTroopByPlayer(player);
            int tankId = worldTroops.get(index).getTroopArmy().getTankList().get(0).getId();
            player.playerCaptive().setLv(5);
            if(player.playerCaptive().getBeCaptiveTank(tankId) != null) {
                player.playerCaptive().removeBeCaptiveTank(tankId);
            }else{
                player.playerCaptive().addBeCaptiveTank(new BeCaptiveTankInfo(player, tankId, System.currentTimeMillis()+2*GameConstants.HOUR));
            }
            player.sendUserUpdateMsg();
        }else if ("captank".equals(action)){// 战俘营俘虏坦克  bPlayerId：被俘虏的玩家  index:被俘虏的部队
            String value = cmd.split("#")[1];
            int bPlayerId = PubFunc.parseInt(value.split(",")[0]);
            int index = PubFunc.parseInt(value.split(",")[1]);
            doCaptiveTank(player,bPlayerId,index);
        }else if("clearday".equals(action)){
            LoginBiz loginBiz = SpringUtil.getBean(LoginBiz.class);
            player.playerBaseInfo().setServerDayMark("20200908");
            loginBiz.resetPlayerByZeroLock(player);
            player.sendUserUpdateMsg();
        } else if ("clearLevelEvent".equals(action)) {
            long playerId = Long.parseLong(cmd.split("#")[1]);
            Player targetPlayer = PlayerUtils.getPlayer(playerId);
            targetPlayer.playerEvent().clearAll();
            targetPlayer.sendUserUpdateMsg();
        } else if ("initCell".equals(action)) {
            ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
            serverData.getServerEliminate().initCells();
            serverData.save();
        } else if ("clearWarMakes".equals(action)) {
            PlayerWarMakes playerWarMakes = player.playerWarMakes();
            playerWarMakes.setSeasonId(0);
            WarMakesBiz warMakesBiz = SpringUtil.getBean(WarMakesBiz.class);
            warMakesBiz.checkWarMakes(player);
            player.playerWarMakes().SetChanged();
            player.sendUserUpdateMsg();
        } else if ("addwarmake".equals(action)) {
            int count = Integer.parseInt(cmd.split("#")[1]);
            player.playerWarMakes().addExperience(count);
            player.sendUserUpdateMsg();
        } else if ("addkftask".equals(action)) {
            String value = cmd.split("#")[1];
            int type = Integer.parseInt(value.split(",")[0]);
            int val = Integer.parseInt(value.split(",")[1]);

            BaseKFTask kfTask = KFPlayerTaskCache.getKFPlayerTaskFromCache(KFTaskType.getKFTaskType(type), player.getId());
            kfTask.addEvent(KFTaskEventType.getKFTaskEventType(val), 10000);
            kfTask.saveRedis();
        } else if ("doHour".equals(action)) {
            int hour = Integer.parseInt(cmd.split("#")[1]);
            ObserverRouter.getInstance().notifyObservers(ObservableEnum.HourEvent, null, hour);
        }else if("reBindPhone".equals(action)) {
            //#cmd:reBindPhone
            //RedisTypeEnum.BindPhone.rename();
            //
            RedisUtil.updateRedisBindPhone(player, "18236968603");
            player.playerBaseInfo().checkAndBindPhone();
            player.sendUserUpdateMsg();
            player.notifyObservers(ObservableEnum.BindPhone, "18236968603");

            activityBiz.checkActivity();
        }else if("openSeasonShop".equals(action)) {
            seasonBiz.openSeasonShop(1, 90, 100);
        }else if("openSeason".equals(action)) {
            player.notifyObservers(ObservableEnum.KFActOpen, player.getServerId());
        }else if("SmallGameTest".equals(action)) {
            player.getPlayerCDs().addCdNum(CDType.GameEnergy, 200);
            player.sendUserUpdateMsg();
        }else if("SmallGameExp".equals(action)) {
            //增加及经验信息
            int exp = Integer.parseInt(cmd.split("#")[1]);
            int gametype = Integer.parseInt(cmd.split("#").length>=3?cmd.split("#")[2]:"1");
            BaseSamllGame game = player.playerGame().getGame(GameType.getGameType(gametype));
            smallGameBiz.addExp(player, game, exp, 200);
            player.sendUserUpdateMsg();
        } else if("clearLeaveGuildTime".equals(action)){
            player.playerGuild().setLevelTime(null);
            player.sendUserUpdateMsg();
        }else if("unlockall".equals(action)){
            UnlockAll(player);
        }else if("itemrange".equals(action)){
            String item = cmd.split("#")[1];
            int sid = Integer.parseInt(item.split(",")[0]);
            int eid = Integer.parseInt(item.split(",")[1]);
            int count = Integer.parseInt(cmd.split("#")[2]);
            List<Items> itemList = Lists.newArrayList();
            for (int i = sid; i <= eid; i++) {
                ItemTemplate itemTemplate = itemConfig.getItemTemplateById(i);
                if(itemTemplate != null) {
                    itemList.add(new Items(i,count,ItemType.ITEM));
                }
            }
            itemBiz.addItem(player,itemList,LogType.ReduceByGM);
            player.sendUserUpdateMsg();
        }else if("wx".equals(action)){
            String s = cmd.split("#")[1];
            WXSubsType subsType = WXSubsType.getWXSubsType(Integer.parseInt(s));
            if(subsType == null) {
                System.out.println("找不到:"+s);
                return;
            }
            SpringUtil.getBean(WXBiz.class).sendPlayerWXEvent(player,subsType);
        }else if("clearAct".equals(action)){
            int type = Integer.parseInt(cmd.split("#")[1]);
            ActivityType activityType = ActivityType.getActivityType(type);
            if(!activityBiz.checkActivityIsOpen(player, activityType)){
                return;
            }
            PlayerActivityValue value = player.getPlayerActivity().getPlayerActivityValue(activityType);
            value.clearRepeatValue();
            player.getPlayerActivity().SetChanged();
            player.sendUserUpdateMsg();
        }else if("frame".equals(action)){
            int lv = Integer.parseInt(cmd.split("#")[1]);
            WarConstants.FrameTime = Math.max(50,lv);
        }else if("gift".equals(action)){
            int lv = Integer.parseInt(cmd.split("#")[1]);
            FriendAction.sendFriendGift(player,lv);
        }else if("clearfgift".equals(action)){
			player.playerFriend().doDayReset();
			player.sendUserUpdateMsg();
		}else if("guildImpeachEnd".equals(action)){
			Guild guild = guildBiz.getGuild(player);
			if(!guild.getGuildImpeach().isEnd()){
				guild.getGuildImpeach().end();
				guild.saveDB();
				guild.broadMemberGuildUpdate();
			}
		}else if("editLastLogintime".equals(action)){
			long playerId = Long.parseLong(cmd.split("#")[1]);
			Player playerData = PlayerUtils.getPlayer(playerId);
			playerData.playerBaseInfo().setLastLoginDate(new Date(new Date().getTime()-100*GameConstants.HOUR));
			playerData.playerBaseInfo().setLastOffLineDate(new Date(new Date().getTime()-100*GameConstants.HOUR));
			playerData.saveNowDB();
			RedisUtil.updateRedisPlayer(playerData);
			guildBiz.sendGuildMember(player);
			log.error("执行editLastLogintime结束！");
		}else if("clearads".equals(action)){
			player.playerAds().doDayReset();
			player.sendUserUpdateMsg();
		}else if("dayreset".equals(action)){
			player.playerBaseInfo().setServerDayMark("20210930");
			player.sendUserUpdateMsg();
		}else if("gd".equals(action)){
			PlayerStockRes playerStockRes = PlayerStockRes.getPlayerStockRes(player.getId());
			Map<Long,Long> timeMap = playerStockRes.getTimeMap();
			for (long id : Lists.newArrayList(timeMap.keySet())) {
				timeMap.put(id,System.currentTimeMillis()-2*GameConstants.DAY);
			}
			playerStockRes.saveDB();
		}else if ("agentDispatch".equals(action)) {
			int id = Integer.parseInt(cmd.split("#")[1]);
			int n = Integer.parseInt(cmd.split("#")[2]);
			Agent agent = player.playerAgent().getAgent(id);
			if (agent != null) {
				agent.incPower(n);
				player.playerAgent().SetChanged();
				player.sendUserUpdateMsg();
			}
		} else if ("agentDispatchTime".equals(action)) {
			int id = Integer.parseInt(cmd.split("#")[1]);
			int n = Integer.parseInt(cmd.split("#")[2]);
			Agent agent = player.playerAgent().getAgent(id);
			if (agent != null) {
				agent.setLastUpdateTime(agent.getLastUpdateTime() + n * GameConstants.SECOND);
				player.playerAgent().SetChanged();
				player.sendUserUpdateMsg();
			}

		}else if ("hftimes".equals(action)) {
			int id = Integer.parseInt(cmd.split("#")[1]);
			ServerData sd = ServerDataManager.getIntance().getServerData(player.getServerId());
			sd.getServerMergeData().setMergeTimes(id);
			sd.save();
		}else if ("ad".equals(action)) {
			int id = Integer.parseInt(cmd.split("#")[1]);
			AdTemplate adTemplate = SpringUtil.getBean(AdConfig.class).getTemplate(id);
			adTemplate.getAdsType().reward(player,adTemplate,msg);
			player.sendUserUpdateMsg();
		}else if ("vip".equals(action)) {
			int id = Integer.parseInt(cmd.split("#")[1]);
			int day = Integer.parseInt(cmd.split("#")[2]);
			player.getPlayerRecharge().addCzYueka(id,day);
			player.sendUserUpdateMsg();
		} else if("FishExp".equals(action)){
			int expAdd = Integer.parseInt(cmd.split("#")[1]);
			int expTotal = player.playerFish().addExp(expAdd);
			int fishLevel = fishConfig.getGoFishLevel(expTotal);
			if(fishLevel <= 0){
				return;
			}
			player.playerFish().setLv(fishLevel);
			player.playerFish().SetChanged();
			player.sendUserUpdateMsg();
		} else if("port".equals(action)){
            WorldItemContainer worldItemContainer = WorldServerContainer.of(player);
            List<Integer> ids = StringUtil.splitStr2IntegerList(cmd.split("#")[1],",");
            for (int id : ids) {
                worldItemContainer.getWorldCity(id).setPortCity(true);
            }
        }else if("giveup".equals(action)){
            int cityId = Integer.parseInt(cmd.split("#")[1]);
            WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
            SpringUtil.getBean(WorldCityBiz.class).giveUpCity(player,worldCity);
        }else if ("clearpch".equals(action)){
            player.playerPaperResearch().resetDay();
            player.sendUserUpdateMsg();
        }else if ("downstone".equals(action)){
            for (Equipment equs : player.playerEquip().getEqus()) {
                int[] stones = equs.getStone();
                for (int i = 0; i < stones.length; i++) {
                    stones[i] = 0;
                }
            }
            player.playerEquip().SetChanged();
            player.sendUserUpdateMsg();
        }else if("resetFieldBoss".equals(action)){
            player.playerFieldBoss().resetDay();
            player.sendUserUpdateMsg();
        }else if("logout".equals(action)){
            long playerId = Long.parseLong(cmd.split("#")[1]);
            Player player1 = PlayerUtils.getPlayer(playerId);
            sysFacade.sendLeavePlayer(player1, LeaveOnlineType.SERVER);
        }else if ("mail".equals(action)) {
            int type = Integer.parseInt(cmd.split("#")[1]);
            List<Items> items = ItemUtils.str2DefaultItemImmutableList("1:1:5");
            if (cmd.split("#").length > 2) {
                items = Lists.newArrayList();
            }
            SpringUtil.getBean(MailBiz.class).sendSysMail(player, MailConfigEnum.getMailType(type), items,LanguageVo.createStr("无敌"));
            player.sendUserUpdateMsg();
        }else if ("sysmail".equals(action)) {
            MailCustomContent content = MailCustomContent.buildDefault("你好","hello world");
            List<Items> items = ItemUtils.str2DefaultItemImmutableList("6:63101:10,6:63201:10,6:63301:10,6:63401:10,6:63501:10,6:63601:10,6:63701:10,6:63801:10");
            if(cmd.split("#").length > 1) {
                items = null;
            }
            SpringUtil.getBean(MailBiz.class).sendCustomAllMail(player.getServerId(), Sets.newHashSet(player.getId()),
                    content, items);
            player.sendUserUpdateMsg();
        }else if ("resetTower".equals(action)) {
            TowerBattle battle = (TowerBattle) player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
            battle.resetDay(player);
            player.playerBattle().SetChanged();
            player.sendUserUpdateMsg();
        }else if ("notice".equals(action)) {
            player.notifyObservers(ObservableEnum.WorldBossStart,player.getServerId());
        }else if ("mainTask".equalsIgnoreCase(action)) {
            int taskId = Integer.parseInt(cmd.split("#")[1]);
            MainTaskBiz bean = SpringUtil.getBean(MainTaskBiz.class);
            TaskConfig taskConfig = SpringUtil.getBean(TaskConfig.class);
            TaskMainTemplateImpl taskMainTemplate = taskConfig.getTaskMainTemplate(taskId);
            if (taskMainTemplate != null) {
                player.playerMainTask().getMainTaskList().clear();
                bean.addMainTask(player, taskMainTemplate);
                player.sendUserUpdateMsg();
            }
        }else if ("doMainTask".equalsIgnoreCase(action)) {
            MainTask task = CollUtil.getFirst(player.playerMainTask().getMainTaskList());
            if (task == null) {
                return;
            }
            task.setState(TaskStatus.COMPLETE);
            player.playerMainTask().SetChanged();
            player.sendUserUpdateMsg();
        } else if ("allDailyTask".equalsIgnoreCase(action)) {
            for (DailyTask task : player.playerDailyTask().getTaskMap().values()) {
                if (task.isOpen()) {
                    task.setState(TaskStatus.COMPLETE);
                }
            }
            player.playerDailyTask().SetChanged();
            player.sendUserUpdateMsg();
        } else if ("taskExp".equalsIgnoreCase(action)) {
            int num = Integer.parseInt(cmd.split("#")[1]);
            player.playerDailyTask().incWeekPoint(num);
            player.playerDailyTask().SetChanged();
            player.sendUserUpdateMsg();
        }else if ("equ".equalsIgnoreCase(action)) {
            int index = Integer.parseInt(cmd.split("#")[1]);
            int id = Integer.parseInt(cmd.split("#")[2]);
            PlayerArmExtraTemplate template = SpringUtil.getBean(EquipmentConfig.class).getEquTemplate(id);
            if(template.getArm_pos() != index) {
                return;
            }
            player.playerEquip().changeEqu(index,template);
            player.sendUserUpdateMsg();
        }else if ("kf".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(cmd.split("#")[1]);
            SpringUtil.getBean(DKFServerBiz.class).intoKFWorld(player,id);
        }else if ("resetReTank".equalsIgnoreCase(action)) {
            player.playerResearchTank().resetDay();
            player.sendUserUpdateMsg();
        }else if("carlv".equals(action)){
            int lv = Integer.parseInt(cmd.split("#")[1]);
            if(commanderConfig.getCarTemplate(lv) == null) {
                return;
            }
            player.playerCommander().setCarLv(lv);
            player.sendUserUpdateMsg();
        }else if("obs".equals(action)){
            ObserverRouter.notifyObservers(ObservableEnum.KfScoreStart, player.getServerId());
        }else if("clearDs".equals(action)){
            player.getPlayerStatistics().clearTodayData();
            player.sendUserUpdateMsg();
        }
    }



	public void doCaptiveTank(Player player, int bPlayerId, int index){
		Player bPlayer = PlayerUtils.getPlayer(bPlayerId);
		List<WorldTroop> worldTroops = TroopServerContainer.of(bPlayer).getWorldTroopByPlayer(bPlayer);
		int tankId = worldTroops.get(index).getTroopArmy().getTankList().get(0).getId();
		if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
			player.getPlayerFunction().addOpenFunction(PlayerFunctionType.TankCaptive.getType());
			player.notifyObservers(ObservableEnum.FunctionUnlock, player, PlayerFunctionType.TankCaptive.getType());
		}
		long endTime = System.currentTimeMillis() + 10 * GameConstants.MINUTE;
		CaptiveTankInfo tankInfo = new CaptiveTankInfo(tankId, bPlayerId, bPlayer.getName() ,endTime, 5);
		CaptiveSlot luckCaptiveSlot = player.playerCaptive().findIdleCaptiveSlot(captiveBiz.calMaxCaptiveCount(player));
		luckCaptiveSlot.loadCaptiveTankInfo(tankInfo);
		player.playerCaptive().addLog(new CaptiveOtherLog(bPlayer.getName(), tankInfo.getTankId()));
		player.notifyObservers(ObservableEnum.TankCaptive, tankInfo);

		if (bPlayer.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
			if(bPlayer.playerCaptive().getBeCaptiveTank(tankId) != null) {
				bPlayer.playerCaptive().removeBeCaptiveTank(tankId);
			}else{
				bPlayer.playerCaptive().addBeCaptiveTank(new BeCaptiveTankInfo(player, tankId, endTime));
			}
			bPlayer.playerCaptive().addLog(new CaptiveMeLog(player.getName(), tankId));
			bPlayer.sendUserUpdateMsg();
		}
		player.sendUserUpdateMsg();
		//添加俘虏日志
	}
	
	public void changeWorldCity(Player player,List<Integer> cityIds) {
		GuildWorldBiz guildWorldBiz = SpringUtil.getBean(GuildWorldBiz.class);
		WorldBiz worldBiz = SpringUtil.getBean(WorldBiz.class);
		WorldItemContainer worldItemContainer = WorldServerContainer.of(player);
		int winGuildId = player.getGuildId();
		for (int cityId : cityIds) {
			WorldCity worldCity = worldItemContainer.getWorldCity(cityId);
			worldCity.clearData();
            worldCity.getDefCityTroop().removeAllNpc();
            //把属于胜利方军团的部队移到防御方
			worldBiz.moveWinTroopToDef(worldCity, winGuildId);
			guildWorldBiz.guildWinWorldCity(player, player.getGuildId(), worldCity);
			//重新生成npc
			worldBiz.resetNpc(worldCity,false);
			worldBiz.broadWorldCityUpdate(worldCity);//广播城池变化
		}
	}
	
	
	@MsgMethod ( MessageComm.C2S_Test_ChangeMissionId )
	public void changeMissionId(Player player, JsonMsg msg){
		if(!ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
			return;
		}
		int missionId = msg.getInt("missionId");
		player.playerMission().setMissionId(missionId);
		int openCity = player.playerMission().getRelOpenCity();
		for(int i=1;i<=openCity;i++){
			player.playerMission().checkArmory(i);
		}
		//触发一次解放城市信号来检查功能解锁
		player.notifyObservers(ObservableEnum.RecaptureCity, player.playerMission().getOpenCity());
		player.sendUserUpdateMsg();
		
	}

}
