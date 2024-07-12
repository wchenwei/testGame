package com.hm.util;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 日志管理器
 * @author siyunlong  
 * @date 2020年8月7日 下午8:34:27 
 * @version V1.0
 */
@NoArgsConstructor
public class LogMode<T>{
	private ArrayList<T> logList = new ArrayList<>();
	private int maxNum = 10;
	
	public LogMode(int maxNum) {
		super();
		this.maxNum = maxNum;
	}

    public T addLog(T log) {
		this.logList.add(log);
		if(this.logList.size() > maxNum) {
            return this.logList.remove(0);
        }
        return null;
	}

	public List<T> getLogList() {
		return logList;
	}
	
}
