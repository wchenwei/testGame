package com.hm.action.bag;

import com.google.common.collect.Lists;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.vip.VipBiz;
import com.hm.chat.InnerChatFacade;
import com.hm.config.excel.DropConfig;
import com.hm.config.excel.GiftPackageConfig;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.log.LogBiz;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
@Slf4j
@Biz
public class BagBiz{
	@Resource
    private LogBiz logBiz;
	@Resource
	private GiftPackageConfig giftPackageConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private DropConfig dropConfig;
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private VipBiz vipBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private InnerChatFacade innerChatFacade;
	
	public boolean spendItem(Player player,int itemId,long spendCount,LogType logType) {
		if(player.playerBag().spendItem(itemId,spendCount)){
			logBiz.spendGoods(player,itemId,spendCount,ItemType.ITEM.getType(),logType);
			return true;
		}
		return false;
	}
	public boolean spendItem(Player player,int itemId,LogType logType) {
		return spendItem(player, itemId, 1, logType);
	}
	public void addItem(Player player,int itemId,long count,LogType logType) {
		player.playerBag().addItem(itemId,count);
		logBiz.addGoods(player,itemId,count,ItemType.ITEM.getType(),logType);
		player.notifyObservers(ObservableEnum.AddBagItem, itemId,count,logType);
	}
	public long getItemNum(Player player, int itemId) {
		return player.playerBag().getItemCount(itemId);
	}
	
	//使用道具
	public List<Items> rewardBagItem(Player player,int itemId, long count) {
		ItemTemplate itemTemplate = itemConfig.getItemTemplateById(itemId);
		return rewardBagItem(player, itemTemplate, count, 0);
	}
	public List<Items> rewardBagItem(Player player,Items item) {
		return rewardBagItem(player, item.getId(), item.getCount());
	}
	
	public List<Items> rewardBagItem(Player player,ItemTemplate itemTemplate,long count,int index) {
		/*if(!itemTemplate.isCanBagUse()) {
			return Lists.newArrayList();
		}*/
		SubItemType itemType = itemTemplate.getItemType();
		switch (itemType) {
			case FUNCTIONITEM:
			return rewardFunctionitemItem(player, itemTemplate,count);
            case PACKAGE:
				return rewardBoxItem(player, itemTemplate,count);
			case CHOICEBOX:
				return rewardChoiceItem(player,itemTemplate,count,index);
			case RANDOM_DROP:
				return rewardRandomDropItem(player, itemTemplate, count);
		}
		return null;
	}
	/**
	 * 使用可选礼包
	 * @param player
	 * @param itemTemplate
	 * @param index 选择的物品index
	 * @return
	 */
	private List<Items> rewardChoiceItem(Player player,
			ItemTemplate itemTemplate,long count, int index) {
		List<Items> drops = itemTemplate.getChoiceItems();
		if(drops.size()<index){//如果
			Random random = new Random();
			index = random.nextInt(index)+1;
		}
		Items item = itemTemplate.getChoiceItems().get(index-1).clone();
		item.setCount(item.getCount()*count);
		itemBiz.addItem(player, item, LogType.Box_Get.value(itemTemplate.getId()));
		return Lists.newArrayList(item);
	}
	/**
	 * 宝箱道具，会随机
	 * @param player
	 * @param itemTemplate
	 */
	private List<Items> rewardBoxItem(Player player,ItemTemplate itemTemplate,long count) {
		List<Items> itemList = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			List<Items> tempList = dropConfig.randomItem(itemTemplate.getDrop());
			itemList.addAll(tempList);
		}
		//合并道具
		List<Items> newItemList = itemBiz.createItemList(itemList);
		LogType logType = LogType.Box_Get.value(itemTemplate.getId());
		for (Items item : newItemList) {
			itemBiz.addItem(player, item, logType);
		}
		return newItemList;
	}

	private List<Items> rewardRandomDropItem(Player player,ItemTemplate itemTemplate,long count) {
		List<Items> tempList = dropConfig.randomItem(itemTemplate.getDrop());
		//合并道具
		List<Items> newItemList = ItemUtils.calItemRateReward(tempList, count);
		LogType logType = LogType.Box_Get.value(itemTemplate.getId());
		for (Items item : newItemList) {
			itemBiz.addItem(player, item, logType);
		}
		return newItemList;
	}

	public List<Items> getRandomDropItem(List<Items> items){
		List<Items> itemsList = Lists.newArrayList();
		items.forEach(e -> itemsList.addAll(getRandomDropItem(e)));
		return itemsList;
	}


	private List<Items> getRandomDropItem(Items item) {
		ItemTemplate itemTemplate = itemConfig.getItemTemplateById(item.getId());
		SubItemType itemType = itemTemplate.getItemType();
		if (itemType == SubItemType.RANDOM_DROP){
			List<Items> tempList = dropConfig.randomItem(itemTemplate.getDrop());
			return ItemUtils.calItemRateReward(tempList, item.getCount());
		}
		return Lists.newArrayList(item);
	}

	
    //添加背包物品
  	public void addBagItem(Player player, int itemId,long add, ItemType itemType, LogType logType){
  		ItemTemplate template = itemConfig.getItemTemplateById(itemId);
  		if(template == null) {
  			log.error(player.getId()+"找不到背包物品："+itemId);
  			return;
  		}
  		if(template.isCanInBag()) {
  			addItem(player, itemId, add, logType);
  		}else{
  			//不能进背包直接使用
  			rewardBagItem(player, new Items(itemId, add, ItemType.ITEM.getType()));
  		}
  	}
  	
  	/**
	 * 功能道具使用
	 * @param player
	 * @param itemTemplate
	 */
	private List<Items> rewardFunctionitemItem(Player player,ItemTemplate itemTemplate,long count) {
		FunctionType functionType = itemTemplate.getFunctionType();
		switch (functionType) {
            case RedPacket:
				for(int i = 0; i < count; i ++ ) {
					innerChatFacade.saveAndSendRedPacket(player);
				}
            	break;
			case SeasonVipCard:
				player.getPlayerRecharge().addCzYueka(RechargeType.SeasonVipCard.getType(), (int)count);
				break;
			case QywxMark:
				doQywxMark(player);
				break;
        }
		return null;
	}

	public void doQywxMark(Player player) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		if(serverData.getServerKefuData().getQywxMark() > 0) {
			player.playerAds().setQywxMark(serverData.getServerKefuData().getQywxMark());
		}
	}
}







