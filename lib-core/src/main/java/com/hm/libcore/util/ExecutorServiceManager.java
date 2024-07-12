package com.hm.libcore.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceManager {
	private static ExecutorServiceManager instance;
	public static ExecutorServiceManager getInstance() {
		if(instance == null) {
			instance = new ExecutorServiceManager();
		}
		return instance;
	}
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	public ExecutorService getExecutorService() {
		return executor;
	}
	
	public void shutdown() {
		executor.shutdown();
	}
}
