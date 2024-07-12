package com.hm.action.http;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.http.model.ActivityEntity;
import com.hm.enums.ActivityType;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.util.string.StringUtil;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.ActivityFactory;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2018-05-16
 */
@Service("activity.do")
public class GmActivityHandlerAction implements IHttpAction {

    @Resource
    private ActivityBiz activityBiz;
    /**
     * 删除活动
     */
    public void delete(HttpSession session) {
        Integer serverId = getServerId(session);
        if(!GameServerManager.getInstance().isDbServer(serverId)) {
			return;
		}

        try {
    		String activityIdList = URLDecoder.decode(session.getParams("activityList"), "utf-8");
            List<String> idList = StringUtil.splitStr2StrList(activityIdList, ",");
            
            boolean isChange = false;//是否改变了内存数据
            for (String id : idList) {
            	if(ActivityServerContainer.of(serverId).removeActivity(id)) {
            		isChange = true;
            	}
    		}
            if(isChange) {//通知所有在线玩家
            	activityBiz.broadPlayerActivityUpdate(serverId);
            }
            session.Write("1");
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}
    }

    /**
     * 添加活动
     */
    public void batchPass(HttpSession session) {
    	try {
    		String activityList = URLDecoder.decode(session.getParams("activityList"), "utf-8");
    		Integer serverId = getServerId(session);
    		if(!GameServerManager.getInstance().isDbServer(serverId)) {
    			return;
    		}
            List<ActivityEntity> entityList = JSON.parseArray(activityList, ActivityEntity.class);
            boolean isChange = false;//是否改变了内存数据
            
            for (ActivityEntity entity : entityList) {
                ActivityType activityType = entity.getActivityType();
                // 不允许操作永久性活动
                if (activityType == null || activityType.isForeverType()) {
                    continue;
                }
                AbstractActivity activity = ActivityFactory.createAbstractActivity(activityType,
                        entity.getStartTime().getTime(), entity.getEndTime().getTime());

                activity.setShowTime(entity.getViewTime().getTime());
                activity.setCalTime(entity.getClearingTime());
                activity.setServerId(serverId);
                activity.setId(entity.getId() + "");
                
                if(StrUtil.isNotBlank(entity.getExtend())){
                	activity.loadExtend(entity.getExtend());
                }
                if(ActivityServerContainer.of(serverId).addActivity(activity)) {
                	isChange = true;
                }
            }

            if (isChange) {//通知所有在线玩家
                activityBiz.broadPlayerActivityUpdate(serverId);
            }
            session.Write("1");
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}
    }
    
    public void batchDelete(HttpSession session) {
        try {
    		String activityIdList = URLDecoder.decode(session.getParams("activityList"), "utf-8");
            List<String> idList = StringUtil.splitStr2StrList(activityIdList, ",");
            GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
            	removeActivityList(serverId, idList);
            });
            session.Write("1");
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}
    }
    
    private void removeActivityList(int serverId,List<String> idList) {
    	boolean isChange = false;//是否改变了内存数据
        for (String id : idList) {
        	if(ActivityServerContainer.of(serverId).removeActivity(id)) {
        		isChange = true;
        	}
		}
        if(isChange) {//通知所有在线玩家
        	activityBiz.broadPlayerActivityUpdate(serverId);
        }
    }

    public void refreshHideAct(HttpSession session) {
        List<Integer> hideList = StringUtil.splitStr2IntegerList(session.getParams("types"), ",");
        for (int serverId : GameServerManager.getInstance().getServerIdList()) {
            ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
            if (serverData != null) {
                serverData.getServerActData().setHideTypes(hideList);
                serverData.broadServerUpdate();
                serverData.save();
            }
        }
        session.Write("suc");
    }
}
