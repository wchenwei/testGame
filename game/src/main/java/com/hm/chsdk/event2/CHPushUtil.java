package com.hm.chsdk.event2;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.actor.ActorDispatcherType;
import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.event2.pack.CHPackEventBiz;
import com.hm.enums.KfType;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CHPushUtil {
    public static void initPush() {
        ActorDispatcherType.startAll();
        CHSDKContants.showLog = true;
        CHObserverBiz2.PackMaxNum = 10;
    }


    public static List<CHKeyVal> buildList(List<LeaderboardInfo> topRanks) {
        return topRanks.stream().map(e -> new CHKeyVal(e.getId(), (int) e.getScore()))
                .collect(Collectors.toList());
    }

    public static List<CHKeyVal> buildListForMap(Map<Object, Object> playerMap) {
        return playerMap
                .entrySet()
                .stream()
                .map(e -> new CHKeyVal(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public static void playerKfJoin(Player player, KfType kfType) {
        notifyObservers(ObservableEnum.CHKFPlayerJoin, player, kfType);
    }

    public static void playerKfOil(Player player, KfType kfType) {
        notifyObservers(ObservableEnum.CHKFPlayerOil, player, kfType);
    }

    public static void kfPlayerResult(KfType kfType, int serverId, Map<Long,Long> resultMap) {
        Map<Long,Long> playerMap = Maps.newHashMap();
        for (Map.Entry<Long, Long> entry : resultMap.entrySet()) {
            if(entry.getValue() > 0) {
                playerMap.put(entry.getKey(),entry.getValue());
            }
        }
        System.out.println("============CHPushUtil================");
        System.out.println(kfType.getDesc() + "->" + serverId + "->" + GSONUtils.ToJSONString(playerMap));
        notifyObservers(ObservableEnum.CHKFPlayerKFResult, null, serverId, kfType, playerMap);
        CHPackEventBiz.sendCHSdk();//立即发送
    }

    public static void kfPlayerResultForSport(KfType kfType, int serverId, Map playerMap) {
        System.out.println("============CHPushUtil================");
        System.out.println(kfType.getDesc() + "->" + serverId + "->" + GSONUtils.ToJSONString(playerMap));
        notifyObservers(ObservableEnum.CHKFPlayerKFResult, null, serverId, kfType, playerMap);
        CHPackEventBiz.sendCHSdk();//立即发送
    }

    public static void notifyObservers(ObservableEnum observableEnum, Player player, Object... argv) {
        CHObserverBiz2 chObserverBiz2 = SpringUtil.getBean(CHObserverBiz2.class);
        chObserverBiz2.checkEventMap();
        chObserverBiz2.invoke(observableEnum, player, argv);
    }
}
