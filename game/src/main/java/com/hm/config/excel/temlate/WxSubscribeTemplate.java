package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("wx_subscribe")
public class WxSubscribeTemplate {
    private int id;
    private int type;
    private String gameid;
    private String wx_key;
    private String param;
}
