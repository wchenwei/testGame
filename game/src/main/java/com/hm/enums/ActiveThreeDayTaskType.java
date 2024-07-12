package com.hm.enums;

import com.hm.action.activity.ActivityAction;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.Player;

import java.util.Arrays;

/**
 * @Author chenwei
 * @Date 2024/6/29
 * @Description:
 */
public enum ActiveThreeDayTaskType {

    Task(0,"活动任务"){
        @Override
        protected void doTask(ActivityAction activityAction, Player player, JsonMsg msg) {
            activityAction.taskReceive(player, msg);
        }
    },
    Shop(1,"活动商店"){
        @Override
        protected void doTask(ActivityAction activityAction, Player player, JsonMsg msg) {
            activityAction.shopBuy(player, msg);
        }
    }
    ;
    private int type;
    private String desc;

    ActiveThreeDayTaskType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void doTask(Player player, JsonMsg msg, Integer id){
        ActivityAction activityAction = SpringUtil.getBean(ActivityAction.class);
        msg.addProperty("id", id);
        doTask(activityAction, player, msg);
    }

    protected abstract void doTask(ActivityAction activityAction, Player player, JsonMsg msg);

    public static ActiveThreeDayTaskType getThreeDayTaskType(int type){
        return Arrays.stream(ActiveThreeDayTaskType.values()).filter(e -> e.getType() == type).findFirst().orElse(null);
    }

}
