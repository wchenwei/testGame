package com.hm.model.task.Random;

import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.RandomTaskType;
import com.hm.model.player.Player;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-22
 */
public class RandomTaskNormal extends BaseRandomTask {

    public RandomTaskNormal() {
        super();
    }

    public RandomTaskNormal(RandomTaskType taskType) {
        super(taskType);
    }

    @Override
    public boolean doFinish(Player player, JsonMsg msg) {
        return true;
    }

}
