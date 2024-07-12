package com.hm.timerjob.server;

import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.spring.SpringUtil;
import com.hm.timerjob.WorldBuildJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description: 此job执行完后才会执行下一个
 * @author siyunlong  
 * @date 2018年11月19日 下午5:15:29 
 * @version V1.0
 */
@Slf4j
public class WorldBuildQuartzJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		int serverId = (int)job.getJobDataMap().get("serverId");
		try {	
			if(!ServerStateCache.serverIsRun()) {
				return;
			}
			WorldBuildJob temp = SpringUtil.getBean(WorldBuildJob.class);
			temp.doJob(serverId);
		} catch (Exception e) {
			log.error("WorldBuildQuartzJob执行出错:"+serverId, e);
		}
	}
	
}
