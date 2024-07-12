package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("wx_game")
public class WxGameTemplate {
    private String gameid;
    private String wx_id;
    private String wx_key;
}
