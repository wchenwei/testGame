package com.hm.model.activity;

import com.hm.enums.PlayerAddrType;

public interface IPlayerAddrValue {
	void addPlayerAddrValue(String name,String phone,String addr);
	PlayerAddrType getPlayerAddrType();

}
