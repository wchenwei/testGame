package com.hm.action.loginmsg;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginMsg {
    @Id
    int id;
    // int interval;
    List<Integer> channel = Lists.newArrayList();
    int count;
    long startTime;
    long endTime;
    String context;

    public boolean timeOk(long t) {
        return t >= startTime && t <= endTime;
    }
    public boolean timeOk() {
        return timeOk(System.currentTimeMillis());
    }

    public boolean isValid(int ch) {
        return timeOk() && CollUtil.contains(channel, ch);
    }

}
