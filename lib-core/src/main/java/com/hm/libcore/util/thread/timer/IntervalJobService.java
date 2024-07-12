package com.hm.libcore.util.thread.timer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.hm.libcore.annotation.ServerIntervalJob;
import com.hm.libcore.handler.ServerStateCache;

/**
 * 检查
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/12/3 13:41
 */
public class IntervalJobService {
    private TickTimer tickTimer;
    private IServerThreadCase serverThreadCase;

    public IntervalJobService(IServerThreadCase serverThreadCase) {
        this.serverThreadCase = serverThreadCase;
        long interval = AnnotationUtil.getAnnotationValue(serverThreadCase.getClass(), ServerIntervalJob.class);
        this.tickTimer = new TickTimer(interval);
    }

    //检查
    public void doCheck(long now, int serverId) {
        if (this.tickTimer.isPeriod(now)) {
            try {
                if(!ServerStateCache.serverIsRun()) {
                    return;
                }
                serverThreadCase.doServerTimer(serverId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
