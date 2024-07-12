package com.hm.util;

import cn.hutool.http.HttpRequest;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * 钉钉发送
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/6/18 15:46
 */
public class DDTalkUtil {
    //钉钉的加密key
    public static String DINGTAIL_SECRET = "SEC8a9f3a873d0cba5689b181208b11e540413024b39237f20d05c29fe05fc04391";
    public static String ACCESS_TOKEN = "471f92ed854c527a29d5a54eb3a71f2a2facc445d73e1596e85af5eea3550084";
    //钉钉的url
    public static String DINGTAIL_URL = "https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s";

    /**
     * 发送钉钉群消息
     *
     * @param content
     */
    public static void sendDingTalk(String content) {
        try {
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + DINGTAIL_SECRET;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(DINGTAIL_SECRET.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");

            String body = "{\"text\":{\"content\":\"" + content + "\"},\"msgtype\":\"text\"}";
            String url = String.format(DINGTAIL_URL, ACCESS_TOKEN, timestamp, sign);
            String result = HttpRequest.post(url)
                    .body(body, "application/json;charset=UTF-8")
                    .execute().body();
            System.out.println("dingding:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
