package com.hm.timerjob.server;

import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.spring.SpringUtil;
import com.hm.timerjob.WorldCityJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.quartz.*;

@Slf4j
@DisallowConcurrentExecution
public class WorldCityQuartzJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		int serverId = (int)job.getJobDataMap().get("serverId");
		try {	
			if(!ServerStateCache.serverIsRun()) {
				return;
			}
			WorldCityJob temp = SpringUtil.getBean(WorldCityJob.class);
			temp.doJob(serverId);
		} catch (Exception e) {
			log.error("WorldCityQuartzJob执行出错:"+serverId, e);
		}
	}
	
}
