package com.hm.libcore.util.thread.timer;


public class TickTimer {
    private boolean running = false;        //运行状态
    private long interval = -1;                //间隔时间
    private long nextTime = -1;                //下次执行时间
    private boolean intervalCheckLog;            //间隔查询错误是否输出

    public TickTimer() {
    }

    public TickTimer(long interval) {
        start(interval);
    }

    public TickTimer(long timeStart, long interval) {
        start(timeStart, interval);
    }

    public TickTimer(long interval, boolean immediate) {
        start(interval, immediate);
    }

    public TickTimer(long timeStart, long interval, boolean immediate) {
        start(timeStart, interval, immediate);
    }

    /**
     * 开始
     *
     * @param interval 执行间隔
     */
    public void start(long interval) {
        start(interval, false);
    }

    /**
     * 开始
     *
     * @param interval 执行间隔
     */
    public void start(long timeStart, long interval) {
        start(timeStart, interval, false);
    }

    /**
     * 开始
     *
     * @param interval  执行间隔
     * @param immediate 立即执行一次
     */
    public void start(long interval, boolean immediate) {
        long timeStart = (System.currentTimeMillis() / interval) * interval;
        start(timeStart, interval, immediate);
    }

    /**
     * 开始
     *
     * @param interval  执行间隔
     * @param immediate 立即执行一次
     */
    public void start(long timeStart, long interval, boolean immediate) {
        //时间间隔
        this.interval = interval;
        //是否立即执行
        if (immediate) {
            this.nextTime = timeStart;
        } else {
            this.nextTime = timeStart + interval;
        }

        this.running = true;
    }

    /**
     * 停止
     */
    public void stop() {
        running = false;
    }

    /**
     * 本次时间已到
     *
     * @param now
     * @return
     */
    public boolean isOnce(long now) {
        //未初始化或已停止
        if (!running) {
            return false;
        }

        //未达到时间
        if (nextTime > now) {
            return false;
        }

        //达成一次后停止
        stop();

        return true;
    }

    /**
     * 周期间隔时间已到
     *
     * @param now
     * @return
     */
    public boolean isPeriod(long now) {
        //未初始化或已停止
        if (!running) {
            return false;
        }

        //间隔时间<=0 会出现严重的逻辑问题 必须进行提示
        if (interval <= 0 && !intervalCheckLog) {
            intervalCheckLog = true;
//			LogCore.core.error("发现执行间隔为0的TickTimer，很可能会引起重大问题，请检查业务逻辑。", new Throwable());
        }

        //未达到时间
        if (nextTime > now) {
            return false;
        }

        //更新周期时间
        nextTime += interval;

        return true;
    }

    /**
     * 是否是开始状态
     *
     * @return
     */
    public boolean isStarted() {
        return running;
    }

    public long getInterval() {
        return interval;
    }

    /**
     * 距最近时间点还有多久，需要配合isOnce或者isPeriod使用
     *
     * @param curr
     * @return
     */
    public long getTimeLeft(long curr) {
        //未初始化或已停止
        if (!running) {
            return 0;
        }

        //已达到时间
        if (nextTime <= curr) {
            return 0;
        }

        return nextTime - curr;
    }

    /**
     * 重新计时
     */
//    public void reStart() {
//        this.nextTime = System.currentTimeMillis() + interval;
//        this.running = true;
//    }

    /**
     * 强制设置下一时刻而不改变间隔
     */
    public void setTimeNext(long timeNext) {
        this.nextTime = timeNext;
    }

    /**
     * 延长nextTime
     *
     * @param extend
     */
    public void extendTimeNext(int extend) {
        this.nextTime += extend;
    }
}
