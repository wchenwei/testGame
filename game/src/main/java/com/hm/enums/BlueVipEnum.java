package com.hm.enums;

import com.hm.config.excel.templaextra.ActiveRewardQqvipTemplateImpl;
import com.hm.model.player.Player;
import com.hm.util.bluevip.BlueVipUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;

/**
 * @author wyp
 * @description 蓝钻礼包类型
 * @date 2021/8/4 14:33
 */
@NoArgsConstructor
@AllArgsConstructor
public enum BlueVipEnum {

    newcomerPackage(0, "新手礼包") {
        @Override
        public boolean checkCanReceive(Player player, Map<String, Object> params, ActiveRewardQqvipTemplateImpl template) {
            BlueVipUtil.checkCanReceive(player, params, BlueVipEnum.blueVip);
            if (!player.playerBlueVip().checkBlueVip() || !player.playerBlueVip().checkCanRec(template.getId())) {
                return false;
            }
            return true;
        }

        @Override
        public void addGift(Player player, int id) {
            player.playerBlueVip().addGift(id);
        }
    },
    blueVip(1, "is_blue_vip", StatisticsType.Blue_Vip) {
        @Override
        public boolean checkCanReceive(Player player, Map<String, Object> params, ActiveRewardQqvipTemplateImpl template) {
            super.checkCanReceive(player, params, template);
            long todayStatistics = player.getPlayerStatistics().getTodayStatistics(StatisticsType.Blue_Vip);
            if (todayStatistics > 0) {
                return false;
            }
            // 校验蓝钻等级是否正确
            int blueVipLv = player.playerBlueVip().getBlueVipLv();
            if (!player.playerBlueVip().checkBlueVip() || blueVipLv < template.getLv()) {
                return false;
            }
            return true;
        }
    },
    lvGift(2, "等级礼包") {
        @Override
        public boolean checkCanReceive(Player player, Map<String, Object> params, ActiveRewardQqvipTemplateImpl template) {
            if (!player.playerBlueVip().checkCanRec(template.getId())) {
                return false;
            }
            // 判断玩家等级信息
            if (player.playerLevel().getLv() < template.getLv()) {
                return false;
            }
            BlueVipUtil.checkCanReceive(player, params, BlueVipEnum.blueVip);
            if (!player.playerBlueVip().checkBlueVip()) {
                return false;
            }
            return true;
        }

        @Override
        public void addGift(Player player, int id) {
            player.playerBlueVip().addGift(id);
        }
    },
    superBlueVip(3, "is_super_blue_vip", StatisticsType.super_blue_vip),
    blueYearVip(4, "is_blue_year_vip", StatisticsType.blue_year_vip),
    ;

    private BlueVipEnum(int type, String title) {
        this.type = type;
        this.title = title;
    }

    private int type;
    private String title;
    private StatisticsType statisticsType;

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public static BlueVipEnum getVipEnum(int type) {
        BlueVipEnum vipTitle = Arrays.stream(BlueVipEnum.values()).filter(e -> e.getType() == type).findFirst().orElse(null);
        return vipTitle;
    }

    public boolean checkCanReceive(Player player, Map<String, Object> params, ActiveRewardQqvipTemplateImpl template) {
        long todayStatistics = player.getPlayerStatistics().getTodayStatistics(this.statisticsType);
        if (todayStatistics > 0) {
            return false;
        }
        return BlueVipUtil.checkCanReceive(player, params, this);
    }

    public void addGift(Player player, int id) {
        player.getPlayerStatistics().addTodayStatistics(this.statisticsType, 1);
    }
}
