package com.hm.config.excel.templaextra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.levelEvent.vo.LevelEventLimit;
import com.hm.config.excel.temlate.MissionEventTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.RandomRatio;
import com.hm.util.ItemUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@FileConfig("mission_event")
public class LevelEventNewTemplate extends MissionEventTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Integer> nextIds = Lists.newArrayList();
	private List<LevelEventLimit> limits = Lists.newArrayList();
	private RandomRatio buffRatio;
	public void init(){
		this.rewards = ItemUtils.str2ItemImmutableList(this.getReward(), ",", ":");
		this.nextIds = ImmutableList.copyOf(StringUtil.splitStr2IntegerList(this.getLink_city(), ","));
		List<LevelEventLimit> limits = Lists.newArrayList();
		if(StringUtils.isNotBlank(this.getMission_limit())){
			for(String s:this.getMission_limit().split(",")){
				String[] ss = s.split(":");
				int id = Integer.parseInt(ss[0]);
				int type = Integer.parseInt(ss[1]);
				int num = Integer.parseInt(ss[2]);
				limits.add(new LevelEventLimit(id, type, num));
			}
			this.limits = ImmutableList.copyOf(limits);
		}
		if (StringUtils.isNotBlank(this.getBuff_library())) {
			this.buffRatio = new RandomRatio(this.getBuff_library());
		}
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public List<Integer> getNextIds() {
		return nextIds;
	}
	
	public boolean isNextId(int id){
		return nextIds.contains(id);
	}
	public List<LevelEventLimit> getLimits(){
		return limits;
	}

	public int randomBuffGroupId() {
		if (buffRatio == null) {
			return 0;
		}
		return buffRatio.randomRatio();
	}
	
}
