package com.hm.model.activity.kfactivity;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarServerTeamUtils;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSumUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "KfWorldWarServerGroup")
public class KfWorldWarServerGroup extends BaseKfServerGroup {
    private int snum;
    private int serverTypeId;

    public KfWorldWarServerGroup(List<Integer> serverIds, String url, ListMultimap<Integer, Integer> dbServerGroup) {
        super(serverIds, url);
        setId(serverIds.get(0) + "");
        Map<Integer, Integer> server2TeamMap = Maps.newHashMap();
        for (int teamId : serverIds) {
            for (int createServerId : dbServerGroup.get(teamId)) {
                server2TeamMap.put(createServerId, teamId);
            }
            server2TeamMap.put(teamId, teamId);
        }
        KfWorldWarServerTeamUtils.addServerTeam(server2TeamMap);
        this.snum = KfWorldWarSumUtils.getSnumId();
    }

    public void addServerId(int teamId, ListMultimap<Integer, Integer> dbServerGroup) {
        if (this.serverIds.contains(teamId)) {
            return;
        }
        this.serverIds.add(teamId);
        Map<Integer, Integer> server2TeamMap = Maps.newHashMap();
        for (int createServerId : dbServerGroup.get(teamId)) {
            server2TeamMap.put(createServerId, teamId);
        }
        server2TeamMap.put(teamId, teamId);
        KfWorldWarServerTeamUtils.addServerTeam(server2TeamMap);
    }

    public int getGroupId() {
        return Integer.parseInt(getId());
    }
}
