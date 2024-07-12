package com.hm.model.task.Random;

import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.enums.LogType;
import com.hm.enums.RandomTaskType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

/**
 * Description:需要消耗的任务
 * User: yang xb
 * Date: 2019-04-22
 *
 * @author Administrator
 */
public class RandomTaskCost extends BaseRandomTask {
    private Items costItems;

    public RandomTaskCost() {
        super();
    }

    public RandomTaskCost(RandomTaskType taskType) {
        super(taskType);
    }

    public void setCostItems(Items costItems) {
        this.costItems = costItems;
    }

    public boolean checkAndSpend(Player player) {
        ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
        return itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.RandomTask);
    }

    @Override
    public boolean doFinish(Player player, JsonMsg msg) {
        return checkAndSpend(player);
    }

}
