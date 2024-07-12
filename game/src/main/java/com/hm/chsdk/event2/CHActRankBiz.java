package com.hm.chsdk.event2;


import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.chsdk.event2.activity.CHActkRankEvent;
import com.hm.chsdk.event2.pack.CHPackEventBiz;
import com.hm.enums.ActivityType;
import com.hm.enums.RankType;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.model.activity.ActivityConfItem;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import redis.clients.jedis.Tuple;

import java.util.*;
import java.util.stream.Collectors;

@Biz
public class CHActRankBiz implements IObserver {

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum) {
            case HourEvent:
                doHour();
                break;
        }
    }

    public static Map<Integer, List<RankType>> actRankMap = Maps.newHashMap();

    static {
        actRankMap = Arrays.stream(RankType.values())
                .filter(e -> RankType.getActivityTypeByRankType(e) != null)
                .collect(Collectors.groupingBy(e -> RankType.getActivityTypeByRankType(e).getType()));
    }

    /**
     * 只有997服务器每天凌晨5点发送
     */
    public void doHour() {
        if (DateUtil.thisHour(true) != 5) {
            return;
        }
        if (!ServerConfig.getInstance().isSendChRankEvent()) {
            return;
        }

        Collection<Integer> typeList = actRankMap.keySet();
        List<Integer> allServerId = getRunServerIds();
        //凌晨5点查询昨日活动排行数据给草花
        long endTime = DateUtil.beginOfDay(new Date()).getTime();
        List<ActivityConfItem> activityConfItemList = getActByEndtime(endTime, typeList);
        for (ActivityConfItem actConf : activityConfItemList) {
            //找出昨日结束的活动列表
            doActivityConfItem(actConf, allServerId);
        }
        CHPackEventBiz.stopServer();
    }

    public void doActivityConfItem(ActivityConfItem actConf, List<Integer> allServerId) {
        //找出此活动对应的排行列表
        List<RankType> rankTypeList = actRankMap.get(actConf.getActivityType());
        if (CollUtil.isNotEmpty(rankTypeList)) {
            //此活动的排行分组情况
            List<Integer> groupIdList = getKFGroupIdList(actConf, allServerId);
            for (RankType rankType : rankTypeList) {
                doPushRank(actConf, rankType, groupIdList);
            }
        }
    }

    /**
     * 推送活动排行
     *
     * @param activity
     * @param rankType
     * @param groupIdList
     */
    public void doPushRank(ActivityConfItem activity, RankType rankType, List<Integer> groupIdList) {
        String rankName = rankType.getRankName() + "_" + activity.getId();
        for (int groupId : groupIdList) {
            System.out.println("chsdk act rank:" + rankName + " groupId:" + groupId);
            List<Tuple> topRanks = RankRedisUtils.getRankList(groupId, rankName, 1, 9999);
            if (CollUtil.isNotEmpty(topRanks)) {
                CHActkRankEvent event = new CHActkRankEvent();
                event.init(null, groupId);
                event.initInfo(null, groupId);
                event.loadRank(topRanks, rankType, activity.getActivityType());
                new AUploadEvent(null, event).sendSDK();
            }
        }
    }

    public static List<Integer> getRunServerIds() {
        MongodDB mongodDB = MongoUtils.getLoginMongodDB();
        Criteria criteria = Criteria.where("openstate").is(0)
                .and("db_id").is(0);
        Query query = new Query(criteria);
        query.limit(Integer.MAX_VALUE);
        return mongodDB.query(query, ServerInfo.class)
                .stream().map(e -> e.getServer_id()).collect(Collectors.toList());
    }

    public static List<Integer> getKFGroupIdList(ActivityConfItem actConf, List<Integer> allServerIds) {
        return allServerIds.stream().map(e -> getKFid(actConf.getExtend(), e))
                .distinct()
                .collect(Collectors.toList());
    }

    public static int getKFid(String extend, int serverId) {
        List<String> strings = StringUtil.splitStr2StrList(extend, "%");
        List<String> str = StringUtil.splitStr2StrList(strings.get(0), ";");
        for (String ss : str) {
            for (String s : ss.split(",")) {
                int startId = Integer.parseInt(s.split("_")[0]);
                int endId = Integer.parseInt(s.split("_")[1]);
                if (serverId >= startId && serverId <= endId) {
                    return Integer.parseInt(ss.split("_")[0]);
                }
            }
        }
        return 0;
    }

    public static List<ActivityConfItem> getActByEndtime(long endTime, Collection<Integer> typeList) {
        MongodDB mongoDb = MongoUtils.getLoginMongodDB();
        Criteria criteria = Criteria
                .where("status").is(1)
                .and("endTime").is(endTime)
                .and("activityType").in(typeList);
        Query query = new Query(criteria);
        query.limit(Integer.MAX_VALUE);
        return mongoDb.query(query, ActivityConfItem.class);
    }

    public static ActivityConfItem getActivityConfItem(String id) {
        MongodDB mongoDb = MongoUtils.getLoginMongodDB();
        return mongoDb.get(id, ActivityConfItem.class);
    }
}
