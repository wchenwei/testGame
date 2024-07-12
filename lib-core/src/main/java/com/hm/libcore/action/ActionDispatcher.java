package com.hm.libcore.action;


import com.hm.libcore.actor.ActorDispatcher;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.serverConfig.ServerConfig;


/**
 * action处理器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/2/7 15:26
 */
public class ActionDispatcher {
    public static ActionDispatcher instance = new ActionDispatcher();

    public static ActionDispatcher getInstance() {
        return instance;
    }

    private ActorDispatcher dispatcher;

    public synchronized void startDispatcher() {
        this.dispatcher = new ActorDispatcher(ServerConfig.getInstance().getActionDispatcherNum(), "action-dispatcher");
        this.dispatcher.start();
    }

    public synchronized void stopDispatcher() {
        if (this.dispatcher != null) {
            this.dispatcher.stopWhenEmpty();
        }
    }

    public void putTask(long dispatchId, IRunner runner) {
        this.dispatcher.getActor(dispatchId)
                .put(runner);
    }
}
