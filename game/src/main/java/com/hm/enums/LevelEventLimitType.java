package com.hm.enums;

public enum LevelEventLimitType {
		PlayerLevel(1,"指挥官达到N级",1),
		PlayerCombat(2,"指挥官战力达到N",1),
		TankCamp(3,"上阵坦克必须为X阵营",0),
		TankCampNum(4,"上阵坦克必须有X阵营坦克N量",0),
		TankCampLimit(5,"上阵坦克不能有X阵营",0),
		TankType(6,"上阵坦克必须全部为X类型",0),
		TankTypeNum(7,"上阵坦克必须有X类型坦克N量",0),
		TankTypeLimit(8,"上阵坦克不能有X类型坦克",0),
		TankStar(9,"上阵坦克必须达到X星",0),
		TankStarNum(10,"上阵坦克必须有N辆X星坦克",0),
		TankReLv(11,"上阵坦克必须全部达到X阶",0),
		TankReLvNum(12,"上阵坦克必须有N量X阶坦克",0),
		TankGrade(13,"上阵坦克必须全部为X资质",0),
		TankGradeNum(14,"上阵坦克必须有N量X资质坦克",0),
		TankNum(15,"只能上阵N量坦克",0),
		;
		
		private LevelEventLimitType(int type,String desc,int flag){
			this.type = type;
			this.desc = desc;
			this.flag = flag;
		}
		private int type;
		private String desc;
		private int flag;//1-用于检查路线 0-用于战斗限制
		
		public int getType() {
			return type;
		}

		public String getDesc() {
			return desc;
		}

		public int getFlag() {
			return flag;
		}
		public static LevelEventLimitType getType(int type){
			for(LevelEventLimitType limitType:LevelEventLimitType.values()){
				if(limitType.getType()==type){
					return limitType;
				}
			}
			return null;
		}
		
		
}
