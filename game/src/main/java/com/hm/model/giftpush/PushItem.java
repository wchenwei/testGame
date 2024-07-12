package com.hm.model.giftpush;

import cn.hutool.core.collection.CollUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.templaextra.GiftPushTemplateImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 激活礼包详情
 *
 * @author xb
 */
@Data
@NoArgsConstructor
public class PushItem {
    /**
     * @see GiftPushTemplateImpl#getId()
     */
    private int id;
    private long endTime;//截止时间 now>endTime 不能再买了
    private List<Integer> rechargeIdList;

    public PushItem(GiftPushTemplateImpl template) {
        this.id = template.getId();
        this.endTime = System.currentTimeMillis() + template.getLast_time() * GameConstants.SECOND;;
        this.rechargeIdList = template.getRechargeIdList();
    }

    public void doBuy(Integer giftId) {
        this.rechargeIdList.remove(giftId);
    }

    public boolean isCanDel() {
        return System.currentTimeMillis() > this.endTime || CollUtil.isEmpty(rechargeIdList);
    }

}
