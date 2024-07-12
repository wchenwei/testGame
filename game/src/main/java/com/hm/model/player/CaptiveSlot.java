package com.hm.model.player;

import lombok.Data;

/**
 * @Description: 俘虏位
 * @author siyunlong  
 * @date 2020年7月1日 下午2:09:31 
 * @version V1.0
 */
@Data
public class CaptiveSlot {
	private CaptiveTankInfo tankInfo;
	private int count;//今日俘虏次数
	
	public void loadCaptiveTankInfo(CaptiveTankInfo tankInfo) {
		this.tankInfo = tankInfo;
		this.count ++;
	}
	
	/**
	 * 处理玩家赎回坦克
	 * @param tankId
	 * @param redeemPlayerId
	 * @return
	 */
	public boolean doRedeemTank(int tankId,long redeemPlayerId) {
		if(tankInfo == null) {
			return false;
		}
		if(tankInfo.getTankId() == tankId && tankInfo.getPlayerId() == redeemPlayerId) {
			doFreedTank();
			return true;
		}
		return false;
	}

	public void doFreedTank(){
		tankInfo = null;
	}
	
	public boolean isIdleState() {
		return tankInfo == null || !tankInfo.isFitTime();
	}

	public boolean checkCanTech(){
		if (!isIdleState() && !tankInfo.isDidTech()){
			return true;
		}
		return false;
	}

	public void doTech(){
		tankInfo.doTech();
	}
	
}
