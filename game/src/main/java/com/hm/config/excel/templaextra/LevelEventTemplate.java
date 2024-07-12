package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.levelEvent.vo.LevelEventLimit;
import com.hm.config.excel.temlate.MissionLevelTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@FileConfig("mission_level")
public class LevelEventTemplate extends MissionLevelTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Integer> nextIds = Lists.newArrayList();
	private List<LevelEventLimit> limits = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.nextIds = StringUtil.splitStr2IntegerList(this.getNext_mission(), ",");
		if(StringUtils.isNotBlank(this.getMission_limit())){
			for(String s:this.getMission_limit().split(",")){
				String[] ss = s.split(":");
				int id = Integer.parseInt(ss[0]);
				int type = Integer.parseInt(ss[1]);
				int num = Integer.parseInt(ss[2]);
				limits.add(new LevelEventLimit(id, type, num));
			}
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
	
}
