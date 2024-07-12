package com.hm.libcore.leaderboards;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hm.libcore.util.Log;
import com.hm.libcore.util.gson.GSONUtils;
import org.json.JSONArray;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

// ************************************************************************
// class responsible for communicating with the leaderboards server

public class LeaderboardsService extends RedisService
{
	// ************************************************************************
	// types
	
	// ------------------------------------------------------------------------
	public static class PutModes
	{
		public	static final String PUT			= "PUT";
		public	static final String PUT_GREATER	= "PUT_GREATER";
	}
	
	// ------------------------------------------------------------------------
	public static class LeaderboardMetadata
	{
		public Set< String >	Dimensions	= new HashSet<>();
		public Set< String >	Scores		= new HashSet<>();
	}
	
	// ------------------------------------------------------------------------
	public static class UserRankAndScore
	{
		public		String	userID;
		public		long	rank;
		public		double	score;
		protected	String	userMetadata;
		
		public < T > T GetUserMetadata( Class< T > metadataClass )
		{
			return new Gson().fromJson( userMetadata, metadataClass );
		}
	}
	
	
	// ************************************************************************
	// constants
	
	protected	static final String METADATA_USERS			= "metadata:users";
	protected	static final String METADATA_LEADERBOARDS	= "metadata:leaderboards";
	
	
	// ************************************************************************
	// fields
	
	protected	Map< String, String >	LeaderboardsMetadata;
	
	
	// ************************************************************************
	// constructor
	
	// ------------------------------------------------------------------------
	public LeaderboardsService()
	{
		LeaderboardsMetadata = GetAllLeaderboardsMetadata();
	}
	
	
	// ************************************************************************
	// public interface
	
	// ------------------------------------------------------------------------
	public Boolean PutScores( LeaderboardPutDescriptor leaderboard, String mode )
	{
		Object responseValue = RunScript( "put_online_scores", new Gson().toJson( leaderboard ), mode );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		if( null != responseValue )
		{
			return Boolean.parseBoolean( responseValue.toString() );
		}
		
		return null;
	}
	public Boolean PutSyncScores( LeaderboardPutDescriptor leaderboard, String mode )
	{
		Object responseValue = RunScriptSync( "put_online_scores", new Gson().toJson( leaderboard ), mode );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		if( null != responseValue )
		{
			return Boolean.parseBoolean( responseValue.toString() );
		}
		
		return null;
	}
	public Boolean PutScoresForAdd( LeaderboardPutDescriptor leaderboard, String mode )
	{
		Object responseValue = RunScript( "put_online_scores_add", new Gson().toJson( leaderboard ), mode );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		if( null != responseValue )
		{
			return Boolean.parseBoolean( responseValue.toString() );
		}
		
		return null;
	}
	public Boolean PutScoresForTimeAdd( LeaderboardPutDescriptor leaderboard, String mode )
	{
		Object responseValue = RunScript( "put_online_scores_add_time", new Gson().toJson( leaderboard ), mode );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		if( null != responseValue )
		{
			return Boolean.parseBoolean( responseValue.toString() );
		}
		
		return null;
	}
	
	public Boolean PutScoresForBacthAdd( LeaderboardPutDescriptor leaderboard)
	{
		Object responseValue = RunScript( "put_online_scores_batchadd", new Gson().toJson( leaderboard ), PutModes.PUT );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		if( null != responseValue )
		{
			return Boolean.parseBoolean( responseValue.toString() );
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	public Boolean DelScores( LeaderboardPutDescriptor leaderboard, String mode )
	{
		Object responseValue = RunScriptSync( "del_online_rank_scores", new Gson().toJson( leaderboard ), mode );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		if( null != responseValue )
		{
			return Boolean.parseBoolean( responseValue.toString() );
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	public String getRanks(LeaderboardGetRank getRankDescriptor, int startRank, int endRank){
		Object responseValue = RunScriptSync( "get_online_range_of_ranks_with_scores", new Gson().toJson( getRankDescriptor ), startRank , endRank );
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( getRankDescriptor ) );
		return String.valueOf(responseValue);
	}
	
	public String getRanksAndPlayerIds(LeaderboardGetRank getRankDescriptor, int startRank, int endRank,Collection<Long> playerIds){
		Object responseValue = RunScriptSync( "get_online_range_of_ranks_and_playerids", new Gson().toJson( getRankDescriptor ), startRank , endRank ,playerIds);
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( getRankDescriptor ) );
		return String.valueOf(responseValue);
	}
	
	public String getRanks(LeaderboardGetRankForNames getRankDescriptor){
		Object responseValue = RunScriptSync( "get_online_range_of_ranks_with_scores_ranklist", new Gson().toJson( getRankDescriptor ));
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( getRankDescriptor ) );
		return String.valueOf(responseValue);
	}
	public String getPlayerRankByList(LeaderboardGetPlayerRankForList getRankDescriptor){
		Object responseValue = RunScriptSync( "get_online_player_rank_forlist", new Gson().toJson( getRankDescriptor ));
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( getRankDescriptor ) );
		return String.valueOf(responseValue);
	}
	
	public void exchangeScore(LeaderboardGetRank getRankDescriptor, String pid, String oid){
		RunScriptSync( "exchange_player_score", new Gson().toJson( getRankDescriptor ), pid , oid );
	}
	
	public String getRankThanScore(LeaderboardGetRank getRankDescriptor, long minScore,long maxScore){
		Object responseValue = RunScriptSync( "get_online_range_of_ranks_than_score", new Gson().toJson( getRankDescriptor ), minScore,maxScore);
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( getRankDescriptor ) );
		return String.valueOf(responseValue);
	}
	public List<Integer> getUserIdsByRankTankScore(LeaderboardGetRank getRankDescriptor, long minScore,long maxScore){
		List<Integer> userIds = Lists.newArrayList();
		String result = getRankThanScore(getRankDescriptor, minScore, maxScore);
		if("{}".equals(result) || "null".equals(result)) {
			return userIds;
		}
		JsonArray jsonArray = GSONUtils.FromJSONString(result, JsonArray.class);
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			JsonObject leObj = el.getAsJsonObject();
			userIds.add(leObj.get("userID").getAsInt());
		}
		return userIds;
	}
	
	// ------------------------------------------------------------------------
	public String getRankByIds(LeaderboardGetRank getRankDescriptor, Collection<Long> ids){
		Object responseValue = RunScriptSync( "get_online_rank_by_userid", new Gson().toJson( getRankDescriptor ), new Gson().toJson( ids ));
		return String.valueOf(responseValue);
	}
	
	
	public String getRankListByUserId(LeaderboardGetRank getRankDescriptor, List<String> names){
		Object responseValue = RunScriptSync( "get_online_ranklist_by_userid", new Gson().toJson( getRankDescriptor ), new Gson().toJson( names ));
		return String.valueOf(responseValue);
	}
	// ------------------------------------------------------------------------
	public String getRankByRanks(LeaderboardGetRank getRankDescriptor, List<Integer> ranks){
		Object responseValue = RunScriptSync( "get_online_rank_by_ranks", new Gson().toJson( getRankDescriptor ), new Gson().toJson( ranks ));
		return String.valueOf(responseValue);
	}
	public String getRankByRanksAndUserIds(LeaderboardGetRank getRankDescriptor, List<Integer> ranks,List<String> userIds){
		Object responseValue = RunScriptSync( "get_online_rank_by_rankAndUsers", new Gson().toJson( getRankDescriptor ), new Gson().toJson( ranks ), new Gson().toJson( userIds ));
		return String.valueOf(responseValue);
	}
	public String getRankByAround(LeaderboardGetRank getRankDescriptor, String playerId,int size){
		Object responseValue = RunScriptSync( "get_online_ranks_in_around", new Gson().toJson( getRankDescriptor ), playerId, size);
		return String.valueOf(responseValue);
	}
	
	// ------------------------------------------------------------------------
	public void PutScores( LeaderboardPutDescriptor leaderboard )
	{
		PutScores( leaderboard, PutModes.PUT );
	}
	//同步提交排行,失败返回false
	public Boolean PutScoresSync( LeaderboardPutDescriptor leaderboard )
	{
		return PutSyncScores( leaderboard, PutModes.PUT );
	}
	public Boolean UpdatePlayerInfo( LeaderboardPutDescriptor leaderboard )
	{
		Object responseValue = RunScript( "update_player_info", new Gson().toJson( leaderboard ), PutModes.PUT );
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		if( null != responseValue )
		{
			return Boolean.parseBoolean( responseValue.toString() );
		}
		return null;
	}
	
	
	public Boolean PutScoresForAdd( LeaderboardPutDescriptor leaderboard )
	{
		return PutScoresForAdd( leaderboard, PutModes.PUT );
	}
	public Boolean PutScoresForTimeAdd( LeaderboardPutDescriptor leaderboard )
	{
		return PutScoresForTimeAdd( leaderboard, PutModes.PUT );
	}
	// ------------------------------------------------------------------------
	public Boolean DelScores( LeaderboardPutDescriptor leaderboard )
	{
		return DelScores( leaderboard, PutModes.PUT );
	}
	
	// ------------------------------------------------------------------------
	public boolean PutScores( LeaderboardPutMultiDescriptor leaderboard )
	{
		Object responseValue = RunScript( "put_online_scores_multi", new Gson().toJson( leaderboard ) );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		return ( null != responseValue );
	}
	
	// ------------------------------------------------------------------------
	public boolean IncrementScores( LeaderboardPutMultiDescriptor leaderboard )
	{
		Object responseValue = RunScript( "incr_online_scores_multi", new Gson().toJson( leaderboard ) );
		
		Log.Debug( "Uploading leaderboard: " + new Gson().toJson( leaderboard ) );
		
		return ( null != responseValue );
	}
	
	// ------------------------------------------------------------------------
	public double GetScore( LeaderboardGetDescriptor leaderboard, String userID )
	{
		Object responseValue = RunCommand( Commands.ZSCORE, leaderboard.GetKeyScores(), userID );
		
		if( null != responseValue )
		{
			Log.Debug( "Score for leaderboard " + new Gson().toJson( leaderboard ) + " is " + Double.parseDouble( responseValue.toString() ) );
			
			return Double.parseDouble( responseValue.toString() );
		}
		
		return 0;
	}
	
	// ------------------------------------------------------------------------
	public long GetRank( LeaderboardGetDescriptor leaderboard, String userID )
	{
		Object responseValue = RunCommand( Commands.ZREVRANK, leaderboard.GetKeyScores(), userID );
		
		if( null != responseValue )
		{
			Log.Debug( "Rank for leaderboard " + new Gson().toJson( leaderboard ) + " is " + Long.parseLong( responseValue.toString() ) );
			
			return Long.parseLong( responseValue.toString() );
		}
		
		return -1;
	}
	
	public long GetRank( String rankName, String userID )
	{
		Object responseValue = RunCommandSync( Commands.ZREVRANK, rankName, userID );
		
		if(!"null".equals(responseValue) && null != responseValue )
		{
			Log.Debug( "Rank for leaderboard " + rankName + " is " + Long.parseLong( responseValue.toString() ) );
			
			return Long.parseLong( responseValue.toString() );
		}
		
		return -1;
	}
	
	// ------------------------------------------------------------------------
	public List< UserRankAndScore > GetRange( LeaderboardGetDescriptor leaderboard, long startRank, int numRanks )
	{
		Object responseValue = RunScript( "get_online_range_of_ranks_with_scores", new Gson().toJson( leaderboard ), startRank, startRank + numRanks - 1 );
		
		if( null != responseValue )
		{
			Type						listType		= new TypeToken< List< UserRankAndScore > >(){}.getType();
			List< UserRankAndScore >	ranksAndScores	= new Gson().fromJson( responseValue.toString(), listType );
			
			Log.Debug( "Range of ranks for leaderboard " + new Gson().toJson( leaderboard ) + " is " + new Gson().toJson( ranksAndScores ) );
			
			return ranksAndScores;
		}
		
		return new ArrayList< UserRankAndScore >();
	}
	
	// ------------------------------------------------------------------------
	public List< UserRankAndScore > GetRanksAndScores( LeaderboardGetDescriptor leaderboard, Set< String > userIDs )
	{
		Object responseValue = RunScript( "get_online_rank_and_score_multi", new Gson().toJson( leaderboard ), new Gson().toJson( userIDs ) );
		
		if( null != responseValue )
		{
			Type						listType		= new TypeToken< List< UserRankAndScore > >(){}.getType();
			List< UserRankAndScore >	ranksAndScores	= new Gson().fromJson( responseValue.toString(), listType );
			
			Log.Debug( "Ranks and scores for leaderboard " + new Gson().toJson( leaderboard ) + " are " + new Gson().toJson( ranksAndScores ) );
			
			return ranksAndScores;
		}
		
		return new ArrayList< UserRankAndScore >();
	}
	
	// ------------------------------------------------------------------------
	public long GetRecordsCount( LeaderboardGetDescriptor leaderboard )
	{
		Object responseValue = RunCommand( Commands.ZCARD, leaderboard.GetKeyScores() );
		
		if( null != responseValue )
		{
			Log.Debug( "Records count for leaderboard " + new Gson().toJson( leaderboard ) + " is " + Long.parseLong( responseValue.toString() ) );
			
			return Long.parseLong( responseValue.toString() );
		}
		
		return 0;
	}
	
	public long GetRecordsCount(String rankName)
	{
		try {
			Object responseValue = RunCommandSync( Commands.ZCARD, rankName);
			
			if( null != responseValue )
			{
				Log.Debug( "Records count for leaderboard " + rankName + " is " + Long.parseLong( responseValue.toString() ) );
				
				return Long.parseLong( responseValue.toString() );
			}
		} catch (Exception e) {
			
		}
		return -1;
	}
	
	// ------------------------------------------------------------------------
	public Map< String, Long > GetScoresSum( LeaderboardGetDescriptor leaderboard )
	{
		Object responseValue = RunCommand( Commands.HGETALL, leaderboard.GetKeyScoresSum() );
		
		if( null != responseValue )
		{
			Map< String, Long >	sumsMap		= new HashMap<>();
			JSONArray			sumsArray	= ( JSONArray ) responseValue;
			
			for( int i = 0; i < sumsArray.length(); i += 2 )
			{
				sumsMap.put( sumsArray.optString( i ), Long.parseLong( sumsArray.optString( i + 1 ) ) );
			}
			
			Log.Debug( "Sum of scores for leaderboard " + new Gson().toJson( leaderboard ) + " is " + new Gson().toJson( sumsMap ) );
			
			return sumsMap;
		}
		
		return new HashMap<>();
	}
	
	// ------------------------------------------------------------------------
	public boolean DeleteUser( String userID )
	{
		Object responseValue = RunScript( "delete_online_user" , userID );
		
		Log.Debug( "Deleting user: " + userID );
		
		return ( null != responseValue );
	}
	
	// ------------------------------------------------------------------------
	public boolean DeleteLeaderboard( String leaderboard )
	{
		Object responseValue = RunScript( "delete_online_lb", leaderboard );
		
		Log.Debug( "Deleting leaderboard: " + leaderboard );
		
		return ( null != responseValue );
	}
	
	// ------------------------------------------------------------------------
	public boolean PutUserMetadata( String userID, String metadata )
	{
		return PutMetadata( METADATA_USERS, userID, metadata );
	}
	
	// ------------------------------------------------------------------------
	public boolean PutLeaderboardMetadata( String leaderboard, LeaderboardMetadata metadata )
	{
		return PutMetadata( METADATA_LEADERBOARDS, leaderboard, new Gson().toJson( metadata ) );
	}
	
	// ------------------------------------------------------------------------
	public String GetUserMetadata( String userID )
	{
		return GetMetadata( METADATA_USERS, userID );
	}
	
	// ------------------------------------------------------------------------
	public String GetLeaderboardMetadata( String leaderboard )
	{
		return GetMetadata( METADATA_LEADERBOARDS, leaderboard );
	}
	
	public boolean renameSet(LeaderboardRenameDescriptor renameDescriptor) {
		Object responseValue = RunScriptSync( "rename_zset", new Gson().toJson( renameDescriptor ), PutModes.PUT );
		Log.Debug( "rename_zset: " + new Gson().toJson( renameDescriptor ) );
		if( null != responseValue ){
			return Boolean.parseBoolean( responseValue.toString() );
		}
		return false;
	}

	public boolean resetRankToScore(ResetRankToScore temp) {
		Object responseValue = RunScriptSync( "online_clearzset_to0", new Gson().toJson( temp ), PutModes.PUT );
		Log.Debug( "online_clearzset_to0: " + new Gson().toJson( temp ) );
		if( null != responseValue ){
			return Boolean.parseBoolean( responseValue.toString() );
		}
		return false;
	}
	
	public boolean delKeys(LeaderboardDelKeys leaderboardDelKeys,List<String> lbs) {
		Object responseValue = RunScriptSync( "delete_online_keys", new Gson().toJson( leaderboardDelKeys ), new Gson().toJson( lbs ) );
		Log.Debug( "delete_online_keys: " + new Gson().toJson( leaderboardDelKeys ) );
		if( null != responseValue ){
			return Boolean.parseBoolean( responseValue.toString() );
		}
		return false;
	}
	
	// ------------------------------------------------------------------------
	public Map< String, String > GetAllLeaderboardsMetadata()
	{
		Object responseValue = RunCommand( Commands.HGETALL, METADATA_LEADERBOARDS );
		
		if( null != responseValue )
		{
			Type					hashMapType	= new TypeToken< HashMap< String, String > >(){}.getType();
			Map< String, String >	metadata	= new Gson().fromJson( responseValue.toString(), hashMapType );
			
			Log.Debug( "Metadata full list :" + new Gson().toJson( metadata ) );
			
			return metadata;
		}
		
		return new HashMap<>();
	}
	
	
	// ************************************************************************
	// internal methods
	
	// ------------------------------------------------------------------------
	protected boolean PutMetadata( String key, String field, String metadata )
	{
		Object responseValue = RunCommand( Commands.HSET, key, field, metadata );
		
		Log.Debug( "Uploading metadata: " + key + " " + new Gson().toJson( metadata ) );
		
		return ( null != responseValue );
	}
	
	// ------------------------------------------------------------------------
	protected String GetMetadata( String key, String field )
	{
		Object responseValue = RunCommand( Commands.HGET, key, field );
		
		if( null != responseValue )
		{
			Log.Debug( "Metadata for " + key + ":" + field + " is " + responseValue );
			
			return ( String ) responseValue;
		}
		
		return "";
	}
}