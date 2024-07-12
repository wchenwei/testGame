package com.hm.chsdk.event;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.actor.IRunner;
import com.hm.actor.ActorDispatcherType;
import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.CHSendClient;
import com.hm.chsdk.ChSDKConf;
import com.hm.chsdk.ICHEvent;
import com.hm.model.player.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractChEvent implements ICHEvent, IRunner {
	public int g = CHSDKContants.AppId;//中间键appId
	public int pfid = CHSDKContants.CPId;//渠道平台ID
	public int cid = CHSDKContants.TFId;//投放渠道ID
	public int sid = CHSDKContants.TFSubId;//投放子渠道ID
	public String spfid = CHSDKContants.ChannelAppId;//渠道应用ID
	public String bn = CHSDKContants.CreateId;//生产批次号
	public String sn;//签名(规则见目录)
	public String v = CHSDKContants.Version;//中间件版本号
	public String dn;//设备号
	public String ts = System.currentTimeMillis()+"";//时间戳
//	public String lifeid;//会话ID MD5（设备号+中间键appId+时间戳）
	public String mac;//手机mac地址
	
	public String in;//邀请者OpenID(通过分享获取对方OpenId)
	public String pn;//省份
	public String cn;//城市
	public String con;//国家
	public String no;//网络运营商
	public String nw;//网络类型(2G/3G/4G/wifi)
	public String os;//操作系统
	public String osv;//系统版本
	public String sl;//系统语言
	public String dm;//设备型号
	public String bv;//浏览器版本
	public String brn;//浏览器
	public String pb;//手机品牌
	public String pm;//手机型号
	
	public AbstractChEvent(Player player,ChSDKConf chSDKConf) {
		if(chSDKConf != null) {
			this.pfid = chSDKConf.getAppId();
			this.spfid = chSDKConf.getChannelAppId();
		}
		buildPlayerClient(player);
	} 
	
	public abstract Class buildClass();
	
	
	public void buildPlayerClient(Player player) {
		this.dn = player.playerTemp().getClientParm("dn", "");
		this.mac = player.playerTemp().getClientParm("mac", "");
	}
	
	public String buildSign() {
		try {
			Map<String,String> sortMap = Maps.newHashMap();
			for (Field field : ReflectUtil.getFields(buildClass())) {
	        	String fieldName = field.getName();
	        	field.setAccessible(true);
				Object obj = field.get(this);
				if(obj == null) {
					continue;
				}
				sortMap.put(fieldName, obj.toString());
			}
			List<String> keys = Lists.newArrayList(sortMap.keySet());
			Collections.sort(keys);
			StringBuilder sb = new StringBuilder();
			for (String key : keys) {
				sb.append(key+"="+sortMap.get(key)+"&");
			}
			if(sb.length() > 0) {
				String data = sb.substring(0, sb.length()-1).toString()+CHSDKContants.AppKey;
				if(CHSDKContants.showLog) {
					System.err.println(data);
				}
				String sign = SecureUtil.md5(data).toUpperCase();
				if(CHSDKContants.showLog) {
					System.err.println(sign);
				}
				return sign;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object runActor() {
		this.sn = buildSign();
		CHSendClient.sendData(this);
		return null;
	}

	public void send() {
        ActorDispatcherType.CHEventSend.putTask(this);
	}
	
	public static void main(String[] args) {
		String data = "bn=202003131741&cid=21012&cprid=600062&cpsid=6&cpsn=6服&dn=106&g=1043&mac=106&pfid=1042&pfuid=106&rn=[S38]伯伦&sid=268415&spfid=104310422020031310540481&ts=1584415860001&v=10020092F6A0432317F4D2D8BAF18B4B375B22F";
		System.err.println(SecureUtil.md5(data).toUpperCase());
	}
}
