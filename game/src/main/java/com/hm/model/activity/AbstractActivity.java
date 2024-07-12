package com.hm.model.activity;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.util.GameIdUtils;
import com.hm.db.ActivityUtils;
import com.hm.enums.ActivityState;
import com.hm.enums.ActivityType;
import com.hm.enums.RankType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerActivity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Description: 抽象的活动类,所有活动必须实现此类
 * @author siyunlong  
 * @date 2018年5月14日 下午1:14:07 
 * @version V1.0
 */
@NoArgsConstructor
@Data
public class AbstractActivity extends DBEntity<String>{
	//活动类型
	private int type;
	//活动是否手动关闭
	private boolean isClose;
	//当前活动状态
	private int state = ActivityState.Normal.getType();
	//活动预展示时间 0-不预先展示  >0 预先展示
	private long showTime;
	//活动开始时间
	private long startTime;
	//活动结束时间 -1代表永久开放
	private long endTime;
	//清算时间 
	private long calTime;
	// 处理玩家活动数据 需要额外处理的 版本号
	private int resetVersion;
	
	public AbstractActivity(ActivityType type) {
		setId("f_"+GameIdUtils.nextStrId());
		this.type = type.getType();
		if(type.isForeverType()) {
			setActivityTime(0, -1);
		}
	}
	/**
	 * 设置获得的开始结束时间
	 * @param startTime
	 * @param endTime
	 */
	public void setActivityTime(long startTime,long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	//活动是否开启
	public boolean isOpen() {
		return isOpenForTime(System.currentTimeMillis());
	}
	
	public boolean isOpenForTime(long tempTime) {
		if(isClose) {
			return false;
		}
		return tempTime >= startTime && ( endTime < 0 || tempTime < endTime);
	}
	
	//此活动对于此玩家是否是关闭状态
	public boolean isCloseForPlayer(BasePlayer player) {
		return false;
	}	
	
	//活动已经到期
	public boolean isOverTime() {
		return endTime > 0 && System.currentTimeMillis() > endTime;
	}
	//活动已结算
	public boolean isCalTime() {
		return calTime > 0&&System.currentTimeMillis() > calTime;
	}
	
	//手动关闭活动
	public void closeActivity() {
		this.isClose = true;
	}
	
	//活动是否预展示
	public boolean isCanShow() {
		long now = System.currentTimeMillis();
		return this.showTime > 0 &&  now > showTime && now < endTime;
	}
	/**
	 * 处理活动的结算时间
     */
	public void checkDoCalActivity() {
		if(this.state == ActivityState.Normal.getType() && System.currentTimeMillis()>this.calTime) {
			doCalActivity();
			changeState(ActivityState.CalOver);
		}
	}

	/**
	 * 启动服务器时初始化执行此方法
	 */
	public void initServerLoad() {

	}
	/**
	 * 结算处理，需要继承者进行重载
	 */
	public void doCalActivity() {}
	
	/**
	 * 创建活动都处理
	 */
	public void doCreateActivity() {
		
	}
	
	/**
	 * 检查活动是否可添加
	 * @return
	 */
	public boolean checkCanAdd() {
		return true;
	}
	
	/**
	 * 处理重复活动第一次加载，有特殊用法！！
	 */
	public void doCreateRepeatActivityForFirst() {
		
	}
	/**
	 * 每天零点执行
	 *
	 */
	public boolean doByZero(){
		return false;
	}
	
	/**
	 * 每小时检查
	 */
	public void doCheckHourActivity() {
		
	}
	
	/**
	 * 检查玩家每日登陆 每天只有第一次才执行此方法
	 * @param player
	 */
	public void checkPlayerLogin(Player player) {}
	
	/**
	 * 实时检查玩家活动信息，用于处理玩家标示
	 * @param player
	 */
	public void checkPlayerActivityValue(Player player) {}
	
	/**
	 * 加载额外信息
	 * @param extend
	 */
	public void loadExtend(String extend) {}
	
	/**
	 * 检查活动条件是否满足赢取条件
	 * @param id
	 * @return true 满足领取条件  false 不满足
	 */
	public boolean checkCondition(BasePlayer player,int id) {
		return true;
	}
	/**
	 * 获取奖励列表
	 * @param id
	 * @return
	 */
	public List<Items> getRewardItems(BasePlayer player,int id) {
		return null;
	}
	
	/**
	 * 检查并获得当前领取id
	 * @param id
	 * @return
	 */
	public int checkGetId(int id,Player player) {
		return id;
	}
	
	public void setCalTime(Date calDate) {
		if(calDate != null) {
			this.calTime = calDate.getTime();
		}
	}
	
	public void changeState(ActivityState state) {
		this.state = state.getType();
	}

	//现在是活动开始的第几天
	public int getDays() {
		return (int) DateUtil.betweenDay(DateUtil.date(startTime), new Date(), true) + 1;
	}
	
	public void saveDB() {
		ActivityUtils.saveOrUpdate(this);
	}

	public String getActivityRankName(RankType rankType){
		return rankType.getRankName()+"_"+getId();
	}

	/**
	 * 校验是否可以重新开启，可以重新开启则重新开启
	 * @return
	 */
	public boolean checkAndRepen(BasePlayer player) {
		return false;
	}
}
