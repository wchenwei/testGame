package com.hm.libcore.util.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 线程池构造器
 * @author siyun
 *
 */
public class ThreadPoolUtils {
	/**
	 * 
	 * @param corePoolSize 核心线程数
	 * @param maximumPoolSize 最大线程数
	 * @param threadNameFormat 线程名前缀标识
	 * @return
	 */
	public static ExecutorService buildExecutorService(int corePoolSize,int maximumPoolSize,String threadNameFormat) {
		return new ThreadPoolExecutor(
				corePoolSize,
				maximumPoolSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat(threadNameFormat)
                    .build());
	}
}
