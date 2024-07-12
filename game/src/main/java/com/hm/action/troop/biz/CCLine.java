package com.hm.action.troop.biz;

import com.hm.model.player.Player;
import lombok.Data;

@Data
public class CCLine {
    private int startId;
    private int toId;
    private int weight;

    public CCLine(int startId, int toId, int weight) {
        this.startId = Math.min(startId,toId);
        this.toId = Math.max(startId,toId);
        this.weight = weight;
    }

    public boolean isUnlock(Player player) {
        return player.isUnlockCity(this.startId) && player.isUnlockCity(this.toId);
    }

    public String getId() {
        return this.startId+"_"+this.toId;
    }

    public boolean containCity(int id) {
        return startId == id || toId == id;
    }

    public void changeMaxWeight() {
        this.weight = 10000;
    }
}
