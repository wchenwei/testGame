package com.hm.action.resBack.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.item.ItemBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.GameConstants;
import com.hm.config.MissionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.HonorConfig;
import com.hm.config.excel.ResBackConfig;
import com.hm.config.excel.templaextra.MissionTypeTemplateImpl;
import com.hm.enums.BattleType;
import com.hm.enums.ResBackType;
import com.hm.enums.VipPowType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.battle.BaseBattle;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.player.ResBackMode;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Biz
public class ResBackBiz {
	@Resource
	private MissionConfig missionConfig;
	@Resource
	private HonorConfig honorConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private ResBackConfig resBackConfig;
	@Resource
	private VipBiz vipBiz;

	//创建活动找回模板
	public Map<ResBackType,ResBackMode> createActivityMode(BasePlayer player){
		Map<ResBackType,ResBackMode> map = Maps.newConcurrentMap();
		//昨天是否登录过
		return map;
	}

	public List<Items> getBackReward(Player player, ResBackType resBackType,int type) {
		double rate = type==1?1:GameConstants.ResBackRate_Nomal;
		ResBackMode mode = player.playerResBack().getResBackMode(resBackType);
		//城战荣誉特殊处理,记录的次数是为了记录有多少档奖励没有领取,实际领取次数只有1次
		int count = resBackType != ResBackType.CityWar?mode.getCount():1;
		List<Items> rewards = resBackType.getBackRes(player);
		if(CollectionUtil.isEmpty(rewards)){
			return null;
		}
		rewards = rewards.stream().map(t ->{
			Items item = t.clone();
			item.setCount((int)(t.getCount()*count*rate));
			return item;
		}).collect(Collectors.toList());
		return itemBiz.createItemList(rewards);
	}
	//昨天是否登陆过
	private boolean isYesterdayLogin(BasePlayer player){
		return DateUtil.betweenDay(player.playerBaseInfo().getLastLoginDate(), new Date(), true)<=1;
	}
	public List<Items> getBackReward(Player player,
			List<ResBackType> backTypes, int type) {
		List<Items> rewards = Lists.newArrayList();
		double rate = type==1?1:GameConstants.ResBackRate_Nomal;
		for(ResBackType resBackType:backTypes){
			ResBackMode mode = player.playerResBack().getResBackMode(resBackType);
			//城战荣誉特殊处理,记录的次数是为了记录有多少档奖励没有领取,实际领取次数只有1次
			int count = resBackType != ResBackType.CityWar?mode.getCount():1;
			List<Items> backReward = resBackType.getBackRes(player);
			if(CollectionUtil.isEmpty(backReward)){
				continue;
			}
			backReward = backReward.stream().map(t ->{
				Items item = t.clone();
				item.setCount((int)(t.getCount()*count*rate));
				return item;
			}).collect(Collectors.toList());
			rewards.addAll(backReward);
		}
		return itemBiz.createItemList(rewards);
	}
	public List<Items> getResBackCost(Player player,int resBackType, int type) {
		List<Items> cost = resBackConfig.getResBackCost(resBackType, type);
		ResBackMode mode = player.playerResBack().getResBackMode(ResBackType.getBackType(resBackType));
		if(mode==null){
			return null;
		}
		return cost.stream().map(t ->{
			Items item = t.clone();
			item.setCount(t.getCount()*mode.getCount());
			return item;
		}).collect(Collectors.toList());
	}
	public List<Items> getResBackCost(Player player,
			List<ResBackType> backTypes, int type) {
		List<Items> costs = Lists.newArrayList();
		for(ResBackType backType:backTypes){
			List<Items> cost = resBackConfig.getResBackCost(backType.getType(), type);
			ResBackMode mode = player.playerResBack().getResBackMode(backType);
			if(mode!=null){
				costs.addAll(cost.stream().map(t ->{
					Items item = t.clone();
					item.setCount(t.getCount()*mode.getCount());
					return item;
				}).collect(Collectors.toList()));
			}
		}
		return costs;
	}	
}
