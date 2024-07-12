package com.hm.actor;


import com.hm.libcore.actor.once.IOnceTask;
import com.hm.libcore.actor.once.OnceActor;
import com.hm.config.GameConstants;

/**
 * actor处理器
 */
public enum OnceQueueTaskType {
    PlayerDB(new OnceActor(20, 200, "player-db-actor", true)),

    //500ms检查执行一次5线程任务
    BuildDB(new OnceActor(20, 100, "BuildDB-actor")),
    TroopDB(new OnceActor(20, 100, "TroopDB-actor")),
    GuildDB(new OnceActor(20, 100, "GuildDB-actor")),

    ;

    private OnceQueueTaskType(OnceActor onceActor) {
        this.onceActor = onceActor;
    }

    private OnceActor onceActor;

    public OnceActor getOnceActor() {
        return onceActor;
    }

    public void putTask(IOnceTask task) {
        if (task == null) {
            return;
        }
        if (!GameConstants.isOpenDBQueue) {
            task.doOnceTask();
            return;
        }
        this.onceActor.put(task);
    }


    public static void startAll() {
        for (OnceQueueTaskType value : OnceQueueTaskType.values()) {
            value.onceActor.start();
        }
    }

    public static void stopAll() {
        for (OnceQueueTaskType value : OnceQueueTaskType.values()) {
            value.onceActor.stopWhenEmpty();
        }
    }
}