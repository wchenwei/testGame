package com.hm.model.activity;

import com.hm.libcore.db.mongo.ClassChanged;
import com.hm.libcore.msg.JsonMsg;
import org.springframework.data.annotation.Transient;

/**
 * @Description: 活动组件
 * @author siyunlong  
 * @date 2019年8月1日 下午2:46:24 
 * @version V1.0
 */
public abstract class ActivityDataContext  extends ClassChanged{
	@Transient
	private transient AbstractActivity Context;

	public final void LateInit(AbstractActivity context) {
		if(this.Context == null) {
			this.Context = context;
			initData();
		}
	}
	/**
	 * 初始化肢体数据
	 */
	public void initData() {
		
	}
	public final AbstractActivity Context() {
		return this.Context;
	}
	protected void fillMsg(JsonMsg msg) {}
}
