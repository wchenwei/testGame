package com.hm.model.activity.loginWelfare;

import cn.hutool.core.date.DateUtil;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.templaextra.ActivitySevenLoginWelfareTemplate;
import com.hm.enums.ActivityType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.activity.PlayerAcitivtyOpenTime;
import com.hm.model.activity.PlayerUnlockActivity;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;

import java.util.Date;
import java.util.List;
/**
 * 登录福利
 * ClassName: SevenDayActivity. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年7月18日11:35:08 <br/>  
 *  
 * @author xjt  
 * @version
 */
public class LoginWelfareActivity extends PlayerUnlockActivity {
	
	public LoginWelfareActivity() {
		super(ActivityType.LoginWelfare);
	}


	@Override
	public boolean isCloseForPlayer(BasePlayer player) {
		if(player.getPlayerActivity().isCloseActivity(this.getType())){
			return true;
		}
		PlayerLoginWelfareValue value = (PlayerLoginWelfareValue)player.getPlayerActivity().getActValueOrNull(ActivityType.LoginWelfare);
		if (value == null) {
			return true;
		}
		if(value.getOpenTime() == null) {//处理异常
			player.getPlayerActivity().getActivityMap().remove(ActivityType.LoginWelfare.getType());
			return true;
		}
		return !value.getOpenTime().isOpen();
	}

	@Override
	public void checkPlayerLogin(Player player) {
		PlayerLoginWelfareValue value = (PlayerLoginWelfareValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.LoginWelfare);
		value.addLoginDay(value.getCurLoginDay());
		player.getPlayerActivity().SetChanged();
	}


	@Override
	public List<Items> getRewardItems(BasePlayer player,int id) {
		ActivityConfig activityConfig = SpringUtil.getBean(ActivityConfig.class);
		ActivitySevenLoginWelfareTemplate template = activityConfig.getSevenLoginWelfare(id);
		if(template == null) {
            return null;
		}
		return template.getRewards();
	}

	@Override
	public void doUnlockActivity(BasePlayer player) {
		PlayerLoginWelfareValue value = (PlayerLoginWelfareValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.LoginWelfare);
		value.setPlayerAcitivtyOpenTime(new PlayerAcitivtyOpenTime(player,0, DateUtil.beginOfDay(new Date()),7));
		value.addLoginDay(1);
		player.getPlayerActivity().SetChanged();
	}
}
