package com.hm.model.tankLimitAdapter;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.enums.TankLimitType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
public class TankLimitAdapterGroup {
	private List<BaseTankLimitAdapter> adapters = Lists.newArrayList();
	public TankLimitAdapterGroup(String str){
		super();
		if(StrUtil.isBlank(str)){
			return;
		}
		Arrays.stream(str.split(";")).forEach(t ->{
			int type = Integer.parseInt(t.split(":")[0]);
			TankLimitType tankLimitType = TankLimitType.getTankLimitType(type);
			this.adapters.add(TankLimitFactory.createProhibitAdapter(tankLimitType,t.split(":")[1]));
		});
		
	}
	public void add(BaseTankLimitAdapter adapter) {
		this.adapters.add(adapter);
	}
	
	//坦克必须满足过滤的所有条件
	public boolean isAllMatchFilter(int tankId) {
		return this.adapters.stream().allMatch(t ->t.isFit(tankId));
	}
	public List<Integer> getAllMatchFilerTankId(List<Integer> tankIds) {
		return tankIds.stream().filter(e -> isAllMatchFilter(e)).collect(Collectors.toList());
	}
	
	//坦克满足其中一个过滤条件
	public boolean isAnyMatchFilter(int tankId) {
		return this.adapters.stream().anyMatch(t ->t.isFit(tankId));
	}
	public List<Integer> getAnyMatchFilerTankId(List<Integer> tankIds) {
		return tankIds.stream().filter(e -> isAnyMatchFilter(e)).collect(Collectors.toList());
	}
	
}
