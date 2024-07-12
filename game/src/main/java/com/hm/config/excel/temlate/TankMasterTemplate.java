package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("tank_master")
public class TankMasterTemplate {
    private Integer star_total;
    private String attr_add;

}
