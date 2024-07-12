package com.hm.enums;

public enum SpecialPlayerEnum {
    CUSTOMSERVICE(-1, "客服");

    SpecialPlayerEnum(long playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    private long playerId;
    private String playerName;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public static SpecialPlayerEnum getSpecialPlayer(long playerId) {
        for (SpecialPlayerEnum playerEnum : SpecialPlayerEnum.values()) {
            if (playerEnum.playerId == playerId) {
                return playerEnum;
            }
        }
        return null;
    }

}
