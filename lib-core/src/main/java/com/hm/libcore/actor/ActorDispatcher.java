package com.hm.libcore.actor;

import java.util.concurrent.atomic.AtomicLong;

public class ActorDispatcher {
    private final AtomicLong ID = new AtomicLong();
    private Actor[] actors;
    private String name;
    private boolean isStart = false;

    public ActorDispatcher(int poolSize, String name) {
        if (poolSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        actors = new Actor[poolSize];
        for (int i = 0; i < actors.length; ++i) {
            actors[i] = new Actor("Dispatcher-" + name + "-" + i);
        }
    }

    public ActorDispatcher(int poolSize, String name, int capacity) {
        if (poolSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        actors = new Actor[poolSize];
        for (int i = 0; i < actors.length; ++i) {
            actors[i] = new Actor("Dispatcher-" + name + "-" + i, capacity);
        }
    }

    public IActor getActor(int dispatchId) {
        int idx = Math.abs(dispatchId) % actors.length;
        return actors[idx];
    }


    public IActor getActor(long dispatchId) {
        int idx = Math.abs(((int) dispatchId)) % actors.length;
        return actors[idx];
    }

    public IActor getActorByIncrement() {
        return getActor(ID.getAndIncrement());
    }


    public void put(int dispatchId, IRunner runner) {
        getActor(dispatchId).put(runner);
    }

    public void put(int dispatchId, IRunner runner, ICallback callback, IActor target) {
        getActor(dispatchId).put(runner, callback, target);
    }

    public boolean start() {
        for (int i = 0; i < actors.length; ++i) {
            if (false == actors[i].start()) {
                return false;
            }
        }
        this.isStart = true;
        return true;
    }

    public int getPoolSize() {
        return this.actors.length;
    }

    public void changeActorSize(int newSize) {
        int oldSize = getPoolSize();
        if (oldSize >= newSize) {
            return;
        }
        Actor[] newActors = new Actor[newSize];
        for (int i = 0; i < newActors.length; i++) {
            if (i >= oldSize) {
                newActors[i] = new Actor("Dispatcher-" + name + "-" + i);
                newActors[i].start();
                System.out.println(name + "_" + i + " new actor start");
            } else {
                newActors[i] = this.actors[i];
            }
        }
        this.actors = newActors;
    }

    public void stop() {
        for (int i = 0; i < actors.length; ++i) {
            actors[i].stop();
        }
    }

    public void stopWhenEmpty() {
        for (int i = 0; i < actors.length; ++i) {
            actors[i].stopWhenEmpty();
        }
        this.isStart = false;
    }

    public void waitForStop() {
        while (isRunning()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public boolean isStopping() {
        for (int i = 0; i < actors.length; ++i) {
            if (actors[i].isStopping()) {
                return true;
            }
        }
        return false;
    }

    public boolean isRunning() {
        for (int i = 0; i < actors.length; ++i) {
            if (actors[i].isRunning()) {
                return true;
            }
        }
        return false;
    }

    public String getActorStatus() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < actors.length; ++i) {
            ret.append(actors[i].getThreadName() + "=" + actors[i].getMaxQueueSize() + "|");
        }
        return ret.toString();
    }

    public long[] showSize() {
        long[] sizes = new long[this.actors.length];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = this.actors[i].getQueueSize();
        }
        return sizes;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }
}
