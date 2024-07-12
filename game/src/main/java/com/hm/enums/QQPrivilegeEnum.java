package com.hm.enums;

import com.hm.config.excel.templaextra.ActiveRewardQqdatingTemplateImpl;
import com.hm.model.activity.qqprivilege.PrivilegeValue;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;

import java.util.Arrays;

/**
 * @author wyp
 * @description
 *      qq 大厅特权
 * @date 2021/10/8 9:46
 */
public enum QQPrivilegeEnum {
    newcomerPackage(0, "新手礼包"),
    lvGift(2, "等级礼包") {
        @Override
        public boolean checkCanReward(Player player, PrivilegeValue privilegeValue, ActiveRewardQqdatingTemplateImpl privilege) {
            int lv = player.playerLevel().getLv();
            // 判断等级
            if (lv < privilege.getLv_down()) {
                return false;
            }
            return super.checkCanReward(player, privilegeValue, privilege);
        }
    },
    dayGift(1, "每日礼包") {
        @Override
        public boolean checkCanReward(Player player, PrivilegeValue privilegeValue, ActiveRewardQqdatingTemplateImpl privilege) {
            int lv = player.playerLevel().getLv();
            // 判断等级
            if (!privilege.checkPlayerLv(lv)) {
                return false;
            }
            return privilegeValue.isDayFlag();
        }

        @Override
        public void setRewardState(BasePlayer player, PrivilegeValue privilegeValue, int id) {
            privilegeValue.setDayFlag(false);
            player.getPlayerActivity().SetChanged();
        }
    },

    ;

    private QQPrivilegeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public boolean checkCanReward(Player player, PrivilegeValue privilegeValue, ActiveRewardQqdatingTemplateImpl privilege) {
        return privilegeValue.checkCanReward(player, privilege.getId());
    }

    public void setRewardState(BasePlayer player, PrivilegeValue privilegeValue, int id) {
        privilegeValue.setRewardState(player, id);
    }

    public static QQPrivilegeEnum getType(int type) {
        return Arrays.asList(QQPrivilegeEnum.values())
                .stream()
                .filter(e -> e.getType() == type)
                .findFirst()
                .orElse(null);
    }

    private int type;

    private String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
