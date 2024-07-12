package com.hm.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.GiftPushTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.model.giftpush.PushItemType;

import java.util.List;
import java.util.Map;

/**
 * 礼包推送配置
 *
 * @author xb
 */
@Config
public class GiftPushConfig extends ExcleConfig {
    private Map<Integer, GiftPushTemplateImpl> giftPushMap = Maps.newConcurrentMap();
    private ArrayListMultimap<Integer, GiftPushTemplateImpl> groupMap = ArrayListMultimap.create();

    private Map<Integer, GiftPushTemplateImpl> rechargeMap = Maps.newConcurrentMap();

    //道具不足单独检查处理
    private ArrayListMultimap<String, GiftPushTemplateImpl> itemNotMap = ArrayListMultimap.create();


    @Override
    public void loadConfig() {
        this.giftPushMap = json2Map(GiftPushTemplateImpl::getId,GiftPushTemplateImpl.class);

        ArrayListMultimap<Integer, GiftPushTemplateImpl> groupMap = ArrayListMultimap.create();
        ArrayListMultimap<String, GiftPushTemplateImpl> itemNotMap = ArrayListMultimap.create();
        for (GiftPushTemplateImpl value : this.giftPushMap.values()) {
            groupMap.put(value.getType().getObserveId(),value);
            if(value.getType() == PushItemType.PIT_5) {
                //根据道具id
                itemNotMap.put(value.getType_parameters().split("#")[0],value);
            }
        }
        this.groupMap = groupMap;
        this.itemNotMap = itemNotMap;

        Map<Integer, GiftPushTemplateImpl> rechargeMap = Maps.newConcurrentMap();
        for (GiftPushTemplateImpl value : this.giftPushMap.values()) {
            for (int rechargeId : value.getRechargeIdList()) {
                rechargeMap.put(rechargeId,value);
            }
        }
        this.rechargeMap = rechargeMap;
    }

    public List<GiftPushTemplateImpl> getCfgList(int obsId) {
        return groupMap.get(obsId);
    }

    public GiftPushTemplateImpl getCfgByRechargeId(int rechargeId) {
        return rechargeMap.get(rechargeId);
    }

    public GiftPushTemplateImpl getCfg(int id) {
        return giftPushMap.get(id);
    }

    public List<GiftPushTemplateImpl> getItemNotList(String itemId) {
        return itemNotMap.get(itemId);
    }

}
