package com.hm.model.guild;

import com.hm.action.guild.biz.GuildBiz;
import com.hm.actor.OnceQueueTaskType;
import com.hm.enums.GuildJob;
import com.hm.libcore.actor.once.IOnceTask;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.message.MessageComm;
import com.hm.model.guild.bean.GuildBaseInfo;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.guild.bean.GuildReqPlayer;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

//部落信息
@NoArgsConstructor
public class Guild extends DBEntity<Integer> implements IOnceTask {
	//自动同意加入部落（0：表示同意，1：表示不同意；谨记）
	private int autoAdd = 1;
	//部落基本信息
	private GuildBaseInfo guildBaseInfo;
	//部落的成员信息
	private GuildMembers guildMembers;
	//部落申请信息
	private GuildReqs guildReqs;
	//部落动态
	private GuildLogs guildLogs;
	//部落邀请加入人员信息
	private GuildInvite guildInvite;
	private GuildFlag guildFlag;
	//部落统计信息
	private GuildStatistics guildStatistics;
	//国战数据
//	private GuildWarData guildWarData;
	//等级信息
	private GuildLevelInfo guildLevelInfo;
	//部落科技
	private GuildTechnology guildTechnology;
	//当前使用的部落战术
	private GuildTactics guildTactics;
	//部落城池产出
	private GuildReward guildReward;
	//部落弹劾
	private GuildImpeach guildImpeach;
	
	private GuildBarrack guildBarrack;
	//部落任务
	private GuildTask guildTask;
	//军工厂
	private GuildFactory guildFactory;
	
	@Transient
	private transient GuildTemp guildTemp;
	//部落指挥
	@Transient
	private transient GuildCommands guildCommands = new GuildCommands();

	
	public Guild(int serverId) {
		this.setServerId(serverId);
		this.setId(PrimaryKeyWeb.getIntPrimaryKey(Guild.class, serverId));
	}
	public GuildMembers getGuildMembers() {
		if (null == this.guildMembers) this.guildMembers = new GuildMembers();
		this.guildMembers.LateInit(this);
		return guildMembers;
	}
	public GuildReqs getGuildReqs() {
		if (null == this.guildReqs) this.guildReqs = new GuildReqs();
		this.guildReqs.LateInit(this);
		return guildReqs;
	}
	public GuildBaseInfo getGuildInfo() {
		if (null == this.guildBaseInfo) this.guildBaseInfo = new GuildBaseInfo();
		this.guildBaseInfo.LateInit(this);
		return guildBaseInfo;
	}
	public GuildLogs getAgLogs() {
		if (null == this.guildLogs) this.guildLogs = new GuildLogs();
		this.guildLogs.LateInit(this);
		return guildLogs;
	}

	public GuildStatistics getGuildStatistics() {
		if (null == this.guildStatistics) this.guildStatistics = new GuildStatistics();
		this.guildStatistics.LateInit(this);
		return guildStatistics;
	}
//	public GuildWarData getGuildWarData() {
//		if (null == this.guildWarData) this.guildWarData = new GuildWarData();
//		this.guildWarData.LateInit(this);
//		return guildWarData;
//	}
	public GuildInvite getGuildInvite() {
		if (null == this.guildInvite) this.guildInvite = new GuildInvite();
		this.guildInvite.LateInit(this);
		return guildInvite;
	}
	public GuildFlag getGuildFlag() {
		if (null == this.guildFlag) this.guildFlag = new GuildFlag();
		this.guildFlag.LateInit(this);
		return guildFlag;
	}
	public GuildTactics getGuildTactics() {
		if (null == this.guildTactics) this.guildTactics = new GuildTactics();
		this.guildTactics.LateInit(this);
		return guildTactics;
	}
	public GuildTemp getGuildTemp() {
		if (null == this.guildTemp) this.guildTemp = new GuildTemp();
		return guildTemp;
	}
	public GuildCommands getGuildCommands() {
		return guildCommands;
	}
	public GuildImpeach getGuildImpeach() {
		if (null == this.guildImpeach) this.guildImpeach = new GuildImpeach();
		this.guildImpeach.LateInit(this);
		return guildImpeach;
	}
	public GuildBarrack getGuildBarrack(){
		if (null == this.guildBarrack) this.guildBarrack = new GuildBarrack();
		this.guildBarrack.LateInit(this);
		return guildBarrack;
	}
	public GuildTask getGuildTask(){
		if (null == this.guildTask) this.guildTask = new GuildTask();
		this.guildTask.LateInit(this);
		return guildTask;
	}
	
	public GuildFactory guildFactory(){
		if (null == this.guildFactory) this.guildFactory = new GuildFactory();
		this.guildFactory.LateInit(this);
		return guildFactory;
	}

	public void init(Player player, String guildName, String flagName) {
		this.getGuildInfo().initBaseInfo(player, guildName);
		this.getGuildFlag().initData(flagName);//军团名字 军团旗帜 军团旗帜的字
		GuildPlayer guildPlayer = new GuildPlayer(player);
		guildPlayer.setGuildJob(GuildJob.PRESIDENT.getType());
		getGuildMembers().addPlayer(guildPlayer);
		this.guildLevelInfo().setLv(1);
		this.guildLevelInfo().addExp(0);
		this.guildBaseInfo.updateLoginTime();
		this.guildBaseInfo.updateLeaderLoginTime();
	}
	
	public GuildLevelInfo guildLevelInfo() {
		if(guildLevelInfo == null){
			guildLevelInfo = new GuildLevelInfo();
		}
		guildLevelInfo.LateInit(this);
		return guildLevelInfo;
	}
	
	public GuildTechnology guildTechnology() {
		if(null == guildTechnology){
			this.guildTechnology = new GuildTechnology();
		}
		this.guildTechnology.LateInit(this);
		return this.guildTechnology;
	}
	public GuildReward getGuildReward() {
		if(null==this.guildReward){
			this.guildReward = new GuildReward();
		}
		this.guildReward.LateInit(this);
		return this.guildReward;
	}
	public void addReqPlayer(Player player, long endTime) {
		this.getGuildReqs().addPlayer(new GuildReqPlayer(player, endTime));
	}
	public void removeReq(Player player) {
		this.getGuildReqs().removePlayer(player.getId());
	}
	public GuildJob getOffic(long playerId) {
		GuildPlayer guildPlayer = getGuildMembers().guildPlayer(playerId);
		return GuildJob.getType(null==guildPlayer?0:guildPlayer.getGuildJob());
	}
	public void removePlayer(long playerId) {
		this.getGuildMembers().removePlayer(playerId);
	}
	public void transfer(Player player, Player transPlayer) {
		this.getGuildMembers().changeJob(player.getId(), GuildJob.NONE);
		this.getGuildMembers().changeJob(transPlayer.getId(), GuildJob.PRESIDENT);
		this.getGuildInfo().setLeader(transPlayer);
	}
	public boolean isLeader(Player player) {
		return player.getId() == this.guildBaseInfo.getLeaderId();
	}
	public boolean isManamger(Player player) {
		return this.isLeader(player) || this.getGuildMembers().getJob(player.getId())==GuildJob.LEGATUS;
	}

	//每日重置
	public void doDayReset() {
		this.getGuildStatistics().doDayReset();
		this.guildTechnology().doDayReset();
		this.getGuildMembers().clearDay();
		this.getGuildTask().resetData();
		saveDB();
	}
	
	public void fillMsg(JsonMsg msg,boolean ignoreChange) {
		msg.addProperty("autoAdd", autoAdd);
		this.getGuildInfo().fillMsg(msg,ignoreChange);
		this.getGuildFlag().fillMsg(msg,ignoreChange);
		this.getGuildInvite().fillMsg(msg,ignoreChange);
		this.getGuildStatistics().fillMsg(msg,ignoreChange);
		this.guildLevelInfo().fillMsg(msg,ignoreChange);
		this.guildTechnology().fillMsg(msg,ignoreChange);
		this.getGuildReward().fillMsg(msg,ignoreChange);
		this.getGuildImpeach().fillMsg(msg,ignoreChange);
		this.getGuildTask().fillMsg(msg,ignoreChange);
		this.guildFactory().fillMsg(msg,ignoreChange);
		this.getGuildMembers().fillMsg(msg, ignoreChange);
	}
	
	public void sendPlayerAllGuild(Player player) {
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Guild_Get);
		fillMsg(serverMsg, true);
		player.sendMsg(serverMsg);
	}
	public void sendPlayerGuild(Player player) {
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Guild_Get);
		fillMsg(serverMsg, false);
		player.sendMsg(serverMsg);
	}
	
	public void broadMemberGuildUpdate() {
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Guild_Get);
		fillMsg(serverMsg, false);
		SpringUtil.getBean(GuildBiz.class).broadGuildMember(this, serverMsg);
	}
	
	public boolean isAutoAdd() {
		return 1==this.autoAdd?false:true;
	}
	
	public void sendManagerGuildUpdate() {
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Guild_Get);
		fillMsg(serverMsg, false);
		SpringUtil.getBean(GuildBiz.class).broadGuildManager(this, serverMsg);
		saveDB();
	}

	public void changeAutoAdd(int type) {
		this.autoAdd=(type==1?1:0);
	}

	public int getAutoAdd() {
		return this.autoAdd;
	}

	public void saveDB() {
		OnceQueueTaskType.GuildDB.putTask(this);
	}

	@Override
	public void doOnceTask() {
		MongoUtils.getMongodDB(getServerId()).update(this);
	}
}










