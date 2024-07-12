package com.hm.timerjob;

import com.hm.action.Threshold.ThresholdBiz;
import com.hm.container.PlayerContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * @Description: 在线玩家检查
 * @author siyunlong  
 * @date 2018年6月7日 下午5:30:57 
 * @version V1.0
 */
@Slf4j
@Service
public class OnlinePlayerCheckJob {

    @Resource
    private ThresholdBiz thresholdBiz;
    

    @Scheduled(cron = "30 0/1 * * * ?")
    public void refreshPlayerOnline() {
    	PlayerContainer.checkOnlinePlayer();
    	thresholdBiz.updatePlayerItemToRedis();
    }
}
