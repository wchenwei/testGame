package com.hm.action.wx;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.voucher.voucher.VoucherUtils;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.sysConstant.SysConstant;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Action
public class WXAction extends AbstractPlayerAction {
    @Resource
    private WXBiz wXBiz;

    @MsgMethod(MessageComm.C2S_Player_WXSubs)
    public void applyVoucher(Player player, JsonMsg msg) {
        String openId = msg.getString("openId");
        String gameId = msg.getString("gameId");
        List<Integer> temps = StringUtil.splitStr2IntegerList(msg.getString("tempIds"),",");

        log.error(player.getId()+"wx:"+openId+"->"+gameId);
        wXBiz.playerWXSubs(player,gameId,openId,temps);

        player.sendMsg(MessageComm.S2C_Player_WXSubs);
    }


    @MsgMethod(MessageComm.C2S_QYWX_Item)
    public void qYWX_Item(Player player, JsonMsg msg) {
        String code = ServerDataManager.getIntance().getServerData(player.getServerId())
                .getServerKefuData().getQywxVoucher();
        if(StrUtil.isEmpty(code)) {
            player.sendErrorMsg(SysConstant.Voucher_Invalid);
            return;
        }

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_QYWX_Item);
        retMsg.addProperty("itemList", VoucherUtils.getVoucherItem(code));
        player.sendMsg(retMsg);
    }
}
