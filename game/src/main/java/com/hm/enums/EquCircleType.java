package com.hm.enums;

import com.hm.config.EquipmentConfig;
import com.hm.config.excel.templaextra.PlayerArmStoneTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.Player;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
public enum EquCircleType {
	StrengthenCircle(1,"强化光环") {
		@Override
		public int getMinLv(Player player) {//强化光环的最低等级
			return Stream.of(player.playerEquip().getEqus()).mapToInt(t -> t.getStrengthenLv()).
					min().orElse(0);
		}
	},
//	QualityCircle(3,"品质光环"),
	StoneCircle(4,"宝石光环") {
		@Override
		public int getMinLv(Player player) {//宝石的最低等级
			EquipmentConfig equipmentConfig = SpringUtil.getBean(EquipmentConfig.class);
			return Arrays.stream(player.playerEquip().getEqus()).flatMapToInt(e -> Arrays.stream(e.getStone()).map(id -> {
				PlayerArmStoneTemplate template = equipmentConfig.getStone(id);
				return template==null?0:template.getLevel();
			})).min().orElse(0);
		}
	},
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private EquCircleType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	private String desc;

	public abstract int getMinLv(Player player);

}
