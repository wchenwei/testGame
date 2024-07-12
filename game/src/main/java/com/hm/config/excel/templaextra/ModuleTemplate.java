package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ModuleOpentimeTemplate;
import com.hm.util.OpenTime;

/**
 * 
 * @Description: 模块配置
 * @author siyunlong  
 * @date 2018年8月7日 上午11:30:23 
 * @version V1.0
 */
@FileConfig("module_opentime")
public class ModuleTemplate extends ModuleOpentimeTemplate{
	private OpenTime openTime;
	
	public void init() {
		this.openTime = new OpenTime(getOpen());
	}

	public OpenTime getOpenTime() {
		return openTime;
	}
	
}
