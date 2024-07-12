package com.hm.redis;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.TankConfig;
import com.hm.enums.TankRareType;
import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleInvitePlayer {
	
	private long playerId;
	private int lv;
	private int seniorTankNum;//高品坦克数量（S以上）
	private String date;//被邀请时间 2023-08-03
	
	public SimpleInvitePlayer(Player player) {
		this.playerId = player.getId();
		this.lv = player.playerLevel().getLv();
		this.date = DateUtil.today();
	}

	public SimpleInvitePlayer(long id,int lv){
		this.playerId = id;
		this.lv = lv;
		this.date = DateUtil.today();
	}

	public void updatePlayerInfo(Player player){
		this.lv = player.playerLevel().getLv();
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		this.seniorTankNum = (int)player.playerTank().getTankList().stream().filter(t->tankConfig.getTankRare(t.getId())>= TankRareType.SR.getType()).count();
	}
}
