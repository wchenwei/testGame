package com.hm.chsdk.event2;

import com.hm.chsdk.CHSDKContants;

import java.util.List;

/**
 * "中间件游戏ID":"1043"
 * "平台渠道ID":"1042",
 * "投放渠道ID":"21012",
 * "投放子渠道ID":"268415"
 * "渠道应用ID":"104310422020031310540481"
 * 以上参数仅需要上报玛雅iOS渠道的数据即可
 *
 * @author siyunlong
 * @version V1.0
 * @date 2020年3月13日 上午1:15:45
 */
public class CHSDKContants2 {
    public static boolean SDKClose = false;
    public final static String AppId = CHSDKContants.AppId + "";//中间键appId
    public final static String CPId = CHSDKContants.CPId + "";//渠道平台ID
    public final static String TFId = CHSDKContants.TFId + "";//投放渠道ID
    public final static String TFSubId = CHSDKContants.TFSubId + "";//投放子渠道ID
    public final static String ChannelAppId = CHSDKContants.ChannelAppId;//渠道应用ID
    public final static String AppKey = CHSDKContants.AppKey;
    public final static String Version = "1002009";//版本
    public final static String CreateDate = "202001021741";

    public static String EventUrl = "https://data-msdk.caohua.com/event/upload";
    public static String EventListUrl = "https://data-msdk.caohua.com/event/list";
    public static String EventListZipUrl = "https://data-msdk.caohua.com/event/gzip";
    public static String PhoneUrl = "https://data-msdk.caohua.com/api/updateMobile";


    public static List<String> SortKeys;
}
