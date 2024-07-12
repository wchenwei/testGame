package com.hm.model.activity.threedays;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.ActivityTaskConfig;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.ActiveThreeDayPointTemplateImpl;
import com.hm.config.excel.templaextra.ActivityShopTemplate;
import com.hm.config.excel.templaextra.ActivityTaskTemplate;
import com.hm.enums.ActivityType;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.IActivityShopValue;
import com.hm.model.activity.IActivityTaskValue;
import com.hm.model.activity.PlayerActivityIdListValue;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerThreeDayValue extends PlayerActivityIdListValue implements IActivityTaskValue, IActivityShopValue {
	private Map<Integer, Integer> buyMap = Maps.newHashMap(); //购买的商品id
	private List<Integer> receiveIds = Lists.newArrayList();// 已领取的积分奖励id
	private int score;

	@Override
	public boolean doActivityClose(BasePlayer player) {
		AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(ActivityType.ThreeDay);
		if(activity==null || activity.isCloseForPlayer(player)){
			ActivityConfig activityConfig = SpringUtil.getBean(ActivityConfig.class);
			List<Items> rewards = Lists.newArrayList();
			for (ActiveThreeDayPointTemplateImpl pointTemplate : activityConfig.getPlayerCanRewardThreeDayPointTemplates(score, receiveIds)) {
				rewards.addAll(pointTemplate.getRewards());
			}
			//发邮件
			if(!rewards.isEmpty()) {
				MailBiz mailBiz = SpringUtil.getBean(MailBiz.class);
				mailBiz.sendSysMail(player, MailConfigEnum.ThreeDayReward, ItemUtils.mergeItemList(rewards));
				player.getPlayerActivity().SetChanged();
			}
			return true;
		}
		return false;
	}

	public int getScore(){
		return score;
	}


	public List<Integer> getReceiveIds() {
		return receiveIds;
	}

	public void receive(int id) {
		this.receiveIds.add(id);
	}

	@Override
	public boolean isCanBuy(Player player, int goodsId, int num) {
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		ActivityShopTemplate template = shopConfig.getActiviyShopTemplate(goodsId);
		if(Objects.isNull(template)){
			return false;
		}
		int limit = template.getLimit();
		if(limit<0){
			return true;
		}
		return this.buyMap.getOrDefault(goodsId, 0)+num <= limit;
	}

	@Override
	public void buy(Player player, int goodsId, int num) {
		this.buyMap.put(goodsId, buyMap.getOrDefault(goodsId, 0)+ num);
	}

	@Override
	public void taskFinish(Player player, int taskId) {
		ActivityTaskConfig activityTaskConfig = SpringUtil.getBean(ActivityTaskConfig.class);
		ActivityTaskTemplate template = activityTaskConfig.getTaskTemplate(taskId);
		this.score += template.getTask_point();
	}

}
