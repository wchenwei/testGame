package com.hm.model.activity;

import com.hm.model.player.Player;

/**
 * @author wyp
 * @description
 * @date 2022/4/6 11:59
 */
public interface ActivityValueChoice {
    /**
     * @param id
     * @return void
     * @description 玩家选择的 数据
     * @author wyp
     * @date 2022/4/7 9:14
     */
    void setChoice(int id);

    /**
     * @param player
     * @param id
     * @return boolean
     * @description 检查该 数据是否可以被选择
     * @author wyp
     * @date 2022/4/7 9:14
     */
    boolean checkCanChoice(Player player, int id);
}
