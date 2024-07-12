package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.TankMagicReformTemplate;
import com.hm.config.excel.templaextra.MagicReformSkillTemplate;
import com.hm.model.tank.TankAttr;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:魔改
 * User: xjt
 * Date: 2019年10月15日10:16:44
 */

@Config
public class MagicReformConfig extends ExcleConfig {
    private Map<Integer, TankMagicReformTemplate> magicReformMap = Maps.newHashMap();
    private Map<Integer,MagicReformSkillTemplate> skillMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
    	loadMagicReformConfig();
    	loadMagicReformSkillConfig();
    }
    
    private void loadMagicReformConfig() {
    	List<TankMagicReformTemplate> list = JSONUtil.fromJson(getJson(TankMagicReformTemplate.class), new TypeReference<List<TankMagicReformTemplate>>() {});
        magicReformMap = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(TankMagicReformTemplate::getId, Function.identity())));
	}
    
    private void loadMagicReformSkillConfig() {
    	List<MagicReformSkillTemplate> list = JSONUtil.fromJson(getJson(MagicReformSkillTemplate.class), new TypeReference<List<MagicReformSkillTemplate>>() {});
    	list.forEach(t ->t.init());
    	skillMap = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(MagicReformSkillTemplate::getId, Function.identity())));
	}
    
    public MagicReformSkillTemplate getSkill(int id){
    	return this.skillMap.get(id);
    }
    
    public TankMagicReformTemplate getMagicReformTemplate(int tankId,int reformLv){
    	return magicReformMap.values().stream().filter(t ->t.getTank_id()==tankId&&reformLv>=t.getChange_num_left()&&reformLv<=t.getChange_num_right()).findFirst().get();
    }
    
    

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(TankMagicReformTemplate.class,MagicReformSkillTemplate.class);
    }
    //获取技能属性
	public TankAttr getSkillAttr(int skillId, int lv) {
		TankAttr tankAttr = new TankAttr();
		MagicReformSkillTemplate template = getSkill(skillId);
		if(template==null){
			return tankAttr;
		}
		tankAttr.addAttr(template.getAttrMap(lv));
		return tankAttr;
	}
}
