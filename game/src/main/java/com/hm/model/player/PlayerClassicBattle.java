package com.hm.model.player;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerClassicBattle extends PlayerDataContext {
	//key-战役类型,value-最高纪录
	private ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();

}
