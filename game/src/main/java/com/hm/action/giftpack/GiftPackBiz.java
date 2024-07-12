package com.hm.action.giftpack;

import com.hm.config.excel.GiftPackageConfig;
import com.hm.config.excel.templaextra.ActiveGiftDayTemplateImpl;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.RechargeType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.item.Items;
import com.hm.model.player.PYGiftGroup;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家礼包处理器
 * @date 2024/5/14 17:51
 */
@Slf4j
@Biz
public class GiftPackBiz extends NormalBroadcastAdapter {
	@Resource
	private GiftPackageConfig giftPackageConfig;


	/**
	 * 处理玩家购买日周月礼包
	 * @param player
	 * @param itemList
	 * @param rechargePriceNewTemplate
	 */
	public void doRechargeActivity(Player player, List<Items> itemList, RechargeGiftTempImpl rechargeGiftTemp) {
		ActiveGiftDayTemplateImpl template = giftPackageConfig.getTemplateByRechargeId(rechargeGiftTemp.getId());
		if(template == null) {
			log.error(player.getId()+"购买礼包出错找不到礼包:"+rechargeGiftTemp.getId());
			return;
		}
		//添加奖励
		itemList.addAll(template.getItemList());

		PYGiftGroup giftGroup = player.playerGiftPack().getGiftGroup(template.getGift_type());
		giftGroup.addGift(template.getId());
		player.playerGiftPack().SetChanged();
	}

	public void addGiftPack(Player player, RechargeGiftTempImpl giftTemplate) {
		if(giftTemplate.getType() == RechargeType.NewPlayerGift.getType()) {
			player.playerGiftPack().addNewPlayerGift(giftTemplate.getId());//新手礼包
		}
	}

}










