package com.hm.libcore.inner.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InnerClientTimer {
	private BaseInnerClient client;
	private boolean isRun;
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	
	public InnerClientTimer(BaseInnerClient client) {
		this.client = client;
	}
	
	public void startCheckServer() {
		if(!isRun) {
			Runnable runnable = new Runnable() {
	            public void run() {
	            	if(client.isConnect()) {
	            		client.checkClient();
	            	}else{
	            		client.connectServer();
	            	}
	            }
	        };
			// 参数：1、任务体 2、首次执行的延时时间  3、任务执行间隔 4、间隔时间单位
		    service.scheduleAtFixedRate(runnable, 3, 3, TimeUnit.SECONDS);
		    this.isRun = true;
		}
	}
}
