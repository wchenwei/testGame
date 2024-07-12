package com.hm.model.giftpush;

import cn.hutool.core.convert.Convert;
import com.hm.config.excel.templaextra.GiftPushTemplateImpl;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.util.StringUtil;

/**
 * @author xb
 */

public enum PushItemType {
    PIT_1(1, ObservableEnum.ClearnceMission.getEnumId()) { //主线

        @Override
        public boolean isFit(Player player, GiftPushTemplateImpl cfg, Object[] argv) {
            int fbId = player.playerMission().getFbId();
            return Convert.toInt(cfg.getType_parameters(), 0) == fbId;
        }
    },

    PIT_2(2, ObservableEnum.MilitaryLvUp.getEnumId()) {//军衔

        @Override
        public boolean isFit(Player player, GiftPushTemplateImpl cfg, Object[] argv) {
            return Convert.toInt(cfg.getType_parameters(), 0) == player.playerCommander().getMilitaryLv();
        }
    },

    PIT_3(3, ObservableEnum.TankAdd.getEnumId()) { //连获得特定tank
        @Override
        public boolean isFit(Player player, GiftPushTemplateImpl cfg, Object[] argv) {
            int tankId = (int) argv[0];
            int para = Convert.toInt(cfg.getType_parameters(), 0);
            return tankId == para;
        }
    },

    PIT_4(4, ObservableEnum.MainFbFail.getEnumId()) {//关卡失败
        @Override
        public boolean isFit(Player player, GiftPushTemplateImpl cfg, Object[] argv) {
            int lv = player.playerCommander().getMilitaryLv();
            int[] range = StringUtil.strToIntArray(cfg.getType_parameters(),":");
            return lv >= range[0] && lv <= range[1];
        }
    },

    PIT_5(5, ObservableEnum.ItemNotEnough.getEnumId()) {//道具不足
        @Override
        public boolean isFit(Player player, GiftPushTemplateImpl cfg, Object[] argv) {
            String[] prams = cfg.getType_parameters().split("#");
            int lv = player.playerCommander().getMilitaryLv();
            int[] range = StringUtil.strToIntArray(prams[1],":");
            return lv >= range[0] && lv <= range[1];
        }
    },


    ;

    PushItemType(int id, int observeId) {
        this.id = id;
        this.observeId = observeId;
    }

    public int getObserveId() {
        return observeId;
    }

    public boolean isFit(Player player, GiftPushTemplateImpl cfg, Object[] argv){
        return false;
    }

    public static PushItemType num2enum(int id) {
        for (PushItemType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    private int id;
    private int observeId;
}
