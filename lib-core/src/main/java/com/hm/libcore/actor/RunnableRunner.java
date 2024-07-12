package com.hm.libcore.actor;

public class RunnableRunner implements IRunner {

    private final Runnable r;

    public RunnableRunner(Runnable r) {
        this.r = r;
    }

    public Object runActor() {
        if (r != null) {
            r.run();
        }
        return null;
    }

}
