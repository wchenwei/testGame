package com.hm.libcore.rpc;

import com.hm.libcore.language.LanguageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Setter
@Getter
@Accessors(chain=true)
public class KFServerMail {
    private int serverId;
    private Set<Long> playerIds;
    private String items;
    private int mailType;
    private LanguageVo[] parms;
}
