/**  
 * Project Name:SLG_GameFuture.  
 * File Name:PlayerAsset.java  
 * Package Name:com.hm.enums  
 * Date:2018年1月9日上午11:09:37  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.enums;

import com.hm.action.player.PlayerBiz;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.item.Items;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;


public enum PlayerAssetEnum {
	SysGold(1){//游戏内金币
		@Override
		public void addAsset(Player player, long num, LogType logType){
			super.addAsset(player,num,logType);
			player.notifyObservers(ObservableEnum.GetSysGold, num);
		}
	},
	Gold(2),
	Cash(4),
	Oil(5),
	Credit(7),
	Sports(8),
	Expedition(9),
	MissionMedal(12),
	Shipping(13),
	SilverBar(14),
	MineKfScore(15),
	Material(16),
	KfMoney(18),
	ExpMoney(19),
	BhMoney(20),
	Crystal(21),
	JnMoney(22),
	HolidayScore(23),
	KfRankScore(24),
	KfWorldWarScore(26) ,
	FishScore(28) ,

	EXP(1001),
	VIPDOTCOUNT(1002){//VIP
		@Override
		public void addAsset(Player player, long num, LogType logType){
			PlayerBiz playerNewBiz = SpringUtil.getBean(PlayerBiz.class);
			playerNewBiz.addVipExp(player, num, logType);
		}
		@Override
		public boolean checkAsset(Player player, long num){
			return false;
		}
		@Override
		public boolean reduceAsset(Player player, long num, LogType logType){
			return true;
		}
	},

	Honor(1004){//荣誉
		@Override
		public void addAsset(Player player, long num, LogType logType){
			PlayerBiz playerNewBiz = SpringUtil.getBean(PlayerBiz.class);
			playerNewBiz.addPlayerHonor(player, HonorType.KillTank, num, logType);
		}
		@Override
		public boolean checkAsset(Player player, long num){
			return player.playerHonor().getTotalHonor() >= num;
		}
		@Override
		public boolean reduceAsset(Player player, long num, LogType logType){
			return false;
		}
	},
	KfWarMakes(1006) {
		@Override
		public void addAsset(Player player, long num, LogType logType) {
			PlayerBiz playerNewBiz = SpringUtil.getBean(PlayerBiz.class);
			playerNewBiz.addWarMakes(player, num, logType);
		}

		@Override
		public boolean checkAsset(Player player, long num) {
			return false;
		}

		@Override
		public boolean reduceAsset(Player player, long num, LogType logType) {
			return true;
		}
	},
	;
	
	private final int typeId;
	
	private PlayerAssetEnum(int typeId){
		this.typeId = typeId;
	}
	public int getTypeId(){
		return this.typeId;
	}
	
	/**
	 * 添加资产,外调接口
	 * @author zigm  
	 */
	public void addPlayerAsset(Player player, long num, LogType logType){
		addAsset(player, num, logType);
	}
	
	/**
	 * 检测资产是否足够
	 */
	public boolean checkPlayerAssetIsEnough(Player player, long num){
		return checkAsset(player, num);
	}
	/**
	 *资产减少
	 */
	public void reducePlayerAsset(Player player, long num, LogType logType){
		reduceAsset(player, num, logType);
	}
	
	public static PlayerAssetEnum getPlayerAssetEnum(int id){
		for(PlayerAssetEnum asset : PlayerAssetEnum.values()){
			if(asset.getTypeId() == id){
				return asset;
			}
		}
		return null;
	}
	

	public static CurrencyKind getCurrencyByPlayerAssetEnum(int id){
		return CurrencyKind.getCurrencyByIndex(id);
	}
	public static boolean isGold(PlayerAssetEnum playerAssetEnum){
		return PlayerAssetEnum.Gold==playerAssetEnum||PlayerAssetEnum.SysGold==playerAssetEnum;
	}
	public static boolean isGold(int id){
		return PlayerAssetEnum.Gold.getTypeId()==id||PlayerAssetEnum.SysGold.getTypeId()==id;
	}
	
	public void addAsset(Player player, long num, LogType logType) {
		PlayerBiz playerNewBiz = SpringUtil.getBean(PlayerBiz.class);
		playerNewBiz.addPlayerCurrency(player,CurrencyKind.getCurrencyByIndex(getTypeId()), num, logType);
	}

	public boolean checkAsset(Player player, long num) {
		PlayerBiz playerNewBiz = SpringUtil.getBean(PlayerBiz.class);
		return playerNewBiz.checkPlayerCurrency(player,CurrencyKind.getCurrencyByIndex(getTypeId()), num);
	}
	
	public boolean reduceAsset(Player player, long num, LogType logType) {
		PlayerBiz playerNewBiz = SpringUtil.getBean(PlayerBiz.class);
		playerNewBiz.spendPlayerCurrency(player, CurrencyKind.getCurrencyByIndex(getTypeId()), num, logType);
		return true;
	}

	public Items buildItems(long count) {
		return new Items(getTypeId(),count,ItemType.CURRENCY);
	}
}
  
