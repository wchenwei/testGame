package com.hm.action.http.gm;

public class OnlineNumInfo {
	private String saveDate;
	private int userNum;
	
	public OnlineNumInfo() {
		super();
	}
	public OnlineNumInfo(String saveDate,int userNum) {
		super();
		this.saveDate = saveDate;
		this.userNum = userNum;
	}
	public String getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(String saveDate) {
		this.saveDate = saveDate;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	
	

}
