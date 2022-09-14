package com.Lycle.Server.controller;

import com.Lycle.Server.config.auth.UserPrincipal;
import com.Lycle.Server.dto.Activity.RequestActivityDto;
import com.Lycle.Server.dto.BasicResponse;
import com.Lycle.Server.service.ActivityService;
import com.Lycle.Server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ActivityController {
    private final ActivityService activityService;
    private final UserService userService;

    //챌린지 기록
    @PostMapping("/user/activity")
    public ResponseEntity<BasicResponse> saveActivity(Authentication authentication, @RequestBody RequestActivityDto requestActivityDto) throws ParseException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse activityResponse = BasicResponse.builder()
                .code(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message("챌린지 종료 시간이 기록되었습니다.")
                .count(1)
                .result(Collections.singletonList(activityService.saveActivity(userPrincipal.getId() ,requestActivityDto)))
                .build();

        //활동일 업데이트
        userService.updateTime(userPrincipal.getId());

        return new ResponseEntity<>(activityResponse,activityResponse.getHttpStatus());
    }

    @GetMapping("/user/activity")
    public ResponseEntity<BasicResponse> searchAllActivity(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse allActivity = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("챌린지 조회가 완료 되었습니다.")
                .count(activityService.searchActivity(userPrincipal.getId()).size())
                .result(Collections.singletonList(activityService.searchActivity(userPrincipal.getId())))
                .build();

        return new ResponseEntity<>(allActivity, allActivity.getHttpStatus());
    }

    //챌린지 공유하기
    @PutMapping("/user/activity/{id}")
    public ResponseEntity<BasicResponse> shareActivity(Authentication authentication, @PathVariable Long id){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse activityResponse;

        if(activityService.shareActivity(id) == true) {
            activityResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("챌린지 리워드가 요청되었습니다.")
                    .build();
        }
        else{
            activityResponse = BasicResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("금일 챌린지 요청이 이미 완료되었습니다.")
                    .build();
        }

        return new ResponseEntity<>(activityResponse, activityResponse.getHttpStatus());
    }



}
