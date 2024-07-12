package com.hm.model.war;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.util.cos.RecordUtils;
import com.hm.war.sg.WarResult;

/**
 * 
 * @Description: 战斗录像存储
 * @author siyunlong  
 * @date 2018年11月22日 上午10:31:48 
 * @version V1.0
 */
public class BattleRecord extends DBEntity<String>{
	private TempResult result;
	private long atkId;
	private long defId;
	private String date;
	private String param;
	
	public BattleRecord() {
		super();
	}

	public BattleRecord(int serverId,WarResult result) {
		setId(RecordUtils.buildId(serverId));
		setServerId(serverId);
		this.date = DateUtil.now();
		this.atkId = result.getAtk().getPlayerId();
		this.defId = result.getDef().getPlayerId();
		this.result = new TempResult(result);
		
		RecordUtils.saveServerResult(getId(), serverId, result);
	}
	
	public  void loadParam(String param){
		this.param = param;
	}

}
