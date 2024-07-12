package com.hm.model.player;

import com.hm.libcore.db.mongo.ClassChanged;
import com.hm.libcore.msg.JsonMsg;
import org.springframework.data.annotation.Transient;

/**
 * @Description: 玩家的肢体类,所有隶属于玩家的模块都需要继承此类
 * @author siyunlong  
 * @date 2017年12月7日 下午4:47:58 
 * @version V1.0
 */
public abstract class PlayerDataContext extends ClassChanged{
	@Transient
	private transient BasePlayer Context;

	public final void LateInit(BasePlayer context) {
		if(this.Context == null) {
			this.Context = context;
			initData();
		}
	}

    public void clearContext() {
        this.Context = null;
    }
	/**
	 * 初始化肢体数据
	 */
	public void initData() {
		
	}
	
	@Override
	public void SetChanged() {
		super.SetChanged();
		if(this.Context != null) {
			this.Context.changeDBChangeMark();
		}
	}
	
	public final BasePlayer Context() {
		return this.Context;
	}
	public void fillMsg(JsonMsg msg,boolean ignoreChange) {
		if((ignoreChange || ClientChanged())&&canFill()) {
			fillMsg(msg);
			ClearClientChanged();
		}
	}
	public boolean canFill() {
		return true;
	}
	protected void fillMsg(JsonMsg msg) {}
	
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
