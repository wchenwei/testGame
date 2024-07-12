package com.hm.model.activity.display;

import cn.hutool.core.date.DateUtil;
import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.player.Player;
import com.hm.redis.type.RedisTypeEnum;

/**
 * @Description: 绑定手机号
 * @date 2019年5月14日09:58:56
 * @version V1.0
 */
public class BindPhoneActivity extends AbstractActivity{
	
	public BindPhoneActivity() {
		super(ActivityType.BindPhone);
	}
	/*@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		return player.playerBaseInfo().isBindRewardFlag()
				&& player.playerBaseInfo().getBindRebendflag().equals(DateUtil.format(new Date(), "yyyyMM"));
	}
*/
	@Override
	public void checkPlayerLogin(Player player) {
		player.playerBaseInfo().checkAndBindPhone();
	}
	//此方法比dozero执行的早。所以用此方法
	@Override
	public void doCheckHourActivity() {
		//每月第一天的0点
		if(DateUtil.thisDayOfMonth()==1 && DateUtil.thisHour(true)==0) {
			RedisTypeEnum.BindPhone.rename();
		}
	}
}
