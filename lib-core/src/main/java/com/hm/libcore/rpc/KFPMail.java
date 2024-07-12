package com.hm.libcore.rpc;

import com.hm.libcore.language.LanguageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain=true)
public class KFPMail {
    private long id;
    private String items;
    private int mailType;
    private LanguageVo[] parms;
}
