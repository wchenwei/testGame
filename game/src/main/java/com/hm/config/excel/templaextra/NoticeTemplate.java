package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.BroadcastTemplate;
import com.hm.observer.ObservableEnum;

@FileConfig("broadcast")
public class NoticeTemplate extends BroadcastTemplate{
	private ObservableEnum observableEnum;
	
	public void init() {
		this.observableEnum = ObservableEnum.getObservableEnum(getObId());
		if(this.observableEnum == null) {
			System.err.println("=====广播出错======="+getObId());
		}
	}
	
	
}
