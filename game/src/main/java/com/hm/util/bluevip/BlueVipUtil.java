package com.hm.util.bluevip;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hm.enums.BlueVipEnum;
import com.hm.model.player.Player;
import com.hm.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wyp
 * @description
 *      QQ 蓝钻信息
 * @date 2021/8/3 11:46
 */
@Slf4j
public class BlueVipUtil {

    private static final String onlineUrl = "openapi.tencentyun.com";
    private static final String testUrl = "https://api-sandbox.urlshare.cn";

    private static final String url_path = "/v3/user/blue_vip_info";

    // 应用基本信息
    private static final String appid = "1111993103";
    private static final String appkey = "huZfbgHMhg3vRI9O";

    private static String method = "GET";
    // 非必填参数
    /*private static String format = "json";
    private static String userip = "112.90.139.30";*/

    public static JSONObject getBlueVipDetail(Map<String, Object> params, Player player) {
        params.put("appid", appid);
        params.put("pf", "qqgame");
        // 签名密钥
        String secret = appkey + "&";
        // 计算签名
        String sig = SnsSigCheck.makeSig(method, url_path, params, secret);
        params.put("sig", sig);

        JSONObject jsonObject = null;
        try {
            String res = HttpUtil.get(onlineUrl + url_path, params, 3000);
            jsonObject = JSONObject.parseObject(res);
            if (jsonObject == null || jsonObject.getInteger("ret") != 0) {
                return jsonObject;
            }
        } catch (Exception e) {
            log.error("qq 蓝砖错误", e);
            jsonObject = buildOldObj(player);
        }
        if (jsonObject == null) {
            log.error("qq 蓝砖错误 is null");
            return null;
        }
        // 重新刷新 redis 中用户 的蓝钻等级
        Integer blueVipLevel = jsonObject.getInteger("blue_vip_level");
        Integer isBlueVip = jsonObject.getInteger("is_blue_vip");
        Integer isBlueYearVip = jsonObject.getInteger("is_blue_year_vip");
        Integer isSuperBlueVip = jsonObject.getInteger("is_super_blue_vip");

        QQBlueVip blueVip = new QQBlueVip(blueVipLevel, isBlueVip, isBlueYearVip, isSuperBlueVip);
        if (!blueVip.isSame(player.playerBlueVip().getBlueVip())) {
            player.playerBlueVip().resetBlueVip(blueVip);
            RedisUtil.updateRedisPlayer(player);
        }
        return jsonObject;
    }

    public static JSONObject buildOldObj(Player player) {
        QQBlueVip blueVip = player.playerBlueVip().getBlueVip();
        if (blueVip == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("blue_vip_level", blueVip.getBlueVipLv());
        jsonObject.put("is_blue_vip", blueVip.getIsBlueVip());
        jsonObject.put("is_blue_year_vip", blueVip.getIsBlueYearVip());
        jsonObject.put("is_super_blue_vip", blueVip.getIsSuperBlueVip());
        return jsonObject;
    }


    public static boolean checkCanReceive(Player player, Map<String, Object> params, BlueVipEnum vipTitle) {
        params.remove("id");
        JSONObject jsonObject = getBlueVipDetail(params, player);
        if (vipTitle == null || jsonObject.getInteger("ret") != 0) {
            return false;
        }
        return jsonObject.getInteger(vipTitle.getTitle()) == 1;
    }


    public static void main(String[] args) {
        // 用户的OpenID/OpenKey
        String openid = "B5E89916067222C89984E36800A76315";
        String openkey = "1B4507D2AEDE6B634C957AE0001991DF";
        // 所要访问的平台, pf的其他取值参考wiki文档: http://wiki.open.qq.com/wiki/API3.0%E6%96%87%E6%A1%A3
        String pf = "qqgame";
        // 添加固定参数
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("openid", openid);
        params.put("openkey", openkey);
        params.put("pf", pf);
        params.put("appid", appid);
        // 签名密钥
        String secret = appkey + "&";
        // 计算签名
        String sig = SnsSigCheck.makeSig(method, url_path, params, secret);
        params.put("sig", sig);
        String res = HttpUtil.get(onlineUrl + url_path, params, 3000);
        JSONObject jsonObject = JSONObject.parseObject(res);

        System.out.println(jsonObject);
    }

}
