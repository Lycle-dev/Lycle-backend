package com.Lycle.Server.service;

import com.Lycle.Server.domain.Activity;
import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.domain.jpa.ActivityRepository;
import com.Lycle.Server.domain.jpa.UserRepository;
import com.Lycle.Server.dto.Activity.RequestActivityDto;
import com.Lycle.Server.dto.Activity.SearchActivityWrapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final RewardService rewardService;

    @Transactional
    public Long saveActivity(Long userId, RequestActivityDto requestActivityDto){
        return activityRepository.save(Activity.builder()
                        .userId(userId)
                        .category(requestActivityDto.getCategory())
                        .activityTime(requestActivityDto.getActivityTime())
                        .finishChecked(requestActivityDto.isFinishChecked())
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public List<SearchActivityWrapper> searchActivity(Long id){
        return activityRepository.findAllByUserIdOrderByCreatedDateDesc(id);
    }

    @Transactional
    public void shareActivity(Long activityId){
        Activity activity = activityRepository.findById(activityId).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 챌린지 입니다."));
        activity.updateRequestReward(true);
    }

    @Transactional
    public Long saveReward(Long id, Long activityId, int point) throws JSONException, IOException {
        Long reward = 0L;

        //로그인 한 유저의 정보 먼저 찾기
        User me =  userRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 회원 입니다."));

        //챌린지 적립해주고자 하는 친구의 정보 찾기
        User friend = userRepository.findById(me.getSharedId()).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 회원 입니다."));

        //친구에게 포인트 적립 해주기
        reward = rewardService.transferReward(me.getEmail(), friend.getEmail(), point);

        //챌린지 찾아서 리워드 적립받은 상태로 변경해주기
        Activity activity = activityRepository.findById(activityId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 활동 입니다."));
        activity.updateRewardChecked(true);

        //적립 후 변한 리워드 코인양 호출
        reward = rewardService.getReward(me.getId());

        return reward;
    }
    
    
    //요청하지 않은 리워드가 있는지 확인
    @Transactional
    public boolean checkRequestReward(Long id){
        if(activityRepository.findActivityByUserId(id) > 1L){
            return true;
        }
        return false;
    }
    
    //친구가 적립받지 못한 리워드가 있는지 확인
    @Transactional
    public boolean checkFriendReward(Long sharedId){
        if(activityRepository.findActivityById(sharedId) > 1L){
            return true;
        }
        return false;
    }
    
}
