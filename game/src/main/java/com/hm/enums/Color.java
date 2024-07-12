package com.hm.enums;
 
/** 
 * 宝石颜色
 *
 * @author  k60
 */
public enum Color {
 
	/**
	 * 红
	 */
	RED(1,"红"),
	
	/**
	 * 黄
	 */
	YELLOW(2,"黄"),
	
	/**
	 * 蓝
	 */
	BLUE(3,"蓝"),
	
	/**
	 * 绿
	 */
	GREEN(4,"绿"),
	
	/**
	 * 紫
	 */
	PURPLE(5,"紫");

	private Color(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;

	private String desc;
	
}