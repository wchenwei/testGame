package com.hm.model.serverpublic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.util.PubFunc;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Map;

@Data
public class ServerKefuData {
	private Map<String,String> infoMap;//连续客服qq群显示
	private Map<String,String> typeConfMap;//游戏类型配置信息
	private int seasonGroupId;//赛事分组
	private transient int kfwwId;//跨服世界大战分组id


	@Transient
	private int qywxMark;//企业微信标识 用于是否显示企业微信的图标
	@Transient
	private transient String qywxVoucher;//企业微信兑换码
	
	public void loadData(ServerData serverData) {
		if(serverData.getGameServerType() != null) {
			this.typeConfMap = GameTypeYYConf.loadGameTypeYYConf(serverData.getGameServerType().getId());
			if (CollUtil.isNotEmpty(this.typeConfMap)) {
				this.seasonGroupId = PubFunc.parseInt(this.typeConfMap.get("seasonGroupId"));
                this.kfwwId = PubFunc.parseInt(this.typeConfMap.get("kfworldwarId"));
            }
		}
		buildQywx();
	}

	public void buildQywx() {
		try {
			String qyinfo = this.infoMap.get("qywx_mark");
			if(StrUtil.isNotEmpty(qyinfo)) {
				this.qywxMark = PubFunc.parseInt(qyinfo.split("#")[0]);
				this.qywxVoucher = qyinfo.split("#")[1];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
