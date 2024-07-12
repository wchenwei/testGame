package com.hm.libcore.voucher;

import java.text.ParseException;
import java.util.Date;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.libcore.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;


public class VoucherSet extends DBEntity<String> {
	//描述
	private String description;
	//操作者
	private String author;
	//生成个数
	private long generatedCount;
	// 每个人可以使用此类型兑换码的次数
	private long maxPerUserCount;
	// 每个兑换码最多使用次数
	private long maxVoucherUsageCount;
	//兑换码有效期
	private String validFrom;
	//有效期
	private String validUntil;
	//产品
	private String product;
	//最小版本号
	private int minimalAppVersion = -1;
	//奖励物品id
	private String rewards;
	//生成时间
	private String generatedAt;
	
	
	public VoucherSet(int serverId,String description, String author, long generatedCount,
			long maxPerUserCount, long maxVoucherUsageCount, String validFrom,
			String validUntil, String product, int minimalAppVersion,
			String rewards) {
		super();
		setId(PrimaryKeyWeb.getPrimaryKey("voucherName", serverId));
		this.description = description;
		this.author = author;
		this.generatedCount = generatedCount;
		this.maxPerUserCount = maxPerUserCount;
		this.maxVoucherUsageCount = maxVoucherUsageCount;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.product = product;
		this.minimalAppVersion = minimalAppVersion;
		this.rewards = rewards;
		this.generatedAt = TimeUtils.GetCurrentTimeStamp();
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getGeneratedCount() {
		return generatedCount;
	}
	public void setGeneratedCount(long generatedCount) {
		this.generatedCount = generatedCount;
	}
	public long getMaxPerUserCount() {
		return maxPerUserCount;
	}
	public void setMaxPerUserCount(long maxPerUserCount) {
		this.maxPerUserCount = maxPerUserCount;
	}
	public long getMaxVoucherUsageCount() {
		return maxVoucherUsageCount;
	}
	public void setMaxVoucherUsageCount(long maxVoucherUsageCount) {
		this.maxVoucherUsageCount = maxVoucherUsageCount;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getMinimalAppVersion() {
		return minimalAppVersion;
	}
	public void setMinimalAppVersion(int minimalAppVersion) {
		this.minimalAppVersion = minimalAppVersion;
	}
	public String getRewards() {
		return rewards;
	}
	public void setRewards(String rewards) {
		this.rewards = rewards;
	}
	public String getGeneratedAt() {
		return generatedAt;
	}
	public void setGeneratedAt(String generatedAt) {
		this.generatedAt = generatedAt;
	}
	/**
	 * 检查是否可以领取兑换码
	 * @param userCount
	 * @param when
	 * @param product
	 * @param appVersion
	 * @param voucherUsageCount
	 * @return
	 */
	public ApplyResult isValid(int userCount, Date when, String product,
			int appVersion, long voucherUsageCount) {
		/*if (true == this.isDisabled()) {
			return ApplyResult.VoucherInvalid;
		}*/
		if (null != this.getProduct()) {
			if (false == this.product.equalsIgnoreCase(product)) {
				return ApplyResult.VoucherInvalid;
			}
		}
		if (this.getMaxPerUserCount() >= 0 && this.getMaxPerUserCount() <= userCount) {
			return ApplyResult.VoucherAlreadyUsed;
		}
		if (this.getMaxVoucherUsageCount() > 0) {
			if (voucherUsageCount >= this.getMaxVoucherUsageCount()) {
				return ApplyResult.VoucherAlreadyUsed;
			}
		}
		try {
			if (StringUtils.isNotBlank(this.getValidFrom())&&!TimeUtils.ParseTimeStamp(this.getValidFrom()).before(when)) {
				return ApplyResult.VoucherInvalid;
			}

			if (StringUtils.isNotBlank(this.getValidUntil())&&TimeUtils.ParseTimeStamp(this.getValidUntil()).before(when)) {
				return ApplyResult.VoucherExpired;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (-1 != this.getMinimalAppVersion()) {
			if(appVersion < this.getMinimalAppVersion()) {
				return ApplyResult.VoucherInvalid;
			}
		}
		return ApplyResult.Ok;
	}
	

}
