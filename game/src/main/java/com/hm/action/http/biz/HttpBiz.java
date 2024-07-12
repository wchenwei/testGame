/**  
 * Project Name:SLG_GameFuture.  
 * File Name:HttpBiz.java  
 * Package Name:com.hm.action.http.biz  
 * Date:2018年1月31日上午11:12:27  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.action.http.biz;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.TimeUtils;
import com.hm.actor.ActorDispatcherType;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.model.player.Player;
import com.hm.model.voucher.AppliedVoucher;
import com.hm.libcore.mongodb.MongoUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
@Biz
public class HttpBiz {
	private int timeout = 5000;//连接超时时间5000ms

	@Resource
    private CommValueConfig commValueConfig;
	
	/**  
	 * getWhiteList:获取白名单  
	 *  
	 * @author Administrator  
	 * @return
	 * @throws Exception  使用说明
	 */
	public String getWhiteList(int serverId){
		Map<String , Object> paramMap = Maps.newConcurrentMap();
		paramMap.put("m","getIpWhiteList");
		paramMap.put("action","login.do");
		paramMap.put("serverId",serverId);
		try {
			 return sendHttpForLoginServer(ServerConfig.getInstance().getLoginUrl(),paramMap);
		} catch(Exception e) {
			log.error("获取白名单出错",e);
		}
		return "";
	}
	
	public void sendLoginCreateServer(Player player) {
		Map<String , Object> paramMap = Maps.newConcurrentMap();
		paramMap.put("m","addCreateServer");
		paramMap.put("action","monitor.do");
		paramMap.put("uid",player.getUid());
		paramMap.put("serverId",player.getServerId());
		paramMap.put("playerId",player.getId());
		try {
			sendAsyncPost(ServerConfig.getInstance().getLoginUrl(),paramMap);
		} catch(Exception e) {
			log.error("创建角色更新登录服务器",e);
		}
	}
	

	/**
	 * 
	 * @param voucherId
	 * @param appliedVoucher 玩家兑换记录
	 * @return
	 */
	public JSONObject applyVoucher(String voucherId, int channelId, int appVersion,int vipLv,List<AppliedVoucher> appliedVoucher) {
		String url =  ServerConfig.getInstance().getGmUrl()+"stat/voucher/applyVoucher";
		//String url =  "http://192.168.1.122:8180/renren-admin/stat/voucher/applyVoucher";
        String gameName = MongoUtils.getGameDBName();
        Map<String, Object> paramMap = Maps.newConcurrentMap();
		paramMap.put("voucherId",voucherId);
		paramMap.put("appVersion", appVersion);
		paramMap.put("product", gameName);
		paramMap.put("channelId", channelId);
		paramMap.put("vipLv", vipLv);
		paramMap.put("appliedVoucher", JSON.toJSONString(appliedVoucher));
		String result = HttpUtil.post(url, paramMap);
		return new JSONObject(result);
	}
	/**
	 * 
	 * @return
	 */
	public int getUserMaxLv(long uid){
		return 0;
	}
	
	
	//同步发送
	private String sendHttpForLoginServer(String url,Map<String , Object> paramMap) throws Exception{
		String result = HttpUtil.get(url,paramMap);
		return new JSONObject(result).getStr("data");
	}
	//异步发送post
	public static void sendAsyncPost(String url,Map<String, Object> parms) {
//		//使用任务线程池处理业务逻辑
//		ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)SpringUtil.getBean("serviceExecutor");
//		executor.execute(new HttpTask(url, parms));
        ActorDispatcherType.Http.putTask(new HttpTask(url, parms));
	}
	

	//获取过滤ip白名单
	public String getIpWhite() {
		try {
			String url =  ServerConfig.getInstance().getGmUrl()+"/stat/ipWhite/getAll";
			Map<String , Object> paramMap = Maps.newConcurrentMap();
			return HttpUtil.post(url, paramMap);
		} catch(Exception e) {
			log.error("获取唯一码过滤白名单ip出错",e);
			return "";
		}
	}
	
	public void report(int tarPlayerId,String content) {
		String url =  ServerConfig.getInstance().getGmUrl()+"stat/playerOperate/checkReport";
		Map<String , Object> paramMap = Maps.newConcurrentMap();
		paramMap.put("playerId",tarPlayerId);
		paramMap.put("content", content);
		HttpUtil.post(url, paramMap);
	}

    //获取被禁言或被拉小黑屋的玩家
    public String getNoChatIds() {
        String url = ServerConfig.getInstance().getGmUrl() + "stat/playerOperate/getNochatPlayerIds";
        Map<String, Object> paramMap = Maps.newConcurrentMap();
        try {
            return HttpUtil.post(url, paramMap);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 拉小黑屋
     *
     * @return
     * @throws Exception 使用说明
     * @author Administrator
     */
    public String blackHome(long playerId, int hour) {
        String url = ServerConfig.getInstance().getGmUrl() + "stat/playerOperate/sysBlackHome";
        Map<String, Object> paramMap = Maps.newConcurrentMap();
        paramMap.put("playerId", playerId);
        paramMap.put("startTime", TimeUtils.getTimeString(System.currentTimeMillis()));
        paramMap.put("endTime", TimeUtils.getTimeString(System.currentTimeMillis() + 2 * GameConstants.HOUR));
        try {
            return HttpUtil.post(url, paramMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

	
	
}
  
