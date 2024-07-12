package com.hm.config.template;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("common_value")
public class CommonValueTemplate {
    private Integer id;
    private Float value;
    private String value_2;
}
