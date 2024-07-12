package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.voucher.VoucherSet;
import com.hm.model.item.Items;
import com.hm.model.voucher.AppliedVoucher;
import com.hm.util.ItemUtils;

import java.util.Date;
import java.util.List;

public class PlayerVoucher extends PlayerDataContext {
	/**
	 * 兑换码记录
	 */
	private	List<AppliedVoucher> appliedLog = Lists.newArrayList();
	
	
	public List<AppliedVoucher> getAppliedLog() {
		return appliedLog;
	}

	public void setAppliedLog(List<AppliedVoucher> appliedLog) {
		this.appliedLog = appliedLog;
	}

	/**
	 * 领取兑换码奖励
	 * @param voucherId
	 * @return
	 *//*
	public VoucherResult ApplyVoucher(String voucherId){
		Voucher voucher = VoucherDBUtils.getVoucherById(voucherId);//找到兑换码
		int appVersion = super.Context().playerBaseInfo().getVersion();
		//根据voucherId找到兑换码
		if( null == voucher ){
			return new VoucherResult(ApplyResult.VoucherInvalid);
		}
		
		String voucherSetName = voucher.getVoucherSet();
		
		VoucherSet voucherSet = VoucherDBUtils.getVoucherSetByName( voucherSetName );//找到兑换码主码
		if( null == voucherSet ){
			return new VoucherResult(ApplyResult.VoucherInvalid);
		}
		int userCount = 0;
		AppliedVoucher applied = findAppliedVoucher( voucherSetName );
		
		if( null != applied ){
			userCount = applied.getUseCount();
		}
		
		Date appliedAt = new Date();
		
		//检查兑换码是否可用
		ApplyResult result = voucherSet.isValid( userCount, appliedAt, "slg_game", appVersion, voucher.getUsageCount() );
		
		if( ApplyResult.Ok != result ){
			return new VoucherResult(result);
		}
		
		if( null == applied ){
			applied = new AppliedVoucher( voucherSetName, appliedAt );
			appliedLog.add( applied );
		}else{
			applied.addUseCount();
		}
		
		//发放奖励
		List<Items> rewards = parseItem(voucherSet);
		ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
		itemBiz.addItem((Player)super.Context(), rewards, LogType.Voucher);
		//使用次数增加
		voucher.voucherUse();
		super.SetChanged();
		//保存兑换码
		VoucherDBUtils.saveOrUpdate(voucher);
		return new VoucherResult(ApplyResult.Ok,rewards);
	}*/
	
	/**
	 * 查找领取过的兑换码记录
	 * @param voucherId
	 * @return
	 */
	private AppliedVoucher findAppliedVoucher( String voucherId ){
		for( AppliedVoucher voucher : appliedLog ){
			if( voucher.getId().equalsIgnoreCase( voucherId ) ){
				return voucher;
			}
		}
		
		return null;
	}
	
	private static List<Items> parseItem(VoucherSet voucherSet) {
		String rewarditems = voucherSet.getRewards();
		List<Items> items = Lists.newArrayList();
		items.addAll(ItemUtils.str2ItemList(rewarditems, ",", ":"));
		return items;
	}

	public void addAppliedLog(String voucherSetName) {
		AppliedVoucher applied = findAppliedVoucher( voucherSetName );
		if(applied==null){
			applied = new AppliedVoucher( voucherSetName, new Date() );
			this.appliedLog.add(applied);
		}else{
			applied.addUseCount();
		}
		SetChanged();
	}
}
