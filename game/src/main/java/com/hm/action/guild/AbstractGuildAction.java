package com.hm.action.guild;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import com.hm.handler.SessionUtil;
import com.hm.libcore.action.BaseAction;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.UpdateLockUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class AbstractGuildAction extends BaseAction {

    @Override
    public void doProcess(JsonMsg clientMsg, HMSession session) {
        Player player = SessionUtil.getPlayer(session);
        if(player == null){
            log.error("玩家不存在:处理"+clientMsg.getMsgId()+"出错");
            session.sendErrorMsg(MessageComm.S2C_ErrorMsg, SysConstant.SESSIONKEY_ERROR);
            return;
        }
        Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
        if (guild == null){
            log.error("玩家{}部落{}不存在,处理{}出错", player.getId(), player.getGuildId(), clientMsg.getMsgId());
            session.sendErrorMsg(MessageComm.S2C_ErrorMsg, SysConstant.Guild_NoExist);
            return;
        }
        long now = System.currentTimeMillis();
        try {
            player.playerTemp().setLastMsgTime(now);
            synchronized (guild){
                doProcess(clientMsg, player, guild);
            }
        } catch (Exception e) {
            session.sendErrorMsg(MessageComm.S2C_ErrorMsg,SysConstant.SESSIONKEY_ERROR);
            log.error("处理"+clientMsg.getMsgId()+"出错", e);
        }
//        finally {
//            MsgLogUtils.showMsgLog(player.getId(), clientMsg);
//        }
    }
    private MethodAccess access;
    private Map<Integer,Integer> msgMethodMap = Maps.newHashMap();

    @Override
    public final void registerMsg() {
        initMethodAccess();
    }

    private void initMethodAccess() {
        this.access = MethodAccess.get(getClass());
        for (Method method : getClass().getDeclaredMethods()) {
            MsgMethod msgMethod = method.getAnnotation(MsgMethod.class);
            if(msgMethod != null) {
                int index = access.getIndex(method.getName(), Player.class, Guild.class, JsonMsg.class);
                int msgId = msgMethod.value();
                registerMsg(msgId);//注册消息
                this.msgMethodMap.put(msgId, index);
            }
        }
    }

    private final void doProcess(JsonMsg clientMsg, Player player, Guild guild) {
        int msgId = clientMsg.getMsgId();
        if(UpdateLockUtil.isLockUpdate(msgId)){
            return;
        }
        if(msgMethodMap.containsKey(msgId)) {
            int index = msgMethodMap.get(msgId);
            access.invoke(this, index, player, guild, clientMsg);
            return;
        }
    }

}
