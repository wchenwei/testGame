package com.hm.action.login.biz;

import cn.hutool.core.util.StrUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.activity.ActivityObserverBiz;
import com.hm.action.battle.biz.BattleBiz;
import com.hm.action.citywarskill.biz.CityWarSkillBiz;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.commander.biz.AircraftCarrierBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildFactoryBiz;
import com.hm.action.honor.biz.HonorBiz;
import com.hm.action.invite.biz.InviteBiz;
import com.hm.action.levelEvent.biz.LevelEventBiz;
import com.hm.action.mastery.biz.MasteryBiz;
import com.hm.action.memorialHall.biz.MemorialHallBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.player.RemovePlayerBiz;
import com.hm.action.player.biz.CombatBiz;
import com.hm.action.player.biz.PlayerDynamicDataBiz;
import com.hm.action.player.biz.PlayerLevelBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.action.robsupply.biz.RobSupplyBiz;
import com.hm.action.serverData.ServerPowerBiz;
import com.hm.action.shop.biz.ShopBiz;
import com.hm.action.task.biz.DailyTaskBiz;
import com.hm.action.task.biz.RandomTaskBiz;
import com.hm.action.troop.biz.TroopFightBiz;
import com.hm.action.warMake.WarMakesBiz;
import com.hm.cache.PlayerCacheManager;
import com.hm.config.LevelEventConfig;
import com.hm.container.PlayerContainer;
import com.hm.container.PushContainer;
import com.hm.enums.StatisticsType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.enums.SocketType;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.log.LogBiz;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.IObservable;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.util.RedisUtil;
import com.hm.servercontainer.idcode.IdCodeContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Biz
public class LoginBiz implements IObservable{
	@Resource
    private PlayerDynamicDataBiz playerDynamicDataBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
    private CombatBiz combatBiz;
	@Resource
    private ActivityBiz activityBiz;
	@Resource
	private BattleBiz battleBiz;
	@Resource
	private HonorBiz honorBiz;
	@Resource
	private PushContainer pushContainer;
	@Resource
	private RobSupplyBiz robSupplyBiz;
	@Resource
	private PlayerLevelBiz playerLevelBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private ActivityObserverBiz activityObserverBiz;
	@Resource
	private TroopFightBiz troopFightBiz;
	@Resource
	private ShopBiz shopBiz;
	@Resource
	private RandomTaskBiz randomTaskBiz;
	@Resource
	private InviteBiz inviteBiz;
	@Resource
	private DailyTaskBiz dailyTaskBiz;
	@Resource
	private ServerPowerBiz serverPowerBiz;
	@Resource
	private QueueBiz queueBiz;
	@Resource
	private GuildFactoryBiz guildFactoryBiz;
	@Resource
	private CityWarSkillBiz cityWarSkillBiz;
	@Resource
	private MasteryBiz masteryBiz;
	@Resource
	private LevelEventConfig levelEventConfig;
	@Resource
	private MemorialHallBiz memorialHallBiz;
	@Resource
	private LevelEventBiz levelEventBiz;
	@Resource
	private WarMakesBiz warMakesBiz;
	@Resource
	private CmqBiz cmqBiz;
	@Resource
	private AircraftCarrierBiz aircraftCarrierBiz;
	@Resource
	private LogBiz logBiz;
	@Resource
	private RemovePlayerBiz removePlayerBiz;


    //每次玩家登陆检查
	public boolean doPlayerLogin(Player player) {
		boolean isDayFirstLogin = false;
		if(player.playerTemp().isCloneLogin()) {
			return isDayFirstLogin;
		}
		// 检查注销用户
		removePlayerBiz.doLogin(player);
		//活动检查
		activityObserverBiz.checkPlayerUnlockActivity(player,false);
		//检查每日数据是否重置
		isDayFirstLogin = resetPlayerByZero(player);
		
		player.playerBaseInfo().setLastLoginDate(new Date());
		//检查发放等级奖励信息
		playerLevelBiz.checkLvReward(player);
		// 检查玩家cd状态
		player.getPlayerCDs().checkPlayerCd();
		// 检查玩家队列信息
		queueBiz.checkPlayerQueue(player);
		//计算动态数据
		playerDynamicDataBiz.calPlayerDynamicData(player);
		//计算最大战力和部队战力
		combatBiz.countPlayerCombat(player);
		//登录检查免费征收次数去除
		//player.playerTreasury().checkFreeCount();
		//检查商店重置
		player.playerShop().loginCheck();
		// 贸易检测
		// player.playerTrade().check();
		player.playerShipTrade().check();
		//检查商店开启
		shopBiz.checkShopOpen(player);

		//检查战役开启
		battleBiz.checkBattleOpen(player);
		//检查预扣资源
		//playerBiz.checkAdvanceOil(player);
		//检查预扣资源
		playerBiz.checkAdvances(player);
		//添加推送
		pushContainer.addPush(player);
		//更新部落最后登录时间
		guildBiz.updateLoginTime(player);
		//检查玩家部队是否异常
		troopFightBiz.checkTroopStateError(player);
		player.playerLevelEvent().checkEvent();
		//检查三军演武
		player.playerOverallWar().checkOverallWar();
		//检查称号
		player.playerTitle().doLoginForTitle();
		//检查推送礼包
		player.playerGiftPush().checkGiftOverTime();
		//检查民情事件
		randomTaskBiz.doLoginRandomEventCheck(player);
		//检查邀请码
		inviteBiz.checkInviteCode(player);
		//检查玩家出征数据
		robSupplyBiz.doPlayerLogin(player);
		// 监测一下是否有新触发开启对每日任务
		dailyTaskBiz.checkNewOpenTask(player);
		//检查总统特权经验
		serverPowerBiz.checkPlayerPresidentExp(player);
		//检查玩家武器开启状态
		guildFactoryBiz.checkPlayerArmsOpen(player);
		//登录时，检查玩家的城战技能是否开启
		cityWarSkillBiz.checkOpenSkill(player, player.playerMission().getOpenCity());
		//检查研修光环
		masteryBiz.checkMasterCircle(player);
		//检查结算旧的等级事件

		levelEventBiz.checkLevelEventCal(player);
		levelEventBiz.checkLevelEventCalNew(player);
		//检查等级事件
		levelEventConfig.checkLevelEventNewOpen(player);
		//检查纪念馆是否开启了新的章节
		memorialHallBiz.doPlayerNextChapter(player);
		// 战令检查
		warMakesBiz.checkWarMakes(player);
		//初始化航母，舰岛，此模块后加的，可能用户已经满足条件，但未开启。所以登录的时候检查一下
		aircraftCarrierBiz.checkIsland(player);
		//登录检查
		player.playerFriend().doLoginCheck();

		// 检查头像是否过期
		player.playerHead().checkHead();
		//计算玩家动态数据
		this.notifyChanges(ObservableEnum.LOGIN, player);
		player.saveDB();

		return isDayFirstLogin;
	}
	
	public boolean resetPlayerByZero(Player player) {
		synchronized (player) {
			return resetPlayerByZeroLock(player);
		}
	}
	//每天0点重置玩家数据
	public boolean resetPlayerByZeroLock(Player player) {
		String serverDayMark = ServerDataManager.getIntance().getServerData(player.getServerId())
				.getServerStatistics().getServerDayMark();
		if(StrUtil.equals(player.playerBaseInfo().getServerDayMark(), serverDayMark)) {
			return false;
		}
		log.error(player.getId()+"每日数据重置");
		//TODO 必须在重置数据前进行统计处理
//		player.playerResBack().resetDay();
		player.getPlayerStatistics().clearTodayData();	
		player.getPlayerStatistics().addLifeStatistics(StatisticsType.LOGIN_DAYS);
		player.getPlayerCDs().resetDayCd();//重置每日cd
		player.playerTreasury().resetDay();//重置
		player.getPlayerRecharge().resetDay();//清除月卡周卡领取标示
		//检查玩家活动
		activityBiz.checkPlayerActivityForDay(player);
		player.playerDailyTask().resetDay();
		player.playerTankFactory().reset();//坦克工厂天重置
		//重置商店数据
		//player.playerShop().resetDay();
		player.playerBattle().resetDay();
		//重置坦克研发和测试数据
		player.playerPaperResearch().resetDay();
		honorBiz.doPlayerReset(player);
		//重置玩家远征数据
//		player.playerExpedition().resetData();
		//删除过期邮件
		player.playerMail().delExpiredMail();
		//重置竞技场
//		player.playerArena().reset();
		//重置全面战争数据
//		player.playerOverallWar().resetDay();
//		robSupplyBiz.dayReset(player);
		// 每日重置地图民情任务完成次数
//		player.playerRandomTask().resetDay();
		//每日重置被邀请奖励领取状态
//		player.playerInviteInfo().resetDay(player);
		//每日重置一次坦克里的信息
		player.playerTank().resetDay();
		//重置商店数据
		player.playerShop().resetDay();
		//重置世界建筑
//		player.playerWorldBuildData().dayReset();
		//处理每周重置
		if(!DateUtil.isSameWeek(serverDayMark, player.playerBaseInfo().getServerDayMark())) {
			doPlayerWeekReset(player);
		}
		//重置每日服务器标识
		player.playerBaseInfo().setServerDayMark(serverDayMark);
		player.playerActivityTask().resetDay();
		player.playerMysteryShop().resetDay();
		player.playerGuild().doDayReset();
		player.playerRepairTrain().resetDay();
//		player.playerCaptive().dayReset();
		player.playerFriend().doDayReset();
		player.playerFieldBoss().resetDay();
		player.playerGiftPack().doDayReset();
		player.playerResearchTank().resetDay();
		// 战令检查
		warMakesBiz.checkWarMakes(player);
        // 跨服赛季 战令任务
//        playerKFTaskBiz.doCheckPlayerKFTask(player);

        //发送每日统计数据
		cmqBiz.sendStatisData(player);
        // 用户回流奖励检查
//        backflowBiz.checkBackFlowPlayer(player);
		//重置小游戏
//		player.playerGame().dayReset();

		player.notifyObservers(ObservableEnum.DayFirstLogin);

		logBiz.addPlayerFirstLoginLog(player);

		return true;
	}
	
	/**
	 * 处理玩家每周重置数据
	 * @param player
	 */
	public void doPlayerWeekReset(Player player) {
		//重置玩家的部落任务领取进度
		player.playerGuild().doWeekReset();
		//每周重置
		player.playerGiftPack().doWeekReset();
		player.getPlayerStatistics().clearWeekData();
	}

	/**
	 * 玩家退出处理
	 * @param player
	 */
	public void doLoginOut(Player player) {
		synchronized(player) {
			player.playerTemp().doLoginOut();

			//对player加锁，防止和每日重置逻辑冲突
			if(!player.playerTemp().isCloneLogin()) {
				player.playerBaseInfo().setLastOffLineDate(new Date());
				RedisUtil.updateRedisPlayer(player);
			}
			//统计当天在线时长
			player.getPlayerStatistics().addTodayStatistics(StatisticsType.ONLINE_TIME,TimeUtils.getDifferSecs(player.playerBaseInfo().getLastLoginDate(), new Date()));

			player.notifyObservers(ObservableEnum.PlayerLoginOut);

            player.saveNowDB();
			IdCodeContainer.of(player).removeIp(player.getIp(),player.getId());

            int nowHour = DateUtil.thisHour(true);
            if (player.playerLevel().getLv() < 40) {
				PlayerCacheManager.getInstance().removePlayerCache(player.getId());
            } else if (nowHour == 0 && DateUtil.thisMinute() < 5) {
				PlayerCacheManager.getInstance().addPlayerToCache(player);
			}
			PlayerContainer.removePlayer(player.getId());
			
			log.error(player.getId()+"退出游戏");
			player.clearSession();
		}
	}
	public boolean isNormalSocket(HMSession session){
		return session.getSocketType()==SocketType.Normal.getType();
	}

	@Override
	public void notifyChanges(ObservableEnum observableEnum, Player player, Object... argv) {
		ObserverRouter.getInstance().notifyObservers(observableEnum, player, argv);
	}
}
