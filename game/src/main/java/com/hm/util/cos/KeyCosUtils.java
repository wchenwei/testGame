package com.hm.util.cos;

import com.google.common.collect.Maps;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Setter
@Component
@ConfigurationProperties(prefix = "cos")
public class KeyCosUtils {
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String ISO_8859_1_ENCODING = "ISO-8859-1";
    public static final String UTF8_ENCODING = "UTF-8";

    public static final String LINE_SEPARATOR = "\n";
    public static final String Q_SIGN_ALGORITHM_KEY = "q-sign-algorithm";
    public static final String Q_SIGN_ALGORITHM_VALUE = "sha1";
    public static final String Q_AK = "q-ak";
    public static final String Q_SIGN_TIME = "q-sign-time";
    public static final String Q_KEY_TIME = "q-key-time";
    public static final String Q_HEADER_LIST = "q-header-list";
    public static final String Q_URL_PARAM_LIST = "q-url-param-list";
    public static final String Q_SIGNATURE = "q-signature";

    public static String bucketName = "";
    public static String secretId = "";
    public static String secretKey = "";
    public static String region = "";



    public static void main(String[] args) {
        upload("test_5","nihao");
    }

    public static String buildCosUrl(String fileName) {
        String host = bucketName +".cos."+ region +".myqcloud.com";
        return "https://"+host+"/"+fileName;
    }

    public static Map<String,String> buildHead(String fileName,long contentLength) {
        //head
        String host = bucketName +".cos."+ region +".myqcloud.com";

        Map<String,String> headMap = Maps.newHashMap();
        headMap.put("Content-Type","text/pain");
        headMap.put("Content-Length",contentLength+"");
        headMap.put("Host",host);
        //加密
        String sign = buildSign(fileName,headMap);
        headMap.put("Authorization",sign);
        headMap.put("User-Agent","Win10");
        System.out.println(sign);
        return headMap;
    }

    public static Map<String,String> buildClientHead(String toFile,long contentLength) {
        Map<String,String> headMap = buildHead(toFile,contentLength);
        headMap.remove(CONTENT_LENGTH);
        headMap.put("x-cos-sdk-log-debug", "off");
        return headMap;
    }

    /**
     * 上传cos
     * @param id 文件名字
     * @param content 文件里的内容
     */
    public static void upload(String id,String content) {
        try {
            String fileName = id+".txt";
            int contentLength = content.length();
            //变成数据流
            InputStream contentInput = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            //访问url
            String host = bucketName +".cos."+ region +".myqcloud.com";
            String url = "https://"+host+"/"+fileName;

            Map<String,String> headMap = buildHead(fileName,contentLength);

            //把head从utf-8转成88591
//            convertFromUtf8ToIso88591(headMap);

            HttpRequestBase httpRequestBase = new HttpPut();

            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                if (entry.getKey().equals(CONTENT_LENGTH)) {
                    continue;
                }
                httpRequestBase.addHeader(entry.getKey(),entry.getValue());
            }
            httpRequestBase.addHeader("x-cos-sdk-log-debug", "off");
            InputStreamEntity reqEntity =
                    new InputStreamEntity(contentInput, contentLength);
            HttpEntityEnclosingRequestBase entityRequestBase =
                    (HttpEntityEnclosingRequestBase) httpRequestBase;
            entityRequestBase.setEntity(reqEntity);
            httpRequestBase.setURI(new URI(url));

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpContext context = HttpClientContext.create();
            CloseableHttpResponse response = httpClient.execute(httpRequestBase,context);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取时间的秒数
    public static long getStartTime() {
        return System.currentTimeMillis()/1000;
    }
    public static long getEndTime() {
        return System.currentTimeMillis()/1000+3600;
    }

    public static String buildSign(String toFile,Map<String,String> headMap) {
        String formatHeaders = formatMapToStr(headMap);

        String formatStr = new StringBuilder()
                .append("put").append(LINE_SEPARATOR)
                .append("/"+toFile).append(LINE_SEPARATOR)
                .append("").append(LINE_SEPARATOR)
                .append(formatHeaders).append(LINE_SEPARATOR)
                .toString();
        //使用 HMAC-SHA1 以 SecretKey 为密钥
        String hashFormatStr = DigestUtils.sha1Hex(formatStr);

        String qSignTimeStr = getStartTime()+";"+getEndTime();
        String qKeyTimeStr = qSignTimeStr;
        String qHeaderListStr = headMap.keySet().stream().sorted()
                .map(e -> e.toLowerCase())
                .collect(Collectors.joining(";"));

        String signKey = HmacUtils.hmacSha1Hex(secretKey, qSignTimeStr);

        String stringToSign = new StringBuilder().append(Q_SIGN_ALGORITHM_VALUE)
                .append(LINE_SEPARATOR).append(qSignTimeStr).append(LINE_SEPARATOR)
                .append(hashFormatStr).append(LINE_SEPARATOR).toString();

        String signature = HmacUtils.hmacSha1Hex(signKey, stringToSign);

        String authoriationStr = new StringBuilder().append(Q_SIGN_ALGORITHM_KEY).append("=")
                .append(Q_SIGN_ALGORITHM_VALUE).append("&").append(Q_AK).append("=")
                .append(secretId).append("&").append(Q_SIGN_TIME).append("=")
                .append(qSignTimeStr).append("&").append(Q_KEY_TIME).append("=").append(qKeyTimeStr)
                .append("&").append(Q_HEADER_LIST).append("=").append(qHeaderListStr).append("&")
                .append(Q_URL_PARAM_LIST).append("=").append("").append("&")
                .append(Q_SIGNATURE).append("=").append(signature).toString();
        return authoriationStr;
    }

    public static String formatMapToStr(Map<String,String> headMap) {
        //排序
        StringBuilder sb = new StringBuilder();
        List<String> sortList = headMap.keySet().stream().sorted().collect(Collectors.toList());
        for (String key : sortList) {
            String val = headMap.get(key);
            sb.append(encode(key).toLowerCase()).append("=").append(encode(val)+"&");
        }
        return sb.substring(0,sb.length()-1);
    }

    public static String encode(String originUrl) {
        try {
            return URLEncoder.encode(originUrl, UTF8_ENCODING).replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
        }
        return null;
    }

    public static void convertFromUtf8ToIso88591(Map<String,String> headMap) {
        for (Map.Entry<String, String> headerEntry : headMap.entrySet()) {
            String headerKey = headerEntry.getKey();
            String headerValue = headerEntry.getValue();
            if (headerKey.equals(CONTENT_LENGTH)) {
                continue;
            }
            headerValue = convertFromUtf8ToIso88591(headerValue);
            headMap.put(headerKey,headerValue);
        }
    }

    public static String convertFromUtf8ToIso88591(String value) {
        if(value == null) {
            return null;
        }
        try {
            return new String(value.getBytes(UTF8_ENCODING), ISO_8859_1_ENCODING);
        } catch (Exception e) {

        }
        return value;
    }

    public void setBucketName(String bucketName) {
        KeyCosUtils.bucketName = bucketName;
    }

    public void setSecretId(String secretId) {
        KeyCosUtils.secretId = secretId;
    }

    public void setSecretKey(String secretKey) {
        KeyCosUtils.secretKey = secretKey;
    }

    public void setRegion(String region) {
        KeyCosUtils.region = region;
    }
}
