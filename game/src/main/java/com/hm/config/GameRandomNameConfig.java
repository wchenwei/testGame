package com.hm.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.NameLibraryTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Config
public class GameRandomNameConfig extends ExcleConfig {
    private List<String> name1List = Lists.newArrayList();
    private List<String> name2List = Lists.newArrayList();

    @Override
    public void loadConfig() {
        loadName();
    }


    public void loadName() {
        List<NameLibraryTemplate> templateList = JSONUtil.fromJson(getJson(NameLibraryTemplate.class), new TypeReference<ArrayList<NameLibraryTemplate>>(){});
        this.name1List = templateList.stream().map(e -> StrUtil.trim(e.getName_1()))
                .filter(e -> StrUtil.isNotEmpty(e))
                .collect(Collectors.toList());
        this.name2List = templateList.stream().map(e -> StrUtil.trim(e.getName_2()))
                .filter(e -> StrUtil.isNotEmpty(e))
                .collect(Collectors.toList());
    }

    public String randomName() {
        return RandomUtil.randomEle(this.name1List) + RandomUtil.randomEle(this.name2List);
    }


    public List<String> getPrefix(){
        return name1List;
    }
    public List<String> getSuffix(){
        return name2List;
    }


    public String randomName(List<String> npcNameList) {
        for (int i = 0; i < 200; i++) {
            String name = randomName();
            if(!CollUtil.contains(npcNameList,name)) {
                return name;
            }
        }
        return randomName();
    }
}
