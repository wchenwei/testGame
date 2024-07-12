package com.hm.libcore.util.gson;


import java.lang.reflect.Type;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// ************************************************************************
// helper class for manipulating GSON / JSON

public class GSONUtils
{
	
	// ************************************************************************
	// constants
	// ------------------------------------------------------------------------
	public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static Gson gson = new GsonBuilder()
			.setDateFormat( DATEFORMAT )
			.create();
	
	// ************************************************************************
	// public methods
	
	// ------------------------------------------------------------------------
	public static String ToJSONString( Object object )
	{
		return gson.toJson( object );
	}
	
	// ------------------------------------------------------------------------
	public static <T> T FromJSONString( String jsonString, Class<T> classOfT )
	{
		
		return gson.fromJson( jsonString, classOfT );
	}
	
	// ------------------------------------------------------------------------
	public static <T> T FromJSONString( String jsonString, Type typeOfT )
	{
		
		return gson.fromJson( jsonString, typeOfT );
	}
		
	// ------------------------------------------------------------------------
	public static JSONObject ToJSONObject( Object object )
	{
		String jsonString = ToJSONString( object );
		
		if( null == jsonString )
		{
			return null;
		}
		
		try
		{
			return JSONUtils.ToJSONObject( jsonString );
		}
		catch( Exception e )
		{
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	// Ideal for light-weight objects
	// For bigger objects be aware of performance overhead
	public static Object DuplicateObject( Object object )
	{
		return new Gson().fromJson( new Gson().toJson( object ), object.getClass() );
	}
	
	// ------------------------------------------------------------------------
	public static < T > T BuildObjectFromJSON( Gson gson, String stringJSON, Class< T > classOfT )
	{
		if( null != stringJSON )
		{
			T result = gson.fromJson( stringJSON, classOfT );
			
			return result;
		}
		else
		{
			try
			{
				return classOfT.newInstance();
			}
			catch( Exception e )
			{
//				Log.Error( "Cannot create instance of class " + classOfT + ", exception : " + e );
			}
		}
		
		return null;
	}
}
