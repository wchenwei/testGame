package com.hm.model.item;

import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;

import java.util.Objects;

public class Items {
	private int type;//物品类型 和 ItemType枚举对应
	private int id;		//玩家资源id PlayerAssetEnum对应
	private long count;
	
	public Items() {
		super();
	}
	public Items(int id) {
		this.id = id;
	}

	
	public Items(int id, long count, int type) {
		this.id = id;
		this.count = count;
		this.type = type;
	}
	public Items(int id,long count, ItemType itemType){
		this(id,count,itemType.getType());
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	public void addCount(long add) {
		this.count += add;
	}
	//按照百分比增加数量
	public void addCountRate(double rate) {
		//四舍五入
		this.count += (int)(count*rate+0.5);
	}
	
	public void reduceCount(long reduce) {
		this.count -= reduce;
	}
	public ItemType getEnumItemType() {
		return ItemType.getType(this.type);
	}
	public void setItemType(ItemType itemType) {
		this.type = itemType.getType();
	}
	public void setItemType(int itemType) {
		this.type = itemType;
	}
	public int getItemType() {
		return type;
	}
	
	public Items clone() {
		Items clone = new Items(id);
		clone.setItemType(type);
		clone.setCount(count);
		return clone;
	}
	
	//是否是玩家经验
	public boolean isPlayerExp() {
		return this.type == ItemType.CURRENCY.getType() && this.id == PlayerAssetEnum.EXP.getTypeId();
	}
	
	public boolean isSameItem(Items item) {
		return this.type == item.getItemType() && this.id == item.getId();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Items)) {
			return false;
		}
		Items items = (Items) o;
		return type == items.type && id == items.id && count == items.count;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, id, count);
	}
}










