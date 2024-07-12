/**  
 * Project Name:SLG_GameHot.
 * File Name:PlayerFix.java  
 * Package Name:com.hm.model.player  
 * Date:2018年6月29日上午10:01:13  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.model.player;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.chsdk.event2.CHSDKContants2;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.util.PubFunc;
import lombok.Getter;

/**  
 * ClassName: PlayerFix. <br/>  
 * Function: 用户相对固定的数据. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年6月29日 上午10:01:13 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Getter
public class PlayerFix extends PlayerDataContext{
	private String sysType;
	private String iosToken;
	//是否已校服老兵回归；1：老兵回归；0：不是老兵回归；-1：未检验
	private int isOldSoldierBack = -1;

	private String phoneModel;
	private String mac;

	private String channelApplyId;		 	//channelApplyId
	private String chChannelId;  			//pfid
	private String chAppid;			 		//g
	private String chsourceId;				//sid
	private String chuserId;		 		//cid

    private String account;

    private String ov;      //热更前版本号
    private String nv;     //热更后版本号
    private String vc;             //客户端版本code
	private String openId;
	private int loginChannelId;//本次登录的渠道id
	private String language = "1";
	private String inviteCode;//邀请码

	private transient String ip;


	public void initMsg(HMSession session, JsonMsg msg) {
		this.ip = session.getRemoteIp();
		this.sysType = msg.getString("sysType");
		this.phoneModel = msg.getString("phoneModel");
		this.chuserId = msg.getString("chuserId");
		this.mac = msg.getString("mac");
		if ("ios".equals(this.chuserId) || "windows".equals(this.chuserId)) {
			this.chuserId = CHSDKContants2.TFId;
			this.channelApplyId = null;
			this.chChannelId = null;
			this.chAppid = null;
			this.chsourceId = null;
		} else {
			this.channelApplyId = msg.getString("channelApplyId");
			this.chChannelId = msg.getString("chChannelId");
			this.chAppid = msg.getString("chAppid");
			this.chsourceId = msg.getString("chsourceId");
		}
		this.ov = msg.getString("old_hot_version");      //热更前版本号
		this.nv = msg.getString("new_hot_version");     //热更后版本号
		this.vc = msg.getString("versionCode");             //客户端版本code
		this.account = msg.getString("account");
		this.openId = msg.getString("openid");

		try {
			if(StrUtil.isNotEmpty(this.account)) {
				RedisTypeEnum.Account2Id.put(this.account,super.Context().getId()+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SetChanged();
	}


	public void setIsOldSoldierBack(Player player, int isOldSoldierBack) {
		this.isOldSoldierBack = isOldSoldierBack;
		player.playerFix().SetChanged();
	}

	public void setLoginChannelId(int loginChannelId) {
		this.loginChannelId = loginChannelId;
		SetChanged();
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
		SetChanged();
	}

	public boolean isSendCHSDK() {
		return PubFunc.parseInt(chuserId) > 0;
	}
}



