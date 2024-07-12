package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.InviteTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:邀请有礼
 * User: xjt
 * Date: 2019年4月26日09:14:01
 */

@Config
public class InviteConfig extends ExcleConfig {
    private Map<Integer, InviteTemplate> inviteTaskMap = Maps.newHashMap();

    @Override
    public void loadConfig() {
        List<InviteTemplate> inviteTaskList = JSONUtil.fromJson(getJson(InviteTemplate.class), new TypeReference<List<InviteTemplate>>() {
        });
        inviteTaskList.forEach(t ->{
        	t.init();
        });
        inviteTaskMap = ImmutableMap.copyOf(inviteTaskList.stream().collect(Collectors.toMap(InviteTemplate::getId, Function.identity())));
    }

    
    public InviteTemplate getInviteTask(int id) {
    	return this.inviteTaskMap.get(id);
    }
    

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(
        		InviteTemplate.class
        );
    }
}
