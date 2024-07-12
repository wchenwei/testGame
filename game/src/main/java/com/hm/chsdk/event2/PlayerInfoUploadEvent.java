package com.hm.chsdk.event2;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.actor.IRunner;
import com.hm.actor.ActorDispatcherType;
import com.hm.chsdk.CHPlayerInfoSendClient;
import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.ICHEvent;
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
public class PlayerInfoUploadEvent implements ICHEvent, IRunner {

    public String g;//中间键appId
    public String channelID;//渠道id
    public String platformUserid;//渠道用户id
    public String sn;//sn
    //public String divJson;//自定义参数

    public PlayerInfoUploadEvent(Player player) {
        loadPlayerInfo(player);
    }


    public void loadPlayerInfo(Player player) {
        String tempG = player.playerFix().getChAppid();
        g = StringUtil.isNullOrEmpty(tempG) ? String.valueOf(CHSDKContants.AppId) : tempG;
        String tempPfid = player.playerFix().getChChannelId();
        channelID = player.playerFix().getChChannelId();
        this.platformUserid = player.playerFix().getAccount();
    }

    public void loadExtraParams(CommonParamInter param) {

    }


    public String buildSign() {
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
                String data = sb.substring(0, sb.length() - 1).toString() + CHSDKContants.AppKey;
                if (CHSDKContants.showLog) {
                    System.err.println(data);
                }
                String sign = SecureUtil.md5(data).toUpperCase();
                if (CHSDKContants.showLog) {
                    System.err.println(sign);
                }
                return sign;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendSDK() {
        final PlayerInfoUploadEvent event = this;
        //发送
        ActorDispatcherType.CHEventSend.putTask(event);

    }

    @Override
    public String buildUrl() {
        return CHSDKContants2.PhoneUrl;
    }


    @Override
    public Class buildClass() {
        return PlayerInfoUploadEvent.class;
    }

    @Override
    public boolean isUploadEvent2() {
        return true;
    }

    public boolean isZip() {
        return false;
    }

    public static void main(String[] args) {
        String str = "{\"mobile\":\"152251709811\",\"aaa\":\"123\"}";
        JSONObject jsonObject = new JSONObject(str);
        for (String key : jsonObject.keySet()) {
            System.out.println(key + "-------->" + jsonObject.getStr(key));
        }
    }

    @Override
    public Object runActor() {
        this.sn = buildSign();
        CHPlayerInfoSendClient.sendData(this);
        return null;
    }
}
