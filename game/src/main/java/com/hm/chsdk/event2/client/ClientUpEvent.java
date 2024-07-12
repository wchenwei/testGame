package com.hm.chsdk.event2.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import java.util.Map;

/**
 * 客户端上传
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2022/11/14 16:45
 */
@EventMsg(obserEnum = ObservableEnum.CHClientUp)
public class ClientUpEvent extends CommonParamEvent {
    private transient Map<String, Object> hash;

    @Override
    public void init(Player player, Object... argv) {
        //客户端上传
        String groupId = (String) argv[0];
        String groupName = (String) argv[1];
        String eventId = (String) argv[2];
        String eventName = (String) argv[3];

        this.hash = (Map<String, Object>) argv[4];

        this.event_id = Convert.toInt(eventId);
        this.event_name = eventName;
        this.event_type_id = groupId;
        this.event_type_name = groupName;
    }

    @Override
    public String toEventJson() {
        if (CollUtil.isEmpty(hash)) {
            return super.toEventJson();
        } else {
            Map<String, Object> userMap = BeanUtil.beanToMap(this);
            userMap.remove("hash");
            userMap.putAll(hash);
            return GSONUtils.ToJSONString(userMap);
        }
    }
}
