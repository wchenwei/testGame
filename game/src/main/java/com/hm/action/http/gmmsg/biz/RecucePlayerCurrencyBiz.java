package com.hm.action.http.gmmsg.biz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.db.PlayerUtils;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RecucePlayerCurrencyBiz
 * @Deacription gm扣用户资源信息
 * @Author zxj
 * @Date 2022/4/22 17:07
 * @Version 1.0
 **/
@Slf4j
@Biz
public class RecucePlayerCurrencyBiz {
    @Resource
    private ItemBiz itemBiz;

    public String reduce(Map<String, String> paramMap) {
        Map<String, String> result = Maps.newHashMap();
        String dataStr = paramMap.get("data");
        Map<Long, List<Items>> reduceItems = this.strToMap(dataStr);
        reduceItems.forEach((key, value)->{
            try{
                Player player = PlayerUtils.getPlayer(key);
                List<Items> reduceItem = itemBiz.gmReduceItem(player, value);
                player.sendUserUpdateMsg();
                //key为string类型。用int时，json转换的时候出错
                result.put(String.valueOf(key), JSON.toJSONString(reduceItem));
            } catch (Exception e){
                result.put(String.valueOf(key), "error");
                log.error("gm减用户资源出错", e);
            }
        });
        return JSON.toJSONString(result);
    }

    public Map<Long, List<Items>> strToMap(String dataStr) {
        Map<Long, List<Items>> result = Maps.newHashMap();
        try{
            Arrays.stream(dataStr.split("#")).forEach(e->{
                long playerId = Long.parseLong(e.split("-")[0]);
                List<Items> costs = ItemUtils.str2DefaultItemList(e.split("-")[1]);
                result.put(playerId,costs);
            });
        } catch (Exception e) {
            log.error("gm减用户资源，Str数据转换时出错", e);
        }
        //"1061046-1:1:250200,6:60031:8340,6:60009:8340-835#2160854-1:1:229800,6:60031:7660,6:60009:7660#5551032-1:1:138000,6:60031:4600,6:60009:4600#2410393-1:1:97200,6:60031:3240,6:60009:3240#2431061-1:1:93600,6:60031:3120,6:60009:3120#3180999-1:1:91200,6:60031:3040,6:60009:3040#2460962-1:1:82500,6:60031:2750,6:60009:2750#2440143-1:1:82200,6:60031:2740,6:60009:2740#2161553-1:1:60900,6:60031:2030,6:60009:2030#2360199-1:1:55500,6:60031:1850,6:60009:1850#2401255-1:1:53100,6:60031:1770,6:60009:1770#2411393-1:1:37500,6:60031:1250,6:60009:1250#2430596-1:1:32700,6:60031:1090,6:60009:1090#2390457-1:1:18900,6:60031:630,6:60009:630#3170689-1:1:18300,6:60031:610,6:60009:610#2100072-1:1:17700,6:60031:590,6:60009:590#2131324-1:1:10800,6:60031:360,6:60009:360#3230029-1:1:4800,6:60031:160,6:60009:160";
        return result;
    }
}
