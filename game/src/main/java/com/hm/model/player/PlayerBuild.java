package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.BuildConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.temlate.BuildingUnlockTemplate;
import com.hm.enums.CommonValueType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @desc 玩家内城建筑
 * @author xjt
 * @date 2019年12月19日09:09:57
 */
public class PlayerBuild extends PlayerDataContext {
	//key->建筑唯一id  value -> 建筑等级
	private ConcurrentHashMap<Integer, Integer> builds = new ConcurrentHashMap<Integer, Integer>();
	//key->blockId  value -> 建筑唯一id
	private ConcurrentHashMap<Integer, Integer> blocks = new ConcurrentHashMap<Integer, Integer>();
    //区域划分
	private long resCollectTime;//上一次资源收取时间
	
	private long autoEndTime;//自动建造结束时间
	
	public int getBuildLv(int id){
		return this.builds.getOrDefault(id, 0);
	}
	
	public void lvUp(int id){
		this.builds.put(id, this.getBuildLv(id)+1);
		SetChanged();
	}
	//建造
	public void build(int buildId,int lv,int blockId){
		this.builds.put(buildId, lv);
		this.blocks.put(blockId, buildId);
		SetChanged();
	}
	
	public int getBuildId(int blockId){
		return this.blocks.getOrDefault(blockId, 0);
	}
	//判断该建筑是否已经存在（即占了一个位置，即使该建筑正在建造中等级为0）
	public boolean isExitBuildId(int buildId){
		return blocks.values().stream().anyMatch(t ->t==buildId);
	}
	
	public ConcurrentHashMap<Integer, Integer> getBuilds() {
		return builds;
	}
	public ConcurrentHashMap<Integer, Integer> getBlocks() {
		return blocks;
	}

	/**
	 * 创建建筑，有可能是玩家自己手动建造，此时lv=0;也有可能是解锁即存在，lv为默认解锁等级
	 * @param blockId
	 * @param id
	 * @param lv
	 */
	public void createBuild(int blockId,int buildId,int lv){
		this.builds.put(buildId, lv);
		this.blocks.put(blockId, buildId);
		SetChanged();
	}

	public long getResCollectTime() {
		return resCollectTime;
	}
	
	public void setResCollectTime(long time){
		this.resCollectTime = time;
		SetChanged();
	}
	//获取生产时间(分钟)
	public long getProductTime() {
		if(resCollectTime==0){
			return 1;
		}
		return (System.currentTimeMillis() - this.resCollectTime)/GameConstants.MINUTE;
	}
	//获取某种建筑的等级
	public Map<Integer,Integer> getBuildByBuildType(int buildType){
		Map<Integer,Integer> lvs = Maps.newConcurrentMap();
		BuildConfig buildConfig = SpringUtil.getBean(BuildConfig.class);
		builds.forEach((k,v)->{
			BuildingUnlockTemplate template = buildConfig.getBuild(k);
			if(template!=null&&template.getBuild_type()==buildType) {
				lvs.put(k, v);
			}
		});
		return lvs;
	}
	
	public void addBlockId(int blockId,int buildId){
		this.blocks.put(blockId, buildId);
		SetChanged();
	}
	
	public void remBlockId(int blockId){
		this.blocks.remove(blockId);
		SetChanged();
	}
	
	public void changeBlockId(int blockId,int blockId2){
		int buildId = getBuildId(blockId);
		int buildId2 = getBuildId(blockId2);
		remBlockId(blockId);
		remBlockId(blockId2);
		if(buildId2>0){
			addBlockId(blockId, buildId2);
		}
		if(buildId>0){
			addBlockId(blockId2, buildId);
		}
		SetChanged();
	}
	
	//是否解锁建筑征讨
	public boolean isUnlockExtort() {
		return getBuildLv(GameConstants.CommandCentreBuildId) >= GameConstants.BuildExtortMinLv;
	}
	//获取指挥中心登记
	public int getCenterLv() {
		return getBuildLv(GameConstants.CommandCentreBuildId);
	}
	//根据buildId获取位置
	public int getBlockId(int buildId){
		for(Map.Entry<Integer, Integer> entry:blocks.entrySet()){
			if(buildId==entry.getValue()){
				return entry.getKey();
			}
		}
		return 0;
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerBuild", this);
	}

	public void deleteBuild(int buildId) {
		this.builds.remove(buildId);
		int blockId = getBlockId(buildId);
		if(blockId>0){
			this.blocks.remove(blockId);
		}
		SetChanged();
	}

	public void autoBuild(int type) {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int seconds = commValueConfig.getCommValue(CommonValueType.BuildAutoSecond);
		this.autoEndTime =type==0?0:System.currentTimeMillis()+seconds*GameConstants.SECOND;
		SetChanged();
	}

	public boolean inAutoTime() {
		return this.autoEndTime>=System.currentTimeMillis();
	}
	
	public boolean inAutoTime(long time) {
		return this.autoEndTime>=time;
	}

}
