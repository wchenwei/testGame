package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("field_boss_base")
public class FieldBossBaseTemplate {
    private Integer id;
    private String open_day;
    private String reward;
}
