package com.hm.libcore.util;

//************************************************************************
// key-value pair generic class

public class KeyValue< K, V >
{
	protected K	Key;
	protected V	Value;
	
	public KeyValue() {}
	
	public KeyValue( K key, V value )
	{
		Key		= key;
		Value	= value;
	}
	
	public K GetKey()
	{
		return Key;
	}
	
	public V GetValue()
	{
		return Value;
	}
	
	public void SetValue(V value) {
		this.Value = value;
	}
}