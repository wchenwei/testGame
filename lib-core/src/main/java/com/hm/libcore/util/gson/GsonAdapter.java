package com.hm.libcore.util.gson;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

// ************************************************************************
// generic adapter for serialization of derived sub-classes 
// json适配器

public class GsonAdapter< T > implements JsonSerializer< T >, JsonDeserializer< T >
{
	
	// ************************************************************************
	// fields
	
	private String		ClassBase;
	
	private Class< T >	ClassOfT;
	
	private Gson		Fallback;
	
	private String		SubclassNameKeyword;
	
	
	
	// ************************************************************************
	// constructors
	
	// ------------------------------------------------------------------------
	public GsonAdapter( Class <T> baseClassOfT )
	{
		this( baseClassOfT.getPackage().getName() + ".", baseClassOfT );
	}
	
	// ------------------------------------------------------------------------
	public GsonAdapter( String classBase, Class <T> classOfT )
	{
		this( classBase, classOfT, "_type" );
	}
	
	// ------------------------------------------------------------------------
	public GsonAdapter( String classBase, Class <T> classOfT, String subclassNameKeyword )
	{
		this.ClassBase	= classBase;
		this.ClassOfT	= classOfT;
		
		this.Fallback	= new Gson();
		
		this.SubclassNameKeyword = subclassNameKeyword;
	}
	
	
	
	// ************************************************************************
	// JsonSerializer interface
	// 将类对象转换成json数据
	// ------------------------------------------------------------------------
	@Override
	public JsonElement serialize( T serializedObject, Type typeOfCondition, JsonSerializationContext context )
	{
		String fullClassName	= serializedObject.getClass().getName();
		
		String className		= fullClassName.replace( this.ClassBase, "" );
		
		if( true == this.ClassOfT.equals( serializedObject.getClass() ) ) // base class
		{
			return this.Fallback.toJsonTree( serializedObject );
		}
		
		JsonElement elem = context.serialize( serializedObject );
		
		JsonObject result = new JsonObject();
		result.addProperty( this.SubclassNameKeyword, className );
		
		JsonObject obj = elem.getAsJsonObject();
		
		for( Map.Entry<String, JsonElement> entry : obj.entrySet() )
		{
			result.add( entry.getKey(), entry.getValue() );
		}
		
		return result;
	}
	
	
	
	// ************************************************************************
	// JsonDeserializer interface
	// 将json数据转换成类对象
	// ------------------------------------------------------------------------
	@Override
	public T deserialize( JsonElement json, Type typeofObject, JsonDeserializationContext context ) throws JsonParseException
	{
		JsonObject jsonObject = json.getAsJsonObject();
		
		String className;
		
		if( jsonObject.has( this.SubclassNameKeyword ) )
		{
			className = jsonObject.get( this.SubclassNameKeyword ).getAsString();
		}
		else
		{
			
			return this.Fallback( json );
		}
		
		String fullClassName = String.format( "%s%s", this.ClassBase, className );
		
		Class< ? > desiredClass = null;
		
		try
		{
			desiredClass = Class.forName( fullClassName );
		}
		catch( ClassNotFoundException e )
		{
		}
		
		if( null == desiredClass )
		{
			
			return this.Fallback( json );
		}
		
		return context.deserialize( jsonObject, desiredClass );
	}
	
	
	
	// ************************************************************************
	// internal methods
	
	// ------------------------------------------------------------------------
	private T Fallback( JsonElement json )
	{
		return this.Fallback.fromJson( json, this.ClassOfT );
	}
	
}
