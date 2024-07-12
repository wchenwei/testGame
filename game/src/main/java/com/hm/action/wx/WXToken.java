package com.hm.action.wx;

import cn.hutool.crypto.SecureUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 微信token
 * @date 2023/4/23 15:05
 */
@Setter
@Getter
public class WXToken{
    private String wxid;
    private String appid;
    private String key;
    private String sign;

    private String token;
    private long endTime;

    public WXToken(String appid, String wxid,String key) {
        this.wxid = wxid;
        this.appid = appid;
        this.key = key;
        this.sign = buildSign();
    }

    public boolean isExpire() {
        return System.currentTimeMillis() > endTime;
    }


    private String buildSign() {
        String temp = this.appid +"&" + this.wxid + key;
        System.out.println(temp);
        String result = SecureUtil.md5(temp).toUpperCase();
        System.out.println(result);
        return result;
    }

    public void loadToken() {
        if(!isExpire()) {
            return;
        }
        loadNewToken();
    }

    public void loadNewToken() {
        WXUtils.loadNewToken(this);
    }

}
