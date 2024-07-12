package com.hm.actor;


import com.hm.libcore.actor.ActorDispatcher;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.serverConfig.ServerConfig;

/**
 * actor处理器
 */
public enum ActorDispatcherType {
    Msg(new ActorDispatcher(ServerConfig.getInstance().getActionDispatcherNum(), "msg-actor")),
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
        this.dispatcher.getActorByIncrement()
                .put(runner);
    }

    public int getPoolSize() {
        return this.dispatcher.getPoolSize();
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
}