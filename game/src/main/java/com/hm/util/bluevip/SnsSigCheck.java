package com.hm.util.bluevip;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;


/**
 * 生成签名类
 *
 * @author open.qq.com
 * @version 3.0.1
 * @copyright © 2012, Tencent Corporation. All rights reserved.
 * @History: 3.0.1 | 2012-08-28 17:34:12 | support cpay callback sig verifictaion.
 * 3.0.0 | nemozhang | 2012-03-21 12:01:05 | initialization
 * @since jdk1.5
 */
@Slf4j
public class SnsSigCheck {

    /**
     * URL编码 (符合FRC1738规范)
     *
     * @param input 待编码的字符串
     * @return 编码后的字符串
     */
    public static String encodeUrl(String input) {
        try {
            return URLEncoder.encode(input, CONTENT_CHARSET).replace("+", "%20").replace("*", "%2A");
        } catch (UnsupportedEncodingException e) {
            log.error("生成签名失败", e);
            return null;
        }
    }

    /* 生成签名
     *
     * @param method HTTP请求方法 "get" / "post"
     * @param url_path CGI名字, eg: /v3/user/get_info
     * @param params URL请求参数
     * @param secret 密钥
     * @return 签名值
     * @throws OpensnsException 不支持指定编码以及不支持指定的加密方法时抛出异常。
     */
    public static String makeSig(String method, String url_path, Map<String, Object> params, String secret) {
        String sig = null;
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);

            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(CONTENT_CHARSET), mac.getAlgorithm());

            mac.init(secretKey);

            String mk = makeSource(method, url_path, params);

            byte[] hash = mac.doFinal(mk.getBytes(CONTENT_CHARSET));

            // base64
            sig = Base64.encode(hash);
        } catch (Exception e) {
            log.error("生成签名失败", e);
        }
        return sig;
    }

    /* 生成签名所需源串
     *
     * @param method HTTP请求方法 "get" / "post"
     * @param url_path CGI名字, eg: /v3/user/get_info
     * @param params URL请求参数
     * @return 签名所需源串
     */
    public static String makeSource(String method, String url_path, Map<String, Object> params) {
        Object[] keys = params.keySet().toArray();

        Arrays.sort(keys);

        StringBuilder buffer = new StringBuilder(128);

        buffer.append(method.toUpperCase()).append("&").append(encodeUrl(url_path)).append("&");

        StringBuilder buffer2 = new StringBuilder();

        for (int i = 0; i < keys.length; i++) {
            buffer2.append(keys[i]).append("=").append(params.get(keys[i]));

            if (i != keys.length - 1) {
                buffer2.append("&");
            }
        }

        buffer.append(encodeUrl(buffer2.toString()));

        return buffer.toString();
    }

    // 编码方式
    private static final String CONTENT_CHARSET = "UTF-8";

    // HMAC算法
    private static final String HMAC_ALGORITHM = "HmacSHA1";
}
