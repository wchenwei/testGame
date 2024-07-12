package com.hm.action.player.biz;

import com.hm.annotation.Broadcast;
import com.hm.enums.PlayerFunctionType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;

@Biz
public class PlayerCommanderBiz extends NormalBroadcastAdapter {

    @Broadcast(value = ObservableEnum.FunctionUnlock,order = {1})
    public void doFunctionUnlock(ObservableEnum observableEnum, Player player, Object... argv) {
        int funcId = (int)argv[0];
        if(funcId == PlayerFunctionType.MilitaryRank.getType()) {
            player.playerCommander().setMilitaryLv(1);
        }
        if(funcId == PlayerFunctionType.Mounts.getType()) {
            player.playerCommander().setCarLv(1);
        }
    }

    @Broadcast(value = ObservableEnum.LOGIN)
    public void doLoginCheck(ObservableEnum observableEnum, Player player, Object... argv) {
        if(player.getPlayerFunction().isOpenFunction(PlayerFunctionType.MilitaryRank)
                &&  player.playerCommander().getMilitaryLv() <= 0) {
            player.playerCommander().setMilitaryLv(1);
        }
        if(player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mounts)
                &&  player.playerCommander().getCarLv() <= 0) {
            player.playerCommander().setCarLv(1);
        }
    }

}
