package com.Lycle.Server.dto.Activity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RequestActivityDto {
    private String category;
    private String activityTime;
    private Boolean finishChecked;
    //리워드 받았는지
    private Boolean rewardChecked;
    //리워드 요청했는지
    private Boolean rewardRequested;

    @Builder
    public RequestActivityDto(String category, String activityTime , Boolean finishChecked,
                              Boolean rewardChecked, Boolean rewardRequested){
        this.category = category;
        this.activityTime = activityTime;
        this.finishChecked = finishChecked;
        this.rewardChecked = rewardChecked;
        this.rewardRequested = rewardRequested;
    }

}
