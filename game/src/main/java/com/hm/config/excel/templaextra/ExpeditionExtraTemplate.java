package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.PlayerLevelConfig;
import com.hm.config.excel.temlate.ExpeditionTemplate;
import com.hm.enums.CommonValueType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@FileConfig("expedition")
public class ExpeditionExtraTemplate extends ExpeditionTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Drop> flops = Lists.newArrayList();
	private List<Drop> boxRewards = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getWar_reward(), ",", ":");
		List<String> dropStr = Lists.newArrayList(this.getTurn_card().split(";"));
		this.flops = dropStr.stream().map(t -> new Drop(t)).collect(Collectors.toList());
		List<String> boxRewards = Lists.newArrayList(this.getBox_reward().split(";"));
		this.boxRewards = boxRewards.stream().map(t -> new Drop(t)).collect(Collectors.toList());
	}
	//获取常规奖励
	public List<Items> getRewards(){
		return rewards.stream().map(t ->t.clone()).collect(Collectors.toList());
	}
	
	//获取翻牌奖励
	public List<Items> getFlopList() {
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:flops){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		return dropItems;
	} 
	//获取宝箱奖励
	public List<Items> getBoxRewards(){
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:boxRewards){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}
	public List<Items> getFlopRewardByType(int type) {
		List<Items> flopRewards = getFlopList(); 
		List<Items> temp = Lists.newArrayList(flopRewards);
		createRandomFlopReward(temp);
		switch(type){
		case 0://不购买翻牌奖励
			return temp.subList(0, 2);
		case 1://购买一次翻牌奖励
			return temp.subList(0, 3);
		}
		return temp;
	}
	
	private void createRandomFlopReward(List<Items> reward){
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int rate = commValueConfig.getCommValue(CommonValueType.ExpeditionFlopRate2);
		//25%概率使用纯随机
		if(MathUtils.random(1, 100)<=rate){
			Collections.shuffle(reward);
			return;
		}
		Items first = reward.get(0);
		Items second = reward.get(1);
		int i = 0;
		while(first.getId()==GameConstants.HonorCertificateId&&second.getId()==GameConstants.HonorCertificateId&&i<5){
			Collections.shuffle(reward);
			i++;
		}
	}
	public List<Drop> getFlops() {
		return flops;
	}
	/**
	 * 根据档次取战力区间
	 * @param grade
	 * @return
	 */
	public String getCombatRange(int grade){
		switch(grade){
			case 3:
				return this.getThree_pass();
			case 6:
				return this.getSix_pass();
			case 9:
				return this.getNine_pass();
			case 12:
				return this.getTwelve_pass();
		}
		return this.getThree_pass();
	}
	/**
	 * 
	 * @param player
	 * @param type 1-最小范围 2-最大范围
	 * @return
	 */
	public long getCombatRange(Player player,int type){
		PlayerLevelConfig playerLevelConfig = SpringUtil.getBean(PlayerLevelConfig.class);
		PlayerLevelExtraTemplate levelTemplate = playerLevelConfig.getPlayerLevelTemplate(player);
		String[] rangStr = getCombatRange(levelTemplate.getExpedition_clean()).split("_");
		double range = type==1?Double.parseDouble(rangStr[0]):Double.parseDouble(rangStr[1]);
		return (long) (player.getPlayerDynamicData().getCombat()*range);
	}

	public boolean isFitLv(int lv) {
		return lv >= getPlayer_lv_down() && lv <= getPlayer_lv_up();
	}
	
}
