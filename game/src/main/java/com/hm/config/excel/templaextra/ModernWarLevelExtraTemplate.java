package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.ModernWarLevelTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.model.weight.RandomRatio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@FileConfig("modern_war_level")
public class ModernWarLevelExtraTemplate extends ModernWarLevelTemplate{
	private RandomRatio eventNumRatio;
	private RandomRatio eventRatio;
	@SuppressWarnings("unchecked")
	private List<Drop>[] drops = new ArrayList[7];
	
	public void init(){
		this.eventNumRatio = new RandomRatio(this.getEvent_num());
		this.eventRatio = new RandomRatio(this.getEvent());
		drops[0] = StringUtil.splitStr2StrList(this.getReward1(), ";").stream().map(t -> new Drop(t)).collect(Collectors.toList());
		drops[1] = StringUtil.splitStr2StrList(this.getReward2(), ";").stream().map(t -> new Drop(t)).collect(Collectors.toList());
		drops[2] = StringUtil.splitStr2StrList(this.getReward3(), ";").stream().map(t -> new Drop(t)).collect(Collectors.toList());
		drops[3] = StringUtil.splitStr2StrList(this.getReward4(), ";").stream().map(t -> new Drop(t)).collect(Collectors.toList());
		drops[4] = StringUtil.splitStr2StrList(this.getReward5(), ";").stream().map(t -> new Drop(t)).collect(Collectors.toList());
		drops[5] = StringUtil.splitStr2StrList(this.getReward6(), ";").stream().map(t -> new Drop(t)).collect(Collectors.toList());
		drops[6] = StringUtil.splitStr2StrList(this.getReward7(), ";").stream().map(t -> new Drop(t)).collect(Collectors.toList());
	}
	
	public int randomEventNum(){
		return eventNumRatio.randomRatio();
	}
	
	public int randomEventId(){
		return eventRatio.randomRatio();
	}
	
	public List<Items> getRewards(){
		int week = DateUtil.getCsWeek();
		return getRewards(this.drops[week-1]);
	}
	
	public List<Items> getRewards(List<Drop> drops){
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:drops){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}
}
