package com.hm.action.activity;

import com.hm.libcore.annotation.Biz;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.*;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Biz
public class ActivityEffectBiz {

	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private WorldBuildBiz worldBuildBiz;
	
	/**
	 * 计算活动加成
	 * @param player
	 * @return
	 */
	public double getActivityEffectAdd(BasePlayer player,PlayerRewardMode mode){
		double effect = 0;
		if(mode != null) {
			//判断皇城开启buff
			ActivityItemContainer activityItemContainer = ActivityServerContainer.of(player);
			//判断双倍活动产出
			ActivityType doubleActivityType = mode.getDoubleActivityType();
			if(doubleActivityType != null && activityItemContainer.activityIsOpen(doubleActivityType)) {
				effect+=1;
			}
			//判断世界建筑道具加成
			effect += calWorldBuildItemAdd(player, mode);
		}
		return effect;
	}
	
	private double calLvAddRate(BasePlayer player,PlayerRewardMode mode) {
		if(!mode.isExpAdd()) {
			return 0;
		}
		int minLv = commValueConfig.getCommValue(CommonValueType.PlayerExpAddMinLv);
		int playerLv = player.playerLevel().getLv();
		//计算等级加成
		if(playerLv <= minLv) {
			return 0;
		}
		int serverLv = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerStatistics().getMaxPlayerLv();
		if(serverLv <= minLv) {
			return 0;
		}
		return commValueConfig.getLvModeValue(CommonValueType.PlayerExpAdd, serverLv - playerLv);
	}
	public List<Items> calActivityEffectItemsAdd(BasePlayer player,List<Items> itemList,PlayerRewardMode mode,boolean calExpAdd) {
		if(mode==null){
			return itemList;
		}
		double rate = getActivityEffectAdd(player,mode);
		double expAddRate = calExpAdd?calExpExtraAdd(player, mode):0;
		if(rate > 0 || expAddRate > 0) {
			List<Items> cloneItems = itemList.stream().map(e -> e.clone()).collect(Collectors.toList());
			for (Items item : cloneItems) {
				double totalRate = rate;
				if(item.isPlayerExp()) totalRate += expAddRate;
				item.addCount((long)(item.getCount()*totalRate));
			}
			return cloneItems;
		}
		return itemList;
	}
	
	/**
	 * 计算奖励加成
	 * @param player
	 * @param itemList
	 */
	public List<Items> calActivityEffectItemsAdd(BasePlayer player,List<Items> itemList,PlayerRewardMode mode) {
		return calActivityEffectItemsAdd(player, itemList, mode, true);
	}
	
	public Items calActivityEffectItemAdd(BasePlayer player,Items item,PlayerRewardMode mode) {
		double rate = getActivityEffectAdd(player,mode);
		if(item.isPlayerExp()) {
			rate += calExpExtraAdd(player, mode);
		}
		if(rate > 0) {
			Items clone = item.clone();
			clone.addCount((long)(clone.getCount()*rate));
			return clone;
		}
		return item;
	}
	
	
	/**
	 * 计算经验活动加成
	 * @param player
	 * @param exp
	 * @return
	 */
	public long calActivityEffectExpAdd(Player player,long exp,PlayerRewardMode mode) {
		if(mode==null){
			return exp;
		}
		double rate = getActivityEffectAdd(player,mode);
		rate += calExpExtraAdd(player, mode);
		if(rate > 0) {
			return exp + (long)(rate*exp);
		}
		return exp;
	}
	
	/**
	 * 计算额外等级经验加成
	 * 等级经验+buff经验
	 * @param player
	 * @param mode
	 * @return
	 */
	public double calExpExtraAdd(BasePlayer player,PlayerRewardMode mode) {
		return calLvAddRate(player, mode) + calExpBuffAdd(player, mode) + calWorldBuildExpAdd(player, mode);
	}
	
	public double calExpBuffAdd(BasePlayer player,PlayerRewardMode mode) {
		switch (mode) {
		}
		return 0;
	}
	
	/**
	 * 世界建筑经验额外加成
	 * @param player
	 * @param mode
	 * @return
	 */
	public double calWorldBuildExpAdd(BasePlayer player,PlayerRewardMode mode) {
		return 0;
	}
	/**
	 * 世界建筑道具额外加成(包括了经验)
	 * @param player
	 * @param mode
	 * @return
	 */
	public double calWorldBuildItemAdd(BasePlayer player,PlayerRewardMode mode) {
		return 0;
	}
}
