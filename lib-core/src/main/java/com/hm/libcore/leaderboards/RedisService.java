package com.hm.libcore.leaderboards;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.Log;
import com.hm.libcore.util.URLUtil;
import com.hm.libcore.util.httppool.HttpPoolClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hm.libcore.util.ExecutorServiceManager;

import cn.hutool.core.util.StrUtil;

// ************************************************************************
// class responsible for communicating with Redis server

public abstract class RedisService
{
	// ************************************************************************
	// constants
	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	private HttpPoolClient httpPoolClient = new HttpPoolClient();
	private int httpType = 3;
	private AbstractRankCmq abstractRankCmq;//排行cmq发射器
	
	public static class Commands
	{
		public	static final String HGET		= "HGET";
		public	static final String HGETALL		= "HGETALL";
		public	static final String HSET		= "HSET";
		public	static final String MSET		= "MSET";
		public	static final String SET			= "SET";
		public	static final String ZCARD		= "ZCARD";
		public	static final String ZREVRANGE	= "ZREVRANGE";
		public	static final String ZREVRANK	= "ZREVRANK";
		public	static final String ZSCORE		= "ZSCORE";
	}
	
	
	// ************************************************************************
	// fields

	protected String REDIS_URL	= ServerConfig.getInstance().getRedisUrl();

//	protected final String REDIS_URL	= "http://119.29.89.218:6383";
	protected final String BASIC_AUTH	= "";
	
	public void reloadRedisUrl() {
		this.REDIS_URL	= ServerConfig.getInstance().getRedisUrl();
	}
	
	// ************************************************************************
	// children-wide public interface
	
	
	// ------------------------------------------------------------------------
	protected Future< Object > RunCommandAsync( String name, Object ... args )
	{
		Map< String, Object > payload = new HashMap<>();
		payload.put( "name", name );
		payload.put( "args", ObjectArrayAsStringArray( args ) );
		
		return SendRequestAsync( ComposeRequestURL( "commands" ), payload );
	}
	
	public int getHttpType() {
		return httpType;
	}

	public void setHttpType(int httpType) {
		this.httpType = httpType;
	}

	protected Object RunCommandSync( String name, Object ... args )
	{
		Map<String, Object> payload = new HashMap<>();
		payload.put("name", name);
		payload.put("args", ObjectArrayAsStringArray(args));
		try {
			Future<Object> result = SendRequestSync(ComposeRequestURL("commands"), payload);
			return result != null ? result.get() : null;
		} catch (Exception e) {
			Log.Info(Log.GetExecutedMethodName() + ": " + e.getCause());
		}
		return null;
	}
	
	// ------------------------------------------------------------------------
	protected Object RunCommand( String name, Object ... args )
	{
		try
		{
			Future< Object > result = RunCommandAsync( name, args );
			
			return result != null ? result.get() : null;
		}
		catch ( ExecutionException e )
		{
			Log.Info( Log.GetExecutedMethodName() + ": " + e.getCause() );
		}
		catch ( Exception e )
		{
			Log.Info( Log.GetExecutedMethodName() + ": " + e );
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	protected Future<Object> RunScriptAsync(String name, Object... args) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("name", name);
		payload.put("args", ObjectArrayAsStringArray(args));
		payload.put("cmqType", rankCmqType(name));
		
		return SendRequestAsync(ComposeRequestURL("scripts"), payload);
	}
	
	protected Object RunScriptSync(String name, Object... args) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("name", name);
		payload.put("args", ObjectArrayAsStringArray(args));
		try {
			Future<Object> result = SendRequestSync(ComposeRequestURL("scripts"), payload);
			return result != null ? result.get() : null;
		} catch (Exception e) {
			Log.Info(Log.GetExecutedMethodName() + ": " + e.getCause());
		}
		return null;
	}
	
	// ------------------------------------------------------------------------
	protected Object RunScript(String name, Object... args) {
		try {
			Future<Object> result = RunScriptAsync(name, args);
			return result != null ? result.get() : null;
		} catch (Exception e) {
			Log.Info(Log.GetExecutedMethodName() + ": " + e.getCause());
		}
		return null;
	}
	
	// ************************************************************************
	// internal methods
	
	// ------------------------------------------------------------------------
	private String ComposeRequestURL( String uri )
	{
		String urlTemplate	= "%s/redis/%s";
		
		return String.format( urlTemplate, REDIS_URL, uri );
	}
	
	// ------------------------------------------------------------------------
	//异步
	private Future<Object> SendRequestAsync(final String url, final Map< String, Object > payload) {
		try {
			if(abstractRankCmq != null) {
				abstractRankCmq.sendRankCmq(payload);
				return null;
			}
			final String payloadJson = new Gson().toJson(payload);
			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					if(httpType == 1) {
						httpPoolClient.sendJson(url, payloadJson);
					}else if(httpType == 2) {
						URLUtil.sendNewJsonPost(url, payloadJson);
					}else{
						URLUtil.sendJsonPost(url, payloadJson);
					}
				}
			});
		} catch (Exception e) {
			Log.Info(Log.GetExecutedMethodName() + ": " + e);
		}

		return null;
	}
	//同步
	private Future<Object> SendRequestSync(final String url, final Object payload) {
		try {
			final String payloadJson = new Gson().toJson(payload);
			return ExecutorServiceManager.getInstance().getExecutorService().submit(new Callable<Object>() {
				@Override
				public String call() throws Exception {
					if(httpType == 1) {
						try {
							String result = httpPoolClient.sendJson(url, payloadJson);
							JSONObject response = new JSONObject(result);
							return String.valueOf(response.get("response"));
						} catch (Exception e) {
						}
					}else if(httpType == 2) {
						return URLUtil.sendNewJsonPost(url, payloadJson);
					}else{ 
//						return URLUtil.sendJsonPost(url, payloadJson);
						return URLUtil.sendOkPost(url, payloadJson);
					}
					return null;
				}
			});
		} catch (Exception e) {
			Log.Info(Log.GetExecutedMethodName() + ": " + e);
		}
		return null;
	}
	
	// ------------------------------------------------------------------------
	private Object ValidateResponse( JSONObject response, String requestURL, String requestPayload )
	{
		try
		{
			int errorCode = response.getInt( "errorCode" );
			
			if( 0 == errorCode )
			{
				Object responseValue = response.get( "response" );
				
				return ( true == JSONObject.NULL.equals( responseValue ) ) ? null : responseValue;
			}
			
			Log.Error( Log.GetExecutedMethodName() + ": (" + errorCode + ") " + response.getString( "errorDesc" ) + ", URL: " + requestURL + ", Payload: " + requestPayload );
		}
		catch( JSONException e )
		{
			Log.Info( Log.GetExecutedMethodName() + ": " + e );
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	private String[] ObjectArrayAsStringArray( Object[] objectArray )
	{
		String[] stringArray = new String[ objectArray.length ];
		
		for( int i = 0; i < objectArray.length; i++ )
		{
			stringArray[ i ] = objectArray[ i ].toString();
		}
		
		return stringArray;
	}
	
	public String getUrl() {
		return REDIS_URL+"/redis/scripts";
	}
	
	public AbstractRankCmq getAbstractRankCmq() {
		return abstractRankCmq;
	}

	public void setAbstractRankCmq(AbstractRankCmq abstractRankCmq) {
		this.abstractRankCmq = abstractRankCmq;
	}

	public int rankCmqType(String name) {
		if(StrUtil.equals(name, "put_online_scores")) {
			return 1;
		}
		return 0;
	}
}