package com.hm.model.battle.xiaochu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisCellVo {
    private int x;
    private int y;
    private int id;


    public boolean isEqual(DisCellVo v) {
        return v.getId() == this.id;
    }
}
