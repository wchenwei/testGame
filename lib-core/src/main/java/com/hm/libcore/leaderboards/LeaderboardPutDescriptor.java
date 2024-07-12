package com.hm.libcore.leaderboards;

import java.util.*;

import cn.hutool.core.util.StrUtil;

public class LeaderboardPutDescriptor
{
	// ************************************************************************
	// fields
	
	protected String				Name;
	protected String				UserID;
	
	protected Map< String, Object >	Dimensions	= new HashMap<>();
	protected Map< String, Double >	Scores		= new HashMap<>();
	
	
	// ************************************************************************
	// constructor
	
	// ------------------------------------------------------------------------
	public LeaderboardPutDescriptor( String name, String userID )
	{
		Name	= name;
		UserID	= userID;
	}
	
	
	// ************************************************************************
	// public interface
	
	// ------------------------------------------------------------------------
	public void AddDimension( String name, Object value )
	{
		if(StrUtil.isEmpty(name)) {
			return;
		}
		Dimensions.put( name, value );
	}
	
	// ------------------------------------------------------------------------
	public void AddScore( String name, Double value )
	{
		if(StrUtil.isEmpty(name)) {
			return;
		}
		Scores.put( name, value );
	}
	public void AddScore( String name, long value )
	{
		if(StrUtil.isEmpty(name)) {
			return;
		}
		Scores.put( name, Double.valueOf(value) );
	}
}
