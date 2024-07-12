package com.hm.libcore.util.thread.timer;

public interface IServerThreadCase {
    default void caseStart(int serverId) {

    }

    default void caseStop(int serverId) {

    }

    void doServerTimer(int serverId);
}