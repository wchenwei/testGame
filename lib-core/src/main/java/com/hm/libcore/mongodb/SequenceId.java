package com.hm.libcore.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "sequence")
public class SequenceId {
    @Id
    private String id;

    @Field("seqId")
    private int seqId;

	public int getSeqId() {
		return seqId;
	}

}
