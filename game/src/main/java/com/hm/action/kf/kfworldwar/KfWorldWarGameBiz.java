package com.hm.action.kf.kfworldwar;

import cn.hutool.core.date.DatePattern;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarConf;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarServerTeamUtils;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSnumTime;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSumUtils;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.titile.TitleBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.LeaderboardBiz;
import com.hm.model.activity.kfactivity.KfWorldWarActivity;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.Title;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.ServerUtils;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 跨服世界大战
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/27 11:37
 */
@Slf4j
@Biz
public class KfWorldWarGameBiz implements IObserver {
    @Resource
    private LeaderboardBiz leaderboardBiz;
    @Resource
    private MailBiz mailBiz;
    @Resource
    private MailConfig mailConfig;
    @Resource
    private TitleBiz titleBiz;


    @Override
    public void registObserverEnum() {
//        ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum) {
            case HourEvent:
                doHourEvent();
                break;
        }
    }

    public void doHourEvent() {
        doWeekRankReward();
        //计算是否是赛季结束
        doNewSnumOpen();
    }

    public void doNewSnumOpen() {
        if (DateUtil.thisHour(true) < 9) {
            KfWorldWarSnumTime snumTime = KfWorldWarSumUtils.getCurSnum();
            if (DateUtil.isSameDay(new Date(), new Date(snumTime.getStartTime()))) {
                KfWorldWarConf.init();//初始化跨服世界大战配置
            }
        }
    }


    public void doWeekRankReward() {
        if (DateUtil.getCsWeek() == 1 && DateUtil.thisHour(true) == 0) {
            KfWorldWarSnumTime snumTime = KfWorldWarSumUtils.getCurSnum();
            long yesTime = System.currentTimeMillis() - 12 * GameConstants.HOUR;
            if (!snumTime.isFightWarTime(yesTime)) {
                return;//当前时间是否已经结束了
            }
            String rankName = buildKFWorldRankName(new Date(yesTime));
            KfWorldWarServerTeamUtils.getTeamIdListByMachine().forEach(e -> doRankRewardForTeam(e, rankName));
        }
    }


    public void doRankRewardForTeam(int teamId, String rankName) {
        log.error(teamId + "发放成员排行奖励");
        leaderboardBiz.doRankReward(RankType.KFWorldWarDonate, teamId, rankName, 0);
    }

    /***
     * 和服时处理跨服世界大战
     * @param serverData
     * @param otherServers
     */
    public void doKfWorldWar(int mainServerId, List<Integer> newServerIds) {
        KfWorldWarServerTeamUtils.reloadTeamMap();
        int teamId = KfWorldWarServerTeamUtils.getServerTeamID(mainServerId);
        if (teamId <= 0) {
            //当前主服没有team,需要找一个
            teamId = newServerIds.stream().map(e -> KfWorldWarServerTeamUtils.getServerTeamID(e))
                    .filter(e -> e > 0).findFirst().orElse(0);
            if (teamId <= 0) {
                log.error(mainServerId + "和服找不到开启的team");
                return;//下属服务器也没有开启,直接返回
            }
            //活动开启
            openKFWorldWarAct(mainServerId);
        }
        Set<Integer> serverIds = ServerUtils.getAllServerInfoFromDB().stream().filter(e -> e.getDb_id() == mainServerId || e.getServer_id() == mainServerId)
                .map(e -> e.getServer_id())
                .filter(e -> KfWorldWarServerTeamUtils.getServerTeamID(e) <= 0)
                .collect(Collectors.toSet());
        //记载所有
        log.error(mainServerId + "跨服世界大战和服新加入->" + GSONUtils.ToJSONString(serverIds) + ", teamId:" + teamId);
        KfWorldWarServerTeamUtils.addServerTeamNoMerge(serverIds, teamId);
        KfWorldWarServerTeamUtils.reloadTeamMap();
    }


    public void openKFWorldWarAct(int serverId) {
        KfWorldWarActivity worldWarActivity = (KfWorldWarActivity) ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KfWorldWar);
        worldWarActivity.openAct();//开启
        worldWarActivity.loadSnumInfo();
        worldWarActivity.saveDB();

//        activityBiz.broadPlayerActivityUpdate(worldWarActivity);
    }


    public static String buildKFWorldRankName(Date date) {
        String mark = DateUtil.format(DateUtil.beginOfWeek(date), DatePattern.PURE_DATE_FORMAT);
        return "WorldWarDonate" + KfWorldWarSumUtils.getSnumId() + "_" + mark;
    }


    /**
     * 发放战神称号
     *
     * @param playerId
     */
    public void doWorldWarPlayerTitle(String playerIdInfo, long titleEndTime) {
        int[] infos = StringUtil.splitStr2IntArray(playerIdInfo, ":");
        long playerId = infos[0];
        if (!GameServerManager.getInstance().isServerMachinePlayer(playerId)) {
            return;
        }
        Player player = PlayerUtils.getPlayer(playerId);
        if (player != null) {
            boolean winTitle = infos[3] == 1;
            if (winTitle) {
                Title title = new Title(PlayerTitleType.KFWorldWar.getType());
                title.setEndTime(titleEndTime);
                titleBiz.addTitle(player, title);
            }
            List<Items> itemList = Lists.newArrayList();
            if (infos[1] > 0) {
                itemList.add(new Items(PlayerAssetEnum.Oil.getTypeId(), infos[1], ItemType.CURRENCY));
            }
            if (infos[2] > 0) {
                itemList.add(new Items(PlayerAssetEnum.EXP.getTypeId(), infos[2], ItemType.CURRENCY));
            }
            MailTemplate mailTemplate = mailConfig.getMailTemplate(winTitle ? MailConfigEnum.WorldWarEndRewardAndTitle : MailConfigEnum.WorldWarEndReward);
            if (mailTemplate != null) {
                mailBiz.sendSysMail(player, mailTemplate, itemList);
            }
        }
    }
}
