package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.temlate.MissionTreasureTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * ClassName: MissionTreasureTemplateImpl. <br/>  
 * Function: 夺宝奇兵. <br/>  
 * date: 2019年4月3日 下午7:04:19 <br/>  
 * @author zxj  
 * @version
 */
@FileConfig("mission_treasure")
public class MissionTreasureTemplateImpl extends MissionTreasureTemplate{
	
	List<Integer> npcList = Lists.newArrayList();
	Map<Integer, List<Items>> rewardMap = Maps.newHashMap();
	List<Items> passReard = Lists.newArrayList();
	List<Items> sweepReward = Lists.newArrayList();
	List<Items> sweepCost = Lists.newArrayList();
	List<Items> rewardEnemy = Lists.newArrayList();
	
	public void init(){
		this.npcList = StringUtil.splitStr2IntegerList(this.getEnemy_config(), ",");
		this.sweepReward = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.passReard = ItemUtils.str2ItemList(this.getFirst_reward(), ",", ":");
		String[] rewardStr = this.getEnemy_reward().split(";");
		for(int i=0; i<npcList.size(); i++) {
			List<Items> tempItems = ItemUtils.str2ItemList(rewardStr[i], ",", ":");
			rewardMap.put(npcList.get(i), tempItems);
			rewardEnemy.addAll(tempItems);
		}
		this.sweepCost = ItemUtils.str2ItemList(this.getCost(), ",", ":");
	}

	public List<Items> getCheckReward() {
		List<Items> items = Lists.newArrayList();
		items.addAll(passReard.stream().map(e -> e.clone()).collect(Collectors.toList()));
		items.addAll(rewardEnemy.stream().map(e->e.clone()).collect(Collectors.toList()));
		return items;
	}
	/**
	 * 校验失败的奖励
	 * @param rewards
	 * @return
	 */
	public List<Items> getFailReward(List<Items> rewards) {
		List<Items> items = Lists.newArrayList();
		Map<Integer, Items> itemsMap = Maps.newHashMap();
		//合并奖励信息
		ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
		List<Items> tempReward = itemBiz.createItemList(rewardEnemy);
		//将奖励转化成map
		tempReward.forEach(i->{
			itemsMap.put(i.getId(), i.clone());
		});
		//判断奖励是否正常
		rewards.forEach(e->{
			if(itemsMap.containsKey(e.getId()) && e.getCount()<=itemsMap.get(e.getId()).getCount()) {
				items.add(e);
			}
		});
		return items;
	}
	public List<Items> getSweepReward() {
		return sweepReward.stream().map(e->e.clone()).collect(Collectors.toList());
	}
	public List<Items> getSweepCost() {
		return sweepCost;
	}
}







