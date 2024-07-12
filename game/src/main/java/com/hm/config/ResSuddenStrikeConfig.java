package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ActiveResourceSeverBuffTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Config
public class ResSuddenStrikeConfig extends ExcleConfig{
	private List<ActiveResourceSeverBuffTemplate> buffList = Lists.newArrayList(); 
	@Override
	public void loadConfig() {
//		loadServerBuffConfig();
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveResourceSeverBuffTemplate.class);
	}
	
	private void loadServerBuffConfig(){
		List<ActiveResourceSeverBuffTemplate> templateList = JSONUtil.fromJson(getJson(ActiveResourceSeverBuffTemplate.class), new TypeReference<ArrayList<ActiveResourceSeverBuffTemplate>>(){});
		buffList = templateList;
		log.info("加载物资突袭服务器加成结束");
	}
	
	public double getBuff(int lv){
		ActiveResourceSeverBuffTemplate template =  this.buffList.stream().filter(t ->lv>=t.getSever_lv_down()&&lv<=t.getSever_lv_up()).findFirst().get();
		if(template==null){
			return 0;
		}
		return template.getRate();
	}
	
	
	
	
}
