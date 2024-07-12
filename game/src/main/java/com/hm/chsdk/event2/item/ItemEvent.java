package com.hm.chsdk.event2.item;

import cn.hutool.core.util.StrUtil;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.LogType;
import com.hm.model.player.Player;

/**
 * @ClassName ItemEvent
 * @Deacription ItemEvent
 * @Author zxj
 * @Date 2022/3/4 10:39
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.ItemChange)
public class ItemEvent extends CommonParamEvent {
    @Override
    public void init(Player player, Object... argv) {
        event_id = "5001";
        event_name = "道具相关";
        event_type_id="005";
        event_type_name="道具相关";

        this.Profession_Id = player.getGuildId();
        //itemId, itemType, num, logType, 0
        this.type = (int) argv[4];
        this.itemtype = (int) argv[1];
        this.itemid = (int) argv[0];
        this.count = (long) argv[2];
        LogType log = (LogType)argv[3];
        if(null!=log && StrUtil.isNotEmpty(log.getExtra())) {
            this.event_id =  String.format("%s_%s", log.getCode(), log.getExtra());
        }else if(null!=log) {
            this.event_id =  String.format("%s", log.getCode());
        }
    }

    private int Profession_Id;
    private int type;
    private int itemtype;
    private long itemid;
    private long count;
}
