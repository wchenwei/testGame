package com.hm.timerjob;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;
import com.hm.enums.GuildWarStatus;
import com.hm.libcore.util.KeyValue;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.activity.kfactivity.AbstractKfActivity;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 部落战检查
 * @date 2024/3/13 17:15
 */
@Slf4j
public class GuildWarUtils {
    public static final KeyValue<Integer,Integer> GuildWarTime = new KeyValue<>(9,18);
    public static List<ActivityType> KFActivityList = Lists.newArrayList(ActivityType.KFScoreWar);

    public static Range<Long> OpenTime = null;
    //服务器状态  val: 1-开启中  2-等待结束处理
    public static Map<Integer,Integer> ServerMap = Maps.newConcurrentMap();
    public static WarStateModel warStateModel;


    public static void checkWarOpen() {
        KeyValue<Integer,Integer> keyValue = getHourFit();
        if(keyValue == null) {
            OpenTime = null;
            for (int serverId : ServerMap.keySet()) {
                ServerMap.put(serverId,GuildWarStatus.WaitCal.getType());
            }
            //获取下次开启时间
            warStateModel = GuildWarStatus.WaitCal.build(getNextOpenTime());
            return;
        }
        //加载正在开启的服务器
        Map<Integer,Integer> serverMap = Maps.newConcurrentMap();
        log.error("===========部落战开启================");
        long dayTime = DateUtil.beginOfDay(new Date()).getTime();
        OpenTime = Range.closed(dayTime+keyValue.GetKey()* GameConstants.HOUR,dayTime+keyValue.GetValue()* GameConstants.HOUR);

        for (int serverId : getCanStartWarServerList()) {
            serverMap.put(serverId, GuildWarStatus.Running.getType());
            log.error("===========部落战开启================serverId:"+serverId);
        }
        ServerMap = serverMap;
        warStateModel = GuildWarStatus.Running.build(OpenTime.upperEndpoint());
    }



    public static boolean isOpenWar() {
        return OpenTime!= null && OpenTime.contains(System.currentTimeMillis());
    }


    public static boolean isOpenWar(int serverId) {
        return isOpenWar() && getServerWarStatus(serverId) == GuildWarStatus.Running.getType();
    }

    public static int getServerWarStatus(int serverId) {
        return ServerMap.getOrDefault(serverId,GuildWarStatus.None.getType());
    }
    public static void removeServerWarStatus(int serverId) {
        ServerMap.remove(serverId);
    }

    public static KeyValue<Integer,Integer> getHourFit() {
        int hour = DateUtil.thisHour(true);
        if(hour >= GuildWarTime.GetKey() && hour < GuildWarTime.GetValue()) {
            return GuildWarTime;
        }
        return null;
    }
    public static long getNextOpenTime() {
        int hour = DateUtil.thisHour(true);
        if(hour <= GuildWarTime.GetKey()) {
            return DateUtil.beginOfDay(new Date()).getTime()+GuildWarTime.GetKey()*GameConstants.HOUR;
        }
        //明天
        return DateUtil.beginOfDay(new Date()).getTime()+GameConstants.DAY+GuildWarTime.GetKey()*GameConstants.HOUR;
    }

    public static void checkHourForWar() {
        boolean isOpen = isOpenWar();
        checkWarOpen();
        if(!isOpen && isOpenWar()) {
            for (int serverId : getCanStartWarServerList()) {
                ObserverRouter.notifyObservers(ObservableEnum.GuildWarStart,serverId);
            }
        }
    }

    public static WarStateModel getWarStateModel(int serverId) {
        ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
        if(serverData.getServerOpenData().getOpenDay() > 1) {
            return warStateModel;
        }
        return GuildWarStatus.None.build(0);
    }

    public static boolean isCanOpenGuildWar(int serverId) {
        ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
        if(serverData.getServerOpenData().getOpenDay() <= 1) {
            return false;
        }
        //是否有跨服活动
        for (ActivityType activityType : KFActivityList) {
            AbstractKfActivity kfScoreActivity = (AbstractKfActivity) ActivityServerContainer.of(serverId).getAbstractActivity(activityType);
            if(kfScoreActivity != null && kfScoreActivity.isOpenForServer()) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> getCanStartWarServerList() {
        List<Integer> serverIdList = Lists.newArrayList();
        for (int serverId : GameServerManager.getInstance().getServerIdList()) {
            if(isCanOpenGuildWar(serverId)) {
                serverIdList.add(serverId);
            }
        }
        return serverIdList;
    }


}
