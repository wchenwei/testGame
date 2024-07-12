
package com.hm.sysConstant;

/**
 * Title: SysConstant.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author 李飞
 * @version 1.0
 * @date 2015年4月23日 上午11:04:35
 */
public final class SysConstant {


    public final static int NO = 0;

    public final static int YES = 1;
    public final static int Role_Limit = 9996;//账号超出可以创建的角色个数
    public final static int Server_Is_Full = 9997;//本服人数已满,请选择其他服务器
    public final static int PARAM_ERROR = 9998;//参数错误
    public final static int SYS_ERROR = 9999;//系统错误

    public final static int SESSIONKEY_ERROR = 1000001;//sessionKey非法

    public final static int SESSION_TIMEOUT = 1000002;//无效SESSION 需要重连

    public static final int SERVER_CLOSE = 1000003;//服务器维护中
    
    public static final int SERVER_CHOISE = 1000004;//请选择服务器
    

    //玩家相关错误提示
    public final static int PLAYER_RE_NOT = 10001;//玩家资源不足
    public final static int PLAYER_DIAMOND_NOT = 10002;//玩家钻石不足
    public final static int ITEMS_NOT_ENOUGH = 10003;//道具数量不足
    public final static int PLAYER_NAME_RE = 10004;//玩家名称重复
    public static final int LV_MAX = 10005;//等级已经达到当前上限
    public static final int PLAYER_NOT_EXIST = 10006;//玩家不存在
    public static final int BANNEDPLAYER = 10007;//封号
    public static final int NAME_ILLEGAL = 10008;//名称非法
    public static final int FORBIDDEN_CHARACTER = 10009;//含有非法字符
    public final static int ITEM_HAVENOT = 10010;//道具不足
    public final static int ITEM_NOT_USE = 10011;//道具不能直接使用
    public final static int OPEAR_FAIL = 10012;//操作失败
    public final static int Ala_Buy_VipGift = 10013;//已经购买过vip礼包
    public final static int Player_Task_Complete_Already = 10014;//该任务已领取
    public final static int Player_Level_Not_Enough = 10015;//玩家等级不足
    public final static int Str_OverLength = 10016;//字符长度超限
    public static final int VIP_LV_NOT_ENOUGH = 10017;//VIP等级不足
    public static final int PLAYER_Oil_NOT = 10018;//石油不足
    public static final int Lv_Reward_Received = 10019;//等级奖励已经领取过（没有可领取的等级奖励）
    public static final int Function_Lock = 10020;//功能未解锁
    public static final int PLAYER_Cash_NOT = 10021;//钞票不足
    public final static int ITEM_NOT_USE_BY_TIME = 10022;//道具不在时间使用范围
    //关卡相关
    public final static int Mission_Fight_Not = 20001;//不能攻打该关卡
    public final static int Mission_Sweep_Not = 20002;//军械库不能扫荡(不能运输)
    public final static int City_TankResearch_Not = 20003;//该城市不能研发坦克
    public final static int TankResearch_Not_Enough = 20004;//tank研发次数不足
    public final static int TankTest_Not_Enough = 20005;//tank测试次数不足
    public final static int TankType_Not = 20006;//上阵tank非法
    public final static int TankResult_Not = 20007;//上传战斗结果非法
    public final static int Mission_Fight_Not_Open = 20008;//关卡未开放
    public final static int Expedition_CannotReset_FightNot = 20009;//远征还未挑战，不能重置
    public final static int Day_Reward_Not = 20010;// 领取每日奖励条件不足
    public final static int Battle_Sweep_Not = 20011;// 扫荡条件不足
    public final static int Battle_Tower_Random_Att_Not = 20012;// 随机buff条件不足
    public final static int Battle_Tower_Random_AddAtt_Not = 20013;// 不能添加此buff

    //翻牌
    public final static int Flop_Repeat = 21001;//重复翻牌
    
    //世界部队
    public final static int WorldTroop_MaxNum = 22001;//世界部队操作玩家最大数量(超过5)
    public final static int WorldTroop_TankNumError = 22002;//世界部队坦克数量是5
    public final static int WorldTroop_TankStateError = 22003;//世界部队坦克状态错误
    public final static int WorldTroop_EditorNot = 22004;//编辑世界部队不存在
    public final static int WorldTroop_EditorTankStateNot = 22005;//此部队状态不能编辑
    public final static int WorldTroop_Not_Advance_TroopState = 22006;//只有等待战斗状态才能突进
    public final static int WorldTroop_Not_Advance_TroopNum = 22007;//我方部队比对方多才能突进
    public final static int WorldTroop_Not_Advance_City = 22009;//目的地为我方的和平城市不能突进
    public final static int WorldTroop_Not_Retreat_TroopState = 22010;//只有等待战斗状态才能撤退
    public final static int WorldTroop_Not_Retreat_City = 22011;//只有目的地是我方的和平城市才能撤退
    public final static int WorldTroop_HallUping = 22012;//部队挂机中，不能操作
    public final static int WorldTroop_HallUping_TimeOut = 22013;//该时间段不能挂机
    public final static int WorldTroop_HallUping_Not_Waring = 22014;//国战期间不能挂机
    public final static int WorldTroop_Roadblock = 22015;//有路障
    public final static int WorldTroop_TroopDeath_Clone = 22016;//克隆部队已经死亡
    public final static int WorldTroop_CityNot_Clone = 22017;//克隆部队不在此城市
    public final static int WorldTroop_Operation_Not = 22018;//部队不能操作
    public final static int WorldTroop_HallUping_Not_BerlinWaring = 22019;//首都争夺战期间不能挂机
    public final static int WorldTroop_Airborne_NotSameWorld = 22020;//不在同一世界无法空投
    public final static int WorldTroop_HaveCaptiveTank = 22021;//有俘虏tank
    public final static int WarTimeNot = 22022;
    public final static int WarNot_NoGuild = 22023;
    public final static int Move_NotArrive = 22024;
    public final static int Move_Ways_LockCity = 22025;
    public final static int LockCity = 22026;

    //世界城池
    public final static int WorldCity_NotFight = 23001;//此城没有战斗，无法观战
    public final static int WorldCity_Lock = 23002;//此城Pve为解锁
    public final static int GuildMainCity_NotMove = 23003;//部落主城不能迁移
    public final static int Troop_Pvp1v1_NotSelfTroop = 23004;//我方没有可单挑部队
    public final static int Troop_Pvp1v1_NotTargeTroop = 23005;//单挑没有找不到对手
    public final static int WarResult_Record_NotHave = 23006;//没有找到战斗录像
    public final static int GuildMainCity_NotMove_Waring = 23007;//国战期间不允许迁都
    public final static int Supply_RobTroopOver = 23008;//抢劫部队已经结束
    public final static int Supply_TroopHaveRob = 23009;//此部队已经被打劫过,无法再被打劫
    public final static int Supply_TankNumError = 23010;//补给掠夺坦克数量是5

    
    //游戏服跨服战
    public final static int KfExpeditionDeclare_ConditionsNot = 24001;//宣战条件不足
    public final static int KfExpeditionDeclare_SystemBusy = 24002;//宣战服务器忙,请稍后再试
    public final static int KfExpeditionDeclare_HaveFight = 24003;//本服务器或目标服务器已经宣战
    public final static int KfExpeditionDeclare_NotFind = 24004;//没有找到可宣战服务器
    public final static int KfExpeditionDeclare_Fail = 24005;//宣战失败
    public final static int KfWorldWar_Cal = 24006;//跨服世界大战结算中


    //世界建筑
    public final static int Camp_NotMainCity = 25001;//阵营没有主城
    
    //兑换码
    public static final int Voucher_Expired = 30001;//兑换码过期
    public static final int Voucher_AlreadyUsed = 30002;//兑换码已使用过
    public static final int Voucher_Invalid = 30003;//兑换码异常
    public static final int Network_Anomaly = 30004;//网络异常，请稍后重试
    public static final int Voucher_VipLimit = 30005;//vip等级限制
    public static final int Voucher_ChannelLimit = 30006;//渠道错误

    //坦克
    public final static int TANK_STAR_MAX = 51001;//坦克星级已达上限
    public final static int TANK_NOT_EXIST = 51002; // 坦克不存在
    public final static int TANK_LV_LIMIT = 51003; //坦克等级限制
    public final static int TANK_LV_MAX = 51004;//坦克等级已满
    public final static int TANK_EXIST = 51005; //坦克已存在
    public final static int TANK_PART_COMPOSE = 51006; //配件已合成
    public final static int TANK_PART_UNLOCK = 51007; //配件未解锁
    public final static int TANK_REFORM_LIMIT = 51008; //坦克不能改造
    public final static int TANK_RELV_MAX = 51009; //坦克改造等级已满
    public final static int TANK_SKILL_LOCK = 51010; //坦克技能未解锁
    public final static int TANK_DRAW_LOCKERROR = 51011; //坦克工厂，锁定解锁功能错误
    public final static int WARGOD_LV_MAX = 51012; //战神等级已满
    public final static int DRIVER_LV_MAX = 51013; //车长等级已满
    public final static int DRIVER_SKILL_LV_MAX = 51014; //车长技能等级已满
    public final static int DRIVER_SKILL_LV_LIMIT = 51015; //车长技能等级限制
    public final static int DRIVER_SKILL_LOCK = 51016; //车长技能未解锁
    public final static int FRIEND_STAR_Limit = 51017; //坦克羁绊等级不满足\
    public final static int FRIEND_STAR_MAX = 51018; //羁绊等级已满
    public final static int DRIVER_LOCK= 51019; //车长未解锁
    public final static int FRIEND_LOCK = 51020; //羁绊未解锁


    //邮件
    public final static int MAIL_NOT_EXIST = 52001; //邮件不存在
    public final static int Mail_Reward_NOT_EXIST = 52002; //邮件附件不存在
    public final static int Mail_Reward_GET = 52003; //邮件附件已领

    //聊天
    public static final int BLACK_ADD_FAIL = 54000; //添加黑名单失败
    public static final int CANNOT_SEND_HELP = 54001; //不能请求支援

    //指挥官
    public static final int Car_Lv_Max = 55001; //座驾满级
    public static final int Military_Lv_Max = 55002; //军衔满级 
    public static final int Car_Skill_lock = 55003; //座驾技能未解锁
    public static final int Car_Lock = 55004; //座驾未解锁
    public static final int Military_Lock = 55005; //军衔未解锁
    public static final int CarModel_UnLock= 55006; //幻化已解锁 
    public static final int CarModel_Lock = 55007; //幻化未解锁
    public static final int SuperWeapon_Lock = 55008; //超武未解锁
    public static final int Superweapon_Lv_Max= 55009; //超武满级

    public static final int OneKeyEqStrenNoAny= 55010; //一键强化:没有可以强化的
    public static final int OneKeyEqStoneAllMaxLv= 55011; //一键升级宝石:宝石都到最大等级了
    public static final int OneKeyEqStoneNotFind= 55012; //一键升级宝石:没有找到可升级的
    public static final int OneKeyEqStoneNotUp= 55013; //一键镶嵌:没有可以镶嵌的宝石

    //活动
    public static final int Activity_HaveReward = 56001;//活动已经领取奖励
    public static final int Activity_Close = 56002;//活动已经关闭
    public static final int Activity_ConditionsNot = 56003;//活动领取条件不足
    public static final int Activity_RepeatBuy = 56004; //重复购买
    public static final int Activity_Shooting_Over = 56005; //不能打靶
    public static final int ArmyDayBossCd = 56006; //八一活动，boss冷去时间为到
    public static final int AddrError = 56007; //地址填写错误
    public static final int Activity_HaveOrder =56008;//有未签订订单
    
    public final static int Guild_Name_RE = 57001;//部落名字重复
    public final static int Guild_FlagName_RE = 57002;//部落旗帜重复
    public final static int Guild_VipLv = 57003;//创建部落的时候，vip等级不足
    public final static int Guild_Close = 57004;//部落功能暂未开启
    public final static int Guild_CheckTime = 57005;//离开部落时间需超过24小时
    public final static int Guild_Repeat = 57006;//不能重复加入部落
    public final static int Guild_NoExist = 57007;//部落不存在
    public final static int Guild_ReqSize = 57008;//超过同时申请的部落数量
    public final static int Guild_NoPower = 57009;//部落没有操作权限（一键拒绝，转让部落）
    public final static int Guild_JoinErr = 57010;//部落审核加入失败
    public final static int Guild_QuiltErr = 57011;//部落长退出是，必须部落里面没人
    public final static int Guild_NumErr = 57012;//部落职位人数限制
    public final static int Guild_PlayerNumErr = 57013;//部落人数已经到达上限
    public final static int Guild_reReq = 57014;//重复申请部落
    public final static int Guild_NoneReq = 57015;//玩家申请信息过期
    public final static int Guild_MemberQuit = 57016;//玩家已经退出部落
    public final static int Guild_QuitWar = 57017;//国战期间，不能解散部落
    public final static int Guild_NoneInvite = 57018;//没有接受到部落加入邀请
    public final static int Guild_InviteRepeat = 57019;//不能重复邀请用户
    public final static int Guild_OffLine = 57020;//玩家不在线
    public final static int Guild_Refuse = 57021;//玩家拒绝部落邀请
    public final static int Guild_Agree = 57022;//玩家同意部落邀请
    public final static int Guild_InGuild = 57023;//玩家已经在部落了
    public final static int Guild_TarLeaveTime = 57024;//被邀请玩家离开部落未超过24小时
    public final static int Guild_Noin = 57025;//玩家不在部落
    public final static int Guild_Overload = 57026;//超出捐献次数
    public final static int Guild_Tec_LvMax = 57027;//部落科技等级已经满了
    public final static int Guild_Point_Not_Enough = 57028;//部落科技点数不足
    public final static int Guild_Tec_ResetTime = 57029;//部落科技重置冷却时间不足
    public final static int Guild_Update_Error = 57030;//部落科技升级条件不足
    public final static int Guild_Tec_TimesOver = 57031;//部落科技使用次数不足
    public final static int Guild_Tec_AirborneNoTimes = 57032;//部落科技空头次数不足
    public final static int Guild_Tec_CityNot = 57033;//对城市使用战术出错
    public final static int Guild_SameTecForCity = 57034;//同一战术不能对相同城池使用
    public static final int Guild_FirstAddReward = 57035; //首次加入部落奖励不符合领取条件
    public static final int Guild_CommandExit = 57036; //部落指挥已经存在
    public static final int Guild_CommandAtk_SameGuild = 57037; //指挥进攻是一个部落
    public static final int Guild_CommandDef_NotSameGuild = 57038; //指挥防守不是一个部落
	public static final int Guild_ImpeachMyself = 57039; //部落长不能弹劾自己
	public static final int Guild_ImpeachError = 57040; //举报用户不存在，或者不是部落长
	public static final int Guild_ImpeachCondition = 57041; //被弹劾条件不足
	public static final int Guild_ImpeachRep = 57042; //不能重复参与弹劾
	public static final int Guild_NotSameCamp = 57043; //不是同一阵营
	public static final int Guild_Lv_Not_Enough = 57044; //部落等级不足
	public static final int Guild_Score_Not_Enough = 57045; //部落积分不足
    // 一元夺宝error
    public static final int OneDollarError = 57111;
    //竞技场
    public final static int Arena_ArmyChange = 58001; //对手发生变化，请刷新对手
    public final static int Record_Not_Exist = 58002; //战报不存在
    public final static int Arena_Cannot_Fight = 58003; //不能挑战
    public final static int Arena_Unlock = 58004; //竞技场未解锁
    public final static int Arena_BuyTimes_Limit = 58005; //购买次数限制
    public final static int Arena_Prohibit_Tank = 58006; //坦克禁止出站
    public final static int Arena_Cannot_FightTank = 58007; //不能挑战

    //
    public final static int Day_Card_Received = 59001;//周卡/月卡重复领取
    public final static int Day_Card_Have_Not = 59002;//周卡/月卡未购买

    //贸易相关
    public static final int Trade_Boat_Error = 59100; //船只状态有误

    //
    public static final int OverallWar_FightNum_Not = 59102; //全面战争次数不足
    public static final int OverallWar_Troop_Error = 59103; //全面战争阵容错误
    
    public static final int Player_Title_Null = 59104; //玩家没有此称号

    public static final int Trade_Max_Stock = 59105; //子股东超过最大数量


    //阵营
    public static final int Camp_NoPower = 59201; //没有权限
    
    public static final int Camp_TranSlNotOnline = 59202; //
    //不是阵营前十无法弹劾
    public static final int Camp_NotCombat10 = 59203; //
    
    //邀请
    public static final int InviteCode_Error = 59301; //邀请码错误
    public static final int InviteCode_Received = 59302; //邀请奖励已领取
    
    public static final int Phone_Error = 59303; //手机号码格式不正确
    public static final int PhoneReward_Received = 59304; //绑定手机奖励已领取
    
    //部落派兵
    public static final int Guild_Barrack_Troop_Limit = 59401;//部落内驻兵已达到上限
    public static final int Guild_Barrack_Troop_Dispatch = 59402;//有部队不能派遣
    public static final int Guild_Barrack_Expedition_Not_War = 59403;//必须国战期间才能调兵
    
    //物资突袭次数已达上限
    public static final int ResSuddenStrike_Buy_Limit = 59404;//物资突袭购买次数已达上限
    public static final int ResSuddenStrike_Count_Not = 59405;//物资突袭挑战次数不足
    public static final int RedMsg_Not_Exist = 59406; //红包不存在
    
    public static final int Mastery_Lv_Up_Not = 59407; //专精不满足升级条件
    
    //物资储备
    public static final int MaterialReserve_BuyLimit = 59501;//物资储备购买钥匙数超出上限
    
    
    public final static int QUEUE_PRODUCE_NOT_COMPLETE = 60001;//生产未完成
    public static final int QUEUE_PRODUCE_SPEED_NOT = 60002; //队列不能加速
    public static final int QUEUE_CANCEL_NOT = 60003;//不能取消
    public static final int BUILD_TIME = 60004;//建筑物正在升级中
    public final static int BUILD_QUEUE_FULL = 60004;//建筑队列已满
    public final static int PRODUCT_QUEUE_FULL = 60005;//生产队列已满
    public static final int Res_Limit = 60006;//资源到达上限
    
    
    //前线战争
    public final static int FrontBattle_Troop_Not = 60100;//队伍不存在
    public final static int FrontBattle_Troop_Full = 60101;//队伍已满
    public final static int FrontBattle_Troop_Power_Limit = 60102;//权限不足
    public final static int FrontBattle_Troop_Finish = 60103;//该玩家已完成该战役，不能踢出
    public final static int FrontBattle_Finish = 60104;//战斗已经打过
    public static final int DreamlandBattle_Box_Received = 60105; //已经领取过箱子
    
    //战役次数不足
    public static final int LastCount_Not = 60106;//战役次数不足
    
    //周年庆
    public static final int Fire_Not = 60107;//没有烟花可以燃放
    
    //军工厂
    public static final int GuildArms_Draw_Not = 60108;//图纸已不存在
    public static final int GuildArms_Player_Full = 60109;//玩家槽位已满，不能分配
    
    public static final int RepairTrain_Score_Invalid = 60110;//维修训练积分超上限，积分无效

    public static final int ShareReward_Not = 60111;//领取次数
    public static final int Guild_Del_Error = 60112;//部落解散校验信息失败
    public static final int GrowFundNotItem = 60113;




    //=====================跨服战60001-70001=======================
    //占位!!!
    
    
    public static final int WarCraft_Lv_Max= 70100; //兵法满级

    // 战俘
    public static final int Captive_Researcher_Have = 70120;//研究员已拥有
    public static final int Captive_Researcher_Not_Have = 70121;//研究员未拥有
    public static final int Captive_Lv_Max = 70122;//等级已满
    public static final int Captive_Lv_Not_Enough = 70123;//等级不足

    // 双十二抢购
    public static final int SECOND_KILL_BUY_GOODS = 70124;// 抢购商品已卖完
    public static final int WarMakes_Lv_Not_Enough = 70125;// 战令等级不足

    // 航母
    public static final int Aircraft_Carrier_Lv_Not_Enough = 70131; // 航母等级不足
    
    public static final int Aircraft_Formation_Not =70132;//编队上阵飞机非法
    public static final int Aircraft_Carrier_Airplane_type_repeat = 70133; // 飞机上阵类型重复

    public static final int friendLimit = 70201;//好友达到上限
    public static final int targetFriendLimit = 70202;//对方好友达到上限
    public static final int friendBlack = 70203;//同意添加好友时对方在我黑名单内
    public static final int targetFriendBlack = 70204;//同意添加好友时我在对方黑名单

    public static final int NoChat = 70205;//当前处于小黑屋或禁言状态，不能发言
    public static final int Activity_Retroactive_117 = 70206; // 只能补签一次

    public static final int Camp_NotTicke = 70207; //不是阵营前50不能参与投票

    public static final int Change_New_Camp_Impeach = 70209; //阵营 弹劾中不能转阵营
    public static final int Change_New_Camp_Sl = 70210; //阵营 中司令 不能转阵营
    public static final int Friend_Not_Server = 70211;//不能添加好友，因为不是同一服务器
    public static final int Agent_Talent_Not = 70212;//天赋点不足
    public static final int PLAYER_Oil_Limit = 70213;//石油超过上限

    public static final int NOT_DOUBLE_COMBAT = 70212;//不够双倍战力
    public static final int Record_Reward_Received = 70213;//记录奖励已经领取
    public static final int STRENGTH_STORE_LIMIT = 70214;// 力量配件超过上限
    public static final int STRENGTH_BUY_LIMIT = 70215;// 力量研发许可每日可用金砖购买最大上限
    public static final int TANK_STAR_SCORE_LIMIT = 70301;// 图鉴积分领取条件不足
    public static final int TANK_STAR_SCORE_REWARD = 70302;// 图鉴积分奖励已领取

    public static final int Fight_Times_Limit = 70310;//战斗次数不足
    public static final int Filed_Boos_Not_Troop = 70311;//兽王试炼没有部队

    public static final int Battle_Troop_Not = 70312;//战斗没有设置部队

    public final static int Driver_EvolveStar_Max = 70313; //兽魂的觉醒等级已满
    public final static int Driver_Evolve_Where = 70314; //兽魂的觉醒条件不足

}


