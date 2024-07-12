package com.hm.model.cd;

import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.enums.CDType;
import com.hm.model.player.BasePlayer;
import org.springframework.data.annotation.Transient;

public class CdData {
	private int type;//CDType
	private int count;//当前数量
	private transient long nextCdTime;//下次cd恢复时间
	
	@Transient
	public int nextSecond;//距离下次恢复还有多少秒
	
	public CdData(CDType type) {
		this.type = type.getType();
		this.count = type.getDefaultCount(); 
	}
	public CdData() {
		super();
	}
	
	//重置cd
	public void resetCD(BasePlayer player) {
		this.count = 0;
		this.nextCdTime = 0;
	}

	//清除Cd
	public void clearCd(BasePlayer player){
		CDType cdType = CDType.getCDType(type);
		this.count = 1;
		this.nextCdTime = System.currentTimeMillis()+cdType.getCdSecond(player)*GameConstants.SECOND;
	}
	public boolean checkCD(BasePlayer player) {
		long now = System.currentTimeMillis();
		if(nextCdTime > now) {//判断cd时间
			return false;
		}
		CDType cdType = CDType.getCDType(type);
		if(nextCdTime <= 0) {//第一次触发
			if(cdType.isDayReset()) {
				this.nextCdTime = now;
			}else{
				this.nextCdTime = now+cdType.getCdSecond(player)*GameConstants.SECOND;
			}
			return true;
		}
		int cdSecond = cdType.getCdSecond(player);//恢复间隔
		int maxCount = cdType.getMaxCount(player);//最大恢复数
		if(count >= maxCount) {//判断是否还能恢复
			this.nextCdTime = now+cdSecond*GameConstants.SECOND;
			return true;
		}
		long diffSecond = (now-this.nextCdTime)/GameConstants.SECOND;
		int add = (int)diffSecond/cdSecond+1;
		this.count = Math.min(this.count+add, maxCount);
		if(this.count >= maxCount) {//判断是否满了
			this.nextCdTime = now+cdSecond*GameConstants.SECOND;
			return true;
		}
		//计算下次恢复时间
		long overSecond = diffSecond%cdSecond;
		this.nextCdTime = System.currentTimeMillis()+(cdSecond-overSecond)*GameConstants.SECOND;
		return true;
	}

	public int getNextCdSecond() {
		return DateUtil.getRemainTime(nextCdTime);
	}
	public void calNextCdSecond() {
		this.nextSecond = DateUtil.getRemainTime(nextCdTime);
	}
	
	public void addCount(long add) {
		this.count += add;
	}
	//获取当前cd次数
	public int getCount() {
		return count;
	}
	
	public int getType() {
		return type;
	}
	//使用cd
	public boolean touchCdEvent(BasePlayer player) {
		return touchCdEvent(player,1);
	}
	//使用cd
	public boolean touchCdEvent(BasePlayer player,int reduce) {
		CDType cdType = CDType.getCDType(type);
		int cdSecond = cdType.getCdSecond(player);
		int maxCount = cdType.getMaxCount(player);
		if(this.count >= maxCount) {
			//开始触发倒计时
			this.nextCdTime = System.currentTimeMillis()+cdSecond*GameConstants.SECOND;
		}
		this.count = Math.max(count-reduce, 0);
		return true;
	}
	//重新开始计算cd
	public void cd(BasePlayer player){
		CDType cdType = CDType.getCDType(type);
		int cdSecond = cdType.getCdSecond(player);
		this.nextCdTime = System.currentTimeMillis()+cdSecond*GameConstants.SECOND;
	}
	
}
