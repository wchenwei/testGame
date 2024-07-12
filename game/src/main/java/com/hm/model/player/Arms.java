package com.hm.model.player;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.GuildFactoryConfig;
import com.hm.config.excel.templaextra.GuildFactoryWeaponUpgradeExtraTemplate;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Arms {
	private String uid; //唯一id
    private int id;  //武器id
    private int lv=1;
    private int exp;//当前经验
    private int pos;//所处位置
    
    public Arms(int id,int serverId) {
        this.id = id;
        uid = serverId+"_"+PrimaryKeyWeb.getPrimaryKey("Arms",serverId);
        this.pos = -1;
    }

	public boolean addExp(int exp) {
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		boolean flag =false;
		int maxLv = guildFactoryConfig.getArmsMaxLv(this.id);
		if(this.lv>=maxLv){
			return false;
		}
		this.exp += exp;
		for(int i=lv;i<maxLv;i++){
			GuildFactoryWeaponUpgradeExtraTemplate template = guildFactoryConfig.getWeaponUpgradeTemplate(id, lv);
			if(this.exp>=template.getExp()){
				this.exp -= template.getExp();
				this.lv++;
				flag = true;
			}
		}
		return flag;
	}

	public void up(int index) {
		this.pos=index;
	}
}
