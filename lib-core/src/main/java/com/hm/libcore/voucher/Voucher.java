package com.hm.libcore.voucher;

import com.hm.libcore.db.mongo.DBEntity;


public class Voucher extends DBEntity<String> {
	private String	voucherSet;
	private long	usageCount;
	
	
	public Voucher() {
		super();
	}
	public Voucher(String uuid,String voucherSet) {
		super();
		this.voucherSet = voucherSet;
		this.setId(uuid);
	}
	
	
	public String getVoucherSet() {
		return voucherSet;
	}
	public void setVoucherSet(String voucherSet) {
		this.voucherSet = voucherSet;
	}
	public long getUsageCount() {
		return usageCount;
	}
	public void setUsageCount(long usageCount) {
		this.usageCount = usageCount;
	}
	//使用
	public void voucherUse() {
		this.usageCount++;
	}
		
}
