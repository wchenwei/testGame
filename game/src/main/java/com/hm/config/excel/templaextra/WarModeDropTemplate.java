package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WarDropTemplate;
import com.hm.enums.ActivityType;
import com.hm.enums.DropModeType;
import com.hm.enums.ModeStatisticsType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.List;

@FileConfig("war_drop")
public class WarModeDropTemplate extends WarDropTemplate{
	private WeightMeta<Items> weightMeta;
	private int minLv;
	private int maxLv;
	
	private ActivityType activityType;
	private int version;
	
	public void init() {
		this.weightMeta = RandomUtils.buildItemWeightMeta(getDrop_item());
		this.minLv = Integer.parseInt(getLevel().split(",")[0]);
		this.maxLv = Integer.parseInt(getLevel().split(",")[1]);
		loadActivity();
	}

	public List<Items> dropItemList(BasePlayer player) {
		if(getLimit_num() > 0 
				&& player.getPlayerStatistics().getTodayModeStatistics(ModeStatisticsType.DropMode, getId()) >= getLimit_num()) {
			return null;//判断今日获取次数
		}
		int lv = player.playerLevel().getLv();
		if(lv >= minLv && lv <= maxLv && RandomUtils.randomIsRate(getRate())) {
			if(getLimit_num() > 0) {
				//添加次数
				player.getPlayerStatistics().addTodayModeStatistics(ModeStatisticsType.DropMode, getId());
			}
			Items dropItem = weightMeta.random();
			if(dropItem != null) {
				return Lists.newArrayList(dropItem);
			}
		}
		return null;
	}
	
	public void loadActivity() {
		if(getDrop_case() == DropModeType.ActivityMode.getType()) {
			if(StrUtil.isEmpty(getCase_id())) {
				return;
			}
			String[] infos = getCase_id().split(":");
			this.activityType = ActivityType.getActivityType(Integer.parseInt(infos[0]));
			if(infos.length > 1) {
				this.version = Integer.parseInt(infos[1]);
			}
		}
	}
	
	public boolean isFitActivity(ActivityType tempType,int version) {
//		try {
//			if(StrUtil.isEmpty(getCase_id())) {
//				return false;
//			}
//			String[] infos = getCase_id().split(":");
//			int activityId = Integer.parseInt(infos[0]);
//			if(activityType.getType() != activityId) {
//				return false;
//			}
//			if(infos.length > 1) {
//				return version == Integer.parseInt(infos[1]);
//			}
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if(activityType == tempType) {
			if(this.version > 0) {
				return this.version == version;
			}
			return true;
		}
		return false;
	}

	public ActivityType getActivityType() {
		return activityType;
	}
	
}
