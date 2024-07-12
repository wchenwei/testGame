package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName CHShowPhotoEvent
 * @Deacription 纪念馆，贴照片
 * @Author zxj
 * @Date 2022/3/11 17:57
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHShowPhoto)
public class CHShowPhotoEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9022";
        event_name = "指挥官纪念馆收集";
        this.photoid = Convert.toInt(argv[0]);
        this.count = Convert.toInt(argv[1]);
    }
    private int photoid;
    private int count;
}
