package com.hm.libcore.inner;

import java.lang.reflect.Method;
import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;

public abstract class InnerAction {
    public MethodAccess access;
    public Map<Integer, Integer> msgMethodMap = Maps.newHashMap();


    public void registerMsg() {
        initMethodAccess();
    }

    private void initMethodAccess() {
        this.access = MethodAccess.get(getClass());
        for (Method method : getClass().getDeclaredMethods()) {
            MsgMethod msgMethod = method.getAnnotation(MsgMethod.class);
            if (msgMethod != null) {
                int index = access.getIndex(method.getName(),JsonMsg.class);
                int msgId = msgMethod.value();
                registerMsg(msgId);
                this.msgMethodMap.put(msgId, index);
            }
        }
    }

    public void doProcess(JsonMsg clientMsg) {
        int msgId = clientMsg.getMsgId();
        if (msgMethodMap.containsKey(msgId)) {
            int index = msgMethodMap.get(msgId);
            access.invoke(this, index, clientMsg);
            return;
        }
    }

    public void registerMsg(int msgId) {
        InnerMsgRouter.getInstance().registMsg(msgId, this);
    }
}
