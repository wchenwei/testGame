package com.hm.action.mission.biz;

import com.google.common.collect.Lists;
import com.hm.annotation.Broadcast;
import com.hm.config.GameConstants;
import com.hm.config.MissionBoxConfig;
import com.hm.config.MissionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.enums.CommonValueType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 挂机宝箱奖励
 * @date 2024/3/27 9:24
 */
@Service
public class PlayerMissionBoxBiz extends NormalBroadcastAdapter {
    @Resource
    private MissionConfig missionConfig;
    @Resource
    private MissionBoxConfig missionBoxConfig;
    @Resource
    private CommValueConfig commValueConfig;


    public List<Items> buildBoxItemList(Player player, Map<Integer,Double> itemMap) {
        //关卡基础奖励
        MissionExtraTemplate template = missionConfig.getMission(player.playerMission().getFbId());
        List<Items> baseList = template.getBaseItemList();//没小时
        long diffTime = player.playerMissionBox().getRewardBoxTime();
        double rate = MathUtils.div(diffTime, GameConstants.HOUR,2);
        List<Items> itemsList = ItemUtils.calItemRateReward(baseList,rate);

        //额外奖励
        itemsList.addAll(missionBoxConfig.buildItems(player,itemMap));

        return itemsList;
    }

    // 离线24小时的累计奖励
    public List<Items> getOffLineBoxItemList(Player player) {
        long diffTime = player.playerMissionBox().getOffLineBoxTime();
        if (diffTime <= 0){
            return Lists.newArrayList();
        }
        Map<Integer, Double> itemMap = missionBoxConfig.getPlayerOffLineBoxItems(player);
        //关卡基础奖励
        MissionExtraTemplate template = missionConfig.getMission(player.playerMission().getFbId());
        List<Items> baseList = template.getBaseItemList();//每小时
        // 速度提升 2/3
        double rate = MathUtils.div(diffTime * 5, GameConstants.HOUR * 3,2);
        List<Items> itemsList = ItemUtils.calItemRateReward(baseList,rate);
        //额外奖励
        itemsList.addAll(missionBoxConfig.buildItems(player,itemMap));

        return itemsList;
    }

    /**
     * @author siyunlong
     * @version V1.0
     * @Description: 获取挂机奖励
     * @date 2024/3/27 9:27
     */
    public List<Items> calBoxItemList(Player player, long second) {
        Map<Integer,Double> itemMap= missionBoxConfig.getMissionBoxItems(player,second*GameConstants.SECOND);
        return buildBoxItemList(player,itemMap);
    }



    @Broadcast(ObservableEnum.PlayerLoginSuc)
    public void doPlayerLogin(ObservableEnum observableEnum, Player player, Object... argv){
        Date offLineDate = player.playerBaseInfo().getLastOffLineDate();
        if (offLineDate != null){
            long diffTime = System.currentTimeMillis() - offLineDate.getTime();// 离线累计时间
            int minuteTime = commValueConfig.getCommValue(CommonValueType.OffLineBoxStartTime);
            long minTime = minuteTime * GameConstants.MINUTE;//开始累积时间
            if (diffTime > minTime){
                player.playerMissionBox().addOffLineTime(diffTime - minTime);
            }
        }

    }
}
