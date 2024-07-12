package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveMainTemplate;
@FileConfig("active_main")
public class ActivityMainTemplate extends ActiveMainTemplate {
	public boolean isCanCal() {
		return getCal_type() == 1;
	}
}
