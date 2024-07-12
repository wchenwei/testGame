package com.hm.libcore.voucher;

public class VoucherItem {
	private int id;
	private int type;
	private long count;
	
	public VoucherItem(int id,int type, long count) {
		this.id = id;
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public long getCount() {
		return count;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
