package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.action.guild.task.GuildTaskType;
import com.hm.config.excel.temlate.GuildEmployTemplate;
import com.hm.util.StringUtil;

import java.util.Map;

@FileConfig("guild_employ")
public class GuildTaskTypeTemplate extends GuildEmployTemplate{
	private int[] kills = new int[2];
	private Map<Integer,Double> shopMap = Maps.newHashMap();
	private int[] citys = new int[3];
	private double resRate;
	
	public void init() {
		if(getId() == GuildTaskType.Task1.getId()) {
			this.kills = StringUtil.strToIntArray(getPoints_count(), ":");
		}
		if(getId() == GuildTaskType.Task2.getId()) {
			this.shopMap = StringUtil.strToIntDoubleMap(getPoints_count(), ",", ":");
		}
		if(getId() == GuildTaskType.Task3.getId()) {
			this.citys = StringUtil.strToIntArray(getPoints_count(), ":");
		}
		if(getId() == GuildTaskType.Task4.getId()) {
			this.resRate = Double.parseDouble(getPoints_count());
		}
	}
	public int getKillPoint(int index) {
		return this.kills[index];
	}
	public int getCityPoint(int index) {
		return this.citys[index];
	}
	public double getShopRate(int shopId) {
		return this.shopMap.getOrDefault(shopId, 0D);
	}
	public int getResRate(long val) {
		return (int)(val*resRate);
	}
}
