package com.hm.action.domain;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.db.IpToDomainUtil;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.IpToDomain;
import org.apache.commons.lang.StringUtils;
import com.hm.libcore.annotation.Action;

import java.util.Objects;

/**
 * @author wyp
 * @description
 * @date 2020/12/25 10:13
 */
@Action
public class DomainAction extends AbstractPlayerAction {

    @MsgMethod(MessageComm.C2S_GET_DOMAIN)
    public void getSeasonRank(Player player, JsonMsg msg) {
        String ipAndPort = msg.getString("ipAndPort");
        if(StringUtils.isBlank(ipAndPort)){
            player.sendMsg(MessageComm.S2C_GET_DOMAIN, ipAndPort);
            return;
        }
        IpToDomain ipToDomain = IpToDomainUtil.getInstance().getIpToDomain(ipAndPort);
        if(Objects.isNull(ipToDomain) || StringUtils.isBlank(ipToDomain.getDomain())){
            player.sendMsg(MessageComm.S2C_GET_DOMAIN, ipAndPort);
            return;
        }
        /*String result = IpToDomainUtil.encryptDomain(ipToDomain.getDomain());
        if(Objects.isNull(result)){
            player.sendMsg(MessageComm.S2C_GET_DOMAIN, ipAndPort);
            return;
        }*/
        player.sendMsg(MessageComm.S2C_GET_DOMAIN, ipToDomain.getDomain());
    }

}
