package com.hm.action.serverData.vo;

import com.hm.model.player.SimplePlayerVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunishPlayerVo extends SimplePlayerVo{
	private long endTime;
	
}
