package com.hm.model.battle.xiaochu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FillCellInfo {
    private int id;
    private int colorId;
    private int npcId;
    private int score;

    public FillCellInfo(Cell cellInfo) {
        this.id = cellInfo.getId();
        this.colorId = cellInfo.getColorId();
        this.npcId = cellInfo.getNpcId();
        this.score = cellInfo.getScore();
    }
}
