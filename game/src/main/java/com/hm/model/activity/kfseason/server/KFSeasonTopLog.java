package com.hm.model.activity.kfseason.server;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.type.RedisTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class KFSeasonTopLog {
	private String id;
	private KFSeasonServer winServer;//冠军服务器
	private SesaonGodWarPlayer godWarPlayer;//战神玩家信息
	
	public void save() {
		RedisTypeEnum.KFSeasonTopLog.put(this.id, GSONUtils.ToJSONString(this));
	}
	
	public static KFSeasonTopLog getTopLog(int seasonId,int groupId) {
		String id = seasonId+"_"+groupId;
		String data = RedisTypeEnum.KFSeasonTopLog.get(id);
		if(StrUtil.isEmpty(data)) {
			return null;
		}
		return GSONUtils.FromJSONString(data, KFSeasonTopLog.class);
	}



	public KFSeasonTopLog(int seasonId,int groupId, KFSeasonServer winServer, SesaonGodWarPlayer godWarPlayer) {
		super();
		this.id = seasonId+"_"+groupId;
		this.winServer = winServer;
		this.godWarPlayer = godWarPlayer;
	}
}
