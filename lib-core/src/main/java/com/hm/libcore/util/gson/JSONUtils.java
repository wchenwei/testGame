package com.hm.libcore.util.gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils
{
	// ------------------------------------------------------------------------
	public static JSONArray ToJSONArray( String jsonString ) throws ExpectationFailed
	{
		JSONArray jsonArray = ToJSONArraySafe( jsonString );
		
		if( null != jsonArray )
		{
			return jsonArray;
		}
		
		throw new ExpectationFailed( "Cannot create JSONarray from " + jsonString );
	}
	
	// ------------------------------------------------------------------------
	public static JSONArray ToJSONArraySafe( String jsonString )
	{
		try
		{
			return new JSONArray( jsonString );
		}
		catch( JSONException e )
		{
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	public static JSONObject ToJSONObject( String jsonString ) throws ExpectationFailed
	{
		JSONObject jsonObject = ToJSONObjectSafe( jsonString );
		
		if( null == jsonObject )
		{
			throw new ExpectationFailed( "Cannot create JSONobject from " + jsonString );
		}
		
		return jsonObject;
	}
	
	// ------------------------------------------------------------------------
	public static JSONObject ToJSONObjectSafe( String jsonString )
	{
		try
		{
			return new JSONObject( jsonString );
		}
		catch( JSONException e )
		{
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	public static <T> void Put( JSONObject target, String key, T value ) throws ExpectationFailed
	{
		if( false == PutSafe( target, key, value ) )
		{
			throw new ExpectationFailed( "Cannot put " + key + " into " + target + ". Inner object is " + value );
		}
	}
	
	// ------------------------------------------------------------------------
	public static <T> boolean PutSafe( JSONObject target, String key, T value )
	{
		try
		{
			target.put( key, value );
			return true;
		}
		catch ( JSONException e ){}
		return false;
	}
	
	// ------------------------------------------------------------------------
	public static JSONArray GetJsonArray(JSONObject jsonObject,	String key ) throws ExpectationFailed
	{
		JSONArray result = GetJsonArraySafe( jsonObject, key );
		
		if( null == result )
		{
			throw new ExpectationFailed( "Cannot get " + key + " from" + jsonObject );
		}
		
		return result;
	}
	
	// ------------------------------------------------------------------------
	public static JSONArray GetJsonArraySafe(JSONObject jsonObject,	String key )
	{
		try
		{
			return jsonObject.getJSONArray( key );
		}
		catch( JSONException e )
		{
//			Log.Error( "cannot get JSON array for " + key );
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	public static JSONObject GetJsonObject( JSONObject jsonObject, String key ) throws ExpectationFailed
	{
		JSONObject result = GetJsonObjectSafe( jsonObject, key );
		
		if( null == result )
		{
			throw new ExpectationFailed( "Cannot get " + key + " from" + jsonObject );
		}
		
		return result;
	}
	
	// ------------------------------------------------------------------------
	public static JSONObject GetJsonObjectSafe( JSONObject jsonObject, String key )
	{
		try
		{
			return jsonObject.getJSONObject( key );
		}
		catch( JSONException e )
		{
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	public static JSONObject GetJsonObject( JSONArray jsonObject, int index ) throws ExpectationFailed
	{
		JSONObject result = GetJsonObjectSafe( jsonObject, index );
		
		if( null == result )
		{
			throw new ExpectationFailed( "Cannot get JSONObject at index" + index + " from" + jsonObject );
		}
		
		return result;
	}
	
	// ------------------------------------------------------------------------
	public static JSONObject GetJsonObjectSafe( JSONArray jsonObject, int index )
	{
		try
		{
			return jsonObject.getJSONObject( index );
		}
		catch( JSONException e )
		{
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	public static JSONArray GetJsonArray( JSONArray jsonObject, int index ) throws ExpectationFailed
	{
		try
		{
			return jsonObject.getJSONArray( index );
		}
		catch( JSONException e )
		{
			throw new ExpectationFailed( "Cannot get JSONArray at index" + index + " from" + jsonObject );
		}
	}
	
	// ------------------------------------------------------------------------
	public static Object Get( JSONArray jsonArray, int index ) throws ExpectationFailed
	{
		try
		{
			return jsonArray.get( index );
		}
		catch( JSONException e )
		{
			throw new ExpectationFailed( "Cannot get object at index" + index + " from" + jsonArray );
		}
	}
	
	// ------------------------------------------------------------------------
	public static Object GetSafe( JSONArray jsonArray, int index )
	{
		try
		{
			return jsonArray.get( index );
		}
		catch( JSONException e )
		{
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	public static String PrettyPrint( JSONObject json )
	{
		try
		{
			return json.toString( 2 );
		}
		catch( Exception e )
		{
			return null;
		}
	}
	
	// ------------------------------------------------------------------------
	public static String PrettyPrint( String jsonString, String fallback )
	{
		JSONObject	json		= ToJSONObjectSafe	( jsonString );
		String		formatted	= PrettyPrint		( json );
		
		if( formatted == null )
		{
			formatted = fallback;
		}
		
		return formatted;
	}
	
}
