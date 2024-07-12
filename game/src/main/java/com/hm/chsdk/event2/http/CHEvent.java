package com.hm.chsdk.event2.http;

import cn.hutool.crypto.SecureUtil;

/**
 * 草花上传事件
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/2/18 9:43
 */
public class CHEvent {
    public String g;//中间键appId
    public String pfid;//渠道平台ID
    public String cid;//投放渠道ID
    public String sid;//投放子渠道ID
    public String spfid;//渠道应用ID
    public String lifeid;//会话ID MD5（设备号+中间键appId+时间戳）
    public String pfuid;//渠道用户ID(openID)

    public String bn = CHEventUtil.CreateDate;//生产批次号
    public String sn;//签名(规则见目录)
    public String v = CHEventUtil.Version;//中间件版本号
    public String ts = System.currentTimeMillis() + "";//时间戳
    public String dn;//手机mac地址
    public String eventJson;//自定义事件{"事件key":"事件Value"}

    //===================下面可以不穿=====================================
    public String cpRoleId;//玩家账号
    public String roleName;
    public String roleLevel;
    public String roleVipLevel;
    public String combatPower;
    public String cpServerId;
    public String cpServername;

    public void buildSign() {
        String data = buildParamStr() + CHEventUtil.AppKey;
        System.out.println("chevent data:" + data);
        String sign = SecureUtil.md5(data).toUpperCase();
        System.out.println("chevent sign:" + sign);
        this.sn = sign;
    }

    public String buildParamStr() {
        StringBuilder sb = new StringBuilder();
        fileMsg(sb, "bn", bn);
        fileMsg(sb, "cid", cid);
        fileMsg(sb, "combatPower", combatPower);
        fileMsg(sb, "cpRoleId", cpRoleId);
        fileMsg(sb, "cpServerId", cpServerId);
        fileMsg(sb, "cpServername", cpServername);
        fileMsg(sb, "dn", dn);
        fileMsg(sb, "eventJson", eventJson);
        fileMsg(sb, "g", g);
        fileMsg(sb, "lifeid", lifeid);
        fileMsg(sb, "pfid", pfid);
        fileMsg(sb, "pfuid", pfuid);
        fileMsg(sb, "roleLevel", roleLevel);
        fileMsg(sb, "roleName", roleName);
        fileMsg(sb, "roleVipLevel", roleVipLevel);
        fileMsg(sb, "sid", sid);
        fileMsg(sb, "sn", sn);
        fileMsg(sb, "spfid", spfid);
        fileMsg(sb, "ts", ts);
        fileMsg(sb, "v", v);
        return sb.substring(0, sb.length() - 1);
    }

    public static void fileMsg(StringBuilder sb, String key, Object val) {
        if (val != null) {
            sb.append(key + "=" + val + "&");
        }
    }
}
