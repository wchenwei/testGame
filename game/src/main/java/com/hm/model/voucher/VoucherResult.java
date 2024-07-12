package com.hm.model.voucher;


import com.hm.libcore.voucher.ApplyResult;
import com.hm.model.item.Items;

import java.util.List;

public class VoucherResult {
	private ApplyResult result;
	private List<Items> rewards;
	public VoucherResult(ApplyResult result, List<Items> rewards) {
		this.result = result;
		this.rewards = rewards;
	}
	public VoucherResult(ApplyResult result) {
		this.result = result;
	}
	public ApplyResult getResult() {
		return result;
	}
	public void setResult(ApplyResult result) {
		this.result = result;
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public void setRewards(List<Items> rewards) {
		this.rewards = rewards;
	}
	
	
}
