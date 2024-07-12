package com.hm.model.player;

import com.hm.config.excel.GuildFactoryConfig;
import com.hm.config.excel.templaextra.ItemGuildWeaponExtraTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@RedisMapperType(type = MapperType.STRING_HASH)
public class CentreArms extends BaseEntityMapper<Long> {
	private ArmsPosition[] arms = new ArmsPosition[5];
	private int unlockNum;
	
	public CentreArms(Player player){
		this.setId(player.getId());
		this.setServerId(player.getServerId());
	}

	public int getEmptyPos() {
		for(int i=0;i<1+unlockNum;i++){
			if(arms[i]==null){
				return i;
			}
		}
		return -1;
	}

	public void addPaper(int paperId, int pos) {
		this.arms[pos] = new ArmsPosition(paperId);
	}

	public ArmsPosition getArms(int index) {
		return arms[index];
	}
	
	public ArmsPosition getArmsByType(int type){
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		for(ArmsPosition arms:arms){
			if(arms!=null&&arms.getState()==2){
				ItemGuildWeaponExtraTemplate template = guildFactoryConfig.getWeapon(arms.getArms().getId());
				int thisType = template.getType();
				if(type==thisType){
					return arms;
				}
			}
		}
		return null;
	}
	
	public boolean isHaveThisType(int type){
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		for(ArmsPosition arms:arms){
			if(arms!=null&&arms.getState()==2){
				ItemGuildWeaponExtraTemplate template = guildFactoryConfig.getWeapon(arms.getArms().getId());
				int thisType = template.getType();
				if(type==thisType){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCanUp(int index, int armsId) {
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		ItemGuildWeaponExtraTemplate template = guildFactoryConfig.getWeapon(armsId);
		int type = template.getType();//根据武器id获取武器type
		return !isHaveThisType(type);
	}

	public void up(int index, Arms arms) {
		this.arms[index] = new ArmsPosition(arms);
	}
	//判断除了index位置还有没有该类型的武器
	public boolean isHaveThisTypeWithOutPos(int type, int index) {
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		for(int i=0;i<arms.length;i++){
			if(arms[i]!=null&&arms[i].getState()==2&&i!=index){
				ItemGuildWeaponExtraTemplate template = guildFactoryConfig.getWeapon(arms[i].getArms().getId());
				int thisType = template.getType();
				if(type==thisType){
					return true;
				}
			}
		}
		return false;
	}

	public void down(int index) {
		this.arms[index] = null;
	}

	public void unlockPos() {
		this.unlockNum++;
	}
	
	public void save(){
		saveDB();
	}
}
