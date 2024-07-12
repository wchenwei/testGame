package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("lucky_pond")
public class LuckyPondTemplate {
    private Integer id;
    private Integer pond_id;
    private String reward;
    private Integer weight;

}
