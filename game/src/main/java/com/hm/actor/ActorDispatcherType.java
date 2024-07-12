package com.hm.actor;

import com.hm.libcore.actor.ActorDispatcher;
import com.hm.libcore.actor.IActor;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.serverConfig.ServerConfig;

/**
 * actor处理器
 */
public enum ActorDispatcherType {
    Msg(new ActorDispatcher(ServerConfig.getInstance().getActionDispatcherNum(), "msg-actor")),
    Http(new ActorDispatcher(5, "http-actor")),
    CHEventPack(new ActorDispatcher(1, "ch-pack-actor")),
    CHEventSend(new ActorDispatcher(3, "ch-pack-send-actor")),
    Cmq(new ActorDispatcher(5, "cmq-send-actor")),
    RankCmq(new ActorDispatcher(20, "rank-send-actor")),

    ;

    private ActorDispatcherType(ActorDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    private ActorDispatcher dispatcher;

    public void putTask(long dispatchId, IRunner runner) {
        this.dispatcher.getActor(dispatchId)
                .put(runner);
    }

    public void putTask(IRunner runner) {
        if (!this.dispatcher.isStart()) {
            runner.runActor();
            return;
        }
        IActor actor = this.dispatcher.getActorByIncrement();
        actor.put(runner);
    }

    public void changeActorSize(int newSize) {
        this.dispatcher.changeActorSize(newSize);
    }

    public static void startAll() {
        for (ActorDispatcherType value : ActorDispatcherType.values()) {
            value.dispatcher.start();
        }
    }

    public static void stopAll() {
        for (ActorDispatcherType value : ActorDispatcherType.values()) {
            value.dispatcher.stopWhenEmpty();
        }
    }

    public ActorDispatcher getDispatcher() {
        return dispatcher;
    }


    public int getPoolSize() {
        return this.dispatcher.getPoolSize();
    }
}