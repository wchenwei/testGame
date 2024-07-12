package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.PartsTemplate;
import com.hm.config.excel.templaextra.PartsExtraTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 配件配置文件
 * User: yang xb
 * Date: 2018-03-27
 */

@Config
public class PartsConfig extends ExcleConfig {
    /**
     * item part id : ItemPartTemplate
     */
    private Map<Integer, PartsExtraTemplate> itemPartMap = Maps.newHashMap();

    @Override
    public void loadConfig() {
        List<PartsExtraTemplate> partList = loadItemPart();
        partList.forEach(t ->{
        	t.init();
        });
        itemPartMap = partList.stream().collect(Collectors.toMap(PartsTemplate::getId, Function.identity()));
    }

    private List<PartsExtraTemplate> loadItemPart() {
        return JSONUtil.fromJson(getJson(PartsExtraTemplate.class), new TypeReference<List<PartsExtraTemplate>>() {
        });
    }
    
    public PartsExtraTemplate getParts(int partId) {
    	return this.itemPartMap.get(partId);
    }
    
    public List<PartsExtraTemplate> getItemPartTemplateList() {
    	return Lists.newArrayList(itemPartMap.values());
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(
        		PartsExtraTemplate.class
        );
    }
}
