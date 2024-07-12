package com.hm.action.tank.enums;

import com.google.common.collect.Maps;
import com.hm.action.tank.biz.TankAttrBiz;
import com.hm.action.tank.biz.TankDetailBiz;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.StarLevelTemplateImpl;
import com.hm.config.excel.templaextra.TankMasterTemplateImpl;
import com.hm.enums.TankAttrType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.util.TankAttrUtils;

import java.util.Map;

public enum TankDetailMsg {
	

	Tank(1, "坦克基本信息") {
		@Override
		public Object getData(Player player, int tankId) {
			return player.playerTank().getTank(tankId);
		}
	},
	PlayerCommander(3, "军衔加成、座驾加成") {
		@Override
		public Object getData(Player player, int tankId) {
			return player.playerCommander();
		}
	},
	PlayerEquip(5, "玩家装备") {
		@Override
		public Object getData(Player player, int tankId) {
			return player.playerEquip();
		}
	},
	TankFetters(6, "羁绊是否激活") {
		@Override
		public Object getData(Player player, int tankId) {
			return SpringUtil.getBean(TankDetailBiz.class).isActiveTankFetters(player, tankId);
		}
	},
	Master(7, "图鉴大师") {
		@Override
		public Object getData(Player player, int tankId) {
			return player.playerTankMaster();
		}
	},


	TankStarAttr(101, "坦克星级属性") {
		@Override
		public Object getData(Player player, int tankId) {
			Tank tank = player.playerTank().getTank(tankId);
			StarLevelTemplateImpl levelTemplate = SpringUtil.getBean(TankConfig.class).getTankStarLevelTemplate(tank.getId(), tank.getStar(), tank.getStarNode());
			if (levelTemplate != null){
				return TankAttrUtils.tankAttrToIntMap(levelTemplate.getTotalAttrMap());
			}
			return Maps.newHashMap();
		}
	},
	TankMasterAttr(102, "图鉴大师属性") {
		@Override
		public Object getData(Player player, int tankId) {
			int totalStar = player.playerTankMaster().getTotalStar();
			TankMasterTemplateImpl template = SpringUtil.getBean(TankConfig.class).getTankMasterTemplate(totalStar);
			if (template != null){
				return TankAttrUtils.tankAttrToIntMap(template.getAttAttrMap());
			}
			return Maps.newHashMap();
		}
	},
	MilitaryAttr(103, "军衔属性") {
		@Override
		public Object getData(Player player, int tankId) {
			Map<TankAttrType, Double> militaryAttr = SpringUtil.getBean(TankAttrBiz.class).calMilitaryAttr(player);
			return TankAttrUtils.tankAttrToIntMap(militaryAttr);
		}
	},
	CarAttr(104, "坐骑属性") {
		@Override
		public Object getData(Player player, int tankId) {
			Map<TankAttrType, Double> militaryAttr = SpringUtil.getBean(TankAttrBiz.class).calCarAttr(player);
			return TankAttrUtils.tankAttrToIntMap(militaryAttr);
		}
	},
	EquipStrengthAttr(105, "强化属性") {
		@Override
		public Object getData(Player player, int tankId) {
			return SpringUtil.getBean(TankDetailBiz.class).calEquStrengthAttr(player);
		}
	},
	EquipStoneAttr(106, "宝石属性") {
		@Override
		public Object getData(Player player, int tankId) {
			return SpringUtil.getBean(TankDetailBiz.class).calStoneAttr(player);
		}
	},
	;
	
	private TankDetailMsg(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public abstract Object getData(Player player, int tankId);


	private int type;
	private String desc;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
