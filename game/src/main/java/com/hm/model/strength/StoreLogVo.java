package com.hm.model.strength;

import com.hm.config.strength.excel.BlockPartsTemplateImpl;
import lombok.NoArgsConstructor;

/**
 * @introduce:
 *      机甲配件 日志
 * @author: wyp
 * @DATE: 2023/9/25
 **/
@NoArgsConstructor
public class StoreLogVo {
    private String uid;
    private int id;
    private int star;
    private int gridNum;
    private String extra;

    public StoreLogVo(StrengthStore store, BlockPartsTemplateImpl randomBlockPart, String extra){
        this.uid = store.getUid();
        this.id = store.getId();
        this.star = randomBlockPart.getStar();
        this.gridNum = randomBlockPart.getGird();
        this.extra = extra;
    }


}
