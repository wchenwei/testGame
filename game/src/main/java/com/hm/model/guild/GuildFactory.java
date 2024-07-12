package com.hm.model.guild;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.guild.vo.ArmsVo;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildFactoryConfig;
import com.hm.config.excel.templaextra.GuildFactoryLevelExtraTemplate;
import com.hm.enums.CommonValueType;
import com.hm.model.guild.bean.*;
import com.hm.model.player.Arms;
import com.hm.model.player.Player;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
/**
 * @DESC 军工厂
 * @author xjt
 * @Date 2020年4月2日10:48:14
 *
 */
public class GuildFactory extends GuildComponent {
	private int lv=1;//等级
	private long exp;//当前经验
	private long prosperity;//繁荣度
	private long parts;//配件
	private transient ArrayList<BuildRecord> buildRecord = Lists.newArrayList();//建设日志
	private transient ArrayList<ProduceRecord> produceRecord = Lists.newArrayList();//生产日志
	private transient ArrayList<AllotRecord> allotRecord = Lists.newArrayList();//分配日志
	private int[] draw  = new int[]{0,0,0};//图纸槽位
	private ArmsVo[] arms = new ArmsVo[3];
	@Transient
	private transient ReentrantLock lock = new ReentrantLock(true);
	
	public void addExp(int exp) {
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		int maxLv = guildFactoryConfig.getMaxLv();
		if(lv>=maxLv){
			return;
		}
		this.exp+=exp;
		long expTemp = this.exp;
		int lvTemp = this.lv;
		for(int i = this.lv;i<maxLv;i++){
			GuildFactoryLevelExtraTemplate template = guildFactoryConfig.getLevel(i);
			long nowNeedExp = template.getExp();//当前等级i升级所需经验
			if(expTemp<nowNeedExp){
				break;
			}
			expTemp = this.exp - nowNeedExp;
			lvTemp = i+1;
		}
		this.exp = expTemp;
		this.lv = lvTemp;
		SetChanged();
	}
	
	public int[] getDraw(){
		return draw;
	}
	
	public int getDrawByIndex(int index){
		return this.draw[index];
	}

	public ArmsVo[] getArms() {
		return arms;
	}
	
	public List<Integer> getPlayerArmsIndexs(long playerId){
		List<Integer> indexs = Lists.newArrayList();
		for(int i=0;i<arms.length;i++){
			ArmsVo vo = arms[i];
			if(vo!=null&&vo.getPlayerId()==playerId){
				
			}
		}
		return indexs;
	}

	public void addParts(int parts) {
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		GuildFactoryLevelExtraTemplate template = guildFactoryConfig.getLevel(this.lv);
		if(template==null){
			return;
		}
		//当前等级的配件上限
		long limit = template.getPart();
		this.parts = Math.min(limit, this.parts+parts);
		SetChanged();
	}

	public long getParts() {
		return parts;
	}

	public void addBuildRecord(Player player, int type, int count){
		synchronized (buildRecord) {
			if(buildRecord.size() >=20){
				buildRecord.remove(buildRecord.size()-1);
			}
			buildRecord.add(0,new BuildRecord(player, type, count));
			SetChanged();
		}
	}
	
	public void addProduceRecord(Player player){
		synchronized (produceRecord) {
			if(produceRecord.size() >=30){
				produceRecord.remove(produceRecord.size()-1);
			}
			produceRecord.add(0,new ProduceRecord(player));
			SetChanged();
		}
	}
	
	public void addAllotRecord(Player player,Player targetPlayer,int id){
		synchronized (allotRecord) {
			if(allotRecord.size() >=20){
				allotRecord.remove(allotRecord.size()-1);
			}
			allotRecord.add(0,new AllotRecord(player,targetPlayer, id));
			SetChanged();
		}
	}
	
	public void addProsperity(int prosperity){
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		GuildFactoryLevelExtraTemplate template = guildFactoryConfig.getLevel(this.lv);
		if(template==null){
			return;
		}
		this.prosperity = Math.min(this.prosperity+prosperity, template.getActive_point());
		SetChanged();
	}
	//图纸槽位有空缺的槽位
	public int haveEmpty(){
		for(int i=0;i<draw.length;i++){
			if(draw[i]<=0){
				return i;
			}
		}
		//-1代表没有空的槽位
		return -1;
	}

    //如果产生图纸则返回生成图纸的位置，-1代表没有生成图纸
    public int checkProsperity() {
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		GuildFactoryLevelExtraTemplate template = guildFactoryConfig.getLevel(this.lv);
		if(template==null){
            return -1;
		}
		int prosperityLimit = template.getActive_point();
		if(this.prosperity>=prosperityLimit){
			int point = haveEmpty();
			if(point>=0){
				//有空槽位则生成一张图纸在该槽位上
				draw[point]=template.ranomPaper();
				this.prosperity=this.prosperity-prosperityLimit;
				SetChanged();
                return point;
			}
		}
        return -1;
	}
	//获取强化空闲位置
	public int getStrengthPos() {
		for(int i=0;i<arms.length;i++){
			if(arms[i]==null){
				return i;
			}
		}
		return -1;
	}

	public void strength(Player player,int pos, Arms arms) {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		this.parts = this.parts - commValueConfig.getCommValue(CommonValueType.GuildFactoryStrengthCost);
		this.arms[pos]=new ArmsVo(player,arms);
		SetChanged();
	}
	
	public ArmsVo getArmsByIndex(int index){
		return this.arms[index];
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("guildFactory", this);
	}

	public void allot(int index) {
		this.draw[index]=0;
		SetChanged();
	}
	//该武器是否在强化位上
	public boolean isInStrengthPos(String id) {
		for(ArmsVo vo:arms){
			if(vo!=null&&StrUtil.equals(vo.getUid(), id)){
				return true;
			}
		}
		return false;
	}

	public void strengthDown(int index) {
		this.arms[index] = null;
		SetChanged();
	}
	//该武器是否在强化中
	public boolean isInStength(int id, String uid) {
		for(ArmsVo vo:arms){
			if(vo!=null&&vo.getPlayerId()==id&&StrUtil.equals(vo.getUid(), uid)){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<BuildRecord> getBuildRecord() {
		return buildRecord;
	}

	public ArrayList<ProduceRecord> getProduceRecord() {
		return produceRecord;
	}

	public ArrayList<AllotRecord> getAllotRecord() {
		return allotRecord;
	}
	
	public List<AllotRecordVo> createAllotRecordVos(){
		return allotRecord.stream().map(t ->t.createVo()).collect(Collectors.toList());
	}
	
	public List<ProduceRecordVo> createProduceRecordVos(){
		return produceRecord.stream().map(t ->t.createVo()).collect(Collectors.toList());
	}
	
	public List<BuildRecordVo> createBuildRecordVo(){
		return buildRecord.stream().map(t ->t.createVo()).collect(Collectors.toList());
	}
	public void resetDay(){
		this.prosperity = 0;
		SetChanged();
	}
	
}
