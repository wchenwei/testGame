package com.hm.model.activity.kfSeasonShop;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.KfConfig;
import com.hm.config.excel.templaextra.KfSeasonShopTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.model.activity.PlayerActivityValue;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;

import java.util.List;
import java.util.Map;

public class KfSeasonShopValue extends PlayerActivityValue{
	//购买次数
	private Map<Integer,Integer> buyMap = Maps.newConcurrentMap();
	
	@Override
	public boolean checkCanReward(BasePlayer player, int id) {
		return false;
	}

	@Override
	public void setRewardState(BasePlayer player, int id) {

	}

	public boolean isCanBuy(Player player, int goodsId) {
		KfSeasonShopActivity activity = (KfSeasonShopActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFSeasonShop);
		KfConfig kfConfig = SpringUtil.getBean(KfConfig.class);
		KfSeasonShopTemplateImpl template = kfConfig.getShopTemplate(goodsId);
		if(template==null||template.getType_sub()!=2) {//2為正常商品 1为计费点
			return false;
		}
		//通过goodsId获取配置
		int version = template.getType();
		int limit = template.getLimit();
		if(!template.isFit(activity.getServerLv(),activity.getVersion())) {
			return false;
		}
		return limit==0||getBuyCount(goodsId)<limit;
	}
	
	public int getBuyCount(int goodsId) {
		return buyMap.getOrDefault(goodsId, 0);
	}

	public void buy(Player player, int goodsId) {
		this.buyMap.put(goodsId, getBuyCount(goodsId)+1);
	}

	public List<Items> getCost(Player player, int id) {
		return null;
	}

}
