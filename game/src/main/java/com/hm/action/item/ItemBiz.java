/**
 * Project Name:SLG_GameFuture.
 * File Name:ItemBiz.java
 * Package Name:com.hm.action.item
 * Date:2017年12月28日下午5:10:22
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.
 *
*/
package com.hm.action.item;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.hm.action.bag.BagBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.strength.StrengthBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.titile.TitleBiz;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.enums.PlayerTitleType;
import com.hm.libcore.annotation.Biz;
import com.hm.log.LogBiz;
import com.hm.model.item.Items;
import com.hm.model.passenger.Passenger;
import com.hm.model.player.*;
import com.hm.observer.ObservableEnum;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 道具逻辑处理类
 * ClassName:ItemBiz <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年12月28日 下午5:10:22 <br/>
 * @author zigm
 * @version 1.1
 * @since
 */
@Biz
public class ItemBiz {
	@Resource
	private LogBiz logBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private BagBiz bagBiz;
	@Resource
	private TankBiz tankBiz; 
	@Resource
    private TitleBiz titleBiz;
	@Resource
	private StrengthBiz strengthBiz;

	/**
	 * 添加道具
	 * 没有通知玩家道具变化，没有保存数据
	 */
	public void addItem(Player player, Items item, LogType logType) {
		addItem(player,item.getEnumItemType(),item.getId(),item.getCount(),logType);
	}
	public void addItem(Player player, List<Items> itemList, LogType logType) {
		itemList.forEach(item -> addItem(player,item,logType));
	}
	
	/**
	 * 添加道具
	 * 没有通知玩家道具变化，没有保存数据
	 * @param player
	 * @param itemType
	 * @param itemId
	 * @param num
	 * @param logType 使用说明
	 */
	public void addItem(Player player, ItemType itemType, int itemId, long count, LogType logType) {
		if(count == 0) {
			return;
		}
		if(itemType != null) {
			switch(itemType) {
				case CURRENCY :
					PlayerAssetEnum assetEnum = PlayerAssetEnum.getPlayerAssetEnum(itemId);
					if(assetEnum != null) {
						assetEnum.addPlayerAsset(player, count, logType);
					}
					return;
				case ITEM :
					bagBiz.addBagItem(player, itemId, count, itemType, logType);
					return;//不记录日志,内部已经记录了
				case TANK :
					for(int i=0;i<count;i++){
						tankBiz.addTank(player, itemId,logType);
					}
					return;//不记录日志,内部已经记录了
				case PAPER :
					player.playerTankPaper().addItem(itemId, count);
					player.notifyObservers(ObservableEnum.AddTankPaper, itemId,count,logType);
					break;
				case PIECE :
					player.playerTankpiece().addItem(itemId, count);
					player.notifyObservers(ObservableEnum.AddPiece, itemId,count,logType);
					break;
				case EQUIP :
					player.playerEquip().addItem(itemId, count);
					if(logType!=LogType.ChangeEqu&&logType!=LogType.ComposeEqu) {
						player.notifyObservers(ObservableEnum.AddEquip, itemId, count,logType);
					}
					break;
				case STONE :
					player.playerStone().addItem(itemId,count);
					if(logType!=LogType.ChangeStone) {
						player.notifyObservers(ObservableEnum.AddStone, itemId, count,logType);
					}
					break;
				case ICON:
					playerBiz.addIcon(player, itemId);
					break;
				case HEAD_FRAME:
					player.playerHead().unlockFrameIcon(itemId);
					break;
				case TITLE:
					titleBiz.addTitle(player, PlayerTitleType.getType(itemId));
					break;
				case PassengerPIECE:
					player.playerPassenger().addItem(itemId,count);
					player.notifyObservers(ObservableEnum.AddPassengerPiece, itemId, count,logType);
					break;
				case Passenger:
					for(int i=0;i<count;i++){
						player.playerPassenger().addPassenger(new Passenger(itemId, player.getServerId()));
					}
					if(logType!=LogType.Passenger_Compose){
						player.notifyObservers(ObservableEnum.AddPassenger, itemId, count,logType);
					}
					break;
				case TankPhoto:
					player.playerMemorialHall().addPhoto(itemId,count);
					break;
				case TankStrenTool:
					player.playerTrain().addItem(itemId, count);
					break;
				case Arms:
					for(int i=1;i<=count;i++){
						player.playerArms().addArms(new Arms(itemId,player.getServerId()));
					}
					break;
				case Aircraft:
					for(int i=1;i<=count;i++){
						player.playerAircraft().addAircraft(new Aircraft(itemId, player.getServerId()));
					}
					player.notifyObservers(ObservableEnum.AddAircraftCarrier, itemId, count,logType);
					break;
				case Element:
					player.playerElement().addItem(itemId, count);
					player.notifyObservers(ObservableEnum.AddElement, itemId, count, logType);
					break;
				case StrengthStore:
					strengthBiz.addItem(player, itemId, count);
					break;
			}
			logBiz.addGoods(player, itemId, count, itemType.getType(), logType);
		}
	}
	/**
	 * 消耗道具
	 * 没有通知玩家道具变化，没有保存数据
	 */
	public boolean reduceItem(Player player, Items item, LogType logType) {
		return reduceItem(player,item.getEnumItemType(),item.getId(),item.getCount(),logType);
	}
	public void reduceItem(Player player, List<Items> itemList, LogType logType) {
		itemList.forEach(item -> reduceItem(player, item, logType));
	}

	/**
	 * 消耗道具
	 *  没有通知玩家道具变化，没有保存数据
	 * @param player
	 * @param itemType
	 * @param itemId
	 * @param num
	 * @return 使用说明
	 */
    private boolean reduceItem(Player player, ItemType itemType, int itemId, long count, LogType logType) {
		if(itemType!=null) {
			switch(itemType) {
				case CURRENCY:
					PlayerAssetEnum assetEnum = PlayerAssetEnum.getPlayerAssetEnum(itemId);
					return assetEnum != null && assetEnum.reduceAsset(player, count, logType);
				case ITEM:
					return bagBiz.spendItem(player, itemId, count, logType);
				case PAPER :
					player.playerTankPaper().spendItem(itemId, count);
					break;
				case PIECE :
					player.playerTankpiece().spendItem(itemId, count);
					break;
				case EQUIP :
					player.playerEquip().spendItem(itemId, count);
					break;
				case STONE :
					player.playerStone().spendItem(itemId, count);
					break;
				case PassengerPIECE :
					player.playerPassenger().spendItem(itemId, count);
					break;
				case TankPhoto :
					player.playerMemorialHall().spendPhotos(itemId, count);
					break;
				case TankStrenTool :
					player.playerTrain().spendItem(itemId,count);
				case Element :
					player.playerElement().spendItem(itemId,count);
					break;
			}
			//记录日志
			logBiz.spendGoods(player, itemId, count, itemType.getType(), logType);
			return true;
		}
		return false;
	}
    
    
	public long getItemTotal(BasePlayer player, int itemTypeInt, int itemId) {
		long total = 0;
		ItemType itemType = ItemType.getType(itemTypeInt);
		switch(itemType) {
			case CURRENCY:
				PlayerAssetEnum assetEnum = PlayerAssetEnum.getPlayerAssetEnum(itemId);
				CurrencyKind currency = PlayerAssetEnum.getCurrencyByPlayerAssetEnum(assetEnum.getTypeId());
				if(null!=currency) {
					total = player.playerCurrency().get(currency);
				}
				break;
			case ITEM:
				total = player.playerBag().getItemCount(itemId);
				break;
			case PAPER :
				total = player.playerTankPaper().getItemCount(itemId);
				break;
			case PIECE :
				total = player.playerTankpiece().getItemCount(itemId);
				break;
			case EQUIP :
				total = player.playerEquip().getItemCount(itemId);
				break;
			case STONE :
				total = player.playerStone().getItemCount(itemId);
				break;
			case PassengerPIECE :
				total = player.playerPassenger().getItemCount(itemId);
				break;
			case TankPhoto :
				total = player.playerMemorialHall().getPhotoNum(itemId);
				break;
		}
    	return total;
    }

	//检查并消耗道具
  	public boolean checkItemEnoughAndSpend(Player player, int itemId, long count, ItemType type, LogType logType) {
  		Items item = new Items(itemId);
  		item.setItemType(type);
  		item.setCount(count);
  		return checkItemEnoughAndSpend(player, item, logType);
  	}
	public boolean checkItemEnoughAndSpend(Player player, Items item, LogType logType) {
		if(checkItemEnough(player, item)) {
			return reduceItem(player, item, logType);
		}
		return false;
	}
	//检查并消耗道具
	public boolean checkItemEnoughAndSpend(Player player, List<Items> itemList,LogType logType) {
		List<Items> newItemList = createItemList(itemList);
		if(checkItemEnough(player, newItemList)) {
			reduceItem(player, newItemList, logType);
			return true;
		}
		return false;
	}
	/**
	 * 检查道具是否足够
	 */
	public boolean checkItemEnough(Player player, Items item) {
		return checkItemEnough(player,item.getEnumItemType(),item.getId(),item.getCount());
	}
	public boolean checkItemEnough(Player player, List<Items> itemList) {
		return itemList.stream().allMatch(item -> null!=item&&checkItemEnough(player, item));
	}

	/**
	 * 检查道具是否足够
	 */
	public boolean checkItemEnough(Player player, ItemType itemType, int itemId, long count) {
		boolean isEnough = checkItemEnoughForSelf(player,itemType,itemId,count);
//		if(!isEnough) {
//			player.notifyObservers(ObservableEnum.ItemNotEnough,itemType.getType()+"_"+itemId);
//		}
		return isEnough;
	}
	private boolean checkItemEnoughForSelf(Player player, ItemType itemType, int itemId, long count) {
		if(itemType == null) {
			return false;
		}
		switch(itemType) {
			case CURRENCY:
				PlayerAssetEnum assetEnum = PlayerAssetEnum.getPlayerAssetEnum(itemId);
				return assetEnum != null && assetEnum.checkPlayerAssetIsEnough(player, count);
			case ITEM:
				return player.playerBag().checkCanUse(itemId,count);
			case PAPER :
				return player.playerTankPaper().checkCanUse(itemId, count);
			case PIECE :
				return player.playerTankpiece().checkCanUse(itemId, count);
			case EQUIP :
				return player.playerEquip().checkCanUse(itemId, count);
			case STONE :
				return player.playerStone().checkCanUse(itemId, count);
			case PassengerPIECE :
				return player.playerPassenger().checkCanUse(itemId, count);
			case TankPhoto :
				return player.playerMemorialHall().checkSpend(itemId, count);
			case TankStrenTool :
				return player.playerTrain().checkCanUse(itemId, count);
			case Element :
				return player.playerElement().checkCanUse(itemId, count);
		}
		return false;
	}

	//检查合并itemList
	public static List<Items> createItemList(List<Items> itemList) {
		Table<Integer, Integer, Items> tables = HashBasedTable.create();
		for (Items items : itemList) {
			if(items.getItemType()!=ItemType.OTHER.getType()){
				Items temp = tables.get(items.getItemType(), items.getId());
				if(temp == null) {
					if(items.getCount()>0){
						temp = items.clone();
						tables.put(items.getItemType(), items.getId(), temp);
					}
				}else{
					temp.addCount(items.getCount());
				}
			}
		}
		return Lists.newArrayList(tables.values());
	}

	public boolean checkItemTest(Player player,String str) {
		try {
			if(str.startsWith("item")) {
				str = str.substring(4, str.length());
				for (String itemStr : str.split(",")) {
					Items item = ItemUtils.str2Item(itemStr, ":");
					if(item != null) {
						addItem(player, item, LogType.Box_Get);
					}
				}
				player.sendUserUpdateMsg();
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 清空玩家的背包中的道具
	 * @param player
	 * @param itemIdList
	 */
	public boolean clearPlayerBag(BasePlayer player,Collection<Integer> itemIdList) {
		boolean isChange = false;
		for (int itemId : itemIdList) {
			long count = player.playerBag().getItemCount(itemId);
			if(count > 0) {
				player.playerBag().removeItem(itemId);
				logBiz.spendGoods(player, itemId, count, ItemType.ITEM.getType(), LogType.SystemClean);
				isChange = true;
			}
		}
		return isChange;
	}

	/**
	 * 用于gm,发送用户扣出命令
	 * @param costs
	 * @return
	 */
	public List<Items> gmReduceItem(Player player, List<Items> costs) {
		List<Items> result = Lists.newArrayList();
		for (Items item : costs) {
			Items newItem = item.clone();
			long have = 0;
			long reduce = 0;
			ItemType itemType = ItemType.getType(item.getType());
			switch (itemType) {
				case ITEM:
					have = player.playerBag().getItemCount(item.getId());
					break;
				case CURRENCY:
					CurrencyKind kind = PlayerAssetEnum.getCurrencyByPlayerAssetEnum(item.getId());
					have = kind == null ? 0 : player.playerCurrency().isGold(kind) ? player.playerCurrency().getGold() : player.playerCurrency().get(kind);
					break;
				case PAPER:
					have = player.playerTankPaper().getItemCount(item.getId());
					break;
				case PIECE:
					have = player.playerTankpiece().getItemCount(item.getId());
					break;
				case EQUIP:
					have = player.playerEquip().getItemCount(item.getId());
					break;
				case STONE:
					have = player.playerStone().getItemCount(item.getId());
					break;
				case PassengerPIECE:
					have = player.playerPassenger().getItemCount(item.getId());
					break;
//            case TankPhoto :
//                have = player.playerMemorialHall().getItemCount(item.getId());
//                break;
				case TankStrenTool:
					have = player.playerTrain().getItemCount(item.getId());
					break;
//            case Element :
//                have = player.playerElement().getItemCount(item.getId());
//                break;
			}
			reduce = Math.min(item.getCount(), have);
			newItem.setCount(reduce);
			result.add(newItem);
			this.reduceItem(player, newItem, LogType.ReduceByGM);
		}
		return result;
	}
}













