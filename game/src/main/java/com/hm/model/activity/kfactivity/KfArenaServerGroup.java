package com.hm.model.activity.kfactivity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "KfArenaServerGroup")
public class KfArenaServerGroup extends BaseKfServerGroup{
    private List<Integer> allServerId;

    public KfArenaServerGroup(List<Integer> serverIds, String url) {
        super(serverIds, url);
    }
}
