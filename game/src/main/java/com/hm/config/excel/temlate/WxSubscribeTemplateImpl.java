package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import com.hm.action.wx.WXSubsType;
import lombok.Data;

@Data
@FileConfig("wx_subscribe")
public class WxSubscribeTemplateImpl extends WxSubscribeTemplate{
    private WXSubsType subsType;
    private String[] paramArrays;

    public void init() {
        this.subsType = WXSubsType.getWXSubsType(getType());
        this.paramArrays = getParam().split(",");
    }
}
