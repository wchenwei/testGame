package com.hm.timer;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.json.JSONUtil;
import com.hm.container.FilterContainer;
import com.hm.http.HttpBiz;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class SyncNoChatTimer {
    @Resource
    private FilterContainer filterContainer;
    @Resource
    private HttpBiz httpBiz;

    /**
     * 每5分钟检查一次
     * 暂时屏蔽，让前端拦截
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void SyncNoChat() {
        String ids = httpBiz.getNoChatIds();
        filterContainer.loadNoChatIds(JSONUtil.fromJson(ids, new TypeReference<ArrayList<Integer>>() {
        }));
    }
}
