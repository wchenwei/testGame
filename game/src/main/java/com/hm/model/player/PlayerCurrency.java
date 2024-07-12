package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.model.backRes.AdvanceVo;

/**
 * @Description: 玩家资产信息
 * @author siyunlong
 * @date 2017年12月7日 下午4:39:59
 * @version V1.0
 */
public class PlayerCurrency extends PlayerDataContext {

	private long[] value = new long[CurrencyKind.values().length+1];
    private AdvanceVo advanceVo;
	@Override
	public void initData() {
		if(this.value.length < CurrencyKind.values().length+1) {
			long[] temp = new long[CurrencyKind.values().length+1];
			System.arraycopy(value, 0, temp, 0, this.value.length);
			this.value = temp;
			SetChanged();
		}
	}

	/*public long getAdvanceOil() {
		return advanceOil;
	}*/

	public AdvanceVo getAdvanceVo() {
		return advanceVo;
	}

	public AdvanceVo getAdvanceVo(int tagId) {
		if (advanceVo == null) {
			return null;
		}
		if (advanceVo.getTagId() != tagId) {
			return null;
		}
		return advanceVo;
	}

	/**
	 * 判断是否能消耗
	 *
	 * @param kind
	 * @param spend
	 * @return
	 */
	public boolean canSpend(CurrencyKind kind, long spend) {
		if (isGold(kind)) {
			return getGold() >= spend;
		}
		return this.value[kind.Index]>=spend;
	}
	
	public boolean isGold(CurrencyKind kind){
		return kind==CurrencyKind.Gold||kind==CurrencyKind.SysGold;
	}
	//玩家拥有的金币
	public long getGold(){
		return this.value[CurrencyKind.Gold.Index]+this.value[CurrencyKind.SysGold.Index];
	}
	/**
	 * 消耗资金
	 * @param kind
	 * @param spend
	 * @return
	 */
	public boolean spend(CurrencyKind kind, long spend) {
		if(spend<0) {
			return false;
		}
		if(canSpend(kind,spend)) {
			this.value[kind.Index]-=spend;
			SetChanged();
			return true;
		}
		return false;
	}
	/**
	 * 添加资金
	 * @param kind
	 * @param add
	 */
	public void add(CurrencyKind kind, long add) {
		this.value[kind.Index]+=add;
		SetChanged();
	}
	
	/**  
	 * set:(设置资金). <br/>  
	 *  
	 * @author zqh  
	 * @param kind kind
	 * @param temp  值
	 *
	 */
	public void set(CurrencyKind kind, long temp) {
		this.value[kind.Index]=temp;
		SetChanged();
	}
	/**
	 * 获取资金
	 * @param kind
	 * @return
	 */
	public long get(CurrencyKind kind) {
		return this.value[kind.Index];
	}
	
	@Override
	public String toString() {
		return String.format("gold:%d, diamond:%d, flower:%d, goldflower:%d, aormb:%d",value[CurrencyKind.Gold.Index],
				value[CurrencyKind.Cash.Index]);
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerCurrency", this.value);
	}

    public void addAdvance(AdvanceVo advanceVo) {
        this.advanceVo = advanceVo;
		SetChanged();
	}

	public void clearAdvances() {
        this.advanceVo = null;
		SetChanged();
	}
}
