package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.TroopBuffTemplate;
import com.hm.config.excel.templaextra.WillSkillTemplate;
import com.hm.war.sg.setting.FunctionSetting;
import com.hm.war.sg.setting.SkillSetting;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
@Config
public class TankSkillConfig extends ExcleConfig{
	private Map<Integer, SkillSetting> skillMap = Maps.newHashMap();
	private Map<Integer, FunctionSetting> functionMap = Maps.newHashMap();
	private Map<Integer, TroopBuffTemplate> troopBuffMap = Maps.newHashMap();
    private Map<Integer, WillSkillTemplate> willSkillMap = Maps.newHashMap();

	@Override
	public void loadConfig() {
		this.functionMap = loadFunctionFile().stream()
				.collect(Collectors.toMap(FunctionSetting::getId, Function.identity()));
		this.functionMap.values().stream().forEach(e -> e.init());
		
		List<SkillSetting> skillList = loadSkillFile();
		skillList.stream().forEach(e -> e.init(this));
		this.skillMap = skillList.stream()
				.collect(Collectors.toMap(SkillSetting::getId, Function.identity()));
		this.troopBuffMap = loadTroopBuffTemplate().stream()
				.collect(Collectors.toMap(TroopBuffTemplate::getId, Function.identity()));

        loadWillSkill();
	}
	
	private List<FunctionSetting> loadFunctionFile() {
		return JSONUtil.fromJson(getJson(FunctionSetting.class), new TypeReference<ArrayList<FunctionSetting>>(){});
	}
	private List<SkillSetting> loadSkillFile() {
		return JSONUtil.fromJson(getJson(SkillSetting.class), new TypeReference<ArrayList<SkillSetting>>(){});
	}
	private List<TroopBuffTemplate> loadTroopBuffTemplate() {
		return JSONUtil.fromJson(getJson(TroopBuffTemplate.class), new TypeReference<ArrayList<TroopBuffTemplate>>(){});
	}

    private void loadWillSkill() {
        List<WillSkillTemplate> templateList = JSONUtil.fromJson(getJson(WillSkillTemplate.class), new TypeReference<ArrayList<WillSkillTemplate>>() {
        });
        templateList.forEach(e -> e.init());
        this.willSkillMap = templateList.stream().collect(Collectors.toMap(WillSkillTemplate::getId, Function.identity()));
    }

	@Override
	public List<String> getDownloadFile() {
        return getConfigName(SkillSetting.class, FunctionSetting.class, TroopBuffTemplate.class, WillSkillTemplate.class);
	}
	
	public SkillSetting getSkillSetting(int id) {
		if(!skillMap.containsKey(id)) {
			log.error("技能id不存在=========:"+id);
		}
		return skillMap.get(id);
	}
	public FunctionSetting getFunctionSetting(int id) {
		return functionMap.get(id);
	}
	
	public Map<Integer,Integer> buildTroopBuffSkillList(Set<Integer> buffList) {
		Map<Integer,Integer> skillMap = Maps.newHashMap();
		for (int buffId : buffList) {
			TroopBuffTemplate template = this.troopBuffMap.get(buffId);
			if(template != null) {
				skillMap.put(template.getSkill_id(), 1);
			}
		}
		return skillMap;
	}

    public WillSkillTemplate getWillSkillTemplate(int id) {
        return willSkillMap.get(id);
    }
}
