package com.hmkf.model;

import org.springframework.data.annotation.Transient;

import com.hm.libcore.db.mongo.ClassChanged;
import com.hm.libcore.msg.JsonMsg;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家的肢体类, 所有隶属于玩家的模块都需要继承此类
 * @date 2017年12月7日 下午4:47:58
 */
public abstract class KFPlayerDataContext extends ClassChanged {
    @Transient
    private transient KFBasePlayer Context;

    public final void LateInit(KFBasePlayer context) {
        if (this.Context == null) {
            this.Context = context;
            initData();
        }
    }

    /**
     * 初始化肢体数据
     */
    public void initData() {

    }

    public final KFBasePlayer Context() {
        return this.Context;
    }

    public void fillMsg(JsonMsg msg, boolean ignoreChange) {
        if ((ignoreChange || ClientChanged()) && canFill()) {
            fillMsg(msg);
            ClearClientChanged();
        }
    }

    public boolean canFill() {
        return true;
    }

    protected void fillMsg(JsonMsg msg) {
    }

//	public int getUpdateMsgId() {
//		return 0;
//	}
//	
//	public void sendUpdateMsg() {
//		int msgId = getUpdateMsgId();
//		if(msgId <= 0) {
//			System.err.println("此模块没有更新消息:"+this.getClass().getName());
//			return;
//		}
//		JsonMsg msg = JsonMsg.create(msgId);
//		fillMsg(msg);
//		Context.sendMsg(msg);
//	}
}
