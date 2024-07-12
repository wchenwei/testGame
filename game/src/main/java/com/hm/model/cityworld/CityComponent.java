package com.hm.model.cityworld;

import com.hm.libcore.db.mongo.ClassChanged;
import org.springframework.data.annotation.Transient;

public class CityComponent extends ClassChanged{
	@Transient
	private transient WorldCity Context;

	public final void LateInit(WorldCity context) {
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
	// ------------------------------------------------------------------------
	public final WorldCity Context() {
		return this.Context;
	}
}
