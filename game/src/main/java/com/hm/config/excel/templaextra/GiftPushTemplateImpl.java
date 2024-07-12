package com.hm.config.excel.templaextra;

import com.hm.config.excel.temlate.GiftPushTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.giftpush.PushItemType;
import com.hm.util.StringUtil;

import java.util.List;

@FileConfig("gift_push")
public class GiftPushTemplateImpl extends GiftPushTemplate {
    private PushItemType type;

    @ConfigInit
    public void init() {
        type = PushItemType.num2enum(getType_id());
    }

    public PushItemType getType() {
        return type;
    }


    //防止不同玩家用同一list
    public List<Integer> getRechargeIdList() {
        return StringUtil.splitStr2IntegerList(getRecharge_id(),",");
    }
}
