package com.hm.model;

import com.hm.libcore.db.mongo.ClassChanged;
import org.springframework.data.annotation.Transient;

public class DataContext <T> extends ClassChanged{
	@Transient
	private transient T Context;

	public final void LateInit(T context) {
		if(context != null) {
			this.Context = context;
			initData();
		}
	}
	/**
	 * 初始化肢体数据
	 */
	public void initData() {
		
	}
	
	public final T Context() {
		return this.Context;
	}
}
