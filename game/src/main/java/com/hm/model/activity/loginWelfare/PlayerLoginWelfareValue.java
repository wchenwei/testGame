package com.hm.model.activity.loginWelfare;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityLoginWelfareBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.templaextra.ActivitySevenLoginWelfareTemplate;
import com.hm.enums.LogType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.activity.PlayerActivityIdListValue;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.CurrencyKind;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class PlayerLoginWelfareValue extends PlayerActivityIdListValue{
	private ArrayList<Integer> dayList = Lists.newArrayList();//可以领取的day

	@Override
	public boolean doActivityClose(BasePlayer player) {
		if(getOpenTime() == null || getOpenTime().isOpen()) {
			return false;
		}
		if(CollUtil.isNotEmpty(this.dayList)) {
			SpringUtil.getBean(ActivityLoginWelfareBiz.class).sendRewardMail(player);
			this.dayList.clear();
			player.getPlayerActivity().SetChanged();
		}
		return true;
	}

	public void addLoginDay(int day) {
		if(this.dayList.contains(day)) {
			return;
		}
		this.dayList.add(day);
	}

	@Override
	public void setRewardState(BasePlayer player, int id) {
		super.setRewardState(player, id);
		this.dayList.remove(Integer.valueOf(id));
	}



	public int getCurLoginDay() {
		int day = (int) DateUtil.betweenDay(new Date(getOpenTime().getStartTime()),new Date(),true)+1;
		return day;
	}

	@Override
	public boolean checkCanReward(BasePlayer player,int id) {
		if(!super.checkCanReward(player, id)) {//已经领取过
			return false;
		}
		ActivityConfig activityConfig = SpringUtil.getBean(ActivityConfig.class);
		ActivitySevenLoginWelfareTemplate template = activityConfig.getSevenLoginWelfare(id);
		if(template == null) {
			return false;
		}
		if(CollUtil.contains(dayList,id)) {
			return true;
		}
		if(id > getCurLoginDay()) {
			return false;
		}
		//购买补发的奖励
		int spend = template.getItemqty();
		PlayerBiz playerBiz = SpringUtil.getBean(PlayerBiz.class);
		if(!playerBiz.checkPlayerCurrency(player, CurrencyKind.Gold,spend)) {
			return false;
		}
		playerBiz.spendPlayerCurrency(player, CurrencyKind.Gold,spend, LogType.Activity.value("loginact"));
		return true;
	}

}
