/**  
 * Project Name:SLG_RebornGame.  
 * File Name:PlayerVipInfo.java  
 * Package Name:com.hm.model.player  
 * Date:2017年12月11日下午2:36:08  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import io.netty.util.internal.ConcurrentSet;

/**  
 * ClassName:PlayerVipInfo <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月11日 下午2:36:08 <br/>  
 * @author   zqh  
 * @version  1.1  
 * @since    
 */
public class PlayerVipInfo extends PlayerDataContext {
	private int vipLv;
	private long vipExp;
    //购买过的vip礼包
    private ConcurrentSet<Integer> buyVipGifts = new ConcurrentSet<>();
    private transient boolean isVipPlayer;
    
	public long getVipExp() {
		return vipExp;
	}
	public void addVipExp(long num){
		if(num <= 0) {
			return;
		}
		this.vipExp += num;
		SetChanged();
	}
	public int getVipLv() {
		return vipLv;
	}
	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
		SetChanged();
	}
	public boolean isVip() {
		return this.vipLv>0;
	}
	
	public void addBuyVipGifts(int giftId) {
		this.buyVipGifts.add(giftId);
		SetChanged();
	}
	public boolean isCanBuyVipGift(int giftId) {
		return !this.buyVipGifts.contains(giftId);
	}
	
	public boolean isVipPlayer() {
		return isVipPlayer;
	}
	public void setVipPlayer(boolean isVipPlayer) {
		this.isVipPlayer = isVipPlayer;
		SetChanged();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerVipInfo", this);
	}
}
  
