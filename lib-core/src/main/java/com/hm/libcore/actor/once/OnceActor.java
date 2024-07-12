package com.hm.libcore.actor.once;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Sets;
import com.hm.libcore.util.thread.timer.TickTimer;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 执行队列 此队列会判断当前是否包含此对象
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/25 17:45
 */
@Slf4j
public class OnceActor {
    private final BlockingQueue<IOnceTask> taskQueue;
    private Thread t = null;
    private AtomicBoolean running = new AtomicBoolean(false);
    private String name = "";
    private AtomicBoolean stopWhenEmpty = new AtomicBoolean(false);
    private AtomicInteger maxTaskCount = new AtomicInteger(0);
    private Set<Object> idList = Sets.newConcurrentHashSet();
    public ExecutorService threadExecutor;
    private TickTimer tickTimer;//执行时间间隔
    private int threadNum;


    public OnceActor(int threadNum, long interval, String name) {
        this(threadNum, interval, name, false);
    }

    public OnceActor(int threadNum, long interval, String name, boolean noExecutor) {
        this(threadNum, interval, name, 80 * 1000, noExecutor);
    }

    public OnceActor(int threadNum, long interval, String name, int capacity, boolean noExecutor) {
        this.name = "Once-queue-" + name;
        if (capacity <= 0) {
            // 无限容量
            this.taskQueue = new LinkedBlockingDeque<>();
        } else {
            // 有限容量
            this.taskQueue = new LinkedBlockingDeque<>(capacity);
        }
        this.threadNum = threadNum;
        if (!noExecutor) {
            int poolSize = Math.min(threadNum, 8);
            this.threadExecutor = ThreadUtil.newExecutor(poolSize, poolSize, 3000);
        }
        if (interval > 0) {
            this.tickTimer = new TickTimer(interval);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "|" + getThreadName() + "|" + getThreadId();
    }

    public boolean start() {
        if (running.get()) {
            return false;
        }
        running.set(true);
        t = new Thread(new TaskRunner(), name);
        t.start();
        return true;
    }

    public void clear() {
        taskQueue.clear();
    }

    /**
     * 暴力关
     */
    public void stop() {
        if (!running.get()) {
            return;
        }
        running.set(false);
        t.interrupt();

    }

    /**
     * 自动关
     */
    public void stopWhenEmpty() {
        if (stopWhenEmpty.get()) {
            return;
        }
        stopWhenEmpty.set(true);
    }

    public void waitForStop() {
        while (isRunning()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("waitForStop ", e);
                break;
            }
        }
    }

    public int getQueueSize() {
        return this.taskQueue.size();
    }

    public int getMaxQueueSize() {
        return this.maxTaskCount.get();
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isStopping() {
        return stopWhenEmpty.get();
    }

    public boolean put(IOnceTask task) {
        if (task == null) {
            return true;
        }
        if (stopWhenEmpty.get()) {
            log.error("Actor is stopping: {} , ignore: ", this.toString());
            return false;
        }
        if (!running.get()) {
            String msg = "Actor is not running, invalid put: " + this.getThreadName();
            log.error("", new IllegalStateException(msg));
            return false;
        }
        // 本actor直接运行
        if (Thread.currentThread() == t) {
            try {
                runTask(task);
            } catch (Throwable e) {
                log.error("put ", e);
                e.printStackTrace();
            }
            return true;
        }
        // 加入队列
        try {
            //
            if (this.idList.contains(task.getId())) {
                return true;
            }
            this.idList.add(task.getId());
            int size = taskQueue.size();
            if (maxTaskCount.get() < size) {
                this.maxTaskCount.set(size);
            }
            taskQueue.put(task);
            return true;
        } catch (InterruptedException e) {
            log.error("put ", e);
            return false;
        }
    }

    public long getThreadId() {
        return t == null ? 0 : t.getId();
    }

    public String getThreadName() {
        return t == null ? "" : t.getName();
    }

    //当前是否可以执行
    public boolean isCanTask() {
        return this.tickTimer == null || this.tickTimer.isPeriod(System.currentTimeMillis());
    }

    //获取同时执行任务数量
    public int getRunningTaskNum() {
        return this.tickTimer == null ? 1 : this.threadNum;
    }

    private class TaskRunner implements Runnable {

        public void run() {
            while (running.get()) {
                try {
                    if (stopWhenEmpty.get() && taskQueue.isEmpty()) {
                        running.set(false);
                        break;
                    }
                    if (isCanTask()) {
                        for (int i = 0; i < getRunningTaskNum(); i++) {
                            final IOnceTask task = taskQueue.poll(1000L, TimeUnit.MILLISECONDS);
                            runTask(task);
                        }
                    }
                } catch (InterruptedException e) {
                    log.error("TaskRunner taskQueue InterruptedException : ", e);
                } catch (Throwable e) {
                    log.error("TaskRunner Throwable : ", e);
                }
            }
        }
    }

    private void runTask(final IOnceTask task) {
        if (task == null) {
            return;
        }
        this.idList.remove(task.getId());
        try {
            if (this.threadExecutor == null) {
                task.doOnceTask();
                return;
            }
            this.threadExecutor.submit(task);
        } catch (Exception e) {
            System.out.println(this.name + " ->" + task.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}