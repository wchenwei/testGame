package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Sets;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.SensitiveWordUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ForbidWordTemplate;
import com.hm.libcore.util.sensitiveWord.sensitive.BadWordSensitiveUtil;
import com.hm.libcore.util.sensitiveWord.sensitive.SysWordSensitiveUtil;
import com.hm.redis.type.RedisTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Slf4j
@Config
public class ForbidConfig extends ExcleConfig{
	@Override
	public void loadConfig() {
		loadForbidConfig();
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ForbidWordTemplate.class);
	}
	
	private void loadForbidConfig(){
		List<ForbidWordTemplate> templateList = JSONUtil.fromJson(getJson(ForbidWordTemplate.class), new TypeReference<ArrayList<ForbidWordTemplate>>(){});
		Set<String> wordSet = Sets.newHashSet(); 
		for(ForbidWordTemplate template:templateList){
			wordSet.add(template.getForbid_word());
		}
        SysWordSensitiveUtil.getInstance().loadWord(wordSet);
		SensitiveWordUtil.add(wordSet);
        //自添加敏感字
        Set<String> myWordSet = RedisTypeEnum.BadWord.getBadWord();
        BadWordSensitiveUtil.getInstance().loadWord(myWordSet, true, false);
		log.info("敏感词库加载完成");
	}
	
	
	
	
}
