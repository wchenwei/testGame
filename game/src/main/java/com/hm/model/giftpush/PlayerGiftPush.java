package com.hm.model.giftpush;

import com.google.common.collect.Lists;
import com.hm.config.excel.templaextra.GiftPushTemplateImpl;
import com.hm.libcore.msg.JsonMsg;
import com.hm.model.player.PlayerDataContext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 礼包推送配置
 *
 * @author xb
 */
public class PlayerGiftPush extends PlayerDataContext {
    private ConcurrentHashMap<Integer, PushItem> pushItemMap = new ConcurrentHashMap<>();

    private transient List<Integer> delIdList = Lists.newArrayList();//已经删除的礼包

    public void addPushItem(PushItem item) {
        this.pushItemMap.put(item.getId(), item);
        SetChanged();
    }

    public void doBuyRecharge(GiftPushTemplateImpl template, int giftId) {
        int id = template.getId();
        PushItem pushItem = this.pushItemMap.get(id);
        if(pushItem == null) {
            return;
        }
        pushItem.doBuy(giftId);
        if(pushItem.isCanDel()) {
            delPushItem(pushItem);
        }
        SetChanged();
    }

    public PushItem getPushItem(int id) {
        return pushItemMap.get(id);
    }

    //检查礼包是否过期
    public void checkGiftOverTime() {
        for (PushItem value :  this.pushItemMap.values()) {
            if(value.isCanDel()) {
                delPushItem(value);
            }
        }
    }

    private void delPushItem(PushItem value) {
        this.delIdList.add(value.getId());
        this.pushItemMap.remove(value.getId());
        SetChanged();
    }

    public boolean havePushItem(int id) {
        return this.pushItemMap.containsKey(id) || this.delIdList.contains(id);
    }


    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerGiftPush", this);
    }
}
