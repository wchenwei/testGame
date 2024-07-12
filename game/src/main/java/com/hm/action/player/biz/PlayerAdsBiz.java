package com.hm.action.player.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.templaextra.ActivityArmyFeteTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class PlayerAdsBiz implements IObserver {
    @Resource
    private ActivityConfig activityConfig;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.DayFirstLogin, this);

    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum){
            case DayFirstLogin:
                player.playerAds().doDayReset();
                return;
        }
    }

    public List<Items> calArmyFete(Player player) {
        ActivityArmyFeteTemplate template = activityConfig.getFitArmyFeteTemplate(player.playerLevel().getLv());
        if(template == null) {
            return null;
        }
        return template.getRewards();
    }
}
