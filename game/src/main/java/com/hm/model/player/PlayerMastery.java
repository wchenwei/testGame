package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;

import java.util.Arrays;

public class PlayerMastery extends PlayerDataContext {
	//精炼信息
	private Mastery[] masterys = new Mastery[4];
	//检查是否加新的光环
	private boolean checkNewChange;
	
	public void init(){
		for(int i=0;i<masterys.length;i++){
			Mastery mastery = this.masterys[i];
			if(mastery==null){
				this.masterys[i]=new Mastery(i+1);
			}
		}
		SetChanged();
	}
	
	public Mastery getMastery(int id){
		Mastery mastery = masterys[id-1];
		if(mastery==null){
			mastery = new Mastery(id);
			masterys[id-1] = mastery;
			SetChanged();
		}
		return mastery;
	}
	public int getLv(int id,int index){
		Mastery mastery =getMastery(id);
		return mastery.getLv(index);
	}
	//计算光环等级
	public void calCircleLv(){
		Arrays.stream(masterys).filter(t ->t!=null).forEach(t -> t.calCircleLv());
		SetChanged();
	}

	public boolean checkIsChanged(){
		boolean isChanged = false;
		if (!checkNewChange){
			isChanged = Arrays.stream(masterys).filter(t ->t!=null).anyMatch(Mastery::isChanged);
			checkNewChange = true;
			SetChanged();
		}
		return isChanged;
	}

	public void calCircleLv(int id){
		Mastery mastery = getMastery(id);
		mastery.calCircleLv();
		SetChanged();
	}
	
	public void lvUp(int id,int index){
		Mastery mastery = getMastery(id);
		mastery.lvUp(index);
		SetChanged();
	}
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerMastery", this);
	}

	public Mastery[] getMasterys() {
		return this.masterys;
	}
}
