package com.hm.enums;

import cn.hutool.core.convert.Convert;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.ShareTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.util.MathUtils;

import java.util.Arrays;
import java.util.List;

public enum ShareType {
    Null(0, "null") {
        @Override
        public boolean onShare(Player player, ShareTemplate cfg) {
            return false;
        }

        @Override
        public List<Items> onShareReward(Player player, ShareTemplate cfg) {
            return null;
        }
    },
    Treasury(2, "征收分享") {
        @Override
        public boolean onShare(Player player, ShareTemplate cfg) {
            CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
            int minInterval = commValueConfig.getCommValue(CommonValueType.TreasuryMinInterval);
            long lastTime = player.playerTreasury().getLastTime();
            long now = System.currentTimeMillis();
            int interval = (int) MathUtils.div(now - lastTime, GameConstants.MINUTE);
            // ②在征收界面，点击完成分享后，当前征收的时间增加60分钟，并且只在已累计时间少于5分钟时可以进行分享；
            if (interval > minInterval) {
                return false;
            }

            Integer add = Convert.toInt(cfg.getEffect_value(), 0);
            player.playerTreasury().setLastTime(lastTime - add * GameConstants.SECOND);
            return true;
        }

        @Override
        public List<Items> onShareReward(Player player, ShareTemplate cfg) {
            return null;
        }
    },
    ;
    private int type;
    private String desc;

    ShareType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ShareType getType(Integer type) {
        return Arrays.stream(ShareType.values()).filter(e -> e.getType() == type).findFirst().orElse(Null);
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public abstract boolean onShare(Player player, ShareTemplate cfg);
    public abstract List<Items> onShareReward(Player player, ShareTemplate cfg);
    public boolean canDel(BasePlayer player) { return false;}
}
