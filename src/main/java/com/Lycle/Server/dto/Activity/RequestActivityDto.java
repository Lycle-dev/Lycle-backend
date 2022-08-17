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
    private boolean finishChecked;

    @Builder
    public RequestActivityDto(String category, String activityTime , boolean finishChecked){
        this.category = category;
        this.activityTime = activityTime;
        this.finishChecked = finishChecked;
    }

}
