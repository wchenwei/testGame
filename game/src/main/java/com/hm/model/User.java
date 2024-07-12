package com.hm.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.algorithm.AlgorithmUtil;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

/**
 * 登录user
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/9/9 13:51
 */
@NoArgsConstructor
@Data
public class User {
    private String id;
    private int userId;
    private String password;//密码
    private int channelId;//登录的渠道id
    private long regTime;//注册时间
    private long lastLoginTime;//最后登录时间
    private int type;//0游客账号 1账号密码账号  2sdk登录
    private int lastLoginServerId; //最后登陆服务器id
    private Map<Integer, UserInfo> laterAreaServerMap; // 最近登陆的区和服务器
    private String idCode;//唯一识别码

    //有角色的服务器id列表
    private ArrayList<Integer> servers = Lists.newArrayList();


    public User(String account, String password, int type, int channelId) {
        this.id = account;
        this.userId = getUserIdByAddFor3a();
        try {
            this.password = AlgorithmUtil.md5(password);
        } catch (Exception e) {
        }
        this.type = type;
        this.regTime = System.currentTimeMillis();
        this.lastLoginTime = System.currentTimeMillis();
        this.channelId = channelId;
        this.idCode = this.userId + "";
    }

    private static int getUserIdByAddFor3a() {
        MongodDB mongo = MongoUtils.getLoginMongodDB();
        return mongo.getIncrementKey("User", 1);
    }

    public void addRegisterServers(int serverId) {
        this.servers.add(serverId);
    }


    public void setLastLoginServer(int areaId, int serverId) {
        UserInfo userInfo;
        this.lastLoginServerId = serverId;
        if (laterAreaServerMap == null) {
            laterAreaServerMap = Maps.newHashMap();
        }
        userInfo = laterAreaServerMap.get(areaId);
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        userInfo.setLaterLoginServerList(lastLoginServerId);
        laterAreaServerMap.put(areaId, userInfo);
    }

    public void saveDB() {
        MongoUtils.getLoginMongodDB().save(this);
    }
}
