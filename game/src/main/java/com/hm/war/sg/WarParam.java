package com.hm.war.sg;

import com.hm.war.sg.handler.IWarHandler;
import com.hm.war.sg.unit.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 战斗所需参数
 * @date 2024/4/26 15:48
 */
@Data
@NoArgsConstructor
@Accessors(chain=true)
public class WarParam {
	private boolean isTest;//是否是测试战报 打印属性-内网策划
	private int frameTime = 100;
	private int maxFrame = -1;
	//战斗处理器
	private IWarHandler warHandler;


	//扣除战斗帧
	public void reduceBattleFrame(int battleFrame) {
		if(this.maxFrame < 0) {
			return;
		}
		this.maxFrame = Math.max(0,this.maxFrame-battleFrame);
	}

	public boolean haveNextBattleFrame() {
		return this.maxFrame != 0;
	}


	public void doUnitDeath(Frame frame, Unit unit) {
		if(this.warHandler != null) {
			this.warHandler.doUnitDeath(frame,unit);
		}
	}
}
