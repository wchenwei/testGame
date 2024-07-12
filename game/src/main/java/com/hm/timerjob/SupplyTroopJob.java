package com.hm.timerjob;

import com.hm.db.PlayerUtils;
import com.hm.enums.SupplyTroopType;
import com.hm.model.player.Player;
import com.hm.model.player.SupplyItem;
import com.hm.model.supplytroop.SupplyTroop;
import com.hm.servercontainer.supplyTroop.SupplyTroopItemContainer;
import com.hm.servercontainer.supplyTroop.SupplyTroopServerContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @Description: 补给部队定时器
 * @author siyunlong  
 * @date 2019年1月25日 上午10:39:49 
 * @version V1.0
 */
@Slf4j
@Service
public class SupplyTroopJob {


	
	public void doJob(int serverId) {

		for (SupplyTroop troop : SupplyTroopServerContainer.of(serverId).getSupplyTroop().values()) {
			try {
				doJobSupplyTroop(troop);
			} catch (Exception e) {
				log.error(troop.getServerId()+"_"+troop.getId()+"补给部队检查出错", e);
			}
		}
	}
	
	private void doJobSupplyTroop(SupplyTroop troop) {
		if(System.currentTimeMillis() < troop.getEndTime()) {
			return;
		}
		SupplyTroopItemContainer troopContainer = SupplyTroopServerContainer.of(troop.getServerId());
		//到达目的地了
		Player player = PlayerUtils.getPlayer(troop.getPlayerId());
		if(player == null) {
			log.error(troop.getServerId()+"_"+troop.getId()+"补给部队找不到玩家:"+troop.getPlayerId());
			troopContainer.removeSupplyTroop(troop.getId());
			return;
		}
		List<Integer> tankIds = troop.getTankList().stream().map(e -> e.getId()).collect(Collectors.toList());
		//把补给队列改为结束
		SupplyItem supplyItem = player.playerRobSupply().getSupplyItem(troop.getId());
		if(supplyItem == null) {
			troopContainer.removeSupplyTroop(troop.getId());
			return;
		}
		supplyItem.changeSupplyType(SupplyTroopType.Over);
		player.playerRobSupply().removeDispatchTank(tankIds);
		player.playerRobSupply().SetChanged();
		player.sendUserUpdateMsg();
		//删除部队
		troopContainer.removeSupplyTroop(troop.getId());
	}
}
