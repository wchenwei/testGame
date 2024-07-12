package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.action.giftpack.bean.Pfund;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveRGiftImpl;
import com.hm.config.excel.templaextra.ActivityGrowUpTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.*;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;

/**
 * @Description: 掉落配置
 * @author siyunlong  
 * @date 2018年1月9日 下午4:31:09 
 * @version V1.0
 */
@Config
public class RechargeActivityConfig extends ExcleConfig {
	private Map<Integer, ActiveRGiftImpl> rechargeGift = Maps.newConcurrentMap();

	private ArrayListMultimap<Integer, ActivityGrowUpTemplate> growUpMap = ArrayListMultimap.create();


	@Override
	public void loadConfig() {
		//先排序
		List<ActivityGrowUpTemplate> templates = json2List(ActivityGrowUpTemplate.class)
				.stream().sorted(Comparator.comparingInt(ActivityGrowUpTemplate::getLevel)).collect(Collectors.toList());
		ArrayListMultimap<Integer, ActivityGrowUpTemplate> map = ArrayListMultimap.create();
		for(ActivityGrowUpTemplate ActivityGrowUpTemplate:templates) {
			map.put(ActivityGrowUpTemplate.getType(),ActivityGrowUpTemplate);
		}
		this.growUpMap = map;

		//充值送豪礼
		Map<Integer, ActiveRGiftImpl> rechargeGiftMap = Maps.newConcurrentMap();
		for(ActiveRGiftImpl rechargeGift:json2List(ActiveRGiftImpl.class)) {
			rechargeGift.init();
			rechargeGiftMap.put(rechargeGift.getId(),rechargeGift);
		}
		this.rechargeGift = ImmutableMap.copyOf(rechargeGiftMap);
	}


	public List<Items> getGrowUpItemList(Player player, int type) {
		int plevel = type == 1?player.playerMission().getFbId():player.playerCommander().getMilitaryLv();
		Pfund pfund = player.playerGiftPack().getPfund(type);
		List<Items> itemsList = Lists.newArrayList();
		boolean isBuy = pfund.isBuyRecharge();

		int[] rewardLv = pfund.getVals();//领取到等级
		for (ActivityGrowUpTemplate template : growUpMap.get(type)) {
			if(template.getLevel() > plevel) {
				break;
			}
			//先算普通奖励
			if(rewardLv[0] < template.getLevel()) {
				itemsList.addAll(template.getRewards());
				rewardLv[0] = template.getLevel();
			}
			//计算需要购买的奖励
			if(isBuy && rewardLv[1] < template.getLevel()) {
				itemsList.addAll(template.getSuperReward());
				rewardLv[1] = template.getLevel();
			}
		}
		if(CollUtil.isEmpty(itemsList)) {
			return itemsList;
		}
		player.playerGiftPack().SetChanged();
		return ItemUtils.mergeItemList(itemsList);
	}


	//获取充值送豪礼配置文件
	public ActiveRGiftImpl getRechargeGiftTemplate(int id) {
		return rechargeGift.get(id);
	}
	//寻找下一个符合条件的充值豪礼活动信息
	public ActiveRGiftImpl getNextRechargeGift(int lv, int rechargeId) {
		ActiveRGiftImpl firstGift = null;
		ActiveRGiftImpl secondGift = null;
		
		for(ActiveRGiftImpl tempGift : rechargeGift.values()) {
			if(tempGift.getId()>rechargeId && tempGift.getStartLv()<=lv 
				&& tempGift.getEndLv()>=lv && null==firstGift) {
				firstGift= tempGift;
			}else if(tempGift.getId()<=rechargeId && tempGift.getStartLv()<=lv 
				&& tempGift.getEndLv()>=lv && null==secondGift) {
				secondGift = tempGift;
			}
		}
		if(null!=firstGift) {
			return firstGift;
		}
		return secondGift;
	}
}








