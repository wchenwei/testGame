package com.hm.libcore.voucher;

public enum ApplyResult {
	Ok(1,"可用"),
	VoucherExpired(2,"兑换码失效"),
	VoucherAlreadyUsed(3,"已经使用过"),
	VoucherInvalid(4,"无效的兑换码"),
	NotSameChannel(5,"不同渠道的兑换码"),
	VipLimit(6,"vip等级限制");
	private ApplyResult(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	
	private String desc;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static ApplyResult getApplyResultByStr(String str){
		try {
			return ApplyResult.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
