package com.hm.chsdk.event2;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.enums.LogType;
import com.hm.enums.StatisticsType;
import com.hm.model.player.Player;
import lombok.Getter;

import java.net.URLEncoder;
import java.util.Date;

/**
 * @ClassName CommonParamEvent
 * @Deacription TODO
 * @Author zxj
 * @Date 2022/3/3 0:06
 * @Version 1.0
 **/
@Getter
public abstract class CommonParamEvent implements CommonParamInter {
    public String gv;                   //游戏版本号
    public String td;                   //时间戳 日志字符串时间(yyyy-mm-dd hh:mm:ss)
    public String event_type_id;        //事件类型编号
    public String event_type_name;      //事件类型名称 道具获得
    public Object event_id;             //事件编号 根据研发事件编号枚举
    public String event_name;           //事件名称 根据研发玩家日志名称枚举
    public int server_cp_id;         //服务器编号（区服）
    public String server_cp_name;       //服务器名称（区服）
    public String role_cp_id;           //角色编号
    public String role_cp_name;         //角色名称
    public String role_camp;
    public int level;                //等级
    public long combat;                //战斗力
    public long money;                //游戏币存余
    public int vip_level;            //VIP等级
    public long vip_count;         //充值总金额

    public void loadEventType(CHEventType type, int eventId, String eventName) {
        this.event_id = eventId;
        this.event_name = eventName;

        this.event_type_id = type.getType();
        this.event_type_name = type.getName();
    }

    @Override
    public void initInfo(Player player, Object... argv) {
        try {
            this.td = DateUtil.formatDateTime(new Date());
            if (player != null) {
                this.gv = player.playerFix().getNv();
                this.role_cp_id = player.getId() + "";
                this.role_cp_name = URLEncoder.encode(player.getName(), "utf-8");
                this.level = player.playerLevel().getLv();
                this.combat = player.getCombat();
                this.money = player.playerCurrency().getGold();
                this.vip_level = player.getPlayerVipInfo().getVipLv();
                this.vip_count = player.getPlayerStatistics().getLifeStatistics(StatisticsType.RECHARGE);
            } else {
                this.role_cp_id = "1";
            }
            this.server_cp_id = getServerId(player, argv);
            this.server_cp_name = URLEncoder.encode(this.server_cp_id + "服", "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getServerId(Player player, Object... argv) {
        if (player != null) {
            return player.getServerId();
        }
        Object obj = argv[0];
        if (obj instanceof Integer) {
            return (int) obj;
        }
        if (obj instanceof DBEntity) {
            return ((DBEntity) obj).getServerId();
        }
        return 0;
    }

    public static String getLogTypeDesc(LogType logType) {
        if (logType == null) {
            return "notfind";
        } else {
            return logType.getExtra();
        }
    }

    public String toEventJson() {
        return GSONUtils.ToJSONString(this);
    }
}
