package com.hm.chsdk.event2.http;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.hm.libcore.ok.HttpRequestMap;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.chsdk.CHSDKContants;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 使用http上传草花事件
 * @date 2023/3/24 11:25
 */
public class CHEventUtil {
    public static String AppId = CHSDKContants.AppId+"";//中间键appId
    public static String CPId = CHSDKContants.CPId+"";//渠道平台ID
    public static String TFId = CHSDKContants.TFId+"";//投放渠道ID
    public static String TFSubId = CHSDKContants.TFSubId+"";//投放子渠道ID
    public static String ChannelAppId = CHSDKContants.ChannelAppId;//渠道应用ID


    public static String AppKey = CHSDKContants.AppKey;
    public static String Version = "1002009";//版本
    public static String CreateDate = "202001021741";
    public static String EventUrl = "https://data-msdk.caohua.com/event/upload";

    //==============================================================
    public static String ClientVersion = "1";//客户端版本
    public static String Mac = "1";//mac地址

    public static Map<String,Object> buildDefault() {
        Map<String,Object> map = new HashMap<>();
        map.put("gv",ClientVersion);
        map.put("td", DateUtil.formatDateTime(new Date()));
        return map;
    }

    public static void sendHlEvent(int button_id) {
        Map<String,Object> map = buildDefault();

        //================公共参数========================
        map.put("event_type_id", 4);
        map.put("event_type_name", "operational_activities");
        map.put("event_id", 4017);
        map.put("event_name", "back_click");
        //================事件独有参数========================
        map.put("button_id",button_id);

        sendEvent(map);
    }

    public static void sendEvent(Map<String, Object> mode) {
        CHEvent event = new CHEvent();
        event.g = AppId;
        event.pfid = CPId;
        event.cid = TFId;//投放渠道ID
        event.sid = TFSubId;//投放子渠道ID
        event.spfid = ChannelAppId;//渠道应用ID
        event.pfuid = "1";//渠道用户ID(openID)
        event.bn = CreateDate;//生产批次号
        event.v = Version;//中间件版本号
        event.ts = System.currentTimeMillis() + "";//时间戳
        event.lifeid = event.ts;//会话ID MD5（设备号+中间键appId+时间戳）
        event.dn = Mac;//手机mac地址
        event.eventJson = GSONUtils.ToJSONString(mode);
        event.buildSign();

        String json = GSONUtils.ToJSONString(event);
        String data = Base64.encode(json);
        sendPost(EventUrl,data);
    }


    public static boolean sendPost(String strURL, String params) {
        try {
            String result = HttpRequestMap.create(strURL)
                    .mediaType("application/x-www-form-urlencoded")
                    .putKeyVal("data", params)
                    .sendPost();
            System.out.println(result);
            JSONObject jsonObj = JSONObject.parseObject(result);
            if (jsonObj.containsKey("code") && jsonObj.getIntValue("code") == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        sendHlEvent(1);
    }
}
