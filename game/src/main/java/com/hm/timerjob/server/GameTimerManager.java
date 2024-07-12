package com.hm.timerjob.server;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.worldbuild.WorldBuildBiz;
import com.hm.server.GameServerManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;

import static org.quartz.JobBuilder.newJob;


/**
 * 定时处理管理器
 * @author xiaoaogame
 */
public class GameTimerManager {
	private static final GameTimerManager instance = new GameTimerManager();
    public static GameTimerManager getInstance() {
        return instance;
    }
    private SchedulerFactory sf = new StdSchedulerFactory();
    
    //所有服务器运行job
//    private ArrayListMultimap<Integer, JobDetail> serverJobs = ArrayListMultimap.create();
    private Table<Integer,String,JobDetail> serverJobs = HashBasedTable.create();
    
    public void startTimer() {
    	try {
    		Scheduler sched = sf.getScheduler();
    		sched.start();
    		
    		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
    			startServerTimer(serverId);
    		});

		} catch (Exception e) {
			System.out.println("定时任务加载失败");
		}
    	
    }
    
    public void startServerTimer(int serverId) {
    	addServerTimer(serverId, WorldTroopQuartzJob.class);
    	addServerTimer(serverId, WorldCityQuartzJob.class);
    	addServerTimer(serverId, SupplyTroopQuartzJob.class);
    	
    	SpringUtil.getBean(WorldBuildBiz.class).checkStartBuildJob(serverId);
    }
    
    public void shutdownTimer() {
    	try {
    		Scheduler sched = sf.getScheduler();
        	if(sched.isStarted()) {
        		sched.shutdown(true);
        	}
		} catch (Exception e) {
		}
    }
    
    
    public JobDetail addServerTimer(int serverId,Class <? extends Job> jobClass) {
    	try {
    		//检查
    		checkAndRemoveJob(serverId, jobClass);
    		
    		String groupName = jobClass.getName();
    		Scheduler sched = sf.getScheduler();
    		String jobName = groupName+"-"+serverId;
    		//每隔1秒处理一次场景
    		JobDetail lineJob = newJob(jobClass).withIdentity(jobName+"-Job", groupName+"-group").build();
    		lineJob.getJobDataMap().put("serverId", serverId);
    		Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobName+"-trigger", groupName+"-group")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(1)
                            .withRepeatCount(-1)       
                            )
                    .build();
    		sched.scheduleJob(lineJob, trigger);
    		this.serverJobs.put(serverId, groupName, lineJob);
    		return lineJob;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    
    public JobDetail addServerTimer(int serverId,Class <? extends Job> jobClass,String cron) {
    	try {
    		//检查
    		checkAndRemoveJob(serverId, jobClass);
    		
    		String groupName = jobClass.getName();
    		Scheduler sched = sf.getScheduler();
    		String jobName = groupName+"-"+serverId;
    		//每隔1秒处理一次场景
    		JobDetail lineJob = newJob(jobClass).withIdentity(jobName+"-Job", groupName+"-group").build();
    		lineJob.getJobDataMap().put("serverId", serverId);
    		Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobName+"-trigger", groupName+"-group")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
    		sched.scheduleJob(lineJob, trigger);
    		this.serverJobs.put(serverId, groupName, lineJob);
    		return lineJob;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public void stopServerJob(int serverId) {
    	Map<String,JobDetail> jobMap = Maps.newHashMap(this.serverJobs.row(serverId));
    	for (Map.Entry<String,JobDetail> entry : jobMap.entrySet()) {
			String jobName = entry.getKey();
			JobDetail jobDetail = this.serverJobs.remove(serverId, jobName);
			if(jobDetail != null) {
				deleteJob(jobDetail);
			}
		}
//    	List<JobDetail> jobDetails = this.serverJobs.removeAll(serverId);
//    	jobDetails.forEach(e -> deleteJob(e));
    }
    
    public void deleteJob(JobDetail lineJob) {
    	try {
    		if(lineJob == null) {
    			return;
    		}
    		Scheduler sched = sf.getScheduler();
    		sched.deleteJob(lineJob.getKey());
		} catch (Exception e) {
			System.out.println("加载");
		}
    }
    
    /**
     * 如果存在就删除此job
     * @param serverId
     * @param jobClass
     */
    public void checkAndRemoveJob(int serverId,Class <? extends Job> jobClass) {
    	String groupName = jobClass.getName();
    	JobDetail lineJob = this.serverJobs.remove(serverId, groupName);
    	if(lineJob != null) {
    		deleteJob(lineJob);
    	}
    }
    
    /**
     * 唤醒某一个任务
     * @param lineJob
     */
    public void resumeJob(JobDetail lineJob) {
    	try {
    		if(lineJob == null) {
    			return;
    		}
    		Scheduler sched = sf.getScheduler();
    		sched.resumeJob(lineJob.getKey());
		} catch (Exception e) {
			System.out.println("加载");
		}
    }
    
    public SchedulerFactory getSf() {
		return sf;
	}
    

	public Table<Integer, String, JobDetail> getServerJobs() {
		return serverJobs;
	}
    
	
}
