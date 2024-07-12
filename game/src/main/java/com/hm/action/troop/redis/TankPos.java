package com.hm.action.troop.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TankPos {
    private int id;
    private int index;//位置

    public TankPos(int id, int index) {
        this.id = id;
        this.index = index;
    }
}
