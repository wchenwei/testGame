package com.hmkf.http;

import com.hm.cache.PlayerCacheManager;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.spring.SpringUtil;
import com.hmkf.action.level.KFLevelEndBiz;
import com.hmkf.action.level.KFLevelRankRewardBiz;
import com.hmkf.action.level.KfLevelBiz;
import com.hmkf.container.KFPlayerContainer;
import com.hmkf.db.KfDBUtils;
import com.hmkf.kfcenter.CenterBiz;
import com.hmkf.kfcenter.KfCenterData;
import com.hmkf.model.KFPlayer;
import com.hmkf.server.ServerGroupManager;
import com.hmkf.timerjob.HourTimerKFJob;
import com.hmkf.timerjob.LevelWeekBiz;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("kfope")
public class KFOpeAction {
    @Resource
    private HourTimerKFJob hourTimerKFJob;
    @Resource
    private KFPlayerContainer playerContainer;
    @Resource
    private LevelWeekBiz levelWeekBiz;
    @Resource
    private CenterBiz centerBiz;
    @Resource
    private KFLevelEndBiz kFLevelEndBiz;


    public void rankReward(HttpSession session) {
        SpringUtil.getBean(KFLevelRankRewardBiz.class).doRankReward();
        session.Write("0");
    }

    public void reloadServerGroup(HttpSession session) {
        try {
            ServerGroupManager.getIntance().reloadData();
            PlayerCacheManager.getInstance().clearAllCache();
            session.Write("1");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.Write("0");
        return;
    }

    public void showRank(HttpSession session) {
        String rankName = session.getParams("id");
        List<LeaderboardInfo> rankList = HdLeaderboardsService.getInstance().getGameRank(0, rankName, 1, 10000);
        for (LeaderboardInfo leaderboardInfo : rankList) {
            System.err.println(leaderboardInfo.getId() + "=" + leaderboardInfo.getRank());
        }
        session.Write("0");
    }


    public void doweek(HttpSession session) {
        try {
            KfCenterData centerData = centerBiz.getCenterData();
            //每周一进阶
            levelWeekBiz.doWeek();

            //每日重置
            playerContainer.doDayZero();
            //结算本期数据
            boolean isSnumEnd = kFLevelEndBiz.doSnumEnd();
            session.Write("1");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.Write("0");
        return;
    }

    public void addScore(HttpSession session) {
        long playerId = Long.parseLong(session.getParams("playerId"));
        int addScore = Integer.parseInt(session.getParams("score"));
        KFPlayer player = KfDBUtils.getPlayerSports(playerId);

        KfLevelBiz levelBiz = SpringUtil.getBean(KfLevelBiz.class);
        levelBiz.addPlayerScore(player, addScore);
        player.sendPlayerUpdate();

        session.Write("suc");
    }

}
