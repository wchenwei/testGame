package com.hm.model.serverpublic.kf.pk;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.model.serverpublic.kf.AbstractKfData;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@NoArgsConstructor
public class KfPkLevelData extends AbstractKfData{
	private transient List<Integer> kingIds = Lists.newArrayList();
	@Transient
	private KfPkKingInfo[] kingInfos;
	
	public void checkAndLoad() {
		if(CollUtil.isEmpty(this.kingIds)) {
			this.kingInfos = null;
			return;
		}
		KfPkKingInfo[] kingInfos = new KfPkKingInfo[this.kingIds.size()];
		for (int i = 0; i < kingIds.size(); i++) {
			kingInfos[i] = new KfPkKingInfo(kingIds.get(i));
		}
		this.kingInfos = kingInfos;
	}
	
	public void changeKings(List<Integer> kingIds) {
		this.kingIds = kingIds;
		checkAndLoad();
	}
	
	public boolean loadHourCheck() {
		checkAndLoad();
		return false;
	}
	
}
