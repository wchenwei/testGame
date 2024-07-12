package com.hm.action.http.biz;

import cn.hutool.http.HttpUtil;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.util.gson.GSONUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Slf4j
public class HttpTask implements Runnable, IRunner {
	private String url;
	private Map<String, Object> parms;
	

	public HttpTask(String url, Map<String, Object> parms) {
		super();
		this.url = url;
		this.parms = parms;
	}
	
	@Override
	public void run() {
		try {
			HttpUtil.post(url, parms);
		} catch (Exception e) {
			log.error("http异步调用出错-----"+(url+":"+ GSONUtils.ToJSONString(parms)));
			log.error("http异步调用出错-----", e);
		}
	}

    @Override
    public Object runActor() {
        run();
        return null;
    }
}
