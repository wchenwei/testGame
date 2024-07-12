package com.hm.model.guild;

import com.hm.libcore.db.mongo.ClassChanged;
import com.hm.libcore.msg.JsonMsg;
import org.springframework.data.annotation.Transient;

public class GuildComponent extends ClassChanged{
	@Transient
	private transient Guild guild;
	
	public final void LateInit(Guild context) {
		if(null==guild) {
			this.guild = context;
			initData();
		}
	}
	/**
	 * 初始化肢体数据
	 */
	public void initData() {
		
	}
	public final Guild Context() {
		return this.guild;
	}
	public void fillMsg(JsonMsg msg,boolean ignoreChange) {
		if(ignoreChange || ClientChanged()) {
			fillMsg(msg);
			ClearClientChanged();
		}
	}
	protected void fillMsg(JsonMsg msg) {}
}
