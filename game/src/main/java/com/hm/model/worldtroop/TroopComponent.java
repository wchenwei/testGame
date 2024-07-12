package com.hm.model.worldtroop;

import com.hm.libcore.db.mongo.ClassChanged;
import org.springframework.data.annotation.Transient;

public class TroopComponent extends ClassChanged{
	@Transient
	private transient WorldTroop Context;

	public final void LateInit(WorldTroop context) {
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
	public final WorldTroop Context() {
		return this.Context;
	}
}
