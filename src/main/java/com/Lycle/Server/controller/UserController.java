package com.Lycle.Server.controller;

import com.Lycle.Server.config.auth.UserPrincipal;
import com.Lycle.Server.dto.BasicResponse;
import com.Lycle.Server.dto.User.UpdateInfoDto;
import com.Lycle.Server.dto.User.UserJoinDto;
import com.Lycle.Server.dto.User.UserLoginDto;
import com.Lycle.Server.service.ActivityService;
import com.Lycle.Server.service.RewardService;
import com.Lycle.Server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ActivityService activityService;
    private final RewardService rewardService;

    @Operation(summary = "join", description = "사용자 회원가입")
    @PostMapping("/join")
    public ResponseEntity<BasicResponse> joinUser(@RequestBody UserJoinDto userJoinDto) throws JSONException, IOException {
        BasicResponse joinUserResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.CREATED)
                .message("회원가입이 완료되었습니다.")
                .count(1)
                .result(Collections.singletonList(userService.saveUser(userJoinDto)))
                .build();
        return new ResponseEntity<>(joinUserResponse, joinUserResponse.getHttpStatus());

    }

    @Operation(summary = "login", description = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<BasicResponse> loginUser(@RequestBody UserLoginDto userLoginDto) {
        BasicResponse searchUser = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("로그인에 성공했습니다.")
                .count(1)
                .token(userService.loginUser(userLoginDto))
                .build();
        return new ResponseEntity<>(searchUser, searchUser.getHttpStatus());
    }

    @Operation(summary = "verify", description = "사용자 이메일 중복확인")
    @GetMapping("/verify/email")
    public ResponseEntity<BasicResponse> verifyEmail(@RequestParam String email) {
        BasicResponse verifyResponse;

        if (userService.verifyEmail(email)) {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 이메일 입니다.")
                    .build();

        } else {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("사용이 가능한 이메일 입니다.")
                    .build();
        }

        return new ResponseEntity<>(verifyResponse, verifyResponse.getHttpStatus());
    }

    @Operation(summary = "verify", description = "사용자 닉네임 중복확인")
    @GetMapping("/verify/nickname")
    public ResponseEntity<BasicResponse> verifyNickname(@RequestParam String nickname) {
        BasicResponse verifyResponse;
        if (userService.verifyNickname(nickname)) {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 닉네임 입니다.")
                    .build();

        } else {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("사용이 가능한 닉네임 입니다.")
                    .build();
        }

        return new ResponseEntity<>(verifyResponse, verifyResponse.getHttpStatus());
    }

    //내 운동 현황 조회
    @GetMapping("/user/detail")
    public ResponseEntity<BasicResponse> searchProfile(Authentication authentication) {
        BasicResponse profileResponse;
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String,Object> result = new HashMap<>();
        result.put("profile", userService.searchProfile(userPrincipal.getId()));
        result.put("activities", activityService.searchActivity(userPrincipal.getId()));
        profileResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("회원 정보 조회가 완료되었습니다.")
                .count(1)
                .result(Collections.singletonList(result))
                .build();
        return new ResponseEntity<>(profileResponse, profileResponse.getHttpStatus());
    }

    @PutMapping("/user/profile")
    public ResponseEntity<BasicResponse> updateInfo(Authentication authentication, @RequestBody UpdateInfoDto updateInfoDto){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userService.updateInfo(userPrincipal.getId(), updateInfoDto);
        BasicResponse updateResponse = BasicResponse.builder()
                .code(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message("회원정보 수정이 완료되었습니다.")
                .build();
        return new ResponseEntity<>(updateResponse, updateResponse.getHttpStatus());
    }

    //메인화면 조회
    @GetMapping("/user/main")
    public ResponseEntity<BasicResponse> loadMain(Authentication authentication) throws JSONException, IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        BasicResponse mainResponse;
        Map<String,Object> result = new HashMap<>();
        if(userPrincipal.getSharedId() != null){
            //내 프로필과 친구 프로필을 HashMap에 담아서 클라이언트에게 전송
            result.put("me", userService.searchProfile(userPrincipal.getId()));
            result.put("friend", userService.searchProfile(userPrincipal.getSharedId()));
            result.put("reward", rewardService.getReward(userPrincipal.getId()));
            result.put("requestCheck", activityService.checkRequestReward(userPrincipal.getId()));
            result.put("friendRequest", activityService.checkFriendReward(userPrincipal.getSharedId()));
            result.put("activityFinish", activityService.checkFinishActivity(userPrincipal.getId()));
            mainResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("메인 화면 조회가 완료 되었습니다.")
                    .result(Collections.singletonList(result))
                    .build();
        }
        else{
            result.put("me", userService.searchProfile(userPrincipal.getId()));
            result.put("friend", null);
            result.put("reward", rewardService.getReward(userPrincipal.getId()));
            result.put("request_check", null);
            result.put("friend_request", null);
            result.put("activityFinish", activityService.checkFinishActivity(userPrincipal.getId()));

            mainResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("메인 화면 조회가 완료 되었습니다.")
                    .result(Collections.singletonList(result))
                    .build();
        }

        return new ResponseEntity<>(mainResponse, mainResponse.getHttpStatus());
    }



}
