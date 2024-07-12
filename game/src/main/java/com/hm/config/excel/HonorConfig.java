package com.hm.config.excel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.hm.config.excel.templaextra.HonorLineTemplate;
import com.hm.libcore.annotation.Config;
import com.hm.model.player.BasePlayer;

import java.util.List;

@Config
public class HonorConfig extends ExcleConfig {
	private ArrayListMultimap<Integer,HonorLineTemplate> typeMap = ArrayListMultimap.create();

	@Override
	public void loadConfig() {
		loadHonorLineTemplate();
	}

	
	private void loadHonorLineTemplate(){
		ArrayListMultimap<Integer,HonorLineTemplate> typeMap = ArrayListMultimap.create();
		for (HonorLineTemplate honorLineTemplate : json2List(HonorLineTemplate.class)) {
			typeMap.put(honorLineTemplate.getType(),honorLineTemplate);
		}
		this.typeMap = typeMap;
	}
	

	//获取所有可领取的id列表
	public List<HonorLineTemplate> getCanHonorLineTemplate(BasePlayer player) {
		List<HonorLineTemplate> tempList = Lists.newArrayList();
		for (HonorLineTemplate honorLineTemplate : typeMap.get(0)) {
			if(honorLineTemplate.isFit(player)) {
				tempList.add(honorLineTemplate);
			}
		}
		return tempList;
	}

	public List<HonorLineTemplate> getHonorList(int type) {
		return this.typeMap.get(type);
	}

}






