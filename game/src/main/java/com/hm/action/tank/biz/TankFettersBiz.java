package com.hm.action.tank.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.memorialHall.biz.MemorialHallAttrBiz;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankFettersConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.FettersImpl;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.server.GameServerManager;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.troop.TankArmy;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Biz
public class TankFettersBiz {
	@Resource
	private TankFettersConfig tankFettersConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private MemorialHallAttrBiz memorialHallAttrBiz;
	/**
	 * 计算坦克羁绊属性
	 *
	 * @author yanpeng 
	 * @param tank
	 * @return  
	 *
	 */
	public Map<TankAttrType, Double> calTankFriendAttr(Player player,Tank tank){
		Map<TankAttrType, Double> attrMap = Maps.newHashMap();
		TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
		FettersImpl fetters = tankFettersConfig.getFettersById(tankSetting.getFetters());
		if(null==fetters || null==tankSetting || tank.getFetters()==0) {
			return attrMap;
		}
		attrMap = fetters.getAttrByLv(tank.getFetters()).getAttrMap();
		//计算纪念馆加成
//		attrMap = memorialHallAttrBiz.calPlayerFetterAttr(player, attrMap);
		return attrMap;
	}
	/**
	 * getReplaceSkill:(获取替换的技能信息). <br/>  
	 * @author zxj  
	 * @param tankIds
	 * @return  使用说明
	 */
	public List<FettersImpl> getFettersSkill(List<TankArmy> tankList) {
		List<Integer> tankIds = tankList.stream().filter(e -> !e.isDeath())
					.map(e -> e.getId()).collect(Collectors.toList());
		
		return tankFettersConfig.getFetterByIds(tankIds);
	}
	
	/**
	 * resetPlayerFriendStars:(重置坦克羁绊，并且给补偿邮件). <br/>  
	 * @author zxj  
	 * @param serverId  使用说明
	 */
	public void resetPlayerFriendStars() {
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
		MailBiz mailBiz = SpringUtil.getBean(MailBiz.class);
		MailConfig mailConfig = SpringUtil.getBean(MailConfig.class);
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.Fetters);
		TankBiz tankBiz = SpringUtil.getBean(TankBiz.class);
		
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			 MongoTemplate mongoTemplate = MongoUtils.getServerMongoTemplate(serverId);
		        if (mongoTemplate == null) {
		            return;
		        }
		        for (Document document : mongoTemplate.getCollection("player").find()) {
		            Player player = mongoTemplate.getConverter().read(Player.class, document);
		            if (null != player) {
		            	sendMailForFriend(player, tankConfig, itemBiz, mailBiz, mailTemplate);
		            	tankBiz.updateTank(player, player.playerTank().getTankIdList(), true);
		            	player.sendUserUpdateMsg();
		            }
		        }
		});
    }
	private static void sendMailForFriend(Player player, TankConfig tankConfig, 
		ItemBiz itemBiz,MailBiz mailBiz, MailTemplate mailTemplate) {
		List<Items> listItems = Lists.newArrayList();
    	List<Tank> tankList = player.playerTank().getTankList();
    	tankList.forEach(t->{
    		int[] starts = t.getFriendStars();
    		for(int i=0; i<starts.length; i++) {
    			List<Items> costItems = tankConfig.getFriendTemplate(t.getId(), i+1).getAllCostItems(starts[i]);
    			if(CollectionUtil.isNotEmpty(costItems)) {
    				listItems.addAll(costItems);
    			}
    		}
    		//t.resetFriendStars();
    	});
    	if(CollectionUtil.isNotEmpty(listItems)) {
    		String content = String.format("重置坦克羁绊，并给补偿：%s;补偿内容为%s",player.getId(), JSON.toJSONString(listItems));
    		log.info(content);
        	List<Items> tempList = itemBiz.createItemList(listItems);
        	mailBiz.sendSysMail(player, mailTemplate.getMail_id(), tempList);
    	}
    	player.playerTank().SetChanged();
    	player.saveDB();
	}
}







