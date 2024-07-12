package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_41_gift")
@Data
public class Active41GiftTemplate {
    private Integer id;
    private Integer stage;
    private Integer recharge_group;
    private String gift_name;
    private String icon;
    private Integer quality;
    private Integer price_base;
    private Integer buy_times;
    private Integer player_lv_down;
    private Integer player_lv_up;
    private Integer recharge_id;
}
