package com.hm.timerjob.server;

import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.spring.SpringUtil;
import com.hm.timerjob.WorldTroopJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.quartz.*;

/**
 * @Description: 此job执行完后才会执行下一个
 * @author siyunlong  
 * @date 2018年11月19日 下午5:15:29 
 * @version V1.0
 */
@Slf4j
@DisallowConcurrentExecution
public class WorldTroopQuartzJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		int serverId = (int)job.getJobDataMap().get("serverId");
		try {	
			if(!ServerStateCache.serverIsRun()) {
				return;
			}
			WorldTroopJob temp = SpringUtil.getBean(WorldTroopJob.class);
			temp.doJob(serverId);
		} catch (Exception e) {
			log.error("WorldTroopQuartzJob执行出错:"+serverId, e);
		}
	}
	
}
