package com.hm.observer;
/**
 * 每日任务状态
 *
 */
public class TaskStatus {

	/**
     * 每日任务状态--未完成
     */
    public static final int DOING = 0;
    /**
     * 每日任务状态--已完成未领奖
     */
    public static final int COMPLETE = 1;
    
    /**
     * 每日任务状态--已领奖
     */
    public static final int REWARDED = 2;
//    /**
//     * 每日任务状态--未领取
//     */
//    public static final int NOGET = 4;
//    /**
//     * 每日任务状态--失败
//     */
//    public static final int FAIL = 5;
//    /**
//     * 每日任务状态--全部完成
//     */
//    public static final int FINISH = 6;
    

    private TaskStatus() {
    }
}
