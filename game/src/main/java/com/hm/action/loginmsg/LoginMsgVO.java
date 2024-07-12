package com.hm.action.loginmsg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginMsgVO {
    private int maxTimes;
    private String context;
}
