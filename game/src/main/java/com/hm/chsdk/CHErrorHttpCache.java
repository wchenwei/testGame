package com.hm.chsdk;

import com.hm.libcore.actor.IRunner;
import com.hm.libcore.annotation.Biz;
import com.hm.actor.ActorDispatcherType;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 失败重发缓存
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/11 14:29
 */
@Biz
public class CHErrorHttpCache {
    //发送失败的数据缓存
    public static final ConcurrentLinkedQueue<IRunner> errorList = new ConcurrentLinkedQueue<>();

    //每分钟失败重发
    @Scheduled(cron = "0 0/1 * * * ?")
    public void doMinuteTask() {
        while (!errorList.isEmpty()) {
            ActorDispatcherType.CHEventSend.putTask(errorList.poll());
        }
    }

    public static void addErrorList(IRunner task) {
        errorList.offer(task);
    }

}
