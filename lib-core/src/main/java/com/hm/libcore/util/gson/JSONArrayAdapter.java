package com.hm.libcore.util.gson;

import java.lang.reflect.Type;

import org.json.JSONArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;



// ************************************************************************
// condition adapter for serialization of JSONArray class

public class JSONArrayAdapter implements JsonSerializer< JSONArray >
{
	
	// ************************************************************************
	// JsonSerializer interface
	
	// ------------------------------------------------------------------------
	@Override
	public JsonElement serialize( JSONArray array, Type arg1, JsonSerializationContext context )
	{
		JsonArray result = new JsonArray();
		
		for( int index = 0; index < array.length(); index++ )
		{
			JsonElement child = context.serialize( array.opt( index ) );
			result.add( child );
		}
		
		return result;
	}
	
}

