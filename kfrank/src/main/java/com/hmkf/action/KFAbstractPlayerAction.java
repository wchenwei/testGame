package com.hmkf.action;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.msg.JsonMsg;
import com.hmkf.db.KfDBUtils;
import com.hmkf.model.KFPlayer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public abstract class KFAbstractPlayerAction extends InnerAction {
    @Override
    public void doProcess(JsonMsg clientMsg) {
        long playerId = clientMsg.getPlayerId();

        KFPlayer player = KfDBUtils.getPlayerSports(playerId);
        if (player == null) {
            log.error("玩家不存在:处理" + clientMsg.getMsgId() + "出错");
            return;
        }
        long now = System.currentTimeMillis();
        try {
            doProcess(clientMsg, player);
        } catch (Exception e) {
            log.error("处理" + clientMsg.getMsgId() + "出错", e);
        }
//        finally {
//            MsgLogUtils.showMsgLog(playerId,clientMsg);
//        }
    }
    @Override
    public final void registerMsg() {
        initMethodAccess();
    }

    private void initMethodAccess() {
        this.access = MethodAccess.get(getClass());
        for (Method method : getClass().getDeclaredMethods()) {
            MsgMethod msgMethod = method.getAnnotation(MsgMethod.class);
            if (msgMethod != null) {
                int index = access.getIndex(method.getName(), KFPlayer.class, JsonMsg.class);
                int msgId = msgMethod.value();
                registerMsg(msgId);//注册消息
                this.msgMethodMap.put(msgId, index);
            }
        }
    }

    private final void doProcess(JsonMsg clientMsg, KFPlayer player) {
        int msgId = clientMsg.getMsgId();
        if (msgMethodMap.containsKey(msgId)) {
            int index = msgMethodMap.get(msgId);
            access.invoke(this, index, player, clientMsg);
            return;
        }
    }

}
