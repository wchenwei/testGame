
package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.lv.LvAgent;
import com.hm.config.excel.templaextra.WorldBuildLevelTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Config
public class WorldBuildConfig extends ExcleConfig {
	private LvAgent<WorldBuildLevelTemplate> worldLevel;//阵营等级
	
	@Override
	public void loadConfig() {
		loadWorldBuildLevelTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(WorldBuildLevelTemplate.class);
	}
	
	private void loadWorldBuildLevelTemplate(){
		List<WorldBuildLevelTemplate> templateList = JSONUtil.fromJson(getJson(WorldBuildLevelTemplate.class), new TypeReference<ArrayList<WorldBuildLevelTemplate>>(){});
		templateList.forEach(e -> e.init());
		this.worldLevel = new LvAgent<>(templateList, true);
		for (int i = 1; i <= this.worldLevel.getMaxLv(); i++) {
			WorldBuildLevelTemplate lvp = this.worldLevel.getLevelValue(i-1);
			WorldBuildLevelTemplate lvn = this.worldLevel.getLevelValue(i);
			List<String> newList = lvn.getEffectList();//当前等级
			if(lvp != null) {
				newList.addAll(lvp.getEffectList());//+上一等级
			}
			lvn.setEffectList(newList);
			lvn.loadAddType();//加载属性
		}
		log.info("时间建筑等级加载完成");
	}
	
	public int getLvMax(){
		return this.worldLevel.getMaxLv(); 
	}
	public WorldBuildLevelTemplate getLevelTemplate(int lv){
		return this.worldLevel.getLevelValue(lv);
	}
	public int calNewLv(long exp) {
		return this.worldLevel.getLevel(exp);
	}
}






