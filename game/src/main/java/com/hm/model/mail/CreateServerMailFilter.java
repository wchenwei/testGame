package com.hm.model.mail;

import com.hm.config.GameConstants;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class CreateServerMailFilter implements IMailFilter {
    private int serverId;

    public CreateServerMailFilter(int serverId) {
        this.serverId = serverId;
    }

    @Override
    public boolean isFitPlayer(Player player) {
        long createTime = player.playerBaseInfo().getCreateDate().getTime();
        if(System.currentTimeMillis() - createTime > 10* GameConstants.MINUTE) {
            return false;
        }

        return player.getCreateServerId() == serverId;
    }

    @Override
    public boolean checkAutoDelForTime() {
        return false;
    }
}
