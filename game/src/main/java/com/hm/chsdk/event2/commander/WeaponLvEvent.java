package com.hm.chsdk.event2.commander;

import com.hm.model.player.Player;

/**
 * @ClassName WeaponLvEvent
 * @Deacription 超级武器升级
 * @Author zxj
 * @Date 2022/3/11 15:35
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.SuperWeaponLv)
public class WeaponLvEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9017";
        event_name = "指挥官超级武器";
        int lv = player.playerCommander().getSuperWeaponLv();
        if(lv>1) {
            return;
        }
        this.Level_Before = 0;
        this.Level_After = 1;
    }

    private int Level_Before;
    private int Level_After;
    private String type;
    private String cost;
    private String remain;
}
