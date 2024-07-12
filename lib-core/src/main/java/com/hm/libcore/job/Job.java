package com.hm.libcore.job;//package com.hm.libcore.job;
//
//import static org.quartz.CronScheduleBuilder.cronSchedule;
//import static org.quartz.JobBuilder.newJob;
//import static org.quartz.TriggerBuilder.newTrigger;
//
//import java.util.Date;
//
//import org.slf4j.Logger;
//import org.quartz.CronTrigger;
//import org.quartz.JobDetail;
//import org.quartz.JobKey;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.SchedulerFactory;
//import org.quartz.TriggerKey;
//import org.quartz.impl.StdSchedulerFactory;
//
//
///**
// * Title: Job.java
// * Description:定时任务类
// * Copyright: Copyright (c) 2014
// * Company: Hammer Studio
// * @author 李飞
// * @date 2014-7-23
// * @version 1.0
// */
//public class Job {
//
//	
//	private static Job job = null;//静态对象
//	
//	private SchedulerFactory factory = null;
//	
//	private Logger log = Logger.getLogger(Job.class);
//	
//	private final String JOB_GROUP_NAME = "JOBGROUP";
//
//	private final String TRIGGER_GROUP_NAME = "TRIGGERGROUP";
//	
//	/**
//	 * 构造方法
//	 * @throws SchedulerException
//	 */
//	private Job() throws SchedulerException {
//		this.factory = new StdSchedulerFactory();
//	}
//	
//	public static Job getInstance() {
//		if (job == null) {
//			try {
//				job = new Job();
//			} catch (SchedulerException e) {
//				// TODO Auto-generated catch block
//				// e.printStackTrace();
//				job.log.error(e.getMessage());
//			}
//		}
//		return job;
//	}
//	
//	/**
//	 * 注册配置
//	 * @param jobName
//	 * @param jobClass
//	 * @param time
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public Date addJob(String jobName, Class jobClass, String time) {
//		try {
//			// job 1 will run at time
//			JobDetail job = newJob(jobClass).withIdentity(jobName,JOB_GROUP_NAME).build();
//			CronTrigger trigger = newTrigger().withIdentity(jobName/* "trigger1" */, TRIGGER_GROUP_NAME/* "group1" */).withSchedule(cronSchedule(time)).build();
//			Scheduler schedule = this.factory.getScheduler();
//			Date ft = schedule.scheduleJob(job, trigger);
//			return ft;
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//		return null;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public Date addJob(String jobName, String jobGroupName, String triggerName,
//			String triggerGroupName, Class jobClass, String time) {
//		try {
//			// job 1 will run at time
//			JobDetail job = newJob(jobClass).withIdentity(jobName/* "job1" */,
//					jobGroupName/* "group1" */).build();
//			CronTrigger trigger = newTrigger().withIdentity(
//					triggerName/* "trigger1" */, triggerGroupName/* "group1" */)
//					.withSchedule(cronSchedule(time)).build();
//			Scheduler schedule = this.factory.getScheduler();
//			Date ft = schedule.scheduleJob(job, trigger);
//			return ft;
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//		return null;
//	}
//	
//	public void modifyJobTime(String jobName, String time) {
//		try {
//			Scheduler sched = this.factory.getScheduler();
//			CronTrigger trigger = (CronTrigger) sched.getTrigger(TriggerKey
//					.triggerKey(jobName, TRIGGER_GROUP_NAME));
//			if (trigger == null) {
//				return;
//			}
//			String oldTime = trigger.getCronExpression();
//			if (!oldTime.equalsIgnoreCase(time)) {
//				JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName,
//						JOB_GROUP_NAME));
//				Class jobClass = jobDetail.getJobClass();
//				removeJob(jobName);
//				this.addJob(jobName, jobClass, time);
//			}
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//	}
//	
//	
//	public void modifyJobTime(String jobName, String jobGroupName,
//			String triggerName, String triggerGroupName, String time) {
//		try {
//			Scheduler sched = this.factory.getScheduler();
//			CronTrigger trigger = (CronTrigger) sched.getTrigger(TriggerKey
//					.triggerKey(triggerName, triggerGroupName));
//			if (trigger == null) {
//				return;
//			}
//			String oldTime = trigger.getCronExpression();
//			if (!oldTime.equalsIgnoreCase(time)) {
//				JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName,
//						jobGroupName));
//				Class jobClass = jobDetail.getJobClass();
//				removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
//				addJob(jobName, jobGroupName, triggerName, triggerGroupName,
//						jobClass, time);
//			}
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//	}
//	
//	public void removeJob(String jobName) {
//		try {
//			Scheduler sched = factory.getScheduler();
//			sched.pauseTrigger(TriggerKey.triggerKey(jobName,
//					TRIGGER_GROUP_NAME));
//			sched.unscheduleJob(TriggerKey.triggerKey(jobName,
//					TRIGGER_GROUP_NAME));
//			sched.deleteJob(JobKey.jobKey(jobName, JOB_GROUP_NAME));
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//	}
//	
//	
//	public void removeJob(String jobName, String jobGroupName,
//			String triggerName, String triggerGroupName) {
//		try {
//			Scheduler sched = this.factory.getScheduler();
//			sched.pauseTrigger(TriggerKey.triggerKey(triggerName,
//					triggerGroupName));
//			sched.unscheduleJob(TriggerKey.triggerKey(triggerName,
//					triggerGroupName));
//			sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//	}
//	
//	public void removeAll() throws SchedulerException {
//		Scheduler schedule = this.factory.getScheduler();
//		schedule.clear();
//	}
//	
//	
//	/**
//	 * 	启动job服务
//	 */
//	public void startJobs() {
//		try {
//			Scheduler sched = this.factory.getScheduler();
//			sched.start();
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//	}
//	
//	public void shutdownJobs() {
//		try {
//			Scheduler sched = this.factory.getScheduler();
//			if (!sched.isShutdown()) {
//				sched.shutdown();
//			}
//		} catch (SchedulerException e) {
//			this.log.error(e.getMessage());
//		}
//	}
//
//}
