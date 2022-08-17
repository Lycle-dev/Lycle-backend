package com.Lycle.Server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@DynamicInsert
@DynamicUpdate
public class Activity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 100, nullable = false)
    private String category;

    @Column(length = 50, nullable = true)
    private String activityTime;

    @ColumnDefault("0")
    private boolean finishChecked;

    //리워드 받았는지
    @ColumnDefault("0")
    private boolean rewardChecked;

    //리워드 요청 했는지
    @ColumnDefault("0")
    private boolean requestReward;


    @Builder
    public Activity(Long id, Long userId, String category, String activityTime
            ,boolean finishChecked, boolean rewardChecked, boolean requestReward) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.activityTime = activityTime;
        this.finishChecked = finishChecked;
        this.rewardChecked = rewardChecked;
        this.requestReward = requestReward;
    }

    public void updateRewardChecked(boolean rewardChecked){
        this.rewardChecked = rewardChecked;
    }

    public void updateRequestReward(boolean requestReward){
        this.requestReward = requestReward;
    }


}
