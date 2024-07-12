package com.hm.libcore.util.gson;

import java.lang.reflect.Type;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

// ************************************************************************
// condition adapter for serialization of JSONObject class

public class JSONObjectAdapter implements JsonSerializer< JSONObject >, JsonDeserializer< JSONObject >
{
	
	// ************************************************************************
	// JsonSerializer interface
	
	// ------------------------------------------------------------------------
	@Override
	public JsonElement serialize( JSONObject json, Type arg1, JsonSerializationContext context )
	{
		JsonObject result = new JsonObject();
		
		Iterator<?> iter = json.keys();
		
		while( iter.hasNext() )
		{
			String key = ( String )iter.next();
			JsonElement child = context.serialize( json.opt( key ) );
			result.add( key, child );
		}
		
		return result;
	}
	
	
	
	// ************************************************************************
	// JsonDeserializer interface
	
	// ------------------------------------------------------------------------
	@Override
	public JSONObject deserialize(JsonElement element, Type arg1, JsonDeserializationContext context) throws JsonParseException
	{
		try
		{
			return new JSONObject( element.toString() );
		}
		catch( JSONException e )
		{
//			//Log.Error( "JSONObjectAdapter.deserialize(): unable to reconstruct JSONObject from '%s'", element.toString() );
			return null;
		}
	}
	
}

