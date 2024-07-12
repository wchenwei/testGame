package com.hm.chsdk;

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
public class CHSDKContants {
    public static boolean SDKClose = false;
    public final static int AppId = 1196;//中间键appId
    public final static int CPId = 1098;//渠道平台ID
    public final static int TFId = 21012;//投放渠道ID
    public final static int TFSubId = 268415;//投放子渠道ID
    public final static String ChannelAppId = "119610982023032310230648";//渠道应用ID

    public final static String CreateId = "202003131741";//生产批次号
    public final static String AppKey = "039dd2abac687647ac5170cbeee6086a";
    public final static String Version = "1002009";//版本
    public static boolean showLog = false;

    public static String InitUrl = "https://data-msdk.caohua.com/dataGame/gameInit";
    public static String LoginUrl = "https://data-msdk.caohua.com/dataGame/login";
	public static String HeartUrl = "https://data-msdk.caohua.com/dataGame/heartbeat";
	public static String RechargeUrl = "https://data-msdk.caohua.com/dataGame/payment";
    public static String EventUrl = "https://data-msdk.caohua.com/event/upload";

}
