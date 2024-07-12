package com.hm.libcore.rpc;

import com.hm.libcore.msg.JsonMsg;

import java.util.List;

public interface IGameRpc extends IRPC{
    void sendPlayerHMResponse(long playerId,byte[] data);
    boolean addOrSpendPlayerItem(long playerId,String items,boolean isAdd,String log);

    void sendServerMail(int serverId, List<KFPMail> mailList);
    void sendPlayerMail(long playerId, KFPMail mail);
    void sendServerMail(KFServerMail mail);

    void sendPlayerObs(long playerId,int obId, Object... args);
    void sendServerObs(int serverId,int obId, Object... args);

    void sendGameMsg(JsonMsg msg);

    boolean handFullTankHp(long playerId,long totalSecond);

    //发放金融战奖励
    void sendGuildFinanceReward(long playerId, int cityId, String items);
    //计算clone部队消耗
    String calCloneTroopSpend(long playerId);

    void sendKfExpeditionServerResult(String serverResult);
    void sendKfBackOilServerResult(String serverResult);

    //处理跨服轮空
    void doKFNoMatch(int serverId,int kfType);
}
