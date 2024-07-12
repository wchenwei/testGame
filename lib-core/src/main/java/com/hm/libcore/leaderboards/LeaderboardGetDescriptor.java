package com.hm.libcore.leaderboards;

import com.hm.libcore.util.KeyValue;

public class LeaderboardGetDescriptor
{
	// ************************************************************************
	// fields
	
	protected String						Name;
	protected KeyValue< String, String > Dimension;
	protected String						Score;
	
	
	// ************************************************************************
	// constructor
	
	// ------------------------------------------------------------------------
	public LeaderboardGetDescriptor( String name, String score )
	{
		Name	= name;
		Score	= score;
	}
	
	
	// ************************************************************************
	// public interface
	
	// ------------------------------------------------------------------------
	public void AddDimension( String name, String value )
	{
		Dimension = new KeyValue< String, String >( name, value );
	}
	
	// ------------------------------------------------------------------------
	public String GetKeyScores()
	{
		String key = "leaderboards:" + Name + ":scores";
		
		if( null != Dimension )
		{
			key += ":" + Dimension.GetKey() + ":" + Dimension.GetValue();
		}
		else
		{
			key += ":global";
		}
		
		key += ":" + Score;
		
		return key;
	}
	
	// ------------------------------------------------------------------------
	public String GetKeyScoresSum()
	{
		String key = "leaderboards:" + Name + ":properties:scoresSum:" + Score;
		
		return key;
	}
}
