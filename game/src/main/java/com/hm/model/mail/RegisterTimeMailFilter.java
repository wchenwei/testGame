package com.hm.model.mail;

import com.hm.config.GameConstants;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;

/**
 * 按照玩家注册时间过滤
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/12/16 10:58
 */
@NoArgsConstructor
public class RegisterTimeMailFilter implements IMailFilter {
    private long startTime;
    private long endTIme;

    public RegisterTimeMailFilter(long startTime, long endTIme) {
        this.startTime = startTime;
        this.endTIme = endTIme;
    }

    @Override
    public boolean isFitPlayer(Player player) {
        long createTime = player.playerBaseInfo().getCreateDate().getTime();
        if(createTime < startTime || createTime > endTIme) {
            return false;
        }
//        ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
//        long mdate = serverData.getServerMergeData().getDate();//合服时间
//        if(mdate > 0 && createTime < mdate) {
//            return false;
//        }
        if(System.currentTimeMillis() - createTime > 10* GameConstants.MINUTE) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkAutoDelForTime() {
        return false;
    }
}
