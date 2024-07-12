package com.hm.action.activity;

import com.google.common.collect.Lists;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.templaextra.ActivitySevenLoginWelfareTemplate;
import com.hm.enums.ActivityType;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.annotation.Biz;
import com.hm.model.activity.loginWelfare.PlayerLoginWelfareValue;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.List;
@Biz
public class ActivityLoginWelfareBiz{
	@Resource
	private ActivityConfig activityConfig; 
	@Resource
	private MailBiz mailBiz;

	//发送未领取奖励
	public void sendRewardMail(BasePlayer player) {
		PlayerLoginWelfareValue playerActivityValue = (PlayerLoginWelfareValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.LoginWelfare);
		List<Items> sendRewards = Lists.newArrayList();
		for (int id : playerActivityValue.getDayList()) {
			ActivitySevenLoginWelfareTemplate template = activityConfig.getSevenLoginWelfare(id);
			if(template != null) {
				sendRewards.addAll(template.getRewards());
			}
		}
		if(sendRewards.isEmpty()){
			return;
		}
		sendRewards = ItemUtils.mergeItemList(sendRewards);
		mailBiz.sendSysMail(player, MailConfigEnum.SevenDayLogin, sendRewards);
	}
}
