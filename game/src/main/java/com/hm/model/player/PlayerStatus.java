package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.PlayerStatusType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家状态信息
 * @author siyunlong  
 * @date 2017年12月22日 下午1:12:40 
 * @version V1.0
 */
public class PlayerStatus extends PlayerDataContext{
	private ConcurrentHashMap<Integer,StatusInfo> statusMap = new ConcurrentHashMap<>();
	
	public StatusInfo getStatusInfo(PlayerStatusType type) {
		return statusMap.get(type.getType());
	}
	
	public boolean isStatus(PlayerStatusType type) {
		if(null == getStatusInfo(type)) {
			return false;
		}
		if(getStatusInfo(type).getState()==1&&getStatusInfo(type).getEnd_time()>System.currentTimeMillis()) {
			return true;
		}else {
			return false;
		}
	}
	public void updateStatus(PlayerStatusType type,int state) {
		this.updateStatus(type,0,0,"",state);
	}
	
	public void updateStatus(PlayerStatusType type,long start_time,long end_time,String reason,int state) {
		StatusInfo statusInfo = statusMap.get(type.getType());
		if(null == statusInfo ) {
			statusInfo = new StatusInfo();
			statusMap.put(type.getType(),statusInfo);
		}
		statusInfo.setType(type.getType());
		statusInfo.setEnd_time(end_time);
		statusInfo.setReason(reason);
		statusInfo.setStart_time(start_time);
		statusInfo.setState(state);
		SetChanged();
	}
	public long getStartTime(PlayerStatusType type) {
		int time =0;
		if(null == getStatusInfo(type)) {
			return time;
		}
		return getStatusInfo(type).getStart_time();
	}
	
	public long getEndTime(PlayerStatusType type) {
		int time =0;
		if(null == getStatusInfo(type)) {
			return time;
		}
		return getStatusInfo(type).getEnd_time();
	}
	
	public String getReason(PlayerStatusType type) {
		String str = "";
		if(null == getStatusInfo(type)) {
			return str;
		}
		return getStatusInfo(type).getReason();
	}
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerStatus", this);
	}
	
	private class StatusInfo{
		int type;//状态类型
		long start_time;
		long end_time;
		String reason;//原因
		int state;//状态(1:是.0:非)
		StatusInfo(){
		}
		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state=state;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type=type;
		}
		public long getStart_time() {
			return start_time;
		}
		public void setStart_time(long start_time) {
			this.start_time=start_time;
		}
		public long getEnd_time() {
			return end_time;
		}
		public void setEnd_time(long end_time) {
			this.end_time=end_time;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason=reason;
		}
		
	} 
	
}
