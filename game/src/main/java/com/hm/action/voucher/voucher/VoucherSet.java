package com.hm.action.voucher.voucher;

import com.hm.libcore.db.mongo.DBEntity;
import lombok.Getter;

@Getter
public class VoucherSet extends DBEntity<String> {
    private String rewards;
}