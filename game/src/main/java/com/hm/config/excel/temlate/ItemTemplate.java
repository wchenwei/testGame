package com.hm.config.excel.temlate;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.enums.FunctionType;
import com.hm.enums.ItemType;
import com.hm.enums.QualityType;
import com.hm.enums.SubItemType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

@FileConfig("item_base")
public class ItemTemplate extends ItemBaseTemplate{
	private SubItemType itemType;//道具类型
	private QualityType qualityType;//品质
	private FunctionType functionType;//功能道具类型
	private int effectValue;//buff或者加速道具的持续时间,
	private List<Items> choiceItems = Lists.newArrayList();
	private List<Items> sellReward = Lists.newArrayList();
	//合成所需的数量
	private int needNum;
	//合成的目标装备
	private int equId;
	
	public void init() {
		this.itemType = SubItemType.getType(getItem_type());
		this.qualityType = QualityType.getType(getQuality());
		this.choiceItems = ItemUtils.str2ItemList(getPick_drop(), ",", ":");
		if(StringUtils.isNotBlank(getPlayer_arm())){
			this.needNum = Integer.parseInt(this.getPlayer_arm().split(":")[1]);
			this.equId = Integer.parseInt(this.getPlayer_arm().split(":")[0]);
		}
		this.sellReward = ItemUtils.str2ItemList(this.getPrice(),",",":");
		if (itemType == SubItemType.FUNCTIONITEM) {
        	String effectStr = getEffect();
            if(!StringUtil.isNullOrEmpty(effectStr)) {
    			int[] cc = StringUtil.strToIntArray(effectStr, ":");
    			this.functionType = FunctionType.getType(cc[0]);
				if(cc.length > 1) {
					this.effectValue = cc[1];
				}
    		}
        }
	}

	public SubItemType getItemType() {
		return itemType;
	}

	public QualityType getQualityType() {
		return qualityType;
	}

	public FunctionType getFunctionType() {
		return functionType;
	}
	
	public boolean isCanBagUse() {
		return getUse() == 1;
	}
	
	public boolean isCanInBag() {
		return getTab_type() == 1;
	}

	public int getNeedNum() {
		return needNum;
	}

	public int getEquId() {
		return equId;
	}

	public List<Items> getChoiceItems() {
		return Lists.newArrayList(choiceItems);
	}

	public ItemType getEnumItemType() {
		return ItemType.getType(this.getItem_type());
	}
	public Items getComposeCost(int count){
		return new Items(this.getId(),needNum*count,ItemType.ITEM);
	}
	public List<Items> getSellReward(){
		return sellReward;
	}
	public Items getComposeCost(){
		return getComposeCost(1);
	}

	/**
	 * 是否在使用有效期内
	 * @return
	 */
	public boolean isEffectiveDate(){
		if (!StringUtil.isNullOrEmpty(getDate_effective())){
			String [] effeDate = getDate_effective().split("#");
			DateTime beginTime = DateUtil.beginOfDay(DateUtil.parse(effeDate[0], DatePattern.PURE_DATE_PATTERN));
			DateTime endTime = DateUtil.endOfDay(DateUtil.parse(effeDate[1], DatePattern.PURE_DATE_PATTERN));
			return DateUtil.isIn(new Date(), beginTime, endTime);
		}
		return true;
	}
	
}
