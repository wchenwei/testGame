package com.hm.chsdk.event2;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.util.Log;
import com.hm.actor.ActorDispatcherType;
import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.ChSDKConf;
import com.hm.chsdk.ChSDKConfUtils;
import com.hm.chsdk.ICHEvent;
import com.hm.chsdk.event2.pack.CHPackEventBiz;
import com.hm.enums.SysType;
import com.hm.model.player.Player;
import com.hm.util.StringUtil;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 草花上传事件
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/2/18 9:43
 */
public class AUploadEvent implements ICHEvent {
  /*  public int g = CHSDKContants.AppId;//中间键appId
    public int pfid = CHSDKContants.CPId;//渠道平台ID
    public int cid = CHSDKContants.TFId;//投放渠道ID
    public int sid = CHSDKContants.TFSubId;//投放子渠道ID
    public String spfid = CHSDKContants.ChannelAppId;//渠道应用ID*/

    public String g;//中间键appId
    public String pfid;//渠道平台ID
    public String cid;//投放渠道ID
    public String sid;//投放子渠道ID
    public String spfid;//渠道应用ID
    public String lifeid;//会话ID MD5（设备号+中间键appId+时间戳）
    public String pfuid;//渠道用户ID(openID)

    public String bn = CHSDKContants2.CreateDate;//生产批次号
    public String sn;//签名(规则见目录)
    public String v = CHSDKContants2.Version;//中间件版本号
    public String ts = System.currentTimeMillis() + "";//时间戳
    public String dn;//手机mac地址
    public String eventJson;//自定义事件{"事件key":"事件Value"}

    public String cpRoleId;//玩家账号
    public String roleName;
    public String roleLevel;
    public String roleVipLevel;
    public String combatPower;
    public String cpServerId;
    public String cpServername;
    public int source;//安卓=1 ios=2 无法识别=0

    public AUploadEvent(Player player, CommonParamEvent tempEvent) {
        if (player != null) {
            loadPlayerInfo(player);
        } else {
            loadDefault();
        }
        this.cpServerId = tempEvent.getServer_cp_id() + "";
        this.cpServername = tempEvent.getServer_cp_name();
        this.eventJson = tempEvent.toEventJson();
    }


    public void loadPlayerInfo(Player player) {
        /*
        private String channelApplyId;		 	//channelApplyId
        private String chChannelId;  			//pfid
        private String chAppid;			 		//g
        private String chsourceId;				//sid
        private String chuserId;		 		//cid
*/
        loadPlayerForConf(player);
        this.dn = player.getUid() + "";
        this.lifeid = ts;//时间戳
        this.pfuid = player.playerFix().getAccount();
        if (StrUtil.isEmpty(this.pfuid)) {
            this.pfuid = player.getUid() + "";
        }

        this.cpRoleId = player.getId() + "";
        this.roleName = player.getName();
        this.roleLevel = player.playerLevel().getLv() + "";
        this.roleVipLevel = player.getPlayerVipInfo().getVipLv() + "";
        this.combatPower = player.getCombat() + "";
        this.source = getSourceType(player);
    }

    public int getSourceType(Player player) {
        String sysType = player.playerFix().getSysType();
        if(SysType.ANDROID.getType().equals(sysType)) {
            return 1;
        }if(SysType.IOS.getType().equals(sysType)) {
            return 2;
        }
        return 0;
    }

    public void loadPlayerForConf(Player player) {
        ChSDKConf chSDKConf = ChSDKConfUtils.getChSDKConf(player.getChannelId());
        if (chSDKConf != null) {
            this.g = String.valueOf(CHSDKContants.AppId);
            this.cid = String.valueOf(CHSDKContants.TFId);
            this.sid = String.valueOf(CHSDKContants.TFSubId);
            this.pfid = String.valueOf(chSDKConf.getAppId());
            this.spfid = chSDKConf.getChannelAppId();
        } else {
            String tempG = player.playerFix().getChAppid();
            g = StringUtil.isNullOrEmpty(tempG) ? String.valueOf(CHSDKContants.AppId) : tempG;
            String tempPfid = player.playerFix().getChChannelId();
            pfid = StringUtil.isNullOrEmpty(tempPfid) ? String.valueOf(CHSDKContants.CPId) : tempPfid;
            String tempCid = player.playerFix().getChuserId();
            cid = StringUtil.isNullOrEmpty(tempCid) ? String.valueOf(CHSDKContants.TFId) : tempCid;
            String tempSid = player.playerFix().getChsourceId();
            sid = StringUtil.isNullOrEmpty(tempSid) ? String.valueOf(CHSDKContants.TFSubId) : tempSid;
            String tempSpfid = player.playerFix().getChannelApplyId();
            spfid = StringUtil.isNullOrEmpty(tempSpfid) ? CHSDKContants.ChannelAppId : tempSpfid;
        }
    }

    public void loadDefault() {
        this.g = CHSDKContants2.AppId;
        this.pfid = CHSDKContants2.CPId;
        this.cid = CHSDKContants2.TFId;
        this.sid = CHSDKContants2.TFSubId;
        this.spfid = CHSDKContants2.ChannelAppId;
        this.lifeid = ts;//时间戳
        this.cpRoleId = "1";
    }

    public static void fileMsg(StringBuilder sb, String key, Object val) {
        if (val != null) {
            sb.append(key + "=" + val + "&");
        }
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
        fileMsg(sb,"source",source);
        fileMsg(sb, "spfid", spfid);
        fileMsg(sb, "ts", ts);
        fileMsg(sb, "v", v);
        return sb.substring(0, sb.length() - 1);
    }

    public String buildParamStr2() {
        try {
            Map<String, String> sortMap = Maps.newHashMap();
            for (Field field : ReflectUtil.getFields(buildClass())) {
                String fieldName = field.getName();
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj == null) {
                    continue;
                }
                sortMap.put(fieldName, obj.toString());
            }
            List<String> keys = Lists.newArrayList(sortMap.keySet());
            Collections.sort(keys);
            StringBuilder sb = new StringBuilder();
            for (String key : keys) {
                sb.append(key + "=" + sortMap.get(key) + "&");
            }
            if (sb.length() > 0) {
                String data = sb.substring(0, sb.length() - 1);
                return data;
            }
        } catch (Exception e) {
            Log.Error("", e);
        }
        return null;
    }


    public void buildSign() {
        String data = buildParamStr() + CHSDKContants2.AppKey;
        if (CHSDKContants.showLog) {
            System.out.println("chevent data:" + data);
        }
        String sign = SecureUtil.md5(data).toUpperCase();
        if (CHSDKContants.showLog) {
            System.out.println("chevent sign:" + sign);
        }
        this.sn = sign;
    }

    public void sendSDK() {
        buildSign();
        final AUploadEvent data = this;

        ActorDispatcherType.CHEventPack.putTask(new IRunner() {
            @Override
            public Object runActor() {
                CHPackEventBiz.addEvent(data);
                return null;
            }
        });
    }

    public void setEventJson(String json) {
        this.eventJson = json;
    }

    @Override
    public Class buildClass() {
        return AUploadEvent.class;
    }


    @Override
    public String buildUrl() {
        return CHSDKContants2.EventUrl;
    }

    @Override
    public boolean isUploadEvent2() {
        return true;
    }

    public static void main(String[] args) {
        List<String> keys = Lists.newArrayList();
        for (Field field : ReflectUtil.getFields(AUploadEvent.class)) {
            String fieldName = field.getName();
            keys.add(fieldName);
        }
        Collections.sort(keys);

        for (String key : keys) {
            System.out.println("fileMsg(sb,\"" + key + "\"," + key + ");");
        }

    }
}
