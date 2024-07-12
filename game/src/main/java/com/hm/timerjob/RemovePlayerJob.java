package com.hm.timerjob;

import com.hm.libcore.handler.ServerStateCache;
import com.hm.action.player.RemovePlayerBiz;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 注销用户检查
 */
@Service
public class RemovePlayerJob {
	@Resource
	private RemovePlayerBiz removePlayerBiz;

    @Scheduled(cron ="0/1 * * * * ?")
    public void removePlayer() {
    	if(!ServerStateCache.serverIsRun()) {
			return;
		}
		removePlayerBiz.checkRemovePlayer();
    }

}
