package com.hm.libcore.leaderboards;

import java.util.*;

public class LeaderboardPutMultiDescriptor
{
	// ************************************************************************
	// fields
	
	protected String								Name;
	
	protected Map< String, String >					Dimensions	= new HashMap<>();
	protected Map< String, Map< String, Double > >	Scores		= new HashMap<>();
	
	
	// ************************************************************************
	// constructor
	
	// ------------------------------------------------------------------------
	public LeaderboardPutMultiDescriptor( String name )
	{
		Name = name;
	}
	
	
	// ************************************************************************
	// public interface
	
	// ------------------------------------------------------------------------
	public void AddDimension( String name, String value )
	{
		Dimensions.put( name, value );
	}
	
	// ------------------------------------------------------------------------
	public void AddScore( String userID, String name, Double value )
	{
		Map< String, Double > userScores = Scores.get( userID );
		
		if( null == userScores )
		{
			userScores = new HashMap< String, Double >();
			
			Scores.put( userID, userScores );
		}
		
		userScores.put( name, value );
	}
}
