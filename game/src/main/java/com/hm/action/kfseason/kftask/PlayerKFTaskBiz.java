package com.hm.action.kfseason.kftask;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerKfTask.KFTaskItem;
import com.hm.redis.kftask.BaseKFTask;
import com.hm.redis.kftask.KFPlayerTaskCache;
import com.hm.redis.kftask.KFTaskType;
import com.hm.servercontainer.activity.ActivityServerContainer;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 玩家跨服任务处理器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/4/9 14:11
 */
@Biz
public class PlayerKFTaskBiz {
    @Resource
    private ActivityBiz activityBiz;


    /**
     * 获取跨服任务记录数据
     *
     * @param player
     * @return
     */
    public Map<Integer, Object> getKfTaskList(Player player) {
        return Arrays.stream(KFTaskType.values())
                .filter(e -> e.toActivityType() == null || isOpenAct(player, e.toActivityType()))
                .map(e -> (BaseKFTask) KFPlayerTaskCache.getKFPlayerTaskFromDB(e, player.getId()))
                .collect(Collectors.toMap(BaseKFTask::getIntType, Function.identity()));
    }

    public boolean isOpenAct(Player player, ActivityType activityType) {
        if (activityBiz.checkActivityIsOpen(player, activityType)) {
            if (activityType == ActivityType.KfExpeditionActivity) {
                int week = DateUtil.getCsWeek();
                return week == GameConstants.KfExpeditionWeek || week == GameConstants.KfExpeditionWeek + 1;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取跨服mark
     *
     * @param player
     * @param type
     * @return
     */
    public String getKfTaskMark(Player player, int type) {
        KFTaskType kfTaskType = KFTaskType.getKFTaskType(type);
        if (kfTaskType == null || kfTaskType.toActivityType() == null) {
            return null;//每天一个
        }
        AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(kfTaskType.toActivityType());
        if (activity == null || !activity.isOpen() || activity.isCloseForPlayer(player)) {
            return null;
        }
        if (kfTaskType.toActivityType() == ActivityType.KfExpeditionActivity) {
            if (DateUtil.getCsWeek() > GameConstants.KfExpeditionWeek + 1) {
                return null;//清理
            }
            //如果是跨服远征
            return TimeUtils.formatSimpeTime2(DateUtil.beginOfWeek(new Date()));
        }
        return activity.getId();
    }


    /**
     * 每日登陆检查
     *
     * @param player
     */
    public void doCheckPlayerKFTask(Player player) {
        Map<Integer, KFTaskItem> taskMap = player.playerKfTask().getTaskMap();
        for (int type : taskMap.keySet()) {
            String mark = getKfTaskMark(player, type);
            KFTaskItem taskItem = taskMap.get(type);
            if (StrUtil.isEmpty(mark) || !StrUtil.equals(taskItem.getMark(), mark)) {
                player.playerKfTask().removeKFTaskItem(type);
            }
        }
    }


    /**
     * 获取跨服任务领取数据
     *
     * @param player
     * @param taskType
     * @return
     */
    public KFTaskItem getPlayerKfTaskItem(Player player, KFTaskType taskType) {
        KFTaskItem kFTaskItem = player.playerKfTask().getKFTaskItem(taskType);
        if (kFTaskItem == null) {
            kFTaskItem = new KFTaskItem();
            kFTaskItem.setMark(getKfTaskMark(player, taskType.getType()));
            player.playerKfTask().addKFTaskItem(taskType, kFTaskItem);
        }
        return kFTaskItem;
    }
}
