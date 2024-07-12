package com.hm.action.guild.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;

/**
 * 金融战领取领取记录
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/4/7 13:53
 */
@Biz
public class GuildFinanceCache implements IObserver {
    private static ListMultimap<Long, String> logs = ArrayListMultimap.create();
    /**
     * 今天是否可以领取金融战奖励
     *
     * @param playerId
     * @return
     */
    public static boolean todayCanFinance(long playerId,String date) {
        return !logs.get(playerId).contains(date);
    }

    /**
     * 添加领取记录
     *
     * @param playerId
     */
    public static void addFinanceLog(long playerId,String date) {
        logs.put(playerId, date);
    }


    @Override
    public void registObserverEnum() {
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
    }

}
