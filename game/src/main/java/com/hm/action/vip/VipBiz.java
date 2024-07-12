package com.hm.action.vip;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.temlate.VipTemplate;
import com.hm.config.excel.templaextra.ActivityArmyFeteTemplate;
import com.hm.enums.VipPowType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class VipBiz {
	@Resource
	private ShopConfig shopConfig;
	@Resource
	private ActivityConfig activityConfig;

	/**
	 * 获取vip特权数值
	 * @param player
	 * @param powType
	 * @return
	 */
	public int getVipPow(BasePlayer player,VipPowType vipPowType) {
		VipTemplate template = shopConfig.getVipTemplate(player.getPlayerVipInfo().getVipLv());
		return template.getVipPow(vipPowType);
	}
	public int getVipPow(int vipLv,VipPowType vipPowType){
		VipTemplate template = shopConfig.getVipTemplate(vipLv);
		return template.getVipPow(vipPowType);
	}

	/**
	 * 每日补给奖励
	 * @param player
	 * @return
	 */
	public List<Items> getArmyFeteRewards(BasePlayer player) {
		ActivityArmyFeteTemplate template = activityConfig.getFitArmyFeteTemplate(player.playerLevel().getLv());
		if(template == null) {
			return null;
		}
		// 增加vip特权
		// 32:60,33:50,34:1
		int rate = getVipPow(player, VipPowType.ArmyFete);
		if (rate > 0) {
			return ItemUtils.calItemExtraAdd(template.getRewards(), MathUtils.div(rate, 100));
		}
		return template.getRewards();
	}

}
