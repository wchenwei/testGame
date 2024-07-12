package com.hm.servercontainer.idcode;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.Player;
import com.hm.servercontainer.IdCodeFilterIpWhiteContainer;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class IdCodeItemContainer extends ItemContainer{

	private Map<String, IdCodeInfo> dataMap = Maps.newConcurrentMap();
	private  ArrayListMultimap<String, Long> ipPlayers = ArrayListMultimap.create();
	public IdCodeItemContainer(int serverId) {
		super(serverId);
	}
	@Override
	public void initContainer() {
		try {
			log.info( "加载唯一码信息开始:" +this.getServerId());
			getIdCodeInfos().forEach(idCodeInfo -> {
				this.addIdCodeInfo(idCodeInfo);
			});
			log.info( "加载唯一码信息开始:" +this.getServerId() );
		} catch (Exception e) {
			log.error("加载唯一码信息出错:"+this.getServerId(), e);
		}
	}
	private List<IdCodeInfo> getIdCodeInfos() {
		return RedisMapperUtil.queryListAll(getServerId(), IdCodeInfo.class);
	}
	
	public IdCodeInfo getIdCodeInfo(String idCode,int createServerId) {
		return this.dataMap.get(idCode+"_"+createServerId);
	}
	
	public int getIdCodeInfoState(String idCode,int createServerId,long playerId) {
		IdCodeInfo idCodeInfo = getIdCodeInfo(idCode, createServerId);
		if(idCodeInfo==null){
			return 0;
		}
		return idCodeInfo.getState(playerId);
	}
	
	//获取该唯一码和createServerId下绑定了几个角色
	public int getIdCodeInfoNum(String bindCode,int createServerId){
		try {
			IdCodeInfo idCodeInfo = getIdCodeInfo(bindCode,createServerId);
			if(idCodeInfo==null){
				return 0;
			}
			return idCodeInfo.getBindNum();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public void delIdCodeInfo(IdCodeInfo idCodeInfo){
		this.dataMap.remove(idCodeInfo.getId());
		idCodeInfo.delete();
	}
	
	public void addIdCodeInfo(IdCodeInfo idCodeInfo) {
		this.dataMap.put(idCodeInfo.getId(), idCodeInfo);
		
	}
	public Map<String, IdCodeInfo> getDataMap() {
		return dataMap;
	}
	
	public List<IdCodeInfo> getAllIdCodeInfos() {
		return Lists.newArrayList(dataMap.values());
	}
	
	public void addPlayerByIp(String ip,long id){
		this.ipPlayers.put(ip, id);
	}
	//获取该ip下已经登录的人数
	public int getIpNum(String ip) {
		IdCodeFilterIpWhiteContainer idCodeFilterIpWhiteContainer = SpringUtil.getBean(IdCodeFilterIpWhiteContainer.class);
		List<String> ips = idCodeFilterIpWhiteContainer.getWhiteIps();
		if(ips.contains(ip)){
			return 0;
		}
		return this.ipPlayers.get(ip).size();
	}
	//绑定唯一码
	public void bindIdCode(String bindIdCode, int createServerId,long playerId) {
		IdCodeInfo idCodeInfo = getIdCodeInfo(bindIdCode, createServerId);
		if(idCodeInfo==null){
			idCodeInfo = new IdCodeInfo(this.getServerId(),createServerId,bindIdCode);
		}
		idCodeInfo.addPlayerId(playerId);
		addIdCodeInfo(idCodeInfo);
		idCodeInfo.saveDB();
	}
	public void removeIp(String ip,long playerId) {
		ipPlayers.remove(ip, playerId);
	}
	//解绑唯一码
	public void unBindIdCode(Player player) {
		IdCodeInfo idCodeInfo = getIdCodeInfo(player.getIdCode(), player.getCreateServerId());
		if(idCodeInfo==null){
			return;
		}
		idCodeInfo.delId(player.getId());
		idCodeInfo.saveDB();
	}
	public ArrayListMultimap<String, Long> getIpPlayers() {
		return ipPlayers;
	}
	
}












