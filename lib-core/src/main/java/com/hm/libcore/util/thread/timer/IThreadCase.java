package com.hm.libcore.util.thread.timer;

public interface IThreadCase {
    default void caseStart() {

    }

    default void caseStop() {

    }

    public void caseRunOnce();
}