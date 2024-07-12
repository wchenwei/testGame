package com.hm.model.activity;

import com.hm.libcore.msg.JsonMsg;
import com.hm.config.excel.templaextra.ActivityShopTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;

import java.util.List;

public interface IActivityShopValue {
	public boolean isCanBuy(Player player,int goodsId,int num);
	public void buy(Player player,int goodsId,int num);

	/**
	 * @description
	 * 			操作实际消耗物品，计算积分使用
	 * @return void
	 * @date 2020/11/2 17:06
	 */
	default void addIntegral(List<Items> list){}

	/**
	 * @description
	 *
	 * @param player
	 * @param template 配置
	 * @param num  物品购买数量
	 * @param msg 可自行解析个性化参数
	 * @return java.util.List<com.hm.model.item.Items>
	 * @author wyp
	 * @date 2020/10/24 15:05
	 */
	default List<Items> getCost(Player player, ActivityShopTemplate template, int num, JsonMsg msg){
		return ItemUtils.calItemRateReward(template.getCost(), num);
	}
}
