package com.Lycle.Server.dto.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RewardRequestDto {
    private Long activityId;
    private int point;

    @Builder
    public RewardRequestDto(Long activityId, int point){
        this.activityId = activityId;
        this.point = point;
    }
}
