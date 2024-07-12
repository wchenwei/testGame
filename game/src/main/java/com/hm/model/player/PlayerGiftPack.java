package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.action.giftpack.bean.GiftPackBean;
import com.hm.action.giftpack.bean.Pfund;
import com.hm.enums.GroupGiftType;
import com.hm.libcore.msg.JsonMsg;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家礼包信息
 * @date 2024/5/13 19:46
 */
public class PlayerGiftPack extends PlayerDataContext {
	//新手礼包信息
	private ArrayList<GiftPackBean> newGifts = Lists.newArrayList();
	//基金数据
	private ConcurrentHashMap<Integer, Pfund> fundMap = new ConcurrentHashMap<>();
	//礼包
	private ConcurrentHashMap<Integer,PYGiftGroup> groupMap = new ConcurrentHashMap<>();


	public void addNewPlayerGift(int giftId) {
		GiftPackBean gift = getNewPlayerGiftPack(giftId);
		if(gift != null) {
			System.out.println(super.Context().getId()+"已经买过新手礼包了:"+giftId);
			return;
		}
		this.newGifts.add(new GiftPackBean(giftId));
		SetChanged();
	}

	public GiftPackBean getNewPlayerGiftPack(int giftId) {
		return newGifts.stream().filter(e -> e.getId() == giftId).findFirst().orElse(null);
	}

	public Pfund getPfund(int type) {
		Pfund pfund = this.fundMap.get(type);
		if(pfund == null) {
			pfund = new Pfund(type);
			this.fundMap.put(type,pfund);
			SetChanged();
		}
		return pfund;
	}

	public void doBuyFund(int type) {
		Pfund pfund = getPfund(type);
		pfund.setStatus(1);
		SetChanged();
	}

	public PYGiftGroup getGiftGroup(int type) {
		PYGiftGroup giftGroup = this.groupMap.get(type);
		if(giftGroup == null) {
			giftGroup = new PYGiftGroup(type);
			this.groupMap.put(type,giftGroup);
		}
		return giftGroup;
	}

	public void doDayReset() {
		getGiftGroup(GroupGiftType.Day.getType()).doReset(super.Context());
		SetChanged();
	}

	public void doWeekReset() {
		getGiftGroup(GroupGiftType.Week.getType()).doReset(super.Context());
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerGiftPack", this);
	}
}



