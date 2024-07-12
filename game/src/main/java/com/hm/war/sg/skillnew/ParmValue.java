package com.hm.war.sg.skillnew;

/**
 * @Description:参数
 * @author siyunlong  
 * @date 2018年10月11日 下午5:18:50 
 * @version V1.0
 */
public class ParmValue {
	private double base;
	private double lvup;
	
	public ParmValue(String parm) {
		String[] pp = parm.split(":");
		this.base = Double.parseDouble(pp[0]);
		if(pp.length>1) {
			this.lvup = Double.parseDouble(pp[1]);
		}
	}
	
	public double getLvValue(int lv) {
		return base+(lv-1)*lvup;
	}
}
