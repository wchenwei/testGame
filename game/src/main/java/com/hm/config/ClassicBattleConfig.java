package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ClassicAfricaTemplate;
import com.hm.config.excel.temlate.ClassicMoscowTemplate;
import com.hm.config.excel.templaextra.ClassicAfricaExtraTemplate;
import com.hm.config.excel.templaextra.ClassicMaginotExtraTemplate;
import com.hm.config.excel.templaextra.ClassicMoscowExtraTemplate;
import com.hm.config.excel.templaextra.IClassicBattleTemplate;
import com.hm.enums.BattleType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Config
public class ClassicBattleConfig extends ExcleConfig{
	//马奇诺战役配置
	private Map<Integer,ClassicMaginotExtraTemplate> maginotMap = Maps.newConcurrentMap();
	//北非配置
	private Map<Integer,ClassicAfricaExtraTemplate> africaMap = Maps.newConcurrentMap();
	//波兰配置
	private Map<Integer,ClassicMoscowExtraTemplate> moscowMap = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
		loadMaginotConfig();
		loadAfricaConfig();
		loadMoscowConfig();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ClassicMaginotExtraTemplate.class,ClassicAfricaTemplate.class,ClassicMoscowTemplate.class);
	}
	private void loadMaginotConfig(){
		try {
			Map<Integer, ClassicMaginotExtraTemplate> maginotMap = Maps.newHashMap();
			List<ClassicMaginotExtraTemplate> templateList = JSONUtil.fromJson(getJson(ClassicMaginotExtraTemplate.class), new TypeReference<ArrayList<ClassicMaginotExtraTemplate>>(){});
			for(ClassicMaginotExtraTemplate template:templateList){
				template.init();
				maginotMap.put(template.getWave(),template);
			}
			this.maginotMap = ImmutableMap.copyOf(maginotMap);
			log.info("马奇诺战役加载完成");
		} catch (Exception e) {
			log.info("马奇诺战役加载失败");
		}
	}
	
	private void loadAfricaConfig(){
		try {
			Map<Integer, ClassicAfricaExtraTemplate> africaMap = Maps.newHashMap();
			List<ClassicAfricaExtraTemplate> templateList = JSONUtil.fromJson(getJson(ClassicAfricaExtraTemplate.class), new TypeReference<ArrayList<ClassicAfricaExtraTemplate>>(){});
			for(ClassicAfricaExtraTemplate template:templateList){
				template.init();
				africaMap.put(template.getWave(),template);
			}
			this.africaMap = ImmutableMap.copyOf(africaMap);
			log.info("北非战役配置加载完成");
		} catch (Exception e) {
			log.info("北非战役加载完成");
		}
	}
	
	private void loadMoscowConfig(){
		try {
			Map<Integer, ClassicMoscowExtraTemplate> moscowMap = Maps.newHashMap();
			List<ClassicMoscowExtraTemplate> templateList = JSONUtil.fromJson(getJson(ClassicMoscowExtraTemplate.class), new TypeReference<ArrayList<ClassicMoscowExtraTemplate>>(){});
			for(ClassicMoscowExtraTemplate template:templateList){
				template.init();
				moscowMap.put(template.getWave(),template);
			}
			this.moscowMap = ImmutableMap.copyOf(moscowMap);
			log.info("莫斯科战役加载完成");
		} catch (Exception e) {
			log.info("莫斯科战役加载失败");
		}
	}
	private IClassicBattleTemplate getMaginotTemplate(int id){
		return this.maginotMap.get(id);
	}
	private IClassicBattleTemplate getMoscowTemplate(int id){
		return this.moscowMap.get(id);
	}
	private IClassicBattleTemplate getAfricaTemplate(int id){
		return this.africaMap.get(id);
	}
	private int getMaginotMaxId(){
		return maginotMap.keySet().stream().reduce(Integer::max).get();
	}
	private int getMoscowMaxId(){
		return moscowMap.keySet().stream().reduce(Integer::max).get();
	}
	private int getAfricaMaxId(){
		return africaMap.keySet().stream().reduce(Integer::max).get();
	}
	public int getMaxId(int type){
		return getAfricaMaxId();
	}
	public IClassicBattleTemplate getClassTemplate(int type,int id){
		return null;
	}
	
}
