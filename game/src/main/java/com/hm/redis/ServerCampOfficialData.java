package com.hm.redis;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerCampOfficialData {
    private int serverId;
    private List<ServerOfficalData> officials = Lists.newArrayList();
}
