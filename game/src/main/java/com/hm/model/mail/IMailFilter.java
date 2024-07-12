package com.hm.model.mail;

import com.hm.model.player.Player;

public interface IMailFilter {
    boolean isFitPlayer(Player player);

    default boolean checkAutoDelForTime() {
        return true;
    }
}