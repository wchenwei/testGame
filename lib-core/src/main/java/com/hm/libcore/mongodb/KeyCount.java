package com.hm.libcore.mongodb;

public class KeyCount implements Comparable<KeyCount>{
	private Object id;
	private long count;
	
	public KeyCount(Object id, long count) {
		this.id = id;
		this.count = count;
	}
	public KeyCount() {
		super();
	}


	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	@Override
	public int compareTo(KeyCount o) {
		return o.count - this.count > 0?1:-1;
	}
}