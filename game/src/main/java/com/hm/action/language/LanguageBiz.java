package com.hm.action.language;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.LanguageCnTemplateConfig;

import javax.annotation.Resource;

@Biz
public class LanguageBiz {
	@Resource
	private LanguageCnTemplateConfig langeConfig;

	public String getCampConfigOfficialName(int lv,int officialType) {
		if(officialType <= 3) {
			return "off_name_"+officialType+"_1";
		}else{
			return "off_name_"+officialType+"_"+lv;
		}
	}

}
