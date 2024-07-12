
package com.hm.config.template;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Title: Army.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年5月18日 下午2:44:54
 * @version 1.0
 */
public class Army {
	private int id;//坦克ID
	private int count;//数量
	private int index;//位置
	
	public Army(int id, int count, int index) {
		super();
		this.id = id;
		this.count = count;
		this.index = index;
	}

	public Army() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public Army clone() {
		return new Army(id, count, index);
	}

	
	/**
	 *sorce 格式为  位置:数量:id:将领id 
	 */
	public static List<Army> str2ArmyList(String source,String sp1,String sp2) {
		List<Army> armys = Lists.newArrayList();
		for (String str : source.split(sp1)) {
			int id = Integer.parseInt(str.split(sp2)[0]);
			int count = Integer.parseInt(str.split(sp2)[1]);
			int index = Integer.parseInt(str.split(sp2)[2]);
			armys.add(new Army(id,count,index));
		}
		return armys;
	}

	@Override
	public String toString() {
		return "Army [id=" + id + ", count=" + count + ", index=" + index +  "]";
	}
	
	
}

