package com.hm.model.task.Random;

import cn.hutool.core.util.RandomUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.GameConstants;
import com.hm.enums.RandomTaskStatus;
import com.hm.enums.RandomTaskType;
import com.hm.model.player.Player;
import com.hm.util.MathUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-12
 *
 * @author Administrator
 */
@Data
@NoArgsConstructor
public abstract class BaseRandomTask {
    //事件所在城市
    private int cityId;
    //RandomTaskType::getType
    private int type;
    //状态
    private int status;
    //状态结束时间
    private long stateEndTime;

    public BaseRandomTask(int type) {
        this.type = type;
//        this.status = RandomTaskStatus.NotAccepted.getType();
//        //20分钟后自动刷新
//        this.stateEndTime = System.currentTimeMillis() + 20 * GameConstants.MINUTE;
        //自动接受
        doAccept();
    }

    public BaseRandomTask(RandomTaskType taskType) {
        this(taskType.getType());
    }

    //是否客户端完成
    public abstract boolean doFinish(Player player, JsonMsg msg);

    //完成之后的处理
    public void doFinishAfter(Player player, boolean isSuc) {
        this.status = isSuc ? RandomTaskStatus.Finish.getType() : RandomTaskStatus.Wait.getType();
        if (status == RandomTaskStatus.Wait.getType()) {
            this.stateEndTime = System.currentTimeMillis() + RandomUtil.randomInt(1, 10) * GameConstants.MINUTE;
        } else {
            this.stateEndTime = -1L;
        }
    }

    //领取奖励
    public void doReward(Player player) {
        this.status = RandomTaskStatus.Wait.getType();
        this.stateEndTime = System.currentTimeMillis() + RandomUtil.randomInt(1, 4) * GameConstants.SECOND;
        player.playerRandomTask().incCount();
    }

    //事件触发完成
    public boolean doObservableFinish(Player player, Object... argv) {
        return false;
    }

    public boolean isCanAccept() {
        return this.status == RandomTaskStatus.NotAccepted.getType() && System.currentTimeMillis() <= stateEndTime;
    }

    public boolean isCanReject() {
        return this.status == RandomTaskStatus.Accept.getType() && System.currentTimeMillis() <= stateEndTime;
    }

    /**
     * 处理接受
     */
    public void doAccept() {
        this.status = RandomTaskStatus.Accept.getType();
        //60分钟后
        this.stateEndTime = System.currentTimeMillis() + 60 * GameConstants.MINUTE;
    }

    public void doReject() {
        this.status = RandomTaskStatus.Wait.getType();
        // 放弃后刷新cd改为2～～3分钟
        this.stateEndTime = System.currentTimeMillis() + MathUtils.random(2, 4) * GameConstants.MINUTE;
    }

    public RandomTaskType getTaskType() {
        return RandomTaskType.id2Type(type);
    }

    public RandomTaskStatus getTaskStatus() {
        return RandomTaskStatus.id2Status(status);
    }

    //是否可以自动刷新
    public boolean isCanAutoRefresh() {
        if(status == RandomTaskStatus.NotAccepted.getType()) {
            doAccept();
        }
        if(status == RandomTaskStatus.Accept.getType() || status == RandomTaskStatus.Wait.getType()) {
            return System.currentTimeMillis() >= this.stateEndTime;
        }
        return false;
//        //未接受或已接受且已过期
//        if ((status == RandomTaskStatus.NotAccepted.getType() || status == RandomTaskStatus.Accept.getType()) && System.currentTimeMillis() >= this.stateEndTime) {
//            //自动放弃
//            doReject();
//            return false;
//        }
//        return status == RandomTaskStatus.Wait.getType() && System.currentTimeMillis() >= this.stateEndTime;
    }

    //是否可以手动刷新任务
    public boolean isCanRefreshHand() {
        if(status == RandomTaskStatus.Wait.getType()) {
            return System.currentTimeMillis() >= this.stateEndTime;
        }
        return status != RandomTaskStatus.Finish.getType();
    }
}
