package com.hm.action.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.spring.SpringUtil;
import com.hm.actor.ActorDispatcherType;
import com.hm.config.excel.WXConfig;
import com.hm.config.excel.temlate.WxSubscribeTemplateImpl;
import com.hm.libcore.ok.HttpRequestMap;

import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 微信util
 * @date 2023/4/23 15:02
 */
public class WXUtils {
    public static String TokenUrl = "https://passport-ios.caohua.com/api/wxAccessToken";
    public static String EventUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";

    public static Map<String,WXToken> tokenMap = Maps.newHashMap();
    public static boolean isLog = false;


    public static void checkToken() {
        tokenMap = SpringUtil.getBean(WXConfig.class).buildTokenMap();
        for (WXToken value : tokenMap.values()) {
//            value.loadToken();
        }
    }

    public static void loadNewToken(WXToken token) {
        synchronized (token) {
            String result = HttpRequestMap.create(TokenUrl)
                    .putKeyVal("wxid",token.getWxid())
                    .putKeyVal("appid",token.getAppid())
                    .putKeyVal("sign",token.getSign())
                    .sendPost();
            System.out.println(result);
            JSONObject jsonObj = JSONObject.parseObject(result);
            if(jsonObj.getIntValue("code") != 200) {
                return;
            }
            JSONObject obj = jsonObj.getJSONObject("data");
            token.setToken(obj.getString("acto"));
            token.setEndTime(obj.getLongValue("exti")*1000);
            System.out.println(GSONUtils.ToJSONString(token));
        }
    }

    public static void sendPlayerEvent(WXSubs wxSubs, WXSubsType subsType, WxSubscribeTemplateImpl template) {
        ActorDispatcherType.Http.putTask(new IRunner() {
            @Override
            public Object runActor() {
                sendEventPost(wxSubs,subsType,template);
                return null;
            }
        });
    }

    public static void sendEventPost(WXSubs wxSubs,WXSubsType subsType,WxSubscribeTemplateImpl template) {
        WXToken wxToken = tokenMap.get(wxSubs.getGameId());
        if(wxToken == null) {
            System.out.println(wxSubs.getGameId()+"wx token is null");
            return;
        }
        wxToken.loadToken();

        Map<String, WXVal> data = subsType.buildData(wxSubs.getId(),template);

        Map<String,Object> paraMap = Maps.newHashMap();
        paraMap.put("touser",wxSubs.getOpenId());
        paraMap.put("template_id",template.getWx_key());
        paraMap.put("data",data);

        String json = GSONUtils.ToJSONString(paraMap);
        String result = HttpUtil.post(EventUrl+"?access_token=" + wxToken.getToken(),json);
        if(isLog) {
            System.out.println(result);
        }
        JSONObject jsonObj = JSONObject.parseObject(result);
        int errCode = jsonObj.getIntValue("errcode");
        if(errCode > 0) {
            System.out.println(result);
            if(errCode == 40001 || errCode == 40014) {
                //token过期了 从新要一个新的
                wxToken.loadNewToken();
                //再次发送
                HttpUtil.post(EventUrl+"?access_token=" + wxToken.getToken(),json);
            }
        }
    }



    public static void main(String[] args) {
        checkToken();
        WXSubs wxSubs = new WXSubs();
        wxSubs.setOpenId("oQVfR5L_hEONcz2QeZfEGLJ3tq7s");
        wxSubs.setGameId("1002373");

    }
}
