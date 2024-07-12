package com.hm.model.strength;

import com.hm.config.strength.excel.BlockPartsTemplate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreVo implements Comparable<StoreVo>{
    private String uid;
    private int star;
    private int lv;
    private int gridNum;
    // 提供的经验
    private int exp;

    public StoreVo(StrengthStore store, BlockPartsTemplate template){
        if(template == null){
            return;
        }
        this.uid = store.getUid();
        this.lv = store.getLv();
        this.star = template.getStar() + store.getSublimationTimes();
        this.gridNum = template.getGird();
        this.exp = template.getExp();
    }

    @Override
    public int compareTo(StoreVo o) {
        if(this.star != o.getStar()){
            return this.star - o.getStar();
        }
        if(this.gridNum != o.gridNum){
            return this.gridNum - o.gridNum;
        }
        return this.getLv() - o.getLv();
    }
}
