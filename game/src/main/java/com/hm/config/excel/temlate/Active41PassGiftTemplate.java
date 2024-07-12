package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_41_pass_gift")
@Data
public class Active41PassGiftTemplate {
    private Integer id;
    private Integer recharge_gift_id;
    private Integer pass_exp_add;
    private Integer limit_buy;
    private Integer value;
    private String discount;
}
