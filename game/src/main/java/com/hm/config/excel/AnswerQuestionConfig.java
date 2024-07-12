package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.temlate.ActiveAnswerLibraryTemplate;
import com.hm.config.excel.temlate.AnswerOnceRewardTemplate;
import com.hm.config.excel.templaextra.AnswerRankRewardTemplate;
import com.hm.enums.CommonValueType;
import com.hm.model.item.Items;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Config ( "AnswerQuestionConfig" )
public class AnswerQuestionConfig extends ExcleConfig {
	private List<Integer> questionIds = Lists.newArrayList();
	private Map<Integer,AnswerOnceRewardTemplate> onceRewardMap = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
		loadQuestionConfig();
		loadOnceRewardConfig();
	}

	private void loadOnceRewardConfig() {
        List<ActiveAnswerLibraryTemplate> list = JSONUtil.fromJson(getJson(ActiveAnswerLibraryTemplate.class), new TypeReference<List<ActiveAnswerLibraryTemplate>>() {
        });
        List<Integer> questionIds = list.stream().map(t ->t.getId()).collect(Collectors.toList());
        this.questionIds = ImmutableList.copyOf(questionIds);
	}

	private void loadQuestionConfig() {
		Map<Integer,AnswerOnceRewardTemplate> onceRewardMap = Maps.newConcurrentMap();
		for(AnswerOnceRewardTemplate template :JSONUtil.fromJson(getJson(AnswerOnceRewardTemplate.class), new TypeReference<List<AnswerOnceRewardTemplate>>() {})){
			template.init();
			onceRewardMap.put(template.getId(), template);
		}
		this.onceRewardMap = ImmutableMap.copyOf(onceRewardMap);
	}
	
	public List<Items> getRightReward(int id){
		AnswerOnceRewardTemplate template = onceRewardMap.get(id);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getRewards();
	}
	
	public List<Integer> generateQuestionIds(){
		List<Integer> temp = Lists.newArrayList(questionIds);
		Collections.shuffle(temp);
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		return temp.subList(0, commValueConfig.getCommValue(CommonValueType.ANSWER_NUM));
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(AnswerRankRewardTemplate.class,AnswerOnceRewardTemplate.class,ActiveAnswerLibraryTemplate.class);
	}

}
