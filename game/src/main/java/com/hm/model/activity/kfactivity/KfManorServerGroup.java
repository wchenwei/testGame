package com.hm.model.activity.kfactivity;

import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@Document(collection = "KfManorServerGroup")
public class KfManorServerGroup extends BaseKfServerGroup{
	
	public KfManorServerGroup(List<Integer> serverIds,String url) {
		super(serverIds, url);
	}
}
