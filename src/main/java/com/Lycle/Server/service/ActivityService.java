package com.Lycle.Server.service;

import com.Lycle.Server.domain.Activity;
import com.Lycle.Server.domain.jpa.ActivityRepository;
import com.Lycle.Server.dto.Activity.RequestActivityDto;
import com.Lycle.Server.dto.Activity.SearchActivityWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    @Transactional
    public Long saveActivity(Long userId, RequestActivityDto requestActivityDto){
        return activityRepository.save(Activity.builder()
                        .userId(userId)
                        .category(requestActivityDto.getCategory())
                        .activityTime(requestActivityDto.getActivityTime())
                        .finishChecked(requestActivityDto.isFinishChecked())
                        .rewardChecked(requestActivityDto.isRewardChecked())
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public List<SearchActivityWrapper> searchActivity(Long id){
        return activityRepository.findAllByUserIdOrderByCreatedDateDesc(id);
    }

}
