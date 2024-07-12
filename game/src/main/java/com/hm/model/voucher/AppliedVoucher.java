package com.hm.model.voucher;

import java.util.Date;

public class AppliedVoucher {
	private String	id;//兑换码主码的id
	private int	useCount;
	private Date appliedAt;
	public AppliedVoucher() {}
	public AppliedVoucher( String voucherId, Date appliedAt )
	{
		this.id			= voucherId;
		this.useCount	= 1;
		this.appliedAt	= appliedAt;
	}
	public String getId() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	public Date getAppliedAt() {
		return appliedAt;
	}
	public void setAppliedAt(Date appliedAt) {
		this.appliedAt = appliedAt;
	}
	public void addUseCount() {
		this.useCount++;
	}
	
	
}
