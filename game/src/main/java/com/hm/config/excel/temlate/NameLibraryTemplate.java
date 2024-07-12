package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("name_library")
public class NameLibraryTemplate {
	private Integer id;
	private String name_1;
	private String name_2;
}
