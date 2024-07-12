package com.hm.action.activity.biz;

import com.google.common.collect.Lists;
import com.hm.config.ActivityTaskConfig;
import com.hm.config.excel.ActivityConfig;
import com.hm.enums.ActivityType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.activity.PlayerActivityValue;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class ActivityReceiveBiz {
    @Resource
    private ActivityConfig activityConfig;

    @Resource
    private ActivityTaskBiz activityTaskBiz;
    @Resource
    private ActivityTaskConfig activityTaskConfig;

    public List<Items> getReceiveAllReward(Player player, ActivityType activityType){
        PlayerActivityValue value = player.getPlayerActivity().getPlayerActivityValue(activityType);
        switch (activityType){
            // case Circle:
            //     return getCircleReward(player, (PlayerCircleValue) value);
            default:
                return Lists.newArrayList();
        }
    }




}
