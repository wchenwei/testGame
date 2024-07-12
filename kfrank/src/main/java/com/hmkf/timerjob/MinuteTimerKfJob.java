package com.hmkf.timerjob;

import javax.annotation.Resource;

import com.hmkf.action.npc.NpcCheckBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hm.libcore.handler.ServerStateCache;
import com.hmkf.gametype.KfGroupContainer;
import com.hmkf.server.ServerGroupManager;

import cn.hutool.core.date.DateUtil;

@Slf4j
@Service
public class MinuteTimerKfJob {
    @Resource
    private KfGroupContainer groupContainer;
    @Resource
    private NpcCheckBiz npcCheckBiz;


    //每天0点1秒执行
    @Scheduled(cron = "1 0/1 7-20 * * ?")
    public void doMinuteTask() {
        if (!ServerStateCache.serverIsRun()) {
            return;
        }
        if (DateUtil.thisMinute() == 0) {
            ServerGroupManager.getIntance().reloadData();
            groupContainer.init();
        }
    }

    @Scheduled(cron = "30 0/1 * * * ?")
    public void doNpcTask() {
        if (!ServerStateCache.serverIsRun()) {
            return;
        }
        npcCheckBiz.doCheck();
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void doNpcAdd() {
        npcCheckBiz.doCheckAdd();
    }
}
