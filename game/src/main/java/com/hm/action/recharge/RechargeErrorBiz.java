package com.hm.action.recharge;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.model.player.Player;
import com.hm.model.recharge.RechargeErrorMsg;
import com.hm.libcore.mongodb.MongoUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @ClassName RechargeErrorBiz
 * @Deacription 充值错误信息，记录超过充值次数的信息
 * @Author zxj
 * @Date 2021/4/19 16:18
 * @Version 1.0
 **/
@Slf4j
@Biz
public class RechargeErrorBiz {

    public void save(Player player, RechargeGiftTempImpl rechargeGift, Map<String, String> params) {
        try{
            if(null!=params) {
                RechargeErrorMsg rechargeErrorMsg = new RechargeErrorMsg(player, rechargeGift, params);
                MongoUtils.getLoginMongodDB().save(rechargeErrorMsg);
            }
        } catch (Exception e){
            log.error("报错充值错误信息异常，RechargeErrorBiz:", e);
        }
    }
}
