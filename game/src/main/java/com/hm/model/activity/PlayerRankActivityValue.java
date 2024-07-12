package com.hm.model.activity;



/**
 * @Description: 排行活动的玩家数据
 * @author siyunlong  
 * @date 2018年5月28日 下午1:46:11 
 * @version V1.0
 */
public class PlayerRankActivityValue extends PlayerActivityIdListValue{
	private long value;// 活动期间的数据
	private int minRank; //最高排名

	public void addValue() {
		this.value ++ ;
	}
	public void addValue(long add) {
		this.value += add;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public int getMinRank() {
		return this.minRank;
	}
	public void updateMinRank(int rank){
		if(this.minRank <=0){
			this.minRank = rank; 
		}else{
			this.minRank = (rank == -1 ? minRank:Math.min(rank, minRank));
		}
	}
	
	public void setMinRank(int rank){
		this.minRank = rank;
	}
	@Override
	public void clearRepeatValue() {
		this.value = 0;
		this.minRank = 0; 
		super.clearRepeatValue();
	}
	
	
}
