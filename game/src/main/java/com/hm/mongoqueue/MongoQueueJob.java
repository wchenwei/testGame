package com.hm.mongoqueue;

import com.hm.config.GameConstants;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 
 * @Description: 在线玩家检查
 * @author siyunlong  
 * @date 2018年6月7日 下午5:30:57 
 * @version V1.0
 */
@Slf4j
@Service
public class MongoQueueJob implements IObserver{

    @Override
	public void registObserverEnum() {
//		ObserverRouter.getInstance().registObserver(ObservableEnum.MinuteEvent, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		MongoQueue.minuteCheck();
	}
    
//    @Scheduled(cron = "0/1 * * * * ?")
//    public void saveDBForPlayer() {
//    	if(MongoQueue.SaveDBSecondInterval <= 1) {
//    		doMongoQueue();
//    	}else{
//    		long now = System.currentTimeMillis();
//    		if(now - MongoQueue.LastSaveTime >= MongoQueue.SaveDBSecondInterval*GameConstants.SECOND) {
//    			doMongoQueue();
//    			MongoQueue.LastSaveTime = now;
//    		}
//    	}
//    }
    
    public void doMongoQueue() {
    	for (int i = 0; i < MongoQueue.MaxWaitQueueSize; i++) {
    		try {
    			if(!MongoQueue.doTask()) {
    				break;
    			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
    
}
