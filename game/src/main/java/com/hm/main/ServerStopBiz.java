package com.hm.main;

import cn.hutool.core.thread.ThreadUtil;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.sys.SysFacade;
import com.hm.actor.ActorDispatcherType;
import com.hm.actor.OnceQueueTaskType;
import com.hm.cache.PlayerCacheManager;
import com.hm.chsdk.event2.pack.CHPackEventBiz;
import com.hm.container.PlayerContainer;
import com.hm.enums.LeaveOnlineType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.log.ali.AliLogProducerUtil;
import com.hm.model.player.Player;
import com.hm.mongoqueue.MongoQueue;
import com.hm.timerjob.server.GameTimerManager;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 关闭服务器处理
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/29 10:18
 */
@Slf4j
@Biz
public class ServerStopBiz {
    @Resource
    private LoginBiz loginBiz;
    @Resource
    private SysFacade sysFacade;

    public void doStopServer() {
        log.info("准备关闭服务器");
        if (ServerStateCache.serverIsClose()) {
            log.info("服务器已经关闭");
            return;
        }
        //关闭消息接受
        ServerStateCache.changeServerState(ServerStateCache.ServerState.Close);

        log.info("stop timer");
        GameTimerManager.getInstance().shutdownTimer();

        MongoQueue.closeQueue();
        //把所有玩家踢下线
        log.info("关闭action处理器");
        log.info("准备踢出所有玩家");


        for (Player player : PlayerContainer.getOnlinePlayers()) {
            try {
                sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CHPackEventBiz.stopServer();
        ActorDispatcherType.stopAll();

        OnceQueueTaskType.stopAll();
        log.info("==============踢出所有玩家==============");
        ThreadUtil.sleep(5000);



        PlayerCacheManager.getInstance().clearAllCache();
        AliLogProducerUtil.close();//关闭阿里云日志

        log.info("服务器关闭");
    }

}
