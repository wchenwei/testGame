package com.hmkf.model;

public interface IPKPlayer {
    void addScore(long add);
    long getScore();
    String getRankId();
    String getPlayerId();

    long getCombat();

    void save();

    default boolean isNpc() {
        return false;
    }

    default void sendPlayerUpdate() {
        save();
    }


}
