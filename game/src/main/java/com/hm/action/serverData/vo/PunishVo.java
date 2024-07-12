package com.hm.action.serverData.vo;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunishVo {
	private List<PunishPlayerVo> vos = Lists.newArrayList();
}
