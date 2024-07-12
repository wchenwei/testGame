package com.hm.action.tips;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.BaseTips;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import java.util.List;

@Biz
public class TipsBiz implements IObserver {

    public void sendPlayerTips(Player player, BaseTips tips){
        if (player.isOnline()){
            sendPlayerTips(player, Lists.newArrayList(tips));
        } else {
            player.playerTips().addTips(tips);
        }
    }

    public void sendPlayerTips(Player player, List<BaseTips> tipsList){
        JsonMsg jsonMsg = new JsonMsg(MessageComm.S2C_Player_Tips);
        jsonMsg.addProperty("tips", tipsList);
        player.sendMsg(jsonMsg);
    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.LOGIN,this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        List<BaseTips> tipsList = player.playerTips().getTipsList();
        sendPlayerTips(player, tipsList);
        player.playerTips().clear();
    }



}
