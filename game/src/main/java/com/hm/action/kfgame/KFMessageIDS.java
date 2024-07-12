package com.hm.action.kfgame;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.KFPServerUrl;
import com.hm.model.player.Player;
import com.hm.rpc.GameRpcManager;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class KFMessageIDS {
    public List<Integer> MessageIDList = Lists.newArrayList(
            MessageComm.C2S_IntoWorld,
            MessageComm.C2S_LeaveWorld,
            MessageComm.C2S_WorldCityFightCurFrame,
            MessageComm.C2S_WorldCityFightData,
            MessageComm.C2S_LeaveCity,
            MessageComm.C2S_WorldCityTroopList,
            MessageComm.C2S_Pvp1v1FightData,
            MessageComm.C2S_DispatchWorldTroop,
            MessageComm.C2S_Troop_ChangeWay,
            MessageComm.C2S_AdvanceWorldTroop,
            MessageComm.C2S_AdvanceCityTroop,
            MessageComm.C2S_RetreatWorldTroop,
            MessageComm.C2S_Troop_PvpOneByOne,
            MessageComm.C2S_HandTroopFullHp,
            MessageComm.C2S_CloneTroop,
            MessageComm.C2S_Troop_StropMove,
            MessageComm.C2S_AirborneTroop
            );

    public boolean isSendKF(Player player, JsonMsg msg) {
        KFPServerUrl kfpServerUrl = player.playerTemp().getKfpServerUrl();
        if(kfpServerUrl != null
                && kfpServerUrl.getType() == 0
                && MessageIDList.contains(msg.getMsgId())) {
            GameRpcManager.sendGameMsg(kfpServerUrl.getUrl(),msg);
            return true;
        }
        return false;
    }
}
