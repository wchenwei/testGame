/**  
 * Project Name:SLG_GameHot.
 * File Name:SysCountBiz.java  
 * Package Name:com.hm.action.serverStatistics  
 * Date:2018年4月8日下午2:37:00  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.action.Threshold;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.hm.config.GameConstants;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.type.RedisConstants;
import com.hm.redis.util.RedisDbUtil;
import com.hm.server.GameServerManager;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 阀值监控
 * ClassName: ThresholdBiz. <br/>    
 * date: 2018年11月30日 上午11:44:10 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
@Biz
public class ThresholdBiz implements IObserver{

	private Table<Long,String,Long> playerItemCountTable = HashBasedTable.create();
	//已经设置过过期时间的key
	private List<String> keys = Lists.newArrayList();

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddRes, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddPlayerExp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddBagItem, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddHonor, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddTankPaper, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddPiece, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddEquip, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MailReward, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		if(player!=null&&GameServerManager.getInstance().isTestServer(player.getServerId())) {
			return;
		}
		int itemId = 0;
		int itemType = 0;
		long count = 0;
		LogType logType = null;
		switch(observableEnum) {
			case HourEvent:
				if((int)argv[0]==0){
					clearKeys();
				}
				return;
			case AddRes : {
				int id = (int) argv[0];
				count = (long) argv[1];
				logType = (LogType)argv[2];
				itemId = id;
				itemType = ItemType.CURRENCY.getType(); 
				if(isNoTholdLog(logType,itemType,itemId)) {
					return;
				}
				break;
			}
			case MailReward:
				Items item = (Items)argv[0];
				logType = (LogType)argv[1];
				if(isNoTholdLog(logType,item.getItemType(),item.getId())) {
					return;
				}
				break;
			case AddPlayerExp:{
				count = (long)argv[0];
				logType = (LogType)argv[1];
				itemId = PlayerAssetEnum.EXP.getTypeId();
				itemType = ItemType.CURRENCY.getType(); 
				break;
			}
			case AddBagItem:{
				itemId = (int)argv[0]; 
				count = (long)argv[1]; 
				logType = (LogType)argv[2];
				if(isNoTholdLog(logType,itemType,itemId)) {
					return;
				}
				itemType = ItemType.ITEM.getType(); 
				break;
			}
			case AddTankPaper:{
				itemId = (int)argv[0]; 
				count = (long)argv[1];
				logType = (LogType)argv[2];
				if(isNoTholdLog(logType,itemType,itemId)) {
					return;
				}
				itemType = ItemType.PAPER.getType(); 
				break;
			}
			case AddPiece:{
				itemId = (int)argv[0]; 
				count = (long)argv[1]; 
				logType = (LogType)argv[2]; 
				itemType = ItemType.PIECE.getType(); 
				break;
			}
			case TankAdd:{
				itemId = (int)argv[0];
				logType = (LogType)argv[1];
				count = 1; 
				itemType = ItemType.TANK.getType(); 
				break;
			}
			case AddHonor:{
				int honor = (int)argv[0];
				logType = (LogType)argv[2];
				if(honor == HonorType.KillTank.getType()){
					count= (long)argv[1]; 
					itemId = PlayerAssetEnum.Honor.getTypeId(); 
					itemType = ItemType.CURRENCY.getType();
				}
				break;
			}
			case AddEquip:{
				itemId = (int)argv[0]; 
				count = (long)argv[1];
				logType = (LogType)argv[2];
				itemType = ItemType.EQUIP.getType(); 
				break;
			}
			case AddStone:{
				itemId = (int)argv[0]; 
				count = (long)argv[1];
				logType = (LogType)argv[2];
				itemType = ItemType.STONE.getType(); 
				break;
			}
			case AddPassenger:{
				itemId = (int)argv[0]; 
				count = (long)argv[1];
				logType = (LogType)argv[2];
				itemType = ItemType.Passenger.getType();
				break;
			}
			case AddPassengerPiece:{
				itemId = (int)argv[0]; 
				count = (long)argv[1];
				logType = (LogType)argv[2];
				itemType = ItemType.PassengerPIECE.getType();
				break;
			}
			default :
				break;
		}
		if(logType==null){
			return;
		}
		Items items = new Items(itemId, count, itemType);
		updatePlayerItem(player, items);
		
	}
	
	/**
	 * 不统计
	 * @param logType
	 * @return
	 */
	public static boolean isNoTholdLog(LogType logType,int itemType,int itemId) {
		if(logType==null){
			return false;
		}
		//老兵回归，坦克重铸返还的资源和通过邮件(决战北美)返还的石油资源不统计
		//TODO 军职退伍不统计 2021年1月18日09:43:40
		return LogType.TankRecast.getCode() == logType.getCode()||LogType.TankDriverQuit.getCode() == logType.getCode()||isKfMailReturn(logType,itemType,itemId);
	}

	//是否是邮件跨服资源返还的(目前只判断石油)
	public static boolean isKfMailReturn(LogType logType,int itemType,int itemId){
		if(LogType.Mail_Get.getCode()!=logType.getCode()){
			return false;
		}
		try{
			if(itemType==ItemType.CURRENCY.getType()&&itemId==PlayerAssetEnum.Oil.getTypeId()){
				int mailType = Integer.parseInt(logType.getExtra().split("_")[0]);
				return mailType > 0 && MailConfigEnum.isNoFiterOilMail(mailType);
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}

	@Deprecated
	private void updatePlayerItemRedis(Player player,Items items) {
		if(ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
			return;
		}
		if(items != null && items.getCount() >0){
			Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Threshold);
			String key = createKey(items);
			jedis.zincrby(key, (double)items.getCount(), player.getId()+"");
			if(jedis.ttl(key) == -1){
				jedis.pexpireAt(key, DateUtil.getNextDay(new Date()).getTime());
			}
			RedisDbUtil.returnResource(jedis);
		}
	}


	/**
	 * 保存到内存
	 * @param player
	 * @param items
	 */
	private void updatePlayerItem(Player player,Items items) {
		if(ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
			return;
		}
		if(items != null && items.getCount() >0){
			String key = createKey(items);
			synchronized (playerItemCountTable){
				if (playerItemCountTable.contains(player.getId(),key)){
					playerItemCountTable.put(player.getId(),key,items.getCount() + playerItemCountTable.get(player.getId() ,key));
				}else {
					playerItemCountTable.put(player.getId(),key, items.getCount());
				}
			}
		}
	}

	/**
	 * 更新到redis
	 */
	public void updatePlayerItemToRedis(){
		Table<Long,String,Long> tempTable;
		synchronized (playerItemCountTable){
			tempTable = ImmutableTable.copyOf(playerItemCountTable);
			playerItemCountTable = HashBasedTable.create();
		}
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Threshold);
		try {
			long endTime = DateUtil.getNextDay(new Date()).getTime() - 2*GameConstants.MINUTE;
			for (long playerId : tempTable.rowKeySet()){
				Map<String, Long> itemCount = tempTable.row(playerId);
				for (String key: itemCount.keySet()){
					jedis.zincrby(key, (double) itemCount.get(key), playerId+"");
					if(!keys.contains(key)){
						jedis.pexpireAt(key, endTime);
						keys.add(key);
					}
				}
			}
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
	}

	public void clearKeys() {
		this.keys.clear();
	}

	public static String createKey(Items items) {
		return MongoUtils.getGameDBName() + "_" + items.getItemType() + "_" + items.getId();
	}
}











