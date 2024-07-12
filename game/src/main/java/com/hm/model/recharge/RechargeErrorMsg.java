package com.hm.model.recharge;

import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.model.player.Player;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName RechargeErrorMsg
 * @Deacription 充值错误信息，记录超过充值次数的信息
 * @Author zxj
 * @Date 2021/4/19 16:16
 * @Version 1.0
 **/
@Data
@Document(collection = "rechargeErrorMsg")
public class RechargeErrorMsg {

    public RechargeErrorMsg(Player player, RechargeGiftTempImpl rechargeGift, Map<String, String> params) {
        this.uid = player.getUid();
        this.roleid = player.getId();
        this.giftid = rechargeGift.getId();
        this.amt = Integer.parseInt(params.get("rmb"));
        this.serverid = player.getServerId();
        this.orderid = params.get("orderid");
        this.suporderid = params.get("suporderid");
        this.cretedate = new Date();
    }

    private long uid;
    private long roleid;
    private int giftid;
    private int amt;
    private int serverid;
    private String orderid;
    private String suporderid;
    private Date cretedate;
}
