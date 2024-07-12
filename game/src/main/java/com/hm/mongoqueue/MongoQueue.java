package com.hm.mongoqueue;

import cn.hutool.core.thread.ThreadUtil;
import com.hm.cache.PlayerCacheManager;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
public class MongoQueue {
	public static boolean NoOnlineSaveOpen = false;//存储离线用户
	//====================================================================
	public static int SaveDBSecondInterval = 1;//保存时间间隔,单位秒
	public static long LastSaveTime = 0;//上次保存时间
	//====================================================================
	public static boolean ShowLog = false;//是否打印日志
	public static boolean isClose = false;//是否关闭数据库队列
	public static int MaxWaitQueueSize = 30;//每次最大可保存队列数量,超过次数量后,下次保存
	//====================================================================
	public static boolean checkSecondIntervalTask = true;
	
	public final static AtomicInteger waitQueueSize = new AtomicInteger(0);
	public static ExecutorService threadExecutor = ThreadUtil.newExecutor(5, 30);
	public static ConcurrentLinkedQueue<Long> writeQueue = new ConcurrentLinkedQueue<>();
	

	/**
	 * 每分钟检查当前可存储size的数量
	 */
	public static void minuteCheck() {
		if(checkSecondIntervalTask) {
			int curSize = writeQueue.size();
			SaveDBSecondInterval = Math.min(Math.max(curSize/MaxWaitQueueSize, 1), 5);
			logInfo("MongoQueue:"+"当前存储间隔:"+SaveDBSecondInterval);
		}
	}
	
	public static synchronized boolean doTask() {
		if (waitQueueSize.incrementAndGet() > MaxWaitQueueSize) {
			waitQueueSize.decrementAndGet();
			logInfo("MongoQueue:队列满了");
            return false;
        }else{
			Long luckId = writeQueue.poll();
        	if(luckId == null) {
        		waitQueueSize.decrementAndGet();
//        		logInfo("MongoQueue:没有值");
        		return false;
        	}
        	threadExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                    	doSaveDB(luckId);
                    } finally {
                        waitQueueSize.decrementAndGet();
                    }
                }
            });
        }
		return true;
	}
	
	public static void addWrite(long playerId) {
		if(!writeQueue.contains(playerId)) {
			writeQueue.offer(playerId);
			logInfo("MongoQueue:"+playerId+"进入保存队列");
		}else{
			logInfo("MongoQueue:"+playerId+"已在队列里");
		}
	}
	
	public static void doSaveDB(long id) {
		Player player = getPlayerFromCache(id);
		if(player == null || !player.isDBChangeMark()) {
			logInfo("MongoQueue:已经保存过了"+id);
			return;
		}
		logInfo("MongoQueue:保存玩家"+id);
		player.saveNowDB();
	}
	
	public static Player getPlayerFromCache(long id) {
		Player player = PlayerUtils.getOnlinePlayer(id);
		if(player == null && NoOnlineSaveOpen) {
			return PlayerCacheManager.getInstance().getPlayerOrNull(id);
		}
		return player;
	}
	
	public static void logInfo(String info) {
		if(ShowLog) {
			log.error(info);
		}
	}
	
	public synchronized static void closeQueue() {
		isClose = true;
		logInfo("关闭mongo数据库队列");
//		int size = writeQueue.size();
//		for (int i = 0; i < size; i++) {
//			Integer luckId = writeQueue.poll();
//			if(luckId != null) {
//				doSaveDB(luckId);
//			}
//		}
	}
	
}
