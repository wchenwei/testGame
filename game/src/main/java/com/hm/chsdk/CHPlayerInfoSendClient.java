package com.hm.chsdk;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.actor.IRunner;
import com.hm.actor.ActorDispatcherType;
import com.hm.libcore.ok.HttpRequestMap;

public class CHPlayerInfoSendClient {
    public static boolean isPoolSend = true;

    public static void sendData(ICHEvent event) {
        String json = GSONUtils.ToJSONString(event);
        if (CHSDKContants.showLog) {
            System.err.println(event.buildUrl());
            System.err.println(json);
        }
        String data = Base64.encode(json);
        if (CHSDKContants.showLog) {
            System.err.println(data);
        }
        ActorDispatcherType.CHEventSend.putTask(new IRunner() {
            @Override
            public Object runActor() {
                if (!CHObserverBiz3.isOpen) {
                    return null;
                }
                sendNewJsonPost(event.buildUrl(), data);
                return null;
            }
        });
    }

    public static boolean sendNewJsonPost(String strURL, String params) {
        return sendNewJsonPost(strURL, params, 1);
    }

    public static boolean sendNewJsonPost(String strURL, String params, int num) {
        try {
            long startTime = System.currentTimeMillis();

            String result = HttpRequestMap.create(strURL)
                    .mediaType("application/x-www-form-urlencoded")
                    .putKeyVal("data", params)
                    .sendPost();
            if (CHSDKContants.showLog) {
                System.err.println((System.currentTimeMillis() - startTime) + "ms=" + result);
            }
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

}
