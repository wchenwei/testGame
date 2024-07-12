package com.hm.model.activity.qqprivilege;

import com.hm.model.activity.PlayerActivityIdListValue;
import lombok.Data;

/**
 * @author wyp
 * @description
 * @date 2021/10/8 9:33
 */
@Data
public class PrivilegeValue extends PlayerActivityIdListValue {
    // 每日礼包领取
    private boolean dayFlag = true;

    public void resetDay() {
        dayFlag = true;
    }
}
