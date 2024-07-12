package com.hm.action.shop;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.shop.vo.ShopRewardVo;
import com.hm.config.MysteryShopConfig;
import com.hm.config.excel.GiftPackageConfig;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.temlate.VipTemplate;
import com.hm.config.excel.templaextra.MysteryShopTemplate;
import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.enums.LogType;
import com.hm.enums.PlayerRewardMode;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.PlayerVipInfo;
import com.hm.model.shop.PlayerShopValue;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

@Action
public class ShopAction extends AbstractPlayerAction{
	@Resource
	private ShopConfig shopConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private PlayerBiz playerBiz;
    @Resource
    private GiftPackageConfig giftPackageConfig;
    @Resource
    private ActivityEffectBiz activityEffectBiz;
    @Resource
    private MysteryShopConfig mysteryShopConfig;
	/**
	 * 购买商店物品
	 */
	@MsgMethod(MessageComm.C2S_Buy_Goods)
    public void buyGoods(Player player, JsonMsg msg) {
		int id = msg.getInt("id");//唯一id
		int num = Math.max(1, msg.getInt("num"));
		ShopItemExtraTemplate template = shopConfig.getShopTemplate(id);
		int shopId = template.getShop_id();
		PlayerShopValue playerShopValue = player.playerShop().getPlayerShop(shopId);
		if(playerShopValue==null||playerShopValue.isTimeOut()){
			//商店不存在
			return;
		}
		//是不是能购买(只能买一次的商品售空)
		if(!playerShopValue.isCanBuy(player, template, num)){
			//已卖完
			return;
		}
		int goodsId = playerShopValue.getGoodsId(id);
		List<Items> cost = playerShopValue.getCost(id,goodsId, num);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Shop.value(goodsId))){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		//设置购买数据
		playerShopValue.setShopBuyData(player, id, num);
		Items reward = playerShopValue.getReward(goodsId,num);
		//计算等级加成
		reward = activityEffectBiz.calActivityEffectItemAdd(player, reward, PlayerRewardMode.ShopBuy);
		//发放奖励
		itemBiz.addItem(player, reward, LogType.Shop.value(goodsId));
		player.notifyObservers(ObservableEnum.ShopBuy, shopId,reward.getId(),reward.getItemType(),reward.getCount(),cost);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Buy_Goods, new ShopRewardVo(id,reward));
	}
	/**
	 * 刷新商店
	 */
	@MsgMethod(MessageComm.C2S_Refresh_Goods)
    public void refreshGoods(Player player, JsonMsg msg) {
		int shopId = msg.getInt("shopId");
		PlayerShopValue playerShopValue = player.playerShop().getPlayerShop(shopId);
		if(playerShopValue==null){
			//商店不存在
			return;
		}
		//是不是能刷新
		if(!playerShopValue.isCanRefersh()){
			//此类型商店不能刷新
			return;
		}
		//刷新消耗
		Items cost = playerShopValue.getRefershCost();
		if(cost!=null&&cost.getCount()>0){
			if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Shop.value(shopId))){
				player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
				return;
			}
		}
		//刷新
		player.playerShop().refreshShop(shopId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Refresh_Goods);
	}
	
	@MsgMethod(MessageComm.C2S_BuyShopVipItem)
    public void buyShopVipItem(Player player, JsonMsg msg) {
		int vipId = msg.getInt("vipId");
		int giftId = msg.getInt("giftId");
		
		PlayerVipInfo playerVipInfo = player.getPlayerVipInfo();
		if(playerVipInfo.getVipLv() < vipId) {
			//vip等级不足
			player.sendErrorMsg(SysConstant.VIP_LV_NOT_ENOUGH);
            return;
		}
		if(!playerVipInfo.isCanBuyVipGift(giftId)) {
			player.sendErrorMsg(SysConstant.Ala_Buy_VipGift);
            return;
		}
		VipTemplate vipTemplate = shopConfig.getVipTemplate(vipId);
		if(!vipTemplate.isCanBuyGift(giftId)) {//vip等级不足
			player.sendErrorMsg(SysConstant.VIP_LV_NOT_ENOUGH);
            return;
		}
		int price = vipTemplate.getGiftPrice(giftId);
		LogType logType = LogType.VipGift.value(vipId);
		if (price > 0 && !playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.SysGold, price, logType)) {
            player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
            return;
        }
		List<Items> itemList = giftPackageConfig.rewardGiftList(giftId);
		if(!itemList.isEmpty()) {
			itemBiz.addItem(player, itemList, logType);
		}
		player.getPlayerVipInfo().addBuyVipGifts(giftId);
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_BuyShopVipItem);
        showMsg.addProperty("itemList", itemList);
        player.sendUserUpdateMsg();
        player.sendMsg(showMsg);
	}
	@MsgMethod(MessageComm.C2S_MysteryShopBuy)
	public void mysteryShopBuy(Player player, JsonMsg msg){
		int id = msg.getInt("id");
		if(!player.playerMysteryShop().isCanBuy(id)){
			return ;
		}
		MysteryShopTemplate template = mysteryShopConfig.getMysteryShop(id);
		if(template==null){
			return;
		}
		List<Items> cost = template.getCost();
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.MysteryShop)){
			return;
		}
		List<Items> rewards = template.getRewards();
		itemBiz.addItem(player, rewards, LogType.MysteryShop);
		player.playerMysteryShop().buy(id);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_MysteryShopBuy, rewards);
	}
}
