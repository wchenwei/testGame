package com.hm.model.activity.kfactivity;

import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;
import com.hm.enums.KfType;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.activity.kfseason.server.SeasonScoreUtils;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 跨服远征战
 * @author siyunlong  
 * @date 2019年4月16日 下午4:14:31 
 * @version V1.0
 */
@Getter
@Setter
public class KfExpeditionActivity extends AbstractKfActivity{
	private transient String allUrl;
	//输的列表
	private transient List<Integer> failList = Lists.newArrayList();
	private int openType;//0未开启 1-展示  2-开启
	private int serverType;//服务器类型 1-进攻方  2-防守方
	private int targetServerId;//所要进攻或防守的服务器id
	private String declaration;//对战宣言
	private String declareName;//宣战人昵称
	private int titleId;//宣战人的titleId
	private String icon;
	private int winServerId;//胜利服务器id
	
	public KfExpeditionActivity() {
		super(ActivityType.KfExpeditionActivity);
	}


	@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		return openType == 0;//没有开启算关闭
	}
	@Override
	public boolean checkCanAdd() {
		return true;
	}
	
	public void resetData() {
		this.allUrl = null;
		setServerUrl(null);
		this.serverType = 0;
		this.targetServerId = 0;
		this.declaration = null;
		this.declareName = null;
		this.titleId = 0;
		this.icon = "0";
		this.winServerId = 0;
	}
	
	/**
	 * 有人对我宣战了
	 */
	public void declareServerForMe(int declareId,int targetServerId,String declaration,String allUrl) {
		PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(declareId);
		if(playerRedisData != null) {
			this.declareName = playerRedisData.getName();
			this.titleId = playerRedisData.getTitleId();
			this.icon = playerRedisData.getIcon();
		}
		this.serverType = 2;
		this.targetServerId = targetServerId;
		this.declaration = declaration;
		this.allUrl	= allUrl;
		buildServerUrl();
	}
	
	/**
	 * 我对别人宣战
	 * @param targetServerId
	 * @param allUrl
	 */
	public void declareServerForOther(Player player,int targetServerId,String allUrl,String declaration) {
		this.declareName = player.getName();
		this.titleId = player.playerTitle().getUsingTitleId();
		this.icon = player.playerHead().getIcon(); 
		this.serverType = 1;
		this.declaration = declaration;
		this.targetServerId = targetServerId;
		this.allUrl	= allUrl;
		buildServerUrl();
	}
	
	public void buildServerUrl() {
		String serverUrl = this.allUrl.split("#")[0]+":"+this.allUrl.split("#")[1];
		setServerUrl(serverUrl);
	}

	public boolean isOpen() {
		return this.openType == 2;
	}
	
	public boolean isRunning() {
		return serverType > 0;
	}
	

	public boolean addFailServer(Integer serverId) {
		if(this.failList.contains(serverId) || serverId == getServerId()) {
			return false;
		}
		if(this.failList.size() >= 5) {
			this.failList.remove(0);
		}
		this.failList.add(serverId);
		return true;
	}
	public boolean removeFailServer(Integer serverId) {
		return this.failList.remove(serverId);
	}
	public void doFailServer(int failServerId) {
		if(failServerId == getServerId()) {
			//我失败了
			addFailServer(this.targetServerId);
		}else{
			//我胜利了
			removeFailServer(this.targetServerId);
		}
	}
	
	@Override
	public boolean doByZero(){
		if(DateUtil.getCsWeek() == GameConstants.KfExpeditionWeek+2) {
			resetData();//周4凌晨清空数据
			return true;
		}
		if(this.serverType == 0 && DateUtil.getCsWeek() == GameConstants.KfExpeditionWeek+1) {
			//没有参与
			SeasonScoreUtils.addServerScore(getServerId(), KfType.KfExpedetion, SeasonScoreUtils.YzWheelset);
			return true;
		}
		return false;
	}
}
