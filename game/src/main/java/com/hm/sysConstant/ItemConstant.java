package com.hm.sysConstant;

import com.hm.model.item.Items;

import java.util.List;

/**
 * ClassName:ItemContant <br/>  
 * Date:     2017年12月29日 下午3:57:15 <br/>  
 * @author   zqh
 * Item物品表..切记添加
 * @version  1.1  
 * @since    
 */
public class ItemConstant {
	public final static int Meat_Id = 60001;//肉块

	public final static int COM_PAPER = 60005; //万能图纸
	public final static int SUPER_PAPER = 60006; //超级万能图纸
	public final static int SENIOR_PAPER = 60252; //超高级万能图纸（3s使用）
	public final static int COM_SOUL = 60011; //万能魂魄
	public final static int Military_ShopIndex = 155; //荣誉证书商店索引
	public final static int Car_Parts_ShopIndex = 156; //座驾零件商店索引
	
	public final static int TroopRepairHammer = 60009;//修理锤
	public final static int Parts = 60013; //零件
	public final static int CloneItem = 60060; //克隆道具
	public final static int CloneExpItem = 60012; //克隆部队奖励道具

	//中控系统研究令
	public final static int Control_Research_Order = 60307;
    public final static int KFTankLotteryItemId = 60501; //空中拦截消耗道具ID

	//标识道具
	public static int FilerItemId = 66003;
	public static List<Items> filterHideItems(List<Items> itemsList) {
		itemsList.removeIf(e -> e.getId() == FilerItemId);
		return itemsList;
	}
}
  
