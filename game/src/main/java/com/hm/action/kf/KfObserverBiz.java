package com.hm.action.kf;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.kf.kfexpedition.KfExpeditionScoreUtils;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.player.biz.CombatBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.KfConfig;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.activity.ActivityFactory;
import com.hm.model.activity.kfactivity.KFServerInfoUtil;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.activity.kfactivity.KfWorldWarActivity;
import com.hm.model.activity.kfworldwarshop.KfWorldWarShopActivity;
import com.hm.model.activity.kfworldwarshop.KfWorldWarShopValue;
import com.hm.model.kf.KfExpeditionServer;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 跨服
 * @author siyunlong  
 * @date 2019年3月15日 下午9:03:04 
 * @version V1.0
 */
@Biz
public class KfObserverBiz implements IObserver{
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private ActivityBiz activityBiz;
	@Resource
	private KfConfig kfConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private CombatBiz combatBiz;


	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerQuit, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLoginSuc, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.RecaptureCity, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.KFNoMatch, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.KFActOpen, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch(observableEnum) {
			case GuildPlayerQuit:
			case GuildPlayerAdd:
				doGuildQuit(player);
				break;
			case PlayerLoginSuc:
				doPlayerLogin(player);
				break;
			case RecaptureCity :
				checkKfExpedition(player.getServerId(), (int) argv[0]);
				break;
			case HourEvent :
				calServerKfCombat();
				openKfWorldWarShop();
				KFServerInfoUtil.init();
				break;
			case Recharge :
				doKwWorldWarShop(player,argv);
				break;
			case KFNoMatch :
				doKFNoMatch(argv);
				break;
			case KFActOpen :
				doKFActOpen(argv);
				break;
		}
	}

	public void doKFNoMatch(Object... argv) {
		int serverId = Convert.toInt(argv[0],0);
		if(serverId <= 0) {
			return;
		}
		KfType kfType = KfType.getType(Convert.toInt(argv[1],0));
		if(kfType == null) {
			return;
		}
		mailBiz.sendServerSysMailWithTypeId(serverId, MailConfigEnum.KFNoMatch,null);
	}

	public void doKFActOpen(Object... argv) {
		int serverId = Convert.toInt(argv[0],0);
		if(serverId <= 0) {
			return;
		}
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		serverData.getServerFunction().unlock(ServerFunctionType.KFServer);
		serverData.save();
		serverData.broadServerUpdate();
	}
	
	public void doGuildQuit(Player player) {
		if(player.playerLevel().getLv() < 60){
			return;
		} 
		sendKfServer(player);
	}
	
	public void doPlayerLogin(Player player) {
		if(player.playerLevel().getLv() < 60){
			return;
		} 
		KfMineUtils.sendPlayerMineChange(player);
	}
	
	//检查莫斯科是否开启
	public void checkKfExpedition(int serverId, int cityId) {
		int targetId = commValueConfig.getCommValue(CommonValueType.KfExpeditionOpenCity);
		int openType = 0;
		if(targetId == cityId) openType = 2;
		if(targetId-1 == cityId) openType = 1;
		if(openType == 0) {
			return;
		}
		KfExpeditionActivity expeditionActivity = (KfExpeditionActivity) ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KfExpeditionActivity);
		if(expeditionActivity == null) {
			return;
		}
		if(expeditionActivity.getOpenType() < openType) {
			expeditionActivity.setOpenType(openType);
			expeditionActivity.saveDB();
			//广播活动开启
			activityBiz.broadPlayerActivityUpdate(expeditionActivity);
			//检查跨服远征积分
			if(openType == 2) {
				KfExpeditionScoreUtils.checkFirstScore(serverId);
				calServerKfCombat(serverId);
//				player.notifyObservers(ObservableEnum.KFActOpen, serverId);
				ObserverRouter.getInstance().notifyObservers(ObservableEnum.KFActOpen, null, serverId);
			}
			//检查时间建筑是否开启
//			worldBuildBiz.checkWorldBuildOpen(expeditionActivity);
		}
	}
	
	public void calServerKfCombat() {
		//每周日23点
		if(DateUtil.getCsWeek() == 4 && DateUtil.thisHour(true) == 23) {
			GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
				try {
					calServerKfCombat(serverId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});	
		}
		if(DateUtil.getCsWeek() == 4 && DateUtil.thisHour(true) == 4) {
			RedisTypeEnum.KfExpedtionUrl.dropColl();
		}
	}
	public void calServerKfCombat(int serverId) {
		KfExpeditionServer mainServer = KfExpeditionScoreUtils.getKfExpeditionServer(serverId);
		if(mainServer == null) {
			mainServer = new KfExpeditionServer();
			mainServer.setId(serverId);
			mainServer.setServerId(serverId);
		}
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		if(serverData != null) {
			mainServer.setOpenTime(serverData.getServerOpenData().getFirstOpenDate().getTime());
		}
		//计算获取战力前30的战力和 3天内登录的玩家
		long startTime = System.currentTimeMillis()-3*GameConstants.DAY;
		mainServer.setCombat(combatBiz.getServerActivePlayerTotalCombat(serverId,startTime,30));
		mainServer.saveDB();
	}

	public static boolean sendKfServer(Player player) {
        try {
        	int hour = DateUtil.thisHour(true);
    		if(hour >= 9 && hour <= 20) {
    			String ip = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerKfData().getKfMinehttpIp();
    			String url = "http://" + ip + "?action=kfope.do&m=doGuildChange&playerId="+player.getId();
    			String result = HttpUtil.get(url, 1500);
    			return StrUtil.equals(result, "1");
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	private void doKwWorldWarShop(Player player, Object[] argv) {
		KfWorldWarShopActivity activity = (KfWorldWarShopActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KfWorldWarShop);
		if(activity==null||!activity.isOpen()) {
			return;
		}
		int rechargeId = (int)argv[3];
		if(!kfConfig.isKfWorldWarShopGiftId(rechargeId)) {
			return;
		}
		//根据配置把rechatgeId转换为goodsId
		int goodsId = kfConfig.getKwWorldWarGoodsId(activity.getServerLv(),rechargeId);
		if(goodsId<0) {
			return;
		}
		KfWorldWarShopValue value = (KfWorldWarShopValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.KfWorldWarShop);
		value.buy(player, goodsId);
		player.getPlayerActivity().SetChanged();
	}

	public void openKfWorldWarShop() {
		if(DateUtil.thisHour(true) != 0) {
			return;
		}
		for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
			try {
				KfWorldWarActivity activity = (KfWorldWarActivity) activityItemContainer.getAbstractActivity(ActivityType.KfWorldWar);
				if (null==activity || this.getDays(activity.getSnumstartTime())!= GameConstants.KfWordWarShopOpen) {
					continue;
				}
				KfWorldWarShopActivity activityData = (KfWorldWarShopActivity) activityItemContainer.getAbstractActivity(ActivityType.KfWorldWarShop);
				if(null==activityData || !activityData.isOpen()) {
					Date start = DateUtil.getNowDay(new Date());
					long end = DateUtil.getNextDays(start, GameConstants.KfWordWarShopOpenDays);
					activityData = (KfWorldWarShopActivity) ActivityFactory.createAbstractActivity(ActivityType.KfWorldWarShop,
							start.getTime(), end);
					activityData.setShowTime(start.getTime());
					activityData.setCalTime(new Date(end));
					activityData.setServerId(activityItemContainer.getServerId());
					activityItemContainer.addActivity(activityData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private int getDays(long startTime) {
		return (int) DateUtil.betweenDay(DateUtil.date(startTime), new Date(), true) + 1;
	}

}








