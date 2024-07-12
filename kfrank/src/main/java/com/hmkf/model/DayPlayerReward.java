package com.hmkf.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hmkf.db.KfDBUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "DayPlayerReward")
public class DayPlayerReward {
    @Id
    private int id;
    private int type;
    private Integer rank;

    public DayPlayerReward(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public static DayPlayerReward getDayPlayerReward(long playerId) {
        return KfDBUtils.getMongoDB().get(playerId, DayPlayerReward.class);
    }

    public static void dayClear() {
        KfDBUtils.getMongoDB().dropCollection(DayPlayerReward.class);
    }

    public static void main(String[] args) {
        dayClear();
    }
}
