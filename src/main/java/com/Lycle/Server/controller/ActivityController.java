package com.Lycle.Server.controller;

import com.Lycle.Server.config.auth.UserPrincipal;
import com.Lycle.Server.dto.Activity.RequestActivityDto;
import com.Lycle.Server.dto.BasicResponse;
import com.Lycle.Server.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;


    /*
    //운동 시작 시간 기록
    @PostMapping("/user/test")
    public ResponseEntity<BasicResponse> startActivity(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse activityResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.CREATED)
                .message("챌린지 시작 시간이 기록되었습니다.")
                .count(1)
                .result(Collections.singletonList(userPrincipal.getEmail()))
                .build();
        return new ResponseEntity<>(activityResponse,activityResponse.getHttpStatus());
    }

     */


    //챌린지 기록
    @PostMapping("/user/activity")
    public ResponseEntity<BasicResponse> saveActivity(Authentication authentication, @RequestBody RequestActivityDto requestActivityDto){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse activityResponse = BasicResponse.builder()
                .code(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message("챌린지 종료 시간이 기록되었습니다.")
                .count(1)
                .result(Collections.singletonList(activityService.saveActivity(userPrincipal.getId() ,requestActivityDto)))
                .build();
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

}
