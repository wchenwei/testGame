package com.hm.action.activity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.ActiveThreeDayPointTemplateImpl;
import com.hm.config.excel.templaextra.ActivityThreeDayTemplate;
import com.hm.enums.ActiveThreeDayTaskType;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.activity.threedays.PlayerThreeDayValue;
import com.hm.model.activity.threedays.ThreeDayActivity;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Action
public class ActivitySimpleAction extends AbstractPlayerAction {

	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private ItemBiz itemBiz;
    @Resource
    private ActivityConfig activityConfig;
	/**
	 * 领取新7日活动积分奖励
	 */
	@MsgMethod(MessageComm.C2S_Activity_ThreeDay_Receive)
    public void receiveThreeDayReward(Player player, JsonMsg msg) {
		//==================判断活动是否开启===================================
		ThreeDayActivity activity = (ThreeDayActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.ThreeDay);
		if(activity == null || activity.isCloseForPlayer(player)) {
			player.sendErrorMsg(SysConstant.Activity_Close);
            return;
		}
		PlayerThreeDayValue value = (PlayerThreeDayValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.ThreeDay);

        List<ActiveThreeDayPointTemplateImpl> templates = activityConfig.getPlayerCanRewardThreeDayPointTemplates(value.getScore(), value.getReceiveIds());

        if (CollUtil.isEmpty(templates)){
            return;
        }
        List<Items> rewards = Lists.newArrayList();
        for (ActiveThreeDayPointTemplateImpl template : templates) {
            rewards.addAll(template.getRewards());
            value.receive(template.getId());
        }
        rewards = ItemUtils.mergeItemList(rewards);
        itemBiz.addItem(player, rewards, LogType.ActivityReward.value(ActivityType.ThreeDay.getType()));
		player.getPlayerActivity().SetChanged();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Activity_ThreeDay_Receive, rewards);
	}

    /**
     * 领取新7日活动任务奖励或购买商品
     */
    @MsgMethod(MessageComm.C2S_Activity_ThreeDay_Task_Reward)
    public void threeDayTaskReward(Player player, JsonMsg msg) {
        int tid = msg.getInt("tid");
        ThreeDayActivity activity = (ThreeDayActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.ThreeDay);
        if(activity == null || activity.isCloseForPlayer(player)) {
            player.sendErrorMsg(SysConstant.Activity_Close);
            return;
        }
        PlayerThreeDayValue value = (PlayerThreeDayValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.ThreeDay);

        ActivityThreeDayTemplate threeDayTemplate = activityConfig.getActivityThreeDayTemplate(tid);
        if (threeDayTemplate == null){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        int day = (int) DateUtil.betweenDay(new Date(value.getOpenTime().getStartTime()),new Date(),true)+1;
        if(threeDayTemplate.getDay() > day){
            player.sendErrorMsg(SysConstant.Activity_ConditionsNot);
            return;
        }
        ActiveThreeDayTaskType dayTaskType = ActiveThreeDayTaskType.getThreeDayTaskType(threeDayTemplate.getType());
        if (dayTaskType == null){
            player.sendErrorMsg(SysConstant.Activity_ConditionsNot);
            return;
        }
        dayTaskType.doTask(player, msg, threeDayTemplate.getValue());
        player.sendMsg(MessageComm.S2C_Activity_ThreeDay_Task_Reward);
    }
}
