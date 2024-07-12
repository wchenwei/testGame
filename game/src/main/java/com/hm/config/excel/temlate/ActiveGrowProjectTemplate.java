package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_grow_project")
public class ActiveGrowProjectTemplate {
	private Integer id;
	private Integer type;//1-关卡 2-头衔
	private Integer level;//要求等级
	private String reward;//普通奖励
	private String reward2;//购买计费后奖励
}
