package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("lucky_base")
public class LuckyBaseTemplate {
    private Integer id;
    private Integer weight;
    private Integer type;
    private Integer weight_add;
    private Integer wish_pond;
    private Integer weight_add_base;

}
