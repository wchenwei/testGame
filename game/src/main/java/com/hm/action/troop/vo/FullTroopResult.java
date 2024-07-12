package com.hm.action.troop.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FullTroopResult {
    private boolean isSuc;

    private long spendGold;
    private long spendItemNum;

    public FullTroopResult(boolean isSuc) {
        this.isSuc = isSuc;
    }
}
