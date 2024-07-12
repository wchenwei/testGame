package com.hm.action.smallgame.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.config.SmallGameConfig;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.model.player.BaseSamllGame;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/2/11 14:24
 */
@Biz
public class SmallGameBiz implements IObserver {
    @Resource
    private SmallGameConfig smallGameConfig;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.LOGIN, this);

    }
    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum){
            case PlayerLevelUp:
                smallGameConfig.checkGameOpen(player);
                break;
            case LOGIN:
                firstInitSmallGame(player);
                break;
        }
    }
    /**
     * 更新排行榜信息
     * @param player
     */
    public void updateRank(Player player) {
        long score = player.playerGame().getScoreSum();
        HdLeaderboardsService.getInstance().updatePlayerRankOverrideForTime(player.getId(),
                player.getServerId(), RankType.SmallGameRank.getRankName(), score);
    }
    /**
     * 添加经验
     * @param player
     * @param game
     * @param exp
     * @param score
     */
    public void addExp(Player player, BaseSamllGame game, long exp, long score) {
        game.addExp(exp, score);
        int nowLv = smallGameConfig.getLevel(game.getLv(), game.getExp());
        game.setLv(Math.max(game.getLv(), nowLv));
        player.playerGame().SetChanged();
    }
    /**
     * 用于检测用户是否有小游戏，每次登录检测，主要针对老用户，没用触发升级事件的。初始化小游戏
     * @param player
     */
    public void firstInitSmallGame(Player player) {
        smallGameConfig.checkGameOpen(player);
    }
}
