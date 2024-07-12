package com.hm.model.task;


import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskStatus;
import lombok.Data;

/**
 * 抽象任务
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/11 16:25
 */
@Data
public class AbstractTask {
    private int id;//活动id
    private long progress;//任务进度
    private int state;//状态 0-未完成 1-完成未领取奖励 2-已经领取奖励
    private boolean unlock;

    public AbstractTask() {
    }

    public AbstractTask(int id) {
        this.id = id;
    }

    public void incProgress() {
        incProgress(1);
    }

    public void incProgress(long inc) {
        progress += inc;
    }

    /**
     * 任务解锁、且未完成、未领奖，正处于进行中
     *
     * @return
     */
    public boolean isOpen() {
        return unlock && state == TaskStatus.DOING;
    }

    public boolean isComplete() {
        return this.state == TaskStatus.COMPLETE;
    }


    public boolean isEffectSelf() {
        return false;
    }

    /**
     * 更新进度
     *
     * @param player
     * @param observableEnum
     * @param argv
     * @return
     */
    public boolean chkEffectSelf(Player player, ObservableEnum observableEnum, Object... argv) {
        return false;
    }
}
