package com.hm.config;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.MissionBoxTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.MathUtils;

import java.util.List;
import java.util.Map;

@Config
public class MissionBoxConfig extends ExcleConfig {
    private Table<Integer, Integer, MissionBoxTemplateImpl> missionMap = HashBasedTable.create();

    @Override
    public void loadConfig() {
		Table<Integer, Integer, MissionBoxTemplateImpl> missionMap = HashBasedTable.create();

    	List<MissionBoxTemplateImpl> templateList = json2List(MissionBoxTemplateImpl.class);
		for (MissionBoxTemplateImpl template : templateList) {
			for (int i = template.getMission_min(); i <= template.getMission_max(); i++) {
				missionMap.put(i,template.getReward_group(),template);
			}
		}
		this.missionMap = missionMap;
    }

	/**
	 * @author siyunlong
	 * @version V1.0
	 * @Description: 获取当前时间可以领取的所有道具
	 * @date 2024/3/21 9:58
	 */
	public Map<Integer,Double> getMissionBoxItems(Player player) {
		return getMissionBoxItems(player,player.playerMissionBox().getRewardBoxTime());
	}

	public Map<Integer,Double> getMissionBoxItems(Player player,long diffTime) {
		Map<Integer,MissionBoxTemplateImpl> templateMap = this.missionMap.row(player.playerMission().getFbId());

		Map<Integer,Double> addMap = Maps.newHashMap();
		Map<Integer,Double> playerMap = player.playerMissionBox().getItemMap();

		for (MissionBoxTemplateImpl template : templateMap.values()) {
			addMap.put(template.getReward_group(),template.calItemCount(diffTime));
		}

		playerMap.forEach((key, value) -> addMap.merge(key, value, Double::sum));

		return addMap;
	}

	// 离线宝箱
	public Map<Integer,Double> getPlayerOffLineBoxItems(Player player) {
		Map<Integer,MissionBoxTemplateImpl> templateMap = this.missionMap.row(player.playerMission().getFbId());
		Map<Integer,Double> addMap = Maps.newHashMap();
		long offLineBoxTime = player.playerMissionBox().getOffLineBoxTime();
		for (MissionBoxTemplateImpl template : templateMap.values()) {
			// 速度提升 2/3
			addMap.put(template.getReward_group(), MathUtils.div(5 * template.calItemCount(offLineBoxTime), 3, 2));
		}
		return addMap;
	}


	public List<Items> buildItems(Player player,Map<Integer,Double> itemMap) {
		Map<Integer,MissionBoxTemplateImpl> templateMap = this.missionMap.row(player.playerMission().getFbId());

		List<Items> itemsList = Lists.newArrayList();
		for (Map.Entry<Integer, Double> entry : itemMap.entrySet()) {
			MissionBoxTemplateImpl template = templateMap.get(entry.getKey());
			if(template == null) {
				continue;
			}
			double c = entry.getValue();
			if(c > 1) {
				itemsList.add(new Items(template.getItemId(),(long)c,template.getType()));
			}
		}
		return itemsList;
	}

	//只保留小数点
	public static Map<Integer,Double> lastFloat(Map<Integer,Double> itemMap) {
		for (Map.Entry<Integer, Double> entry : itemMap.entrySet()) {
			int key = entry.getKey();
			double c = entry.getValue();
			if(c > 1) {
				itemMap.put(key, NumberUtil.sub(c,(int)c));
			}
		}
		return itemMap;
	}
}
