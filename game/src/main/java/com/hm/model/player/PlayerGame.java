package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.GameType;

import java.util.Map;

/**
 * @author xjt
 * @version 1.0
 * @desc 小游戏
 * @date 2022/2/11 10:21
 */
public class PlayerGame extends PlayerDataContext {
    private Map<Integer, BaseSamllGame> games = Maps.newConcurrentMap();

    public BaseSamllGame getGame(GameType gameType) {
        return games.get(gameType.getType());
    }

    public void unlockGame(BaseSamllGame game) {
        this.games.put(game.getId(), game);
        SetChanged();
    }

    public boolean isUnlockGame(int gameType) {
        return this.games.containsKey(gameType);
    }

    public long getScoreSum() {
        return this.games.values().stream().mapToLong(smllGame->smllGame.getScore()).sum();
    }

    public void dayReset(){
        games.values().forEach(e->e.dayReset());
        SetChanged();
    }
    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("playerGame", this);
    }
}
